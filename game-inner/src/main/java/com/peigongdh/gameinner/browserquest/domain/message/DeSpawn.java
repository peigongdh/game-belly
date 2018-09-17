package com.peigongdh.gameinner.browserquest.domain.message;

public class DeSpawn implements SerializeAble {

    private int entityId;

    public DeSpawn(int entityId) {
        this.entityId = entityId;
    }

    @Override
    public String serialize() {
        return null;
    }
}
