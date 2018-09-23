package com.peigongdh.gameinner.browserquest.domain.message;

import com.alibaba.fastjson.JSON;
import com.peigongdh.gameinner.browserquest.common.Constant;

import java.util.ArrayList;
import java.util.List;

public class Health implements SerializeAble {

    private int points;

    private boolean isRegen;

    public Health(int points, boolean isRegen) {
        this.points = points;
        this.isRegen = isRegen;
    }

    @Override
    public String serialize() {
        List<Object> list = new ArrayList<>();
        list.add(Constant.TYPES_MESSAGES_HEALTH);
        list.add(this.points);
        if (this.isRegen) {
            list.add(1);
        }
        return JSON.toJSONString(list);
    }
}
