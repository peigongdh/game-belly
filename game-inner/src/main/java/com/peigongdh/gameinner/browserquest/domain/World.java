package com.peigongdh.gameinner.browserquest.domain;

import com.peigongdh.gameinner.client.RegisterClient;
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

    private ConcurrentHashMap<Integer, Entity> entities;

    private ConcurrentHashMap<Integer, Player> players;

    private ConcurrentHashMap<Integer, Mob> mobs;

    private ConcurrentHashMap<Integer, Entity> attackers;

    private ConcurrentHashMap<Integer, Item> items;

    // FIXME: ?
    // equipping, hurt

    private ConcurrentHashMap<Integer, Npc> npcs;

    private List<MobArea> mobAreas;

    private List<ChestArea> chestAreas;

    private ConcurrentHashMap<Integer, Group> groups;

    private List<ConcurrentHashMap<Integer, String>> outgoingQueues;

    private int itemCount;

    private int playerCount;

    private boolean zoneGroupReady;

    private Consumer<Player> connectCallback;

    private Consumer<Player> enterCallback;

    private Consumer<Player> addedCallback;

    private Consumer<Player> removedCallback;

    private Consumer<Entity> regenCallback;

    private Consumer<Entity> entityCallback;

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
        this.outgoingQueues = new ArrayList<>();
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
            // TODO
        });
        this.onPlayerAdded(player -> {
            // TODO
        });
        this.onPlayerRemoved(player -> {
            // TODO
        });
        this.onEntityAttack(entity -> {
            // TODO
        });
        this.onRegenTick(entity -> {
            // TODO
        });
    }

    public void onPlayerConnect(Consumer<Player> callback) {
        this.connectCallback = callback;
    }

    public void onPlayerEnter(Consumer<Player> callback) {
        this.enterCallback = callback;
    }

    public void onPlayerAdded(Consumer<Player> callback) {
        this.addedCallback = callback;
    }

    public void onPlayerRemoved(Consumer<Player> callback) {
        this.removedCallback = callback;
    }

    public void onEntityAttack(Consumer<Entity> callback) {
        this.entityCallback = callback;
    }

    public void onRegenTick(Consumer<Entity> callback) {
        this.regenCallback = callback;
    }

    public boolean isValidPosition(Position pos) {
        // TODO
        return true;
    }

    public void run(String mapFilePath) {
        World self = this;
        this.map = new Map(mapFilePath);
        this.map.onReady(map1 -> {
            initZoneGroups();
            map1.generateCollisionGrid();

            // Populate all mob "roaming" areas
            for (MobAreaConfig c : map1.getMobAreas()) {
                MobArea mobArea = new MobArea(c.getId(), c.getX(), c.getY(), c.getWidth(), c.getHeight(), self, c.getNb(), c.getType());
                mobArea.spawnMob();
                mobArea.onEmpty(area -> {
                    handleEmptyMobArea(mobArea);
                });
                mobAreas.add(mobArea);
            }

            // Create all chest areas
            for (ChestAreaConfig c : map1.getChestAreas()) {
                // FIXME: id = 0 ?
                ChestArea chestArea = new ChestArea(0, c.getX(), c.getY(), c.getW(), c.getH(), self, c.getI(), c.getTx(), c.getTy());
                chestAreas.add(chestArea);
                chestArea.onEmpty(area -> {
                    handleEmptyChestArea(chestArea);
                });
            }

            // Spawn static chests
            for (StaticChestConfig c : map1.getStaticChests()) {
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
                        self.regenCallback.accept(null);
                    }
                    updateCount[0] = 0;
                }
            }
        }, 0, 1000 / this.ups);

        logger.info(this.id + " created capacity: " + this.maxPlayer + " players");
    }

    private Chest createChest(int x, int y, List<Integer> i) {
        // TODO
        return null;
    }

    private void addStaticItem(Chest chest) {
        // TODO
    }

    private void spawnStaticEntities() {
        // TODO
    }

    private void processGroups() {
        // TODO
    }

    private void processQueues() {
        // TODO
    }

    private void handleEmptyMobArea(MobArea mobArea) {

    }

    private void handleEmptyChestArea(ChestArea chestArea) {
        // TODO
    }

    public void initZoneGroups() {
        // TODO
    }

    public void addMob(Entity entity) {
    }
}
