package com.peigongdh.gameinner.browserquest.domain;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Chest extends Item {

    private ConcurrentHashMap<Integer, Item> items;

    public Chest(int id, int kind, int x, int y) {
        super(id, kind, x, y);
    }

    public void setItems(ConcurrentHashMap<Integer, Item> items) {
        this.items = items;
    }

    public Item getRandomItem() {
        Item item = null;
        int size = this.items.size();
        if (size > 0) {
            int index = new Random().nextInt(size);
            item = this.items.get(index);
        }
        return item;
    }
}
