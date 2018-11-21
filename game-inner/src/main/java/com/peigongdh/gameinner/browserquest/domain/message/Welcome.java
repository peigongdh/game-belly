package com.peigongdh.gameinner.browserquest.domain.message;

import com.alibaba.fastjson.JSON;
import com.peigongdh.gameinner.browserquest.common.Constant;

import java.util.ArrayList;
import java.util.List;

public class Welcome implements SerializeAble {

    private int id;

    private String name;

    private int x;

    private int y;

    private int hitPoints;

    public Welcome(int id, String name, int x, int y, int hitPoints) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.hitPoints = hitPoints;
    }

    @Override
    public List<Object> serialize() {
        List<Object> list = new ArrayList<>();
        list.add(Constant.TYPES_MESSAGES_WELCOME);
        list.add(this.id);
        list.add(this.name);
        list.add(this.x);
        list.add(this.y);
        list.add(this.hitPoints);
        return list;
    }
}
