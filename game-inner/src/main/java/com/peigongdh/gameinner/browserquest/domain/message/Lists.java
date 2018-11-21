package com.peigongdh.gameinner.browserquest.domain.message;

import com.peigongdh.gameinner.browserquest.common.Constant;

import java.util.ArrayList;
import java.util.List;

public class Lists implements SerializeAble {

    private List<Integer> ids;

    public Lists(List<Integer> ids) {
        this.ids = ids;
    }

    @Override
    public List<Object> serialize() {
        List<Object> list = new ArrayList<>();
        list.add(Constant.TYPES_MESSAGES_LIST);
        list.addAll(this.ids);
        return list;
    }
}
