package com.peigongdh.gameinner.browserquest.domain.message;

import com.alibaba.fastjson.JSON;
import com.peigongdh.gameinner.browserquest.common.Constant;

import java.util.ArrayList;
import java.util.List;

public class Population implements SerializeAble {

    private int world;

    private int total;

    public Population(int world, int total) {
        this.world = world;
        this.total = total;
    }

    @Override
    public String serialize() {
        List<Object> list = new ArrayList<>();
        list.add(Constant.TYPES_MESSAGES_POPULATION);
        list.add(this.world);
        list.add(this.total);
        return JSON.toJSONString(list);
    }
}
