package com.peigongdh.gameinner.browserquest.domain.message;

public class Welcome implements SerializeAble {

    private String id;

    private String name;

    private int x;

    private int y;

    private int hitPoints;

    public Welcome(String id, String name, int x, int y, int hitPoints) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.hitPoints = hitPoints;
    }

    @Override
    public String serialize() {
        return null;
    }
}
