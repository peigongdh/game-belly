package com.peigongdh.gameinner.browserquest.domain.message;

import com.alibaba.fastjson.JSON;
import com.peigongdh.gameinner.browserquest.common.Constant;
import com.peigongdh.gameinner.browserquest.domain.Hate;
import com.peigongdh.gameinner.browserquest.domain.Item;
import com.peigongdh.gameinner.browserquest.domain.Mob;

import java.util.ArrayList;
import java.util.List;

public class Drop implements SerializeAble {

    private Mob mob;

    private Item item;

    public Drop(Mob mob, Item item) {
        this.mob = mob;
        this.item = item;
    }

    @Override
    public String serialize() {
        List<Object> list = new ArrayList<>();
        list.add(Constant.TYPES_MESSAGES_DROP);
        list.add(this.mob.getId());
        list.add(this.item.getId());
        list.add(this.item.getKind());
        List<String> ids = new ArrayList<>();
        for (Hate hate : this.mob.getHateList()) {
            ids.add(hate.getId());
        }
        list.add(ids);
        return JSON.toJSONString(list);
    }
}
