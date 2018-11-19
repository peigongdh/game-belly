package com.peigongdh.gameinner.browserquest.domain.message;

import com.alibaba.fastjson.JSON;
import com.peigongdh.gameinner.browserquest.common.Constant;

import java.util.ArrayList;
import java.util.List;

public class HitPoints implements SerializeAble {

    private int maxHitPoints;

    public HitPoints(int maxHitPoints) {
        this.maxHitPoints = maxHitPoints;
    }

    @Override
    public String serialize() {
        List<Object> list = new ArrayList<>();
        list.add(Constant.TYPES_MESSAGES_HP);
        list.add(this.maxHitPoints);
        return JSON.toJSONString(list);
    }
}
