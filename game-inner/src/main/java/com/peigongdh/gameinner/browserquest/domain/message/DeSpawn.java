package com.peigongdh.gameinner.browserquest.domain.message;

public class DeSpawn implements SerializeAble {

    private String entityId;

    public DeSpawn(String entityId) {
        this.entityId = entityId;
    }

    @Override
    public String serialize() {
        return null;
    }
}
