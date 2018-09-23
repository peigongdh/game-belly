package com.peigongdh.gameinner.browserquest.domain;

import com.peigongdh.gameinner.browserquest.domain.message.DeSpawn;
import com.peigongdh.gameinner.browserquest.domain.message.Spawn;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Entity {

    protected String id;

    protected Area area;

    protected boolean dead;

    protected String type;

    protected int kind;

    protected int x;

    protected int y;

    protected String groupId;

    protected List<String> recentlyLeftGroupIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<String> getRecentlyLeftGroupIds() {
        return recentlyLeftGroupIds;
    }

    public void setRecentlyLeftGroupIds(List<String> recentlyLeftGroupIds) {
        this.recentlyLeftGroupIds = recentlyLeftGroupIds;
    }

    public Entity(String id, String type, int kind, int x, int y) {
        this.id = id;
        this.type = type;
        this.kind = kind;
        this.x = x;
        this.y = y;
    }

    List<Object> getBaseState() {
        List<Object> list = new ArrayList<>();
        list.add(this.id);
        list.add(this.kind);
        list.add(this.x);
        list.add(this.y);
        return list;
    }

    public List<Object> getState() {
        return this.getBaseState();
    }

    public Spawn spawn() {
        return new Spawn(this);
    }

    DeSpawn deSpawn() {
        return new DeSpawn(this.id);
    }

    void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    void setPosition(Position pos) {
        this.x = pos.getY();
        this.y = pos.getY();
    }

    Position getPositionNextTo(Entity entity) {
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

    public void destroy() {
    }
}
