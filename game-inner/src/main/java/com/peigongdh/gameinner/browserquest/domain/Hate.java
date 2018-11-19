package com.peigongdh.gameinner.browserquest.domain;

public class Hate {

    private String id;

    private int hate;

    public Hate(String id, int hate) {
        this.id = id;
        this.hate = hate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getHate() {
        return hate;
    }

    public void setHate(int hate) {
        this.hate = hate;
    }
}
