package com.peigongdh.gameinner.browserquest.domain.message;

import com.peigongdh.gameinner.browserquest.domain.Item;
import com.peigongdh.gameinner.browserquest.domain.Mob;

public class Drop implements SerializeAble {

    private Mob mob;

    private Item item;

    public Drop(Mob mob, Item item) {
        this.mob = mob;
        this.item = item;
    }

    @Override
    public String serialize() {
        return null;
    }
}
