package com.peigongdh.gameinner.browserquest.domain;

import com.peigongdh.gameinner.browserquest.common.Constant;
import com.peigongdh.gameinner.browserquest.common.Types;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MobArea extends Area {

    private int nb;

    private String kind;

    public MobArea(int id, int x, int y, int width, int height, World world, int nb, String kind) {
        super(id, x, y, width, height, world);
        this.nb = nb;
        this.kind = kind;
    }

    public void spawnMob() {
        for (int i = 0; i < this.nb; i++) {
            this.addToArea(this.createMobInsideArea());
        }
    }

    private Entity createMobInsideArea() {
        // FIXME: id is string
        int k = Types.getKindFromString(this.kind);
        Position pos = this.getRandomPositionInsideArea();
        Mob mob = new Mob(this.getId(), k, pos.getX(), pos.getY());

        // FIXME: onMove
        // mob.onMove();
        return mob;
    }

    public void reSpawnMob(Mob mob, int delay) {
        this.removeFromArea(mob);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                reSpawnMobCallback(mob);
            }
        }, delay);
    }

    private void reSpawnMobCallback(Mob mob) {
        Position pos = this.getRandomPositionInsideArea();
        mob.setX(pos.getX());
        mob.setY(pos.getY());
        mob.setDead(false);
        this.addToArea(mob);
        this.getWorld().addMob(mob);
    }

    public void initRoaming() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                initRoamingCallback();
            }
        }, 0, 500);
    }

    private void initRoamingCallback() {
        this.getEntities().forEach((integer, entity) -> initRoamingEachCallback((Mob) entity));
    }

    private void initRoamingEachCallback(Mob mob) {
        boolean canRoam = new Random().nextInt(20) == 1;
        if (canRoam) {
            if (!mob.hasTarget() && !mob.isDead()) {
                Position pos = this.getRandomPositionInsideArea();
                mob.move(pos.getX(), pos.getY());
            }
        }
    }

    public Reward createReward() {
        Position pos = this.getRandomPositionInsideArea();
        return new Reward(pos.getX(), pos.getY(), Constant.TYPES_ENTITIES_CHEST);
    }
}
