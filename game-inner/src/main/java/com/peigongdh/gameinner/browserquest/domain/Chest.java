package com.peigongdh.gameinner.browserquest.domain;

import com.peigongdh.gameinner.browserquest.common.Constant;

import java.util.List;
import java.util.Random;

public class Chest extends Item {

    private List<Integer> items;

    public Chest(int id, int x, int y) {
        super(id, Constant.TYPES_ENTITIES_CHEST, x, y);
    }

    public void setItems(List<Integer> items) {
        this.items = items;
    }

    public int getRandomItem() {
        int item = 0;
        int size = this.items.size();
        if (size > 0) {
            int index = new Random().nextInt(size);
            item = this.items.get(index);
        }
        return item;
    }
}
