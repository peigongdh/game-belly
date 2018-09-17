package com.peigongdh.gameinner.browserquest.domain;

import java.util.Random;

public class Formulas {

    public static int dmg(int weaponLevel, int armorLevel) {
        Random random = new Random();
        int defaultDmg = weaponLevel * random.nextInt(6) + 5;
        int absorbed = armorLevel * random.nextInt(3) + 1;
        int dmg = defaultDmg - absorbed;
        if (dmg <= 0) {
            return random.nextInt(4);
        } else {
            return dmg;
        }
    }

    public static int hp(int armorLevel) {
        return 80 + (armorLevel - 1) * 30;
    }

}
