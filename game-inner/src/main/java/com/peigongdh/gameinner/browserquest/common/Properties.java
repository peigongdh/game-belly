package com.peigongdh.gameinner.browserquest.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Properties {

    private static final Map<String, MobConfig> properties;

    static {
        properties = new HashMap<>();

        Map<String, Integer> ratDrops = new HashMap<>();
        ratDrops.put("flask", 40);
        ratDrops.put("burger", 10);
        ratDrops.put("firepotion", 5);
        properties.put("rat", new MobConfig(ratDrops, 25, 1, 1));

        Map<String, Integer> skeletonDrops = new HashMap<>();
        skeletonDrops.put("flask", 40);
        skeletonDrops.put("mailarmor", 10);
        skeletonDrops.put("axe", 20);
        skeletonDrops.put("firepotion", 5);
        properties.put("skeleton", new MobConfig(skeletonDrops, 110, 2, 2));

        Map<String, Integer> golinDrops = new HashMap<>();
        golinDrops.put("flask", 40);
        golinDrops.put("leatherarmor", 20);
        golinDrops.put("axe", 10);
        golinDrops.put("firepotion", 5);
        properties.put("goblin", new MobConfig(golinDrops, 90, 2, 1));

        Map<String, Integer> ogreDrops = new HashMap<>();
        ogreDrops.put("burger", 10);
        ogreDrops.put("flask", 50);
        ogreDrops.put("platearmor", 20);
        ogreDrops.put("morningstar", 20);
        ogreDrops.put("firepotion", 5);
        properties.put("ogre", new MobConfig(ogreDrops, 200, 3, 2));

        Map<String, Integer> spectreDrops = new HashMap<>();
        spectreDrops.put("flask", 30);
        spectreDrops.put("redarmor", 40);
        spectreDrops.put("redsword", 30);
        spectreDrops.put("firepotion", 5);
        properties.put("spectre", new MobConfig(spectreDrops, 250, 2, 4));

        Map<String, Integer> deathknightDrops = new HashMap<>();
        deathknightDrops.put("burger", 95);
        deathknightDrops.put("firepotion", 5);
        properties.put("deathknight", new MobConfig(deathknightDrops, 250, 3, 3));

        Map<String, Integer> crabDrops = new HashMap<>();
        crabDrops.put("flask", 50);
        crabDrops.put("axe", 20);
        crabDrops.put("leatherarmor", 10);
        crabDrops.put("firepotion", 5);
        properties.put("crab", new MobConfig(crabDrops, 60, 2, 1));

        Map<String, Integer> snakeDrops = new HashMap<>();
        snakeDrops.put("flask", 50);
        snakeDrops.put("mailarmor", 10);
        snakeDrops.put("morningstar", 10);
        snakeDrops.put("firepotion", 5);
        properties.put("snake", new MobConfig(snakeDrops, 150, 3, 2));

        Map<String, Integer> skeleton2Drops = new HashMap<>();
        skeleton2Drops.put("flask", 60);
        skeleton2Drops.put("platearmor", 15);
        skeleton2Drops.put("bluesword", 15);
        skeleton2Drops.put("firepotion", 5);
        properties.put("skeleton2", new MobConfig(skeleton2Drops, 200, 3, 3));

        Map<String, Integer> eyeDrops = new HashMap<>();
        eyeDrops.put("flask", 50);
        eyeDrops.put("redarmor", 20);
        eyeDrops.put("redsword", 10);
        eyeDrops.put("firepotion", 5);
        properties.put("eye", new MobConfig(eyeDrops, 200, 3, 3));

        Map<String, Integer> batDrops = new HashMap<>();
        batDrops.put("flask", 50);
        batDrops.put("axe", 10);
        batDrops.put("firepotion", 5);
        properties.put("bat", new MobConfig(batDrops, 80, 2, 1));

        Map<String, Integer> wizardDrops = new HashMap<>();
        wizardDrops.put("flask", 50);
        wizardDrops.put("platearmor", 20);
        wizardDrops.put("firepotion", 5);
        properties.put("wizard", new MobConfig(wizardDrops, 100, 2, 6));

        Map<String, Integer> bossDrops = new HashMap<>();
        bossDrops.put("goldensword", 100);
        properties.put("boss", new MobConfig(bossDrops, 700, 6, 7));
    }


    public static int getArmorLevel(int kind) {
        if (Types.isMob(kind)) {
            return Properties.properties.get(Types.getKindAsString(kind)).getArmor();
        }
        return Types.getArmorRank(kind) + 1;
    }

    public static int getWeaponLevel(int kind) {
        if (Types.isMob(kind)) {
            return Properties.properties.get(Types.getKindAsString(kind)).getWeapon();
        }
        return Types.getWeaponRank(kind) + 1;
    }

    public static int getHitPoints(int kind) {
        return Properties.properties.get(Types.getKindAsString(kind)).getHp();
    }

    public static String getRandomDropItemName(String kind) {
        MobConfig config = Properties.properties.get(kind);
        int v = new Random().nextInt(101);
        int p = 0;
        for (Map.Entry<String, Integer> entry : config.getDrops().entrySet()) {
            String itemName = entry.getKey();
            int percentage = entry.getValue();
            p += percentage;
            if (v <= p) {
                return itemName;
            }
        }
        return null;
    }
}

class MobConfig {
    private Map<String, Integer> drops;

    private int hp;

    private int armor;

    private int weapon;

    int getHp() {
        return hp;
    }

    int getArmor() {
        return armor;
    }

    int getWeapon() {
        return weapon;
    }

    Map<String, Integer> getDrops() {
        return drops;
    }

    MobConfig(Map<String, Integer> drops, int hp, int armor, int weapon) {
        this.drops = drops;
        this.hp = hp;
        this.armor = armor;
        this.weapon = weapon;
    }
}
