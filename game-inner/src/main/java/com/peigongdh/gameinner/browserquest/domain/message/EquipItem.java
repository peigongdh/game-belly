package com.peigongdh.gameinner.browserquest.domain.message;

import com.peigongdh.gameinner.browserquest.common.Constant;

import java.util.ArrayList;
import java.util.List;

public class EquipItem implements SerializeAble {

    private int playerId;

    private int itemKind;

    public EquipItem(int playerId, int itemKind) {
        this.playerId = playerId;
        this.itemKind = itemKind;
    }

    @Override
    public List<Object> serialize() {
        List<Object> list = new ArrayList<>();
        list.add(Constant.TYPES_MESSAGES_EQUIP);
        list.add(this.playerId);
        list.add(this.itemKind);
        return list;
    }
}
