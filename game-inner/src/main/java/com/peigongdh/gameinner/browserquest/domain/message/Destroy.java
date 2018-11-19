package com.peigongdh.gameinner.browserquest.domain.message;

import com.alibaba.fastjson.JSON;
import com.peigongdh.gameinner.browserquest.common.Constant;

import java.util.ArrayList;
import java.util.List;

public class Destroy implements SerializeAble {

    private String entityId;

    public Destroy(String entityId) {
        this.entityId = entityId;
    }

    @Override
    public String serialize() {
        List<Object> list = new ArrayList<>();
        list.add(Constant.TYPES_MESSAGES_BLINK);
        list.add(this.entityId);
        return JSON.toJSONString(list);
    }
}
