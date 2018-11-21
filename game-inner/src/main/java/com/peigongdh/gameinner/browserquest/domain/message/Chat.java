package com.peigongdh.gameinner.browserquest.domain.message;

import com.peigongdh.gameinner.browserquest.common.Constant;

import java.util.ArrayList;
import java.util.List;

public class Chat implements SerializeAble {

    private String playerId;

    private String msg;

    public Chat(String playerId, String msg) {
        this.playerId = playerId;
        this.msg = msg;
    }

    @Override
    public List<Object> serialize() {
        List<Object> list = new ArrayList<>();
        list.add(Constant.TYPES_MESSAGES_CHAT);
        list.add(this.playerId);
        list.add(this.msg);
        return list;
    }
}
