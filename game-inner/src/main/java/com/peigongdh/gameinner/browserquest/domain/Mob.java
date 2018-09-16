package com.peigongdh.gameinner.browserquest.domain;

import com.peigongdh.gameinner.browserquest.domain.message.Drop;
import com.peigongdh.gameinner.browserquest.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class Mob extends Character {

    private int spawningX;

    private int spawningY;

    private int armorLevel;

    private int weaponLevel;

    private List<Hate> hateList;

    private Timer reSpawnTimer;

    private Timer returnTimer;

    private Consumer<Mob> reSpawnCallback;

    private Consumer<Mob> moveCallback;

    public Mob(int id, int kind, int x, int y) {
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

    public void destroy() {
        this.setDead(true);
        this.hateList.clear();
        this.clearTarget();
        this.updateHitPoints();
        this.resetPosition();
        this.handleReSpawn();
    }

    public void receiveDamage(int points) {
        this.setHitPoints(this.getHitPoints() - points);
    }

    private boolean hates(int playerId) {
        return Util.any(this.hateList, hate -> hate.getId() == playerId);
        // or use stream anyMatch
        // return this.hateList.stream().anyMatch(hate -> hate.getId() == playerId);
    }

    public void increaseHateFor(int playerId, int points) {
        if (this.hates(playerId)) {
            Hate h = Util.detect(this.hateList, hate -> hate.getId() == playerId);
            if (null != h) {
                h.setHate(h.getHate() + points);
            }
        } else {
            this.hateList.add(new Hate(playerId, points));
        }
    }

    public void forgetPlayer(int playerId, int duration) {
        this.hateList = Util.reject(this.hateList, hate -> hate.getId() == playerId);
        // or use stream filter
        // this.hateList = this.hateList.stream().filter(hate -> hate.getId() != playerId).collect(Collectors.toList());
        if (this.hateList.isEmpty()) {
            this.returnToSpawningPosition(duration);
        }
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
            this.reSpawnCallback.accept(this);
        }
    }

    public void onReSpawn(Consumer<Mob> callback) {
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

    public void onMove(Consumer<Mob> callback) {
        this.moveCallback = callback;
    }

    public void move(int x, int y) {
        this.setPosition(x, y);
        if (null != this.moveCallback) {
            moveCallback.accept(this);
        }
    }

    private void updateHitPoints() {
        this.resetHitPoints(Properties.getHitPoints(this.getKind()));
    }

    public int distanceToSpawningPoint(int x, int y) {
        return Util.distanceTo(x, y, this.spawningX, this.spawningY);
    }

}
