package com.peigongdh.gameinner.browserquest.domain;

import com.peigongdh.gameinner.browserquest.domain.message.DeSpawn;
import com.peigongdh.gameinner.browserquest.domain.message.SerializeAble;
import javafx.util.Pair;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Player extends Character {

    private boolean hasEnteredGame;

    private List<Hate> hater;

    private Checkpoint lastCheckpoint;

    private int disconnectTimeout;

    private int armor;

    private int armorLevel;

    private World world;

    private int weaponLevel;

    private String name;

    private Supplier<Position> requestPositionCallback;

    private Consumer<Pair<SerializeAble, Boolean>> broadcastCallback;

    public boolean isHasEnteredGame() {
        return hasEnteredGame;
    }

    public void setHasEnteredGame(boolean hasEnteredGame) {
        this.hasEnteredGame = hasEnteredGame;
    }

    public List<Hate> getHater() {
        return hater;
    }

    public void setHater(List<Hate> hater) {
        this.hater = hater;
    }

    public Checkpoint getLastCheckpoint() {
        return lastCheckpoint;
    }

    public void setLastCheckpoint(Checkpoint lastCheckpoint) {
        this.lastCheckpoint = lastCheckpoint;
    }

    public int getDisconnectTimeout() {
        return disconnectTimeout;
    }

    public void setDisconnectTimeout(int disconnectTimeout) {
        this.disconnectTimeout = disconnectTimeout;
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

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public int getWeaponLevel() {
        return weaponLevel;
    }

    public void setWeaponLevel(int weaponLevel) {
        this.weaponLevel = weaponLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player(String id, String type, int kind, int x, int y) {
        super(id, type, kind, x, y);
    }

    void onRequestPosition(Supplier<Position> callback) {
        this.requestPositionCallback = callback;
    }

    void removeHater(Mob mob) {
        // TODO
    }

    void onMove(Consumer<Position> callback) {
        // TODO
    }

    void onLootMove(Consumer<Position> callback) {
        // TODO
    }

    void onZone(Runnable callback) {
        // TODO
    }

    void onBroadcast(Consumer<Pair<SerializeAble, Boolean>> callback) {
        // TODO
    }

    void onBroadcastToZone(Consumer<Pair<SerializeAble, Boolean>> callback) {
        // TODO
    }

    void onExit(Runnable callback) {
        // TODO
    }

    public void broadcast(SerializeAble message, boolean ignoreSelf) {
        if (null != this.broadcastCallback) {
            this.broadcastCallback.accept(new Pair<>(message, ignoreSelf));
        }
    }

    public void addHater(Mob mob) {
    }
}
