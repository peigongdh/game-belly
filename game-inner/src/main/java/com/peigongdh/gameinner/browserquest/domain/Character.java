package com.peigongdh.gameinner.browserquest.domain;

import com.peigongdh.gameinner.browserquest.domain.message.Attack;
import com.peigongdh.gameinner.browserquest.domain.message.Health;
import com.peigongdh.gameinner.browserquest.util.Util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class Character extends Entity {

    private int orientation;

    private int maxHitPoints;

    private int hitPoints;

    private int targetId;

    private ConcurrentHashMap<Integer, Entity> attackers;

    public int getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public Character(int id, String type, int kind, int x, int y) {
        super(id, type, kind, x, y);
        this.orientation = Util.randomOrientation();
        this.maxHitPoints = 100;
        this.hitPoints = 10;
        this.targetId = 0;
        this.attackers = new ConcurrentHashMap<>();
    }

    public void resetHitPoints(int maxHitPoints) {
        this.maxHitPoints = maxHitPoints;
        this.hitPoints = this.maxHitPoints;
    }

    public void regenHealthBy(int value) {
        int hp = this.hitPoints;
        int max = this.maxHitPoints;
        if (hp < max) {
            if (hp + value <= max) {
                this.hitPoints += value;
            } else {
                this.hitPoints = max;
            }
        }
    }

    public boolean hasFullHealth() {
        return this.hitPoints == this.maxHitPoints;
    }

    public void setTargetId(Entity target) {
        this.targetId = target.getId();
    }

    public void clearTarget() {
        this.targetId = 0;
    }

    public boolean hasTarget() {
        return this.targetId != 0;
    }

    public Attack attack() {
        return new Attack(this.getId(), this.targetId);
    }

    public Health health() {
        return new Health(this.hitPoints, false);
    }

    public Health regen() {
        return new Health(this.hitPoints, true);
    }

    public void addAttacker(Entity entity) {
        if (null != entity) {
            this.attackers.put(entity.getId(), entity);
        }
    }

    public void removeAttacker(Entity entity) {
        this.attackers.remove(entity.getId());
    }

    public void forEachAttacker(Consumer<Entity> callback) {
        for (Map.Entry<Integer, Entity> entry : this.attackers.entrySet()) {
            callback.accept(entry.getValue());
        }
    }


    /**
     * TEST lambda
     * FIXME: remove
     */
    private Consumer<Entity> callback;

    public void setForEachAttackerCallback(Consumer<Entity> callback) {
        this.callback = callback;
    }

    public void forEachAttacker2() {
        for (Map.Entry<Integer, Entity> entry : this.attackers.entrySet()) {
            this.callback.accept(entry.getValue());
        }
    }

    public void forEachAttacker3() {
        this.callback.accept(this);
    }

    public static void main(String[] args) {
        Character c1 = new Character(1, "", 0, 0, 0);
        Character c2 = new Character(2, "", 0, 0, 0);
        Character c3 = new Character(3, "", 0, 0, 0);
        c1.addAttacker(c2);
        c1.addAttacker(c3);
        c1.setForEachAttackerCallback(entity -> System.out.println(entity.getId()));
        // c1.forEachAttacker2();
        c1.forEachAttacker3();
        // c1.forEachAttacker(entity -> System.out.println(entity.getId()));
    }
}
