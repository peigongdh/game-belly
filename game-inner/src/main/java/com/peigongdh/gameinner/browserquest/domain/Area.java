package com.peigongdh.gameinner.browserquest.domain;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class Area {
    private int id;

    private int x;

    private int y;

    private int width;

    private int height;

    private World world;

    private ConcurrentHashMap<Integer, Entity> entities;

    private boolean hasCompletelyReSpawned;

    private int nbEntities;

    private Consumer<Area> emptyCallback;

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

    public Area(int id, int x, int y, int width, int height, World world) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.world = world;
        this.entities = new ConcurrentHashMap<>();
        // FIXME: true or false?
        this.hasCompletelyReSpawned = true;
        // FIXME: 2?
        this.nbEntities = 2;
    }

    public Position getRandomPositionInsideArea() {
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

    public void removeFromArea(Entity entity) {
        entities.remove(entity.getId());
        // TODO: onEmpty
        if (this.isEmpty() && this.hasCompletelyReSpawned && null != this.emptyCallback) {
            this.emptyCallback.accept(this);
        }
    }

    public void addToArea(Entity entity) {
        if (null != entity) {
            this.entities.put(entity.getId(), entity);
            entity.setArea(this);
            if (entity instanceof Mob) {
                this.world.addMob(entity);
            }
        }
        if (this.isFull()) {
            this.hasCompletelyReSpawned = true;
        }
    }

    public boolean isFull() {
        return !this.isEmpty() && this.nbEntities == this.entities.size();
    }

    public boolean isEmpty() {
        for (Map.Entry<Integer, Entity> entry : this.entities.entrySet()) {
            Entity entity = entry.getValue();
            if (!entity.isDead()) {
                return false;
            }
        }
        return true;
    }

    public void onEmpty(Consumer<Area> callback) {
        this.emptyCallback = callback;
    }
}
