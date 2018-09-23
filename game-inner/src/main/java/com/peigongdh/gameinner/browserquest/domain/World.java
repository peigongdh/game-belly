package com.peigongdh.gameinner.browserquest.domain;

import com.peigongdh.gameinner.browserquest.common.Constant;
import com.peigongdh.gameinner.browserquest.common.Properties;
import com.peigongdh.gameinner.browserquest.common.Types;
import com.peigongdh.gameinner.browserquest.domain.message.*;
import com.peigongdh.gameinner.browserquest.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class World {

    private static final Logger logger = LoggerFactory.getLogger(World.class);

    private int id;

    private int maxPlayer;

    private int ups;

    private Map map;

    private ConcurrentHashMap<String, Entity> entities;

    private ConcurrentHashMap<String, Player> players;

    private ConcurrentHashMap<String, Mob> mobs;

    private ConcurrentHashMap<String, Entity> attackers;

    private ConcurrentHashMap<String, Item> items;

    // FIXME: ?
    // equipping, hurt

    private ConcurrentHashMap<String, Npc> npcs;

    private List<MobArea> mobAreas;

    private List<ChestArea> chestAreas;

    private ConcurrentHashMap<String, Group> groups;

    private ConcurrentHashMap<String, List<String>> outgoingQueues;

    private int itemCount;

    private int playerCount;

    private boolean zoneGroupReady;

    private Consumer<Player> connectCallback;

    private Consumer<Player> enterCallback;

    private Runnable addedCallback;

    private Runnable removedCallback;

    private Runnable regenCallback;

    private Consumer<Entity> attackCallback;

    public Consumer<Player> getEnterCallback() {
        return enterCallback;
    }

    public Map getMap() {
        return map;
    }

    public World(int id, int maxPlayer) {
        this.id = id;
        this.maxPlayer = maxPlayer;
        this.ups = 50;
        this.map = null;
        this.entities = new ConcurrentHashMap<>();
        this.players = new ConcurrentHashMap<>();
        this.mobs = new ConcurrentHashMap<>();
        this.attackers = new ConcurrentHashMap<>();
        this.items = new ConcurrentHashMap<>();
        this.npcs = new ConcurrentHashMap<>();
        this.mobAreas = new ArrayList<>();
        this.chestAreas = new ArrayList<>();
        this.groups = new ConcurrentHashMap<>();
        this.outgoingQueues = new ConcurrentHashMap<>();
        this.itemCount = 0;
        this.playerCount = 0;
        this.zoneGroupReady = false;
        World self = this;
        this.onPlayerConnect(player -> player.onRequestPosition(() -> {
            if (null != player.getLastCheckpoint()) {
                return player.getLastCheckpoint().getRandomPosition();
            }
            return self.map.getRandomStartingPosition();
        }));
        this.onPlayerEnter(player -> {
            logger.info("{} player has joined world {}", player.getName(), self.id);
            if (!player.isHasEnteredGame()) {
                self.incrementPlayerCount();
            }

            // Number of players in this world
            self.pushToPlayer(player, new Population(self.playerCount, 1));
            self.pushRelevantEntityListTo(player);

            Consumer<Position> moveCallback = pos -> {
                logger.info("{} player is moving to {}", player.getName(), "(" + pos.getX() + " " + pos.getY() + ")");
                player.forEachAttacker(mob -> {
                    Entity target = self.getEntityById(mob.getTargetId());
                    if (null != target) {
                        Position p = self.findPositionNextTo(mob, target);
                        if (mob.distanceToSpawningPoint(p.getX(), p.getY()) > 50) {
                            mob.clearTarget();
                            mob.forgetEveryone();
                            player.removeHater(mob);
                        } else {
                            self.moveEntity(mob, pos);
                        }
                    }
                });
            };

            player.onMove(moveCallback);
            player.onLootMove(moveCallback);

            player.onZone(() -> {
                boolean hashChangedGroups = self.handleEntityGroupMembership(player);
                if (hashChangedGroups) {
                    self.pushToPreviousGroups(player, new Destroy(player.getId()));
                    self.pushRelevantEntityListTo(player);
                }
            });

            player.onBroadcast(pair -> self.pushToAdjacentGroups(player.getGroupId(), pair.getKey(), pair.getValue() ? player.getId() : null));
            player.onBroadcastToZone(pair -> self.pushToGroup(player.getGroupId(), pair.getKey(), pair.getValue() ? player.getId() : null));

            player.onExit(() -> {
                logger.info("{} player has left the game.");
                self.removePlayer(player);
                self.decrementPlayerCount();
                if (null != self.removedCallback) {
                    self.removedCallback.run();
                }
            });

            if (null != self.addedCallback) {
                self.addedCallback.run();
            }
        });

        this.onPlayerAdded(() -> {
        });

        this.onPlayerRemoved(() -> {
        });

        this.onEntityAttack(entity -> {
            assert entity instanceof Mob;
            Mob attacker = (Mob) entity;
            Entity target = self.getEntityById(attacker.getTargetId());
            if (null != target && attacker.getType().equals("mob")) {
                Position pos = self.findPositionNextTo(attacker, target);
                self.moveEntity(attacker, pos);
            }
        });

        this.onRegenTick(() -> self.forEachCharacter(character -> {
            if (!character.hasFullHealth()) {
                character.regenHealthBy(character.getMaxHitPoints() / 25);
                if (character.getType().equals("player")) {
                    assert character instanceof Player;
                    self.pushToPlayer((Player) character, character.regen());
                }
            }
        }));
    }

    private void forEachCharacter(Consumer<Character> callback) {
        this.forEachPlayer(callback);
        this.forEachMob(callback);
    }

    private void forEachMob(Consumer<Character> callback) {
        for (java.util.Map.Entry<String, Player> entry : this.players.entrySet()) {
            callback.accept(entry.getValue());
        }
    }

    private void forEachPlayer(Consumer<Character> callback) {
        for (java.util.Map.Entry<String, Mob> entry : this.mobs.entrySet()) {
            callback.accept(entry.getValue());
        }
    }

    private void decrementPlayerCount() {
        if (this.playerCount > 0) {
            this.setPlayerCount(this.playerCount - 1);
        }
    }

    private void setPlayerCount(int count) {
        this.playerCount = count;
    }

    private void removePlayer(Player player) {
        player.broadcast(player.deSpawn(), true);
        this.removeEntity(player);
        this.players.remove(player.getId());
        this.outgoingQueues.remove(player.getId());
    }

    void pushRelevantEntityListTo(Player player) {
        if (null != player && null != this.groups.get(player.getGroupId())) {
            ConcurrentHashMap<String, Entity> entities = this.groups.get(player.getGroupId()).getEntities();
            List<String> entityIds = new ArrayList<>(entities.keySet());
            entityIds = Util.reject(entityIds, id -> id.equals(player.getId()));
            if (null != entityIds && !entityIds.isEmpty()) {
                this.pushToPlayer(player, new Lists(entityIds));
            }
        }
    }

    private void pushToPreviousGroups(Player player, SerializeAble message) {
        // Push this message to all groups which are not going to be updated anymore,
        // since the player left them.
        for (String id : player.getRecentlyLeftGroupIds()) {
            this.pushToGroup(id, message, null);
        }
        player.getRecentlyLeftGroupIds().clear();
    }

    private void moveEntity(Entity entity, Position pos) {
        if (null != entity) {
            entity.setPosition(pos);
            this.handleEntityGroupMembership(entity);
        }
    }

    private Position findPositionNextTo(Entity entity, Entity target) {
        Position pos = null;
        boolean valid = false;
        while (!valid) {
            pos = entity.getPositionNextTo(target);
            valid = this.isValidPosition(pos);
        }
        return pos;
    }

    private void incrementPlayerCount() {
        this.setPlayerCount(this.playerCount + 1);
    }

    public void onPlayerConnect(Consumer<Player> callback) {
        this.connectCallback = callback;
    }

    public void onPlayerEnter(Consumer<Player> callback) {
        this.enterCallback = callback;
    }

    public void onPlayerAdded(Runnable callback) {
        this.addedCallback = callback;
    }

    public void onPlayerRemoved(Runnable callback) {
        this.removedCallback = callback;
    }

    public void onEntityAttack(Consumer<Entity> callback) {
        this.attackCallback = callback;
    }

    public void onRegenTick(Runnable callback) {
        this.regenCallback = callback;
    }

    boolean isValidPosition(Position pos) {
        if (null != this.map && !this.map.isOutOfBounds(pos) && !this.map.isColliding(pos)) {
            return true;
        }
        return true;
    }

    public void run(String mapFilePath) {
        World self = this;
        this.map = new Map(mapFilePath);
        this.map.onReady(() -> {
            initZoneGroups();
            self.map.generateCollisionGrid();

            // Populate all mob "roaming" areas
            for (MobAreaConfig c : self.map.getMobAreas()) {
                MobArea mobArea = new MobArea(c.getId(), c.getX(), c.getY(), c.getWidth(), c.getHeight(), self, c.getNb(), c.getType());
                mobArea.spawnMob();
                mobArea.onEmpty(() -> handleEmptyMobArea(mobArea));
                mobAreas.add(mobArea);
            }

            // Create all chest areas
            for (ChestAreaConfig c : self.map.getChestAreas()) {
                // FIXME: ChestArea id = 0 ?
                ChestArea chestArea = new ChestArea(0, c.getX(), c.getY(), c.getW(), c.getH(), self, c.getI(), c.getTx(), c.getTy());
                chestAreas.add(chestArea);
                chestArea.onEmpty(() -> handleEmptyChestArea(chestArea));
            }

            // Spawn static chests
            for (StaticChestConfig c : self.map.getStaticChests()) {
                Chest chest = createChest(c.getX(), c.getY(), c.getI());
                this.addStaticItem(chest);
            }

            // Spawn static entities
            this.spawnStaticEntities();

            // Set maximum number of entities contained in each chest area
            for (ChestArea chestArea : chestAreas) {
                chestArea.setNumberOfEntities(chestArea.getEntities().size());
            }
        });

        this.map.initMap();

        int regenCount = this.ups * 2;
        final int[] updateCount = {0};
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                self.processGroups();
                self.processQueues();
                if (updateCount[0] < regenCount) {
                    updateCount[0] += 1;
                } else {
                    if (null != self.regenCallback) {
                        self.regenCallback.run();
                    }
                    updateCount[0] = 0;
                }
            }
        }, 0, 1000 / this.ups);

        logger.info(this.id + " created capacity: " + this.maxPlayer + " players");
    }

    private Chest createChest(int x, int y, List<Integer> i) {
        Chest chest = (Chest) this.createItem(Constant.TYPES_ENTITIES_CHEST, x, y);
        chest.setItems(i);
        return chest;
    }

    private Item createItem(int kind, int x, int y) {
        Item item;
        String id = "9" + this.itemCount++;
        if (kind == Constant.TYPES_ENTITIES_CHEST) {
            item = new Chest(id, x, y);
        } else {
            item = new Item(id, kind, x, y);
        }
        return item;
    }

    private Item addStaticItem(Item item) {
        item.setStatic(true);
        item.onReSpawn(() -> addStaticItem(item));
        return this.addItem(item);
    }

    private Item addItem(Item item) {
        this.addEntity(item);
        this.items.put(item.getId(), item);
        return item;
    }

    private void addEntity(Entity entity) {
        this.entities.put(entity.getId(), entity);
        this.handleEntityGroupMembership(entity);
    }

    private boolean handleEntityGroupMembership(Entity entity) {
        boolean hasChangedGroups = false;
        if (null != entity) {
            String groupId = this.map.getGroupIdFromPosition(entity.getX(), entity.getY());
            if (null == entity.getGroupId() || (null != entity.getGroupId() || !entity.getGroupId().equals(groupId))) {
                hasChangedGroups = true;
                this.addAsIncomingToGroup(entity, groupId);
                List<String> oldGroupIds = this.removeFromGroups(entity);
                List<String> newGroupIds = this.addToGroup(entity, groupId);
                if (oldGroupIds.size() > 0) {
                    assert newGroupIds != null;
                    oldGroupIds.removeAll(newGroupIds);
                    entity.setRecentlyLeftGroupIds(oldGroupIds);
                }
            }
        }
        return hasChangedGroups;
    }

    private List<String> addToGroup(Entity entity, String groupId) {
        List<String> newGroupIds = new ArrayList<>();
        World self = this;
        if (null != entity && null != this.groups.get(groupId)) {
            this.map.forEachAdjacentGroup(groupId, id -> {
                self.groups.get(id).getEntities().put(entity.getId(), entity);
                newGroupIds.add(id);
            });
            entity.setGroupId(groupId);
            if (entity instanceof Player) {
                self.groups.get(groupId).getPlayerIds().add(entity.getId());
            }
        }
        return newGroupIds;
    }

    private List<String> removeFromGroups(Entity entity) {
        List<String> oldGroupIds = new ArrayList<>();
        if (null != entity && null != entity.getGroupId()) {
            Group group = this.groups.get(entity.getGroupId());
            if (entity instanceof Player) {
                group.setPlayerIds(Util.reject(group.getPlayerIds(), id -> id.equals(entity.getId())));
            }
            World self = this;
            this.map.forEachAdjacentGroup(entity.getGroupId(), id -> {
                if (null != self.groups.get(id).getEntities().get(entity.getId())) {
                    self.groups.get(id).getEntities().remove(entity.getId());
                    oldGroupIds.add(id);
                }
            });
            entity.setGroupId(null);
        }
        return oldGroupIds;
    }

    /**
     * Registers an entity as "incoming" into several groups, meaning that it just entered them.
     * All players inside these groups will receive a Spawn message when WorldServer.processGroups is called.
     */
    // FIXME: read more
    private void addAsIncomingToGroup(Entity entity, String groupId) {
        boolean isChest = entity instanceof Chest;
        boolean isItem = entity instanceof Item;
        boolean isDroppedItem = null != entity && isItem && !((Item) entity).isStatic() && !((Item) entity).isFromChest();

        World self = this;
        if (null != entity && null != groupId && !groupId.equals("")) {
            this.map.forEachAdjacentGroup(groupId, id -> {
                Group group = self.groups.get(id);
                if (null != group) {
                    // Items dropped off of mobs are handled differently via DROP messages. See handleHurtEntity.
                    if (null != group.getEntities().get(entity.getId()) && (!isItem || isChest || !isDroppedItem)) {
                        group.getIncoming().add(entity);
                    }
                }
            });
        }
    }

    private void spawnStaticEntities() {
        int count = 0;
        for (java.util.Map.Entry<Integer, String> entry : this.map.getStaticEntities().entrySet()) {
            int tId = entry.getKey();
            String kindName = entry.getValue();
            int kind = Types.getKindFromString(kindName);
            Position pos = this.map.titleIndexToGridPostion(tId);
            if (Types.isNpc(kind)) {
                this.addNpc(kind, pos.getX() + 1, pos.getY());
            }
            if (Types.isMob(kind)) {
                Mob mob = new Mob("7" + kind + count++, kind, pos.getX() + 1, pos.getY());
                World self = this;
                mob.onReSpawn(() -> {
                    mob.setDead(false);
                    self.addMob(mob);
                    if (null != mob.getArea() && mob.getArea() instanceof ChestArea) {
                        mob.getArea().addToArea(mob);
                    }
                });
                mob.onMove(self::onMobMoveCallback);
                this.addMob(mob);
                this.tryAddingMobToChestArea(mob);
            }
            if (Types.isItem(kind)) {
                this.addStaticItem(this.createItem(kind, pos.getX() + 1, pos.getY()));
            }
        }
    }

    private void tryAddingMobToChestArea(Mob mob) {
        for (ChestArea chestArea : this.chestAreas) {
            if (chestArea.contains(mob)) {
                chestArea.addToArea(mob);
            }
        }
    }

    private void pushToAdjacentGroups(String groupId, SerializeAble message, String ignoredPlayerId) {
        World self = this;
        this.map.forEachAdjacentGroup(groupId, id -> self.pushToGroup(id, message, ignoredPlayerId));
    }

    private void pushToGroup(String groupId, SerializeAble message, String ignoredPlayerId) {
        Group group = this.groups.get(groupId);
        if (null != group) {
            for (String playerId : group.getPlayerIds()) {
                if (null != ignoredPlayerId && !playerId.equals(ignoredPlayerId)) {
                    this.pushToPlayer((Player) this.getEntityById(playerId), message);
                }
            }
        }
    }

    void pushToPlayer(Player player, SerializeAble message) {
        if (null != player && null != this.outgoingQueues.get(player.getId())) {
            this.outgoingQueues.get(player.getId()).add(message.serialize());
        } else {
            logger.error("pushToPlayer :player was undefined");
        }
    }

    Entity getEntityById(String id) {
        Entity entity = this.entities.get(id);
        if (null != entity) {
            return entity;
        }
        logger.error("Unknown entity {}", id);
        return null;
    }

    public void onMobMoveCallback(Mob mob) {
        this.pushToAdjacentGroups(mob.getGroupId(), new Move(mob), "");
        this.handleEntityGroupMembership(mob);
    }

    private void addNpc(int kind, int x, int y) {
        Npc npc = new Npc("8" + x + "" + y, kind, x, y);
        this.addEntity(npc);
        this.npcs.put(npc.getId(), npc);
    }

    private void processGroups() {
        World self = this;
        if (this.zoneGroupReady) {
            this.map.forEachGroup(id -> {
                for (Entity entity : self.groups.get(id).getIncoming()) {
                    if (entity instanceof Player) {
                        self.pushToGroup(id, new Spawn(entity), entity.getId());
                    } else {
                        self.pushToGroup(id, new Spawn(entity), null);
                    }
                }
                self.groups.get(id).getIncoming().clear();
            });
        }
    }

    private void processQueues() {
        for (java.util.Map.Entry<String, List<String>> entry : this.outgoingQueues.entrySet()) {
            String id = entry.getKey();
            if (null != this.outgoingQueues.get(id)) {
                // TODO: send message
            }
        }
    }

    private void handleEmptyMobArea(MobArea mobArea) {

    }

    private void handleEmptyChestArea(ChestArea area) {
        if (null != area) {
            Item chest = this.addItem(this.createChest(area.getChestX(), area.getChestY(), area.getItems()));
            this.handleItemDeSpawn(chest);
        }
    }

    private void handleItemDeSpawn(Item item) {
        World self = this;
        if (null != item) {
            item.handleDeSpawn(10000,
                    () -> self.pushToAdjacentGroups(item.getGroupId(), new Blink(item.getId()), ""),
                    4000,
                    () -> {
                        self.pushToAdjacentGroups(item.getGroupId(), new Destroy(item.getId()), "");
                        self.removeEntity(item);
                    });
        }
    }

    void removeEntity(Entity entity) {
        this.entities.remove(entity.getId());
        this.mobs.remove(entity.getId());
        this.items.remove(entity.getId());

        if (entity.getType().equals("mob")) {
            this.clearMobAggroLink((Mob) entity);
            this.clearMobHateLinks((Mob) entity);
        }
        entity.destroy();
        this.removeFromGroups(entity);
        logger.info("removed {} : {}", Types.getKindAsString(entity.getKind()), entity.getId());
    }

    private void clearMobHateLinks(Mob mob) {
        if (null != mob) {
            for (Hate hate : mob.getHateList()) {
                Entity player = this.getEntityById(hate.getId());
                if (null != player) {
                    assert player instanceof Player;
                    ((Player) player).removeHater(mob);
                }
            }
        }
    }

    /**
     * The mob will no longer be registered as an attacker of its current target.
     */
    private void clearMobAggroLink(Mob mob) {
        if (null != mob && null != mob.getTargetId()) {
            Entity player = this.getEntityById(mob.getTargetId());
            if (null != player) {
                assert player instanceof Player;
                ((Player) player).removeAttacker(mob);
            }
        }
    }

    private void initZoneGroups() {
        World self = this;
        this.map.forEachGroup(id -> self.groups.put(id, new Group(id)));
    }

    void addMob(Mob mob) {
        this.addEntity(mob);
        this.mobs.put(mob.getId(), mob);
    }

    void handleMobHate(String mobId, String playerId, int hatePoints) {
        Mob mob = (Mob) this.getEntityById(mobId);
        Player player = (Player) this.getEntityById(playerId);
        if (null != mob && null != player) {
            mob.increaseHateFor(playerId, hatePoints);
            player.addHater(mob);
            if (mob.getHitPoints() > 0) {
                // only choose a target if still alive
                this.chooseMobTarget(mob, 0);
            }
        }
    }

    private void chooseMobTarget(Mob mob, int hateRank) {
        Player player = (Player) this.getEntityById(mob.getHatedPlayerId(hateRank));
        // If the mob is not already attacking the player, create an attack link between them.
        if (null != player && null != player.getAttackers().get(mob.getId())) {
            this.clearMobAggroLink(mob);
            player.addAttacker(mob);
            mob.setTargetId(player.getId());
            this.broadcastAttacker(mob);
            logger.info("{} mob is now attacking player {}", mob.getId(), player.getId());
        }
    }

    void broadcastAttacker(Character character) {
        if (null != character) {
            this.pushToAdjacentGroups(character.getGroupId(), character.attack(), character.getId());
        }
        if (null != this.attackCallback) {
            this.attackCallback.accept(character);
        }
    }

    void handleHurtEntity(Character character, Entity attacker, int damage) {
        if (character.getType().equals("player")) {
            // A player is only aware of his own hitPoints
            Player player = (Player) character;
            this.pushToPlayer(player, player.health());
        }
        if (character.getType().equals("mob")) {
            // Let the mob's attacker (player) know how much damage was inflicted
            Player player = (Player) attacker;
            this.pushToPlayer(player, new Damage(character.getId(), damage));
        }

        // If the entity is about to die
        if (character.getHitPoints() <= 0) {
            if (character.getType().equals("mob")) {
                Mob mob = (Mob) character;
                Item item = this.getDroppedItem(mob);

                Player player = (Player) attacker;
                this.pushToPlayer(player, new Kill(mob.getKind()));
                // DeSpawn must be enqueued before the item drop
                this.pushToAdjacentGroups(mob.getGroupId(), mob.deSpawn(), null);
                if (null != item) {
                    this.pushToAdjacentGroups(mob.getGroupId(), mob.drop(item), null);
                    this.handleItemDeSpawn(item);
                }
            }
            if (character.getType().equals("player")) {
                this.handlePlayerVanish((Player) character);
                this.pushToAdjacentGroups(character.getGroupId(), character.deSpawn(), null);
            }
            this.removeEntity(character);
        }
    }

    void handlePlayerVanish(Player player) {
        List<Mob> previousAttackers = new ArrayList<>();
        World self = this;
        // When a player dies or teleports, all of his attackers go and attack their second most hated $player->
        player.forEachAttacker(mob -> {
            previousAttackers.add(mob);
            self.chooseMobTarget(mob, 2);
        });
        for (Mob mob : previousAttackers) {
            player.removeAttacker(mob);
            mob.clearTarget();
            mob.forgetPlayer(player.getId(), 1000);
        }
        this.handleEntityGroupMembership(player);
    }

    private Item getDroppedItem(Mob mob) {
        String kind = Types.getKindAsString(mob.getKind());
        String itemName = Properties.getRandomDropItemName(kind);
        return this.addItem(this.createItem(Types.getKindFromString(itemName), mob.getX(), mob.getY()));
    }

    void pushSpawnsToPlayer(Player player, List<String> ids) {
        for (String id : ids) {
            Entity entity = this.getEntityById(id);
            if (null != entity) {
                this.pushToPlayer(player, new Spawn(entity));
            } else {
                logger.error("bad id:{} ids", ids);
            }
        }
    }

    void addPlayer(Player player) {
        this.addEntity(player);
        this.players.put(player.getId(), player);
        this.outgoingQueues.put(player.getId(), new ArrayList<>());
    }

    void handleOpenedChest(Chest chest, Player player) {
        this.pushToAdjacentGroups(chest.getGroupId(), chest.deSpawn(), null);
        this.removeEntity(chest);
        int kind = chest.getRandomItem();
        if (kind > 0) {
            Item item = this.addItemFromChest(kind, chest.getX(), chest.getY());
            this.handleItemDeSpawn(item);
        }
    }

    private Item addItemFromChest(int kind, int x, int y) {
        Item item = this.createItem(kind, x, y);
        item.setFromChest(true);
        return this.addItem(item);
    }
}
