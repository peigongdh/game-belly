package com.peigongdh.gameinner.browserquest.domain;

import com.peigongdh.gameinner.browserquest.domain.message.DeSpawn;
import com.peigongdh.gameinner.browserquest.domain.message.Spawn;

import java.util.Random;

public class Entity {

    private int id;

    private Area area;

    private boolean dead;

    private String type;

    private int kind;

    private int x;

    private int y;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public Entity(int id, String type, int kind, int x, int y) {
        this.id = id;
        this.type = type;
        this.kind = kind;
        this.x = x;
        this.y = y;
    }

    public Spawn spawn() {
        return new Spawn(this);
    }

    public DeSpawn deSpawn() {
        return new DeSpawn(this.id);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position getPositionNextTo(Entity entity) {
        if (null != entity) {
            // This is a quick & dirty way to give mobs a random position
            // close to another entity.
            int r = new Random().nextInt(4);
            Position pos = new Position(entity.x, entity.y);
            switch (r) {
                case 0:
                    pos.setY(pos.getY() - 1);
                    break;
                case 1:
                    pos.setY(pos.getY() + 1);
                    break;
                case 2:
                    pos.setX(pos.getX() - 1);
                    break;
                case 3:
                    pos.setX(pos.getX() + 1);
                    break;
            }
            return pos;
        }
        return new Position();
    }
}
