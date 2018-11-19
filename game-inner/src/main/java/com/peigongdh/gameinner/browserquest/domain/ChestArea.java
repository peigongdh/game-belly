package com.peigongdh.gameinner.browserquest.domain;

import java.util.List;

public class ChestArea extends Area {

    private List<Integer> items;

    private int chestX;

    private int chestY;

    public List<Integer> getItems() {
        return items;
    }

    public void setItems(List<Integer> items) {
        this.items = items;
    }

    public int getChestX() {
        return chestX;
    }

    public void setChestX(int chestX) {
        this.chestX = chestX;
    }

    public int getChestY() {
        return chestY;
    }

    public void setChestY(int chestY) {
        this.chestY = chestY;
    }

    public ChestArea(int id, int x, int y, int width, int height, World world, List<Integer> items, int chestX, int chestY) {
        super(id, x, y, width, height, world);
        this.items = items;
        this.chestX = chestX;
        this.chestY = chestY;
    }

    public boolean contains(Entity entity) {
        if (null != entity) {
            return entity.getX() >= this.getX() && entity.getY() >= this.getY() && entity.getX() < this.getY() + this.getWidth() && entity.getY() < this.getY() + this.getHeight();
        }
        return false;
    }
}
