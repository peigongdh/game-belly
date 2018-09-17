package com.peigongdh.gameinner.browserquest.domain.message;

import com.peigongdh.gameinner.browserquest.domain.Entity;

public class Spawn implements SerializeAble {


    public Entity entity;

    public Spawn(Entity entity) {
        this.entity = entity;
    }

    @Override
    public String serialize() {
        return null;
    }
}
