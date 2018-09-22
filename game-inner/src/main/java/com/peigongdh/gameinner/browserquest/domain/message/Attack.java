package com.peigongdh.gameinner.browserquest.domain.message;

public class Attack implements SerializeAble {

    private String attackerId;

    private String targetId;

    public Attack(String attackerId, String targetId) {
        this.attackerId = attackerId;
        this.targetId = targetId;
    }

    @Override
    public String serialize() {
        return null;
    }
}
