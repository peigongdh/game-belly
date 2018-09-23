package com.peigongdh.gameinner.browserquest.domain.message;

import com.alibaba.fastjson.JSON;
import com.peigongdh.gameinner.browserquest.common.Constant;

import java.util.ArrayList;
import java.util.List;

public class Attack implements SerializeAble {

    private String attackerId;

    private String targetId;

    public Attack(String attackerId, String targetId) {
        this.attackerId = attackerId;
        this.targetId = targetId;
    }

    @Override
    public String serialize() {
        List<Object> list = new ArrayList<>();
        list.add(Constant.TYPES_MESSAGES_ATTACK);
        list.add(this.attackerId);
        list.add(this.targetId);
        return JSON.toJSONString(list);
    }

    public static void main(String[] args) {
        System.out.println(new Attack("1", "2").serialize());
    }
}
