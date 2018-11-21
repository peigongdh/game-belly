package com.peigongdh.gameinner.browserquest.domain.message;

import com.peigongdh.gameinner.browserquest.common.Constant;

import java.util.ArrayList;
import java.util.List;

public class LootMove implements SerializeAble {

    private int entityId;

    private int itemId;

    public LootMove(int entityId, int itemId) {
        this.entityId = entityId;
        this.itemId = itemId;
    }

    @Override
    public List<Object> serialize() {
        List<Object> list = new ArrayList<>();
        list.add(Constant.TYPES_MESSAGES_LOOTMOVE);
        list.add(this.entityId);
        list.add(this.itemId);
        return list;
    }
}
