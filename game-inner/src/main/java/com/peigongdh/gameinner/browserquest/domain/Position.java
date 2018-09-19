package com.peigongdh.gameinner.browserquest.domain;

public class Position {

    private int x;

    private int y;

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

    public Position() {
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toGroupId() {
        return this.x + "-" + this.y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            return ((Position) obj).getX() == x && ((Position) obj).getY() == y;
        }
        return false;
    }
}
