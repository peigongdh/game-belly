package com.peigongdh.gameinner.browserquest.domain.message;

public class Chat implements SerializeAble {

    private String playerId;

    private String msg;

    public Chat(String playerId, String msg) {
        this.playerId = playerId;
        this.msg = msg;
    }

    @Override
    public String serialize() {
        return null;
    }
}
