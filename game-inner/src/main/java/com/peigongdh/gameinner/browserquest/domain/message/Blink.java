package com.peigongdh.gameinner.browserquest.domain.message;

import com.peigongdh.gameinner.browserquest.common.Constant;

import java.util.ArrayList;
import java.util.List;

public class Blink implements SerializeAble {

    private int itemId;

    public Blink(int itemId) {
        this.itemId = itemId;
    }

    @Override
    public List<Object> serialize() {
        List<Object> list = new ArrayList<>();
        list.add(Constant.TYPES_MESSAGES_DESTROY);
        list.add(this.itemId);
        return list;
    }
}
