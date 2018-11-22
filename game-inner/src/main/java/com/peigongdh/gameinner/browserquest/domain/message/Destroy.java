package com.peigongdh.gameinner.browserquest.domain.message;

import com.peigongdh.gameinner.browserquest.common.Constant;

import java.util.ArrayList;
import java.util.List;

public class Destroy implements SerializeAble {

    private int entityId;

    public Destroy(int entityId) {
        this.entityId = entityId;
    }

    @Override
    public List<Object> serialize() {
        List<Object> list = new ArrayList<>();
        list.add(Constant.TYPES_MESSAGES_BLINK);
        list.add(this.entityId);
        return list;
    }
}
