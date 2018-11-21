package com.peigongdh.gameinner.browserquest.domain.message;

import com.peigongdh.gameinner.browserquest.common.Constant;

import java.util.ArrayList;
import java.util.List;

public class Attack implements SerializeAble {

    private int attackerId;

    private int targetId;

    public Attack(int attackerId, int targetId) {
        this.attackerId = attackerId;
        this.targetId = targetId;
    }

    @Override
    public List<Object> serialize() {
        List<Object> list = new ArrayList<>();
        list.add(Constant.TYPES_MESSAGES_ATTACK);
        list.add(this.attackerId);
        list.add(this.targetId);
        return list;
    }
}
