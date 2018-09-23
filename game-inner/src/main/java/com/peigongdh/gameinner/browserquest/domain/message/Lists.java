package com.peigongdh.gameinner.browserquest.domain.message;

import com.alibaba.fastjson.JSON;
import com.peigongdh.gameinner.browserquest.common.Constant;

import java.util.ArrayList;
import java.util.List;

public class Lists implements SerializeAble {

    private List<String> ids;

    public Lists(List<String> ids) {
        this.ids = ids;
    }

    @Override
    public String serialize() {
        List<Object> list = new ArrayList<>();
        list.add(Constant.TYPES_MESSAGES_LIST);
        list.addAll(this.ids);
        return JSON.toJSONString(list);
    }

    public static void main(String[] args) {
        List<String> ids = new ArrayList<>();
        ids.add("1");
        ids.add("2");
        System.out.println(new Lists(ids).serialize());
    }
}
