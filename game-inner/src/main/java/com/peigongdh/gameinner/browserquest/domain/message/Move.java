package com.peigongdh.gameinner.browserquest.domain.message;

import com.peigongdh.gameinner.browserquest.domain.Mob;

public class Move implements SerializeAble {

    private Mob mob;

    public Move(Mob mob) {
        this.mob = mob;
    }

    @Override
    public String serialize() {
        return null;
    }
}
