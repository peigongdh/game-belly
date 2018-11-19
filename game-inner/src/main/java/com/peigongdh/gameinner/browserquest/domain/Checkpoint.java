package com.peigongdh.gameinner.browserquest.domain;

import java.util.Random;

public class Checkpoint {

    private int id;

    private int x;

    private int y;

    private int width;

    private int height;

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

    public Checkpoint(int id, int x, int y, int width, int height) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Position getRandomPosition() {
        Position pos = new Position();
        Random random = new Random();
        pos.setX(this.x + random.nextInt(this.width));
        pos.setY(this.y + random.nextInt(this.height));
        return pos;
    }
}
