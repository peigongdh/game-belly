package com.peigongdh.gameinner.browserquest.domain.message;

import com.alibaba.fastjson.JSON;
import com.peigongdh.gameinner.browserquest.common.Constant;
import com.peigongdh.gameinner.browserquest.domain.Entity;

import java.util.ArrayList;
import java.util.List;

public class Move implements SerializeAble {

    private Entity entity;

    public Move(Entity entity) {
        this.entity = entity;
    }

    @Override
    public String serialize() {
        List<Object> list = new ArrayList<>();
        list.add(Constant.TYPES_MESSAGES_MOVE);
        list.add(this.entity.getId());
        list.add(this.entity.getX());
        list.add(this.entity.getY());
        return JSON.toJSONString(list);
    }
}
