package com.peigongdh.gameinner.browserquest.domain.message;

import com.alibaba.fastjson.JSON;
import com.peigongdh.gameinner.browserquest.common.Constant;

import java.util.ArrayList;
import java.util.List;

public class Damage implements SerializeAble {

    private String entityId;

    private int points;

    public Damage(String entityId, int points) {
        this.entityId = entityId;
        this.points = points;
    }

    @Override
    public String serialize() {
        List<Object> list = new ArrayList<>();
        list.add(Constant.TYPES_MESSAGES_DAMAGE);
        list.add(this.entityId);
        list.add(this.points);
        return JSON.toJSONString(list);
    }
}
