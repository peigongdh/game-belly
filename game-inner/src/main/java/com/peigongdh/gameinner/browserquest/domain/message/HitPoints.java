package com.peigongdh.gameinner.browserquest.domain.message;

public class HitPoints implements SerializeAble {

    private int maxHitPoints;

    public HitPoints(int maxHitPoints) {
        this.maxHitPoints = maxHitPoints;
    }

    @Override
    public String serialize() {
        return null;
    }
}
