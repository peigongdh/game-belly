package com.peigongdh.gameinner.browserquest.domain.message;

public class Health implements SerializeAble {

    private int points;

    private boolean isRegen;

    public Health(int points, boolean isRegen) {
        this.points = points;
        this.isRegen = isRegen;
    }

    @Override
    public String serialize() {
        return null;
    }
}
