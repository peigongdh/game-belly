package com.peigongdh.gameinner.browserquest.domain.message;

public class Teleport implements SerializeAble {

    private String id;

    private int x;

    private int y;

    public Teleport(String id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    @Override
    public String serialize() {
        return null;
    }
}
