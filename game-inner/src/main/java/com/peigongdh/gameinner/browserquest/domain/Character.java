package com.peigongdh.gameinner.browserquest.domain;

import com.peigongdh.gameinner.browserquest.domain.message.Attack;
import com.peigongdh.gameinner.browserquest.domain.message.Health;
import com.peigongdh.gameinner.browserquest.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class Character extends Entity {

    protected int orientation;

    protected int maxHitPoints;

    protected int hitPoints;

    protected String targetId;

    protected ConcurrentHashMap<String, Entity> attackers;

    public int getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getMaxHitPoints() {
        return maxHitPoints;
    }

    public void setMaxHitPoints(int maxHitPoints) {
        this.maxHitPoints = maxHitPoints;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public ConcurrentHashMap<String, Entity> getAttackers() {
        return attackers;
    }

    public void setAttackers(ConcurrentHashMap<String, Entity> attackers) {
        this.attackers = attackers;
    }

    public Consumer<Entity> getCallback() {
        return callback;
    }

    public void setCallback(Consumer<Entity> callback) {
        this.callback = callback;
    }

    public Character(String id, String type, int kind, int x, int y) {
        super(id, type, kind, x, y);
        this.orientation = Util.randomOrientation();
        this.maxHitPoints = 100;
        this.hitPoints = 10;
        this.targetId = null;
        this.attackers = new ConcurrentHashMap<>();
    }

    @Override
    public List<Object> getState() {
        List<Object> list = new ArrayList<>(this.getBaseState());
        list.add(this.orientation);
        if (null != this.targetId) {
            list.add(targetId);
        }
        return list;
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
        this.targetId = null;
    }

    public boolean hasTarget() {
        return this.targetId != null;
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

    public void forEachAttacker(Consumer<Mob> callback) {
        for (Map.Entry<String, Entity> entry : this.attackers.entrySet()) {
            callback.accept((Mob) entry.getValue());
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
        for (Map.Entry<String, Entity> entry : this.attackers.entrySet()) {
            this.callback.accept(entry.getValue());
        }
    }

    public void forEachAttacker3() {
        this.callback.accept(this);
    }

    public static void main(String[] args) {
        Character c1 = new Character("1", "", 0, 0, 0);
        Character c2 = new Character("2", "", 0, 0, 0);
        Character c3 = new Character("3", "", 0, 0, 0);
        c1.addAttacker(c2);
        c1.addAttacker(c3);
        c1.setForEachAttackerCallback(entity -> System.out.println(entity.getId()));
        // c1.forEachAttacker2();
        c1.forEachAttacker3();
        // c1.forEachAttacker(entity -> System.out.println(entity.getId()));
    }
}
