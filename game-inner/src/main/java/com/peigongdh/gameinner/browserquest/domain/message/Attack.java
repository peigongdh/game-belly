package com.peigongdh.gameinner.browserquest.domain.message;

public class Attack implements SerializeAble {

    private int attackerId;

    private int targetId;

    public Attack(int attackerId, int targetId) {
        this.attackerId = attackerId;
        this.targetId = targetId;
    }

    @Override
    public String serialize() {
        return null;
    }
}
