package com.peigongdh.gameinner.browserquest.domain.message;

import com.peigongdh.gameinner.browserquest.domain.Item;

public class Blink implements SerializeAble {

    private Item item;

    public Blink(Item item) {
        this.item = item;
    }

    @Override
    public String serialize() {
        return null;
    }
}
