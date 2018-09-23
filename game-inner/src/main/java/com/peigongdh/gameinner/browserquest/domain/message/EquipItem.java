package com.peigongdh.gameinner.browserquest.domain.message;

public class EquipItem implements SerializeAble {

    private String playerId;

    private int itemKind;

    public EquipItem(String playerId, int itemKind) {
        this.playerId = playerId;
        this.itemKind = itemKind;
    }

    @Override
    public String serialize() {
        return null;
    }
}
