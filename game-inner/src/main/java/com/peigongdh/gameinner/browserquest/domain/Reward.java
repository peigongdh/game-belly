package com.peigongdh.gameinner.browserquest.domain;

public class Reward {
    private int x;

    private int y;

    private int kind;

    public Reward(int x, int y, int kind) {
        this.x = x;
        this.y = y;
        this.kind = kind;
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
}
