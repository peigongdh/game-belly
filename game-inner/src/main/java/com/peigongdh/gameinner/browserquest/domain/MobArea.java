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
        this.setNumberOfEntities(nb);
        // this.initRoaming();
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
        // FIXME: check mod id
        Mob mob = new Mob(Integer.parseInt("1" + this.getId() + "" + k + "" + this.entities.size()), k, pos.getX(), pos.getY());
        mob.onMove(mob1 -> this.world.onMobMoveCallback(mob1));
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
        }, 500, 500);
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
