package com.peigongdh.gameinner.browserquest.domain.message;

public class LootMove implements SerializeAble {

    private String entityId;

    private String ItemId;

    public LootMove(String entityId, String itemId) {
        this.entityId = entityId;
        ItemId = itemId;
    }

    @Override
    public String serialize() {
        return null;
    }
}
