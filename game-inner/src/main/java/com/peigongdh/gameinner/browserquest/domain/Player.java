package com.peigongdh.gameinner.browserquest.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.peigongdh.gameinner.browserquest.common.Constant;
import com.peigongdh.gameinner.browserquest.common.Properties;
import com.peigongdh.gameinner.browserquest.common.Types;
import com.peigongdh.gameinner.browserquest.domain.message.*;
import com.peigongdh.gameinner.browserquest.util.Util;
import io.netty.channel.ChannelHandlerContext;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Player extends Character {

    private static final Logger logger = LoggerFactory.getLogger(Player.class);

    private boolean hasEnteredGame;

    private ConcurrentHashMap<Integer, Mob> hater;

    private Checkpoint lastCheckpoint;

    private Timer disconnectTimer;

    private Timer firePotionTimer;

    private int armor;

    private int armorLevel;

    private int weapon;

    private int weaponLevel;

    private ChannelHandlerContext ctx;

    private World world;

    private String name;

    private Supplier<Position> requestPositionCallback;

    private Consumer<Pair<SerializeAble, Boolean>> broadcastCallback;

    private Consumer<Pair<SerializeAble, Boolean>> broadcastZoneCallback;

    private Runnable exitCallback;

    private Consumer<Position> moveCallback;

    private Consumer<Position> lootMoveCallback;

    private Runnable zoneCallback;

    private Runnable connectCallback;

    public boolean isHasEnteredGame() {
        return hasEnteredGame;
    }

    public void setHasEnteredGame(boolean hasEnteredGame) {
        this.hasEnteredGame = hasEnteredGame;
    }

    public ConcurrentHashMap<Integer, Mob> getHater() {
        return hater;
    }

    public void setHater(ConcurrentHashMap<Integer, Mob> hater) {
        this.hater = hater;
    }

    public Checkpoint getLastCheckpoint() {
        return lastCheckpoint;
    }

    public void setLastCheckpoint(Checkpoint lastCheckpoint) {
        this.lastCheckpoint = lastCheckpoint;
    }

    public Timer getDisconnectTimer() {
        return disconnectTimer;
    }

    public void setDisconnectTimer(Timer disconnectTimer) {
        this.disconnectTimer = disconnectTimer;
    }

    public Timer getFirePotionTimer() {
        return firePotionTimer;
    }

    public void setFirePotionTimer(Timer firePotionTimer) {
        this.firePotionTimer = firePotionTimer;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public int getArmorLevel() {
        return armorLevel;
    }

    public void setArmorLevel(int armorLevel) {
        this.armorLevel = armorLevel;
    }

    public int getWeapon() {
        return weapon;
    }

    public void setWeapon(int weapon) {
        this.weapon = weapon;
    }

    public int getWeaponLevel() {
        return weaponLevel;
    }

    public void setWeaponLevel(int weaponLevel) {
        this.weaponLevel = weaponLevel;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Runnable getConnectCallback() {
        return connectCallback;
    }

    public void setConnectCallback(Runnable connectCallback) {
        this.connectCallback = connectCallback;
    }

    public Player(int id, ChannelHandlerContext ctx, World world) {
        super(id, "player", Constant.TYPES_ENTITIES_WARRIOR, 0, 0);
        this.ctx = ctx;
        this.world = world;

        this.hasEnteredGame = false;
        this.setDead(false);
        this.hater = new ConcurrentHashMap<>();
        this.lastCheckpoint = null;
        this.disconnectTimer = null;
        this.onConnect(() -> this.send("go"));
    }

    public void onConnect(Runnable callback) {
        this.connectCallback = callback;
    }

    public void onClientMessage(String data) {
        if ("".equals(data)) {
            return;
        }
        JSONArray message = JSONObject.parseArray(data);
        Integer action = message.getInteger(0);

        if (!this.hasEnteredGame && action != Constant.TYPES_MESSAGES_HELLO) {
            this.ctx.close();
            return;
        }

        this.resetTimeout();

        switch (action) {
            case Constant.TYPES_MESSAGES_HELLO:
                this.name = message.getString(1);
                this.kind = Constant.TYPES_ENTITIES_WARRIOR;
                this.equipArmor(message.getInteger(2));
                this.equipWeapon(message.getInteger(3));
                this.orientation = Util.randomOrientation();
                this.updateHitPoints();
                this.updatePosition();

                this.world.addPlayer(this);
                this.world.getEnterCallback().accept(this);

                SerializeAble welcome = new Welcome(this.id, this.name, this.x, this.y, this.hitPoints);
                this.send(JSON.toJSONString(welcome.serialize()));
                this.hasEnteredGame = true;
                this.dead = false;
                break;
            case Constant.TYPES_MESSAGES_WHO:
                List<Integer> ids = new ArrayList<>();
                for (Object m : message) {
                    ids.add((Integer) m);
                }
                // FIXME: array_shift ?
                ids.remove(0);
                this.world.pushSpawnsToPlayer(this, ids);
                break;
            case Constant.TYPES_MESSAGES_ZONE:
                this.zoneCallback.run();
                break;
            case Constant.TYPES_MESSAGES_CHAT:
                String chatMsg = message.getString(1).trim();
                // Sanitized messages may become empty. No need to broadcast empty chat messages.
                if (!chatMsg.equals("")) {
                    this.broadcastToZone(new Chat(this.id, chatMsg), false);
                }
                break;
            case Constant.TYPES_MESSAGES_MOVE:
                if (null != this.moveCallback) {
                    int x = message.getInteger(1);
                    int y = message.getInteger(2);
                    if (this.world.isValidPosition(new Position(x, y))) {
                        this.setPosition(x, y);
                        this.clearTarget();
                        SerializeAble move = new Move(this);
                        this.broadcast(move, true);
                    }
                }
                break;
            case Constant.TYPES_MESSAGES_LOOTMOVE:
                if (null != this.lootMoveCallback) {
                    int lootMoveX = message.getInteger(1);
                    int lootMoveY = message.getInteger(2);
                    int id = message.getInteger(3);
                    this.setPosition(lootMoveX, lootMoveY);
                    Entity item = this.world.getEntityById(id);
                    if (null != item) {
                        this.clearTarget();
                        SerializeAble lootMove = new LootMove(this.id, item.getId());
                        this.broadcast(lootMove, true);
                        this.lootMoveCallback.accept(new Position(lootMoveX, lootMoveY));
                    }
                }
                break;
            case Constant.TYPES_MESSAGES_AGGRO:
                if (null != this.moveCallback) {
                    int mobId = message.getInteger(1);
                    this.world.handleMobHate(mobId, this.id, 5);
                }
                break;
            case Constant.TYPES_MESSAGES_ATTACK:
                int attackMobId = message.getInteger(1);
                Entity attackMob = this.world.getEntityById(attackMobId);
                if (null != attackMob) {
                    this.setTargetId(attackMob.getId());
                    this.world.broadcastAttacker(this);
                }
                break;
            case Constant.TYPES_MESSAGES_HIT:
                int hitMobId = message.getInteger(1);
                Mob hitMob = (Mob) this.world.getEntityById(hitMobId);
                if (null != hitMob) {
                    int dmg = Formulas.dmg(this.weaponLevel, hitMob.getArmorLevel());
                    if (dmg > 0) {
                        hitMob.receiveDamage(dmg, this.id);
                        this.world.handleMobHate(hitMob.getId(), this.id, dmg);
                        this.world.handleHurtEntity(hitMob, this, dmg);
                    }
                }
                break;
            case Constant.TYPES_MESSAGES_HURT:
                int hurtMobId = message.getInteger(1);
                Mob hurtMob = (Mob) this.world.getEntityById(hurtMobId);
                if (null != hurtMob) {
                    this.hitPoints -= Formulas.dmg(hurtMob.getWeaponLevel(), this.armorLevel);
                    this.world.handleHurtEntity(this, null, 0);
                    if (this.hitPoints <= 0) {
                        this.dead = true;
                        if (null != this.firePotionTimer) {
                            this.firePotionTimer.cancel();
                            this.firePotionTimer = null;
                        }
                    }
                }
                break;
            case Constant.TYPES_MESSAGES_LOOT:
                Entity lootEntity = this.world.getEntityById(message.getInteger(1));
                if (null != lootEntity) {
                    int lootKind = lootEntity.getKind();
                    if (Types.isItem(lootEntity.getKind())) {
                        Item item = (Item) lootEntity;
                        this.broadcast(item.deSpawn(), true);
                        this.world.removeEntity(item);

                        if (lootKind == Constant.TYPES_ENTITIES_FIREPOTION) {
                            this.updateHitPoints();
                            this.broadcast(this.equip(Constant.TYPES_ENTITIES_FIREFOX), true);
                            this.firePotionTimer = new Timer();
                            Player self = this;
                            this.firePotionTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    self.firePotionTimeoutCallback();
                                }
                            }, 15 * 1000);
                        } else if (Types.isHealingItem(lootKind)) {
                            int amount = 0;
                            switch (lootKind) {
                                case Constant.TYPES_ENTITIES_FLASK:
                                    amount = 40;
                                    break;
                                case Constant.TYPES_ENTITIES_BURGER:
                                    amount = 50;
                                    break;
                            }
                            if (!this.hasFullHealth()) {
                                this.regenHealthBy(amount);
                                this.world.pushToPlayer(this, this.health());
                            }
                        } else if (Types.isArmor(lootKind) || Types.isWeapon(lootKind)) {
                            this.equipItem(item);
                            this.broadcast(this.equip(lootKind), true);
                        }
                    }
                }
                break;
            case Constant.TYPES_MESSAGES_TELEPORT:
                int lootX = message.getInteger(1);
                int lootY = message.getInteger(2);
                if (this.world.isValidPosition(new Position(lootX, lootY))) {
                    this.setPosition(lootX, lootY);
                    this.clearTarget();
                    SerializeAble teleport = new Teleport(this.id, this.x, this.y);
                    this.broadcast(teleport, true);
                    this.world.handlePlayerVanish(this);
                    this.world.pushRelevantEntityListTo(this);
                }
                break;
            case Constant.TYPES_MESSAGES_OPEN:
                Entity chest = this.world.getEntityById(message.getInteger(1));
                if (chest instanceof Chest) {
                    this.world.handleOpenedChest((Chest) chest, this);
                }
                break;
            case Constant.TYPES_MESSAGES_CHECK:
                Checkpoint checkpoint = this.world.getMap().getCheckpoint(message.getInteger(1));
                if (null != checkpoint) {
                    this.lastCheckpoint = checkpoint;
                }
                break;
            default:
                logger.error("error message {}", message);
        }
    }

    public void onClientClose() {
        if (null != this.firePotionTimer) {
            this.firePotionTimer.cancel();
            this.firePotionTimer = null;
        }
        if (null != this.disconnectTimer) {
            this.disconnectTimer.cancel();
            this.disconnectTimer = null;
        }
        if (null != this.exitCallback) {
            this.exitCallback.run();
        }
    }

    public void firePotionTimeoutCallback() {
        // return to normal after 15 sec
        this.broadcast(this.equip(this.armor), true);
        this.firePotionTimer = null;
    }

    private EquipItem equip(int armor) {
        return new EquipItem(this.getId(), armor);
    }

    public void destroy() {
        this.forEachAttacker(Character::clearTarget);
        this.attackers.clear();
        this.forEachHater(mob -> mob.forgetPlayer(this.id, 0));
        this.hater.clear();
    }

    @Override
    public List<Object> getState() {
        List<Object> list = new ArrayList<>(this.getBaseState());
        list.add(this.name);
        list.add(this.orientation);
        list.add(this.armor);
        list.add(this.weapon);
        if (0 != this.targetId) {
            list.add(targetId);
        }
        return list;
    }

    public void send(String message) {
        this.ctx.writeAndFlush(message);
    }

    void broadcast(SerializeAble message, boolean ignoreSelf) {
        if (null != this.broadcastCallback) {
            this.broadcastCallback.accept(new Pair<>(message, ignoreSelf));
        }
    }

    void broadcastToZone(SerializeAble message, boolean ignoreSelf) {
        if (null != this.broadcastZoneCallback) {
            this.broadcastZoneCallback.accept(new Pair<>(message, ignoreSelf));
        }
    }

    void addHater(Mob mob) {
        if (null != mob) {
            if (null != this.hater.get(mob.getId())) {
                this.hater.put(mob.getId(), mob);
            }
        }
    }

    void removeHater(Mob mob) {
        if (null != mob) {
            if (null != this.hater.get(mob.getId())) {
                this.hater.remove(mob.getId());
            }
        }
    }

    void forEachHater(Consumer<Mob> callback) {
        this.hater.forEach((s, mob) -> {
            callback.accept(mob);
        });
    }

    void equipArmor(int kind) {
        this.armor = kind;
        this.armorLevel = Properties.getArmorLevel(kind);
    }

    void equipWeapon(int kind) {
        this.weapon = kind;
        this.weaponLevel = Properties.getWeaponLevel(kind);
    }

    void equipItem(Item item) {
        if (null != item) {
            if (Types.isArmor(item.getKind())) {
                this.equip(item.getKind());
                this.updateHitPoints();
                SerializeAble message = new HitPoints(this.maxHitPoints);
                this.send(JSON.toJSONString(message.serialize()));
            } else if (Types.isWeapon(item.getKind())) {
                this.equipWeapon(item.getKind());
            }
        }
    }

    private void updateHitPoints() {
        this.resetHitPoints(Formulas.hp(this.armorLevel));
    }

    public void updatePosition() {
        if (null != this.requestPositionCallback) {
            Position pos = this.requestPositionCallback.get();
            this.setPosition(pos);
        }
    }

    private void resetTimeout() {
        if (null != disconnectTimer) {
            this.disconnectTimer.cancel();
            this.disconnectTimer.purge();
        }
        this.disconnectTimer = new Timer();
        Player self = this;
        this.disconnectTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                self.ctx.writeAndFlush("timeout");
            }
        }, 15 * 60 * 1000, 15 * 60 * 1000);
    }

    void onRequestPosition(Supplier<Position> callback) {
        this.requestPositionCallback = callback;
    }

    void onMove(Consumer<Position> callback) {
        this.moveCallback = callback;
    }

    void onLootMove(Consumer<Position> callback) {
        this.lootMoveCallback = callback;
    }

    void onZone(Runnable callback) {
        this.zoneCallback = callback;
    }

    void onBroadcast(Consumer<Pair<SerializeAble, Boolean>> callback) {
        this.broadcastCallback = callback;
    }

    void onBroadcastToZone(Consumer<Pair<SerializeAble, Boolean>> callback) {
        this.broadcastZoneCallback = callback;
    }

    void onExit(Runnable callback) {
        this.exitCallback = callback;
    }
}
