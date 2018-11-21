package com.peigongdh.gameinner.browserquest.domain;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Area {
    protected int id;

    protected int x;

    protected int y;

    protected int width;

    protected int height;

    protected World world;

    protected ConcurrentHashMap<Integer, Entity> entities;

    protected boolean hasCompletelyReSpawned;

    protected int nbEntities;

    protected Runnable emptyCallback;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public ConcurrentHashMap<Integer, Entity> getEntities() {
        return entities;
    }

    public void setEntities(ConcurrentHashMap<Integer, Entity> entities) {
        this.entities = entities;
    }

    Area(int id, int x, int y, int width, int height, World world) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.world = world;
        this.entities = new ConcurrentHashMap<>();
        // FIXME: true or false?
        this.hasCompletelyReSpawned = true;
        this.nbEntities = 2;
    }

    Position getRandomPositionInsideArea() {
        Position pos = new Position();
        boolean valid = false;
        while (!valid) {
            Random random = new Random();
            pos.setX(this.x + random.nextInt(this.width + 1));
            pos.setY(this.y + random.nextInt(this.height + 1));
            valid = this.world.isValidPosition(pos);
        }
        return pos;
    }

    void removeFromArea(Entity entity) {
        entities.remove(entity.getId());
        if (this.isEmpty() && this.hasCompletelyReSpawned && null != this.emptyCallback) {
            this.emptyCallback.run();
        }
    }

    void addToArea(Entity entity) {
        if (null != entity) {
            this.entities.put(entity.getId(), entity);
            entity.setArea(this);
            if (entity instanceof Mob) {
                this.world.addMob((Mob) entity);
            }
        }
        if (this.isFull()) {
            this.hasCompletelyReSpawned = true;
        }
    }

    private boolean isFull() {
        return !this.isEmpty() && this.nbEntities == this.entities.size();
    }

    private boolean isEmpty() {
        for (Map.Entry<Integer, Entity> entry : this.entities.entrySet()) {
            Entity entity = entry.getValue();
            if (!entity.isDead()) {
                return false;
            }
        }
        return true;
    }

    void onEmpty(Runnable callback) {
        this.emptyCallback = callback;
    }

    void setNumberOfEntities(int nb) {
        this.nbEntities = nb;
    }
}
