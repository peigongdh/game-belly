package com.peigongdh.gameinner.browserquest.domain;

import com.peigongdh.gameinner.browserquest.common.Properties;
import com.peigongdh.gameinner.browserquest.domain.message.Drop;
import com.peigongdh.gameinner.browserquest.util.Util;

import java.util.*;
import java.util.function.Consumer;

public class Mob extends Character {

    private int spawningX;

    private int spawningY;

    private int armorLevel;

    private int weaponLevel;

    private List<Hate> hateList;

    private Timer reSpawnTimer;

    private Timer returnTimer;

    private Runnable reSpawnCallback;

    private Consumer<Mob> moveCallback;

    public int getSpawningX() {
        return spawningX;
    }

    public void setSpawningX(int spawningX) {
        this.spawningX = spawningX;
    }

    public int getSpawningY() {
        return spawningY;
    }

    public void setSpawningY(int spawningY) {
        this.spawningY = spawningY;
    }

    public int getArmorLevel() {
        return armorLevel;
    }

    public void setArmorLevel(int armorLevel) {
        this.armorLevel = armorLevel;
    }

    public int getWeaponLevel() {
        return weaponLevel;
    }

    public void setWeaponLevel(int weaponLevel) {
        this.weaponLevel = weaponLevel;
    }

    public List<Hate> getHateList() {
        return hateList;
    }

    public void setHateList(List<Hate> hateList) {
        this.hateList = hateList;
    }

    public Timer getReSpawnTimer() {
        return reSpawnTimer;
    }

    public void setReSpawnTimer(Timer reSpawnTimer) {
        this.reSpawnTimer = reSpawnTimer;
    }

    public Timer getReturnTimer() {
        return returnTimer;
    }

    public void setReturnTimer(Timer returnTimer) {
        this.returnTimer = returnTimer;
    }

    public Runnable getReSpawnCallback() {
        return reSpawnCallback;
    }

    public void setReSpawnCallback(Runnable reSpawnCallback) {
        this.reSpawnCallback = reSpawnCallback;
    }

    public Consumer<Mob> getMoveCallback() {
        return moveCallback;
    }

    public void setMoveCallback(Consumer<Mob> moveCallback) {
        this.moveCallback = moveCallback;
    }

    public Mob(String id, int kind, int x, int y) {
        super(id, "mob", kind, x, y);
        this.spawningX = x;
        this.spawningY = y;
        this.armorLevel = Properties.getArmorLevel(kind);
        this.weaponLevel = Properties.getWeaponLevel(kind);
        this.hateList = new ArrayList<>();
        this.reSpawnTimer = null;
        this.returnTimer = null;
        this.setDead(false);
    }

    @Override
    public void destroy() {
        this.setDead(true);
        this.hateList.clear();
        this.clearTarget();
        this.updateHitPoints();
        this.resetPosition();
        this.handleReSpawn();
    }

    public void receiveDamage(int points, String playerId) {
        this.setHitPoints(this.getHitPoints() - points);
    }

    private boolean hates(String playerId) {
        return Util.any(this.hateList, hate -> hate.getId().equals(playerId));
        // or use stream anyMatch
        // return this.hateList.stream().anyMatch(hate -> hate.getId() == playerId);
    }

    void increaseHateFor(String playerId, int points) {
        if (this.hates(playerId)) {
            Hate h = Util.detect(this.hateList, hate -> hate.getId().equals(playerId));
            if (null != h) {
                h.setHate(h.getHate() + points);
            }
        } else {
            this.hateList.add(new Hate(playerId, points));
        }
    }

    String getHatedPlayerId(int hateRank) {
        String playerId;
        int i;
        int size = this.hateList.size();
        if (hateRank > 0 && hateRank <= size) {
            i = size - hateRank;
        } else {
            i = size - 1;
        }
        this.hateList.sort(Comparator.comparing(Hate::getHate));
        playerId = this.hateList.get(i).getId();
        return playerId;
    }

    public void forgetPlayer(String playerId, int duration) {
        this.hateList = Util.reject(this.hateList, hate -> hate.getId().equals(playerId));
        // or use stream filter
        // this.hateList = this.hateList.stream().filter(hate -> hate.getId() != playerId).collect(Collectors.toList());
        if (this.hateList.isEmpty()) {
            this.returnToSpawningPosition(duration);
        }
    }

    void forgetEveryone() {
        this.hateList.clear();
        this.returnToSpawningPosition(1);
    }

    public Drop drop(Item item) {
        if (null != item) {
            return new Drop(this, item);
        }
        return null;
    }


    private void handleReSpawn() {
        int delay = 30000;
        if (null != this.getArea() && this.getArea() instanceof MobArea) {
            // Respawn inside the area if part of a MobArea
            ((MobArea) this.getArea()).reSpawnMob(this, delay);
        } else {
            if (null != this.getArea() && this.getArea() instanceof ChestArea) {
                this.getArea().removeFromArea(this);
            }
            this.reSpawnTimer = new Timer();
            this.reSpawnTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    reSpawnCallback();
                }
            }, delay);
        }
    }

    private void reSpawnCallback() {
        if (null != this.reSpawnCallback) {
            this.reSpawnCallback.run();
        }
    }

    void onReSpawn(Runnable callback) {
        this.reSpawnCallback = callback;
    }

    private void resetPosition() {
        this.setPosition(this.spawningX, this.spawningY);
    }

    private void returnToSpawningPosition(int waitDuration) {
        int delay = waitDuration != 0 ? waitDuration : 4000;
        this.clearTarget();
        this.returnTimer = new Timer();
        this.returnTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                timeoutCallback();
            }
        }, delay);
    }

    private void timeoutCallback() {
        this.resetPosition();
        this.move(this.getX(), this.getY());
    }

    void onMove(Consumer<Mob> callback) {
        this.moveCallback = callback;
    }

    void move(int x, int y) {
        this.setPosition(x, y);
        if (null != this.moveCallback) {
            moveCallback.accept(this);
        }
    }

    private void updateHitPoints() {
        this.resetHitPoints(Properties.getHitPoints(this.getKind()));
    }

    int distanceToSpawningPoint(int x, int y) {
        return Util.distanceTo(x, y, this.spawningX, this.spawningY);
    }

}
