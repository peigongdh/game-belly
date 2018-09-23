package com.peigongdh.gameinner.browserquest.domain.message;

import com.alibaba.fastjson.JSON;
import com.peigongdh.gameinner.browserquest.common.Constant;

import java.util.ArrayList;
import java.util.List;

public class Teleport implements SerializeAble {

    private String id;

    private int x;

    private int y;

    public Teleport(String id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    @Override
    public String serialize() {
        List<Object> list = new ArrayList<>();
        list.add(Constant.TYPES_MESSAGES_TELEPORT);
        list.add(this.id);
        list.add(this.x);
        list.add(this.y);
        return JSON.toJSONString(list);
    }
}
