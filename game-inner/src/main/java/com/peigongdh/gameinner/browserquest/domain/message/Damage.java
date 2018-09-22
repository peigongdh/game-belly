package com.peigongdh.gameinner.browserquest.domain.message;

import com.peigongdh.gameinner.browserquest.domain.Entity;

public class Damage implements SerializeAble {

    private Entity entity;

    private int damage;

    public Damage(Entity entity, int damage) {
        this.entity = entity;
        this.damage = damage;
    }

    @Override
    public String serialize() {
        return null;
    }
}
