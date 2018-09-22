package com.peigongdh.gameinner.browserquest.domain.message;

import com.peigongdh.gameinner.browserquest.domain.Mob;

public class Kill implements SerializeAble {

    private Mob mob;

    public Kill(Mob mob) {
        this.mob = mob;
    }

    @Override
    public String serialize() {
        return null;
    }
}
