package com.peigongdh.gameinner.browserquest.domain.message;

import com.peigongdh.gameinner.browserquest.domain.Entity;

public class Move implements SerializeAble {

    private Entity entity;

    public Move(Entity entity) {
        this.entity = entity;
    }

    @Override
    public String serialize() {
        return null;
    }
}
