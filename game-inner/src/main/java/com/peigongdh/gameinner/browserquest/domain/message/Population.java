package com.peigongdh.gameinner.browserquest.domain.message;

public class Population implements SerializeAble {

    private int world;

    private int total;

    public Population(int world, int total) {
        this.world = world;
        this.total = total;
    }

    @Override
    public String serialize() {
        return null;
    }
}
