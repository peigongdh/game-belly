package com.peigongdh.gameinner.browserquest.domain.message;

import java.util.List;

public class Lists implements SerializeAble {

    private List<String> ids;

    public Lists(List<String> ids) {
        this.ids = ids;
    }

    @Override
    public String serialize() {
        return null;
    }
}
