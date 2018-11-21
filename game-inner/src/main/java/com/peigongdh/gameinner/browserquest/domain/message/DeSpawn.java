package com.peigongdh.gameinner.browserquest.domain.message;

import com.peigongdh.gameinner.browserquest.common.Constant;

import java.util.ArrayList;
import java.util.List;

public class DeSpawn implements SerializeAble {

    private int entityId;

    public DeSpawn(int entityId) {
        this.entityId = entityId;
    }

    @Override
    public List<Object> serialize() {
        List<Object> list = new ArrayList<>();
        list.add(Constant.TYPES_MESSAGES_DESPAWN);
        list.add(this.entityId);
        return list;
    }
}
