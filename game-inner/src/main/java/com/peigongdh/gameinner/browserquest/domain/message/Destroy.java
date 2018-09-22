package com.peigongdh.gameinner.browserquest.domain.message;

import com.peigongdh.gameinner.browserquest.domain.Entity;

public class Destroy implements SerializeAble {

    private Entity item;

    public Destroy(Entity item) {
        this.item = item;
    }

    @Override
    public String serialize() {
        return null;
    }
}
