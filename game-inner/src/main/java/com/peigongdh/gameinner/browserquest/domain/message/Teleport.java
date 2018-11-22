package com.peigongdh.gameinner.browserquest.domain.message;

import com.peigongdh.gameinner.browserquest.common.Constant;

import java.util.ArrayList;
import java.util.List;

public class Teleport implements SerializeAble {

    private int id;

    private int x;

    private int y;

    public Teleport(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    @Override
    public List<Object> serialize() {
        List<Object> list = new ArrayList<>();
        list.add(Constant.TYPES_MESSAGES_TELEPORT);
        list.add(this.id);
        list.add(this.x);
        list.add(this.y);
        return list;
    }
}
