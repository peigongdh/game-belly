package com.peigongdh.gameinner.browserquest.domain.message;

import com.peigongdh.gameinner.browserquest.common.Constant;
import com.peigongdh.gameinner.browserquest.domain.Entity;

import java.util.ArrayList;
import java.util.List;

public class Spawn implements SerializeAble {

    public Entity entity;

    public Spawn(Entity entity) {
        this.entity = entity;
    }

    @Override
    public List<Object> serialize() {
        List<Object> list = new ArrayList<>();
        list.add(Constant.TYPES_MESSAGES_SPAWN);
        list.addAll(this.entity.getState());
        return list;
    }
}
