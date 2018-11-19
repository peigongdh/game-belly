package com.peigongdh.gameinner.browserquest.common;

import javafx.util.Pair;

import java.util.*;
import java.util.function.Consumer;

public class Types {

    public static final Map<String, Map<Integer, String>> typesToString = new HashMap<>();

    static {
        Map<Integer, String> msg = new HashMap<>();
        msg.put(Constant.TYPES_MESSAGES_HELLO, "HELLO");
        msg.put(Constant.TYPES_MESSAGES_WELCOME, "WELCOME");
        msg.put(Constant.TYPES_MESSAGES_SPAWN, "SPAWN");
        msg.put(Constant.TYPES_MESSAGES_DESPAWN, "DESPAWN");
        msg.put(Constant.TYPES_MESSAGES_MOVE, "MOVE");
        msg.put(Constant.TYPES_MESSAGES_LOOTMOVE, "LOOTMOVE");
        msg.put(Constant.TYPES_MESSAGES_AGGRO, "AGGRO");
        msg.put(Constant.TYPES_MESSAGES_ATTACK, "ATTACK");
        msg.put(Constant.TYPES_MESSAGES_HIT, "HIT");
        msg.put(Constant.TYPES_MESSAGES_HURT, "HURT");
        msg.put(Constant.TYPES_MESSAGES_HEALTH, "HEALTH");
        msg.put(Constant.TYPES_MESSAGES_CHAT, "CHAT");
        msg.put(Constant.TYPES_MESSAGES_LOOT, "LOOT");
        msg.put(Constant.TYPES_MESSAGES_EQUIP, "EQUIP");
        msg.put(Constant.TYPES_MESSAGES_DROP, "DROP");
        msg.put(Constant.TYPES_MESSAGES_TELEPORT, "TELEPORT");
        msg.put(Constant.TYPES_MESSAGES_DAMAGE, "DAMAGE");
        msg.put(Constant.TYPES_MESSAGES_POPULATION, "POPULATION");
        msg.put(Constant.TYPES_MESSAGES_KILL, "KILL");
        msg.put(Constant.TYPES_MESSAGES_LIST, "LIST");
        msg.put(Constant.TYPES_MESSAGES_WHO, "WHO");
        msg.put(Constant.TYPES_MESSAGES_ZONE, "ZONE");
        msg.put(Constant.TYPES_MESSAGES_DESTROY, "DESTROY");
        msg.put(Constant.TYPES_MESSAGES_HP, "HP");
        msg.put(Constant.TYPES_MESSAGES_BLINK, "BLINK");
        msg.put(Constant.TYPES_MESSAGES_OPEN, "OPEN");
        msg.put(Constant.TYPES_MESSAGES_CHECK, "CHECK");
        typesToString.put("Messages", msg);

        Map<Integer, String> entities = new HashMap<>();
        entities.put(Constant.TYPES_ENTITIES_WARRIOR, "WARRIOR");
        // Mobs
        entities.put(Constant.TYPES_ENTITIES_RAT, "RAT");
        entities.put(Constant.TYPES_ENTITIES_SKELETON, "SKELETON");
        entities.put(Constant.TYPES_ENTITIES_GOBLIN, "GOBLIN");
        entities.put(Constant.TYPES_ENTITIES_OGRE, "OGRE");
        entities.put(Constant.TYPES_ENTITIES_SPECTRE, "SPECTRE");
        entities.put(Constant.TYPES_ENTITIES_CRAB, "CRAB");
        entities.put(Constant.TYPES_ENTITIES_BAT, "BAT");
        entities.put(Constant.TYPES_ENTITIES_WIZARD, "WIZARD");
        entities.put(Constant.TYPES_ENTITIES_EYE, "EYE");
        entities.put(Constant.TYPES_ENTITIES_SNAKE, "SNAKE");
        entities.put(Constant.TYPES_ENTITIES_SKELETON2, "SKELETON2");
        entities.put(Constant.TYPES_ENTITIES_BOSS, "BOSS");
        entities.put(Constant.TYPES_ENTITIES_DEATHKNIGHT, "DEATHKNIGHT");
        // Armors
        entities.put(Constant.TYPES_ENTITIES_FIREFOX, "FIREFOX");
        entities.put(Constant.TYPES_ENTITIES_CLOTHARMOR, "CLOTHARMOR");
        entities.put(Constant.TYPES_ENTITIES_LEATHERARMOR, "LEATHERARMOR");
        entities.put(Constant.TYPES_ENTITIES_MAILARMOR, "MAILARMOR");
        entities.put(Constant.TYPES_ENTITIES_PLATEARMOR, "PLATEARMOR");
        entities.put(Constant.TYPES_ENTITIES_REDARMOR, "REDARMOR");
        entities.put(Constant.TYPES_ENTITIES_GOLDENARMOR, "GOLDENARMOR");
        // Objects
        entities.put(Constant.TYPES_ENTITIES_FLASK, "FLASK");
        entities.put(Constant.TYPES_ENTITIES_BURGER, "BURGER");
        entities.put(Constant.TYPES_ENTITIES_CHEST, "CHEST");
        entities.put(Constant.TYPES_ENTITIES_FIREPOTION, "FIREPOTION");
        entities.put(Constant.TYPES_ENTITIES_CAKE, "CAKE");
        // NPCs
        entities.put(Constant.TYPES_ENTITIES_GUARD, "GUARD");
        entities.put(Constant.TYPES_ENTITIES_KING, "KING");
        entities.put(Constant.TYPES_ENTITIES_OCTOCAT, "OCTOCAT");
        entities.put(Constant.TYPES_ENTITIES_VILLAGEGIRL, "VILLAGEGIRL");
        entities.put(Constant.TYPES_ENTITIES_VILLAGER, "VILLAGER");
        entities.put(Constant.TYPES_ENTITIES_PRIEST, "PRIEST");
        entities.put(Constant.TYPES_ENTITIES_SCIENTIST, "SCIENTIST");
        entities.put(Constant.TYPES_ENTITIES_AGENT, "AGENT");
        entities.put(Constant.TYPES_ENTITIES_RICK, "RICK");
        entities.put(Constant.TYPES_ENTITIES_NYAN, "NYAN");
        entities.put(Constant.TYPES_ENTITIES_SORCERER, "SORCERER");
        entities.put(Constant.TYPES_ENTITIES_BEACHNPC, "BEACHNPC");
        entities.put(Constant.TYPES_ENTITIES_FORESTNPC, "FORESTNPC");
        entities.put(Constant.TYPES_ENTITIES_DESERTNPC, "DESERTNPC");
        entities.put(Constant.TYPES_ENTITIES_LAVANPC, "LAVANPC");
        entities.put(Constant.TYPES_ENTITIES_CODER, "CODER");
        // Weapons
        entities.put(Constant.TYPES_ENTITIES_SWORD1, "SWORD1");
        entities.put(Constant.TYPES_ENTITIES_SWORD2, "SWORD2");
        entities.put(Constant.TYPES_ENTITIES_REDSWORD, "REDSWORD");
        entities.put(Constant.TYPES_ENTITIES_GOLDENSWORD, "GOLDENSWORD");
        entities.put(Constant.TYPES_ENTITIES_MORNINGSTAR, "MORNINGSTAR");
        entities.put(Constant.TYPES_ENTITIES_AXE, "AXE");
        entities.put(Constant.TYPES_ENTITIES_BLUESWORD, "BLUESWORD");
        typesToString.put("Entities", entities);

        Map<Integer, String> orientations = new HashMap<>();
        orientations.put(Constant.TYPES_ORIENTATIONS_UP, "UP");
        orientations.put(Constant.TYPES_ORIENTATIONS_DOWN, "DOWN");
        orientations.put(Constant.TYPES_ORIENTATIONS_LEFT, "LEFT");
        orientations.put(Constant.TYPES_ORIENTATIONS_RIGHT, "RIGHT");
        typesToString.put("Orientations", orientations);
    }

    public static final Map<String, Pair<Integer, String>> stringToKindsMap = new HashMap<>();

    static {
        stringToKindsMap.put("warrior", new Pair<>(Constant.TYPES_ENTITIES_WARRIOR, "player"));

        stringToKindsMap.put("rat", new Pair<>(Constant.TYPES_ENTITIES_RAT, "mob"));
        stringToKindsMap.put("skeleton", new Pair<>(Constant.TYPES_ENTITIES_SKELETON, "mob"));
        stringToKindsMap.put("goblin", new Pair<>(Constant.TYPES_ENTITIES_GOBLIN, "mob"));
        stringToKindsMap.put("ogre", new Pair<>(Constant.TYPES_ENTITIES_OGRE, "mob"));
        stringToKindsMap.put("spectre", new Pair<>(Constant.TYPES_ENTITIES_SPECTRE, "mob"));
        stringToKindsMap.put("deathknight", new Pair<>(Constant.TYPES_ENTITIES_DEATHKNIGHT, "mob"));
        stringToKindsMap.put("crab", new Pair<>(Constant.TYPES_ENTITIES_CRAB, "mob"));
        stringToKindsMap.put("snake", new Pair<>(Constant.TYPES_ENTITIES_SNAKE, "mob"));
        stringToKindsMap.put("bat", new Pair<>(Constant.TYPES_ENTITIES_BAT, "mob"));
        stringToKindsMap.put("wizard", new Pair<>(Constant.TYPES_ENTITIES_WIZARD, "mob"));
        stringToKindsMap.put("eye", new Pair<>(Constant.TYPES_ENTITIES_EYE, "mob"));
        stringToKindsMap.put("skeleton2", new Pair<>(Constant.TYPES_ENTITIES_SKELETON2, "mob"));
        stringToKindsMap.put("boss", new Pair<>(Constant.TYPES_ENTITIES_BOSS, "mob"));

        stringToKindsMap.put("sword1", new Pair<>(Constant.TYPES_ENTITIES_SWORD1, "weapon"));
        stringToKindsMap.put("sword2", new Pair<>(Constant.TYPES_ENTITIES_SWORD2, "weapon"));
        stringToKindsMap.put("axe", new Pair<>(Constant.TYPES_ENTITIES_AXE, "weapon"));
        stringToKindsMap.put("redsword", new Pair<>(Constant.TYPES_ENTITIES_REDSWORD, "weapon"));
        stringToKindsMap.put("bluesword", new Pair<>(Constant.TYPES_ENTITIES_BLUESWORD, "weapon"));
        stringToKindsMap.put("goldensword", new Pair<>(Constant.TYPES_ENTITIES_GOLDENSWORD, "weapon"));
        stringToKindsMap.put("morningstar", new Pair<>(Constant.TYPES_ENTITIES_MORNINGSTAR, "weapon"));

        stringToKindsMap.put("firefox", new Pair<>(Constant.TYPES_ENTITIES_FIREFOX, "armor"));
        stringToKindsMap.put("clotharmor", new Pair<>(Constant.TYPES_ENTITIES_CLOTHARMOR, "armor"));
        stringToKindsMap.put("leatherarmor", new Pair<>(Constant.TYPES_ENTITIES_LEATHERARMOR, "armor"));
        stringToKindsMap.put("mailarmor", new Pair<>(Constant.TYPES_ENTITIES_MAILARMOR, "armor"));
        stringToKindsMap.put("platearmor", new Pair<>(Constant.TYPES_ENTITIES_PLATEARMOR, "armor"));
        stringToKindsMap.put("redarmor", new Pair<>(Constant.TYPES_ENTITIES_REDARMOR, "armor"));
        stringToKindsMap.put("goldenarmor", new Pair<>(Constant.TYPES_ENTITIES_GOLDENARMOR, "armor"));

        stringToKindsMap.put("flask", new Pair<>(Constant.TYPES_ENTITIES_FLASK, "object"));
        stringToKindsMap.put("cake", new Pair<>(Constant.TYPES_ENTITIES_CAKE, "object"));
        stringToKindsMap.put("burger", new Pair<>(Constant.TYPES_ENTITIES_BURGER, "object"));
        stringToKindsMap.put("chest", new Pair<>(Constant.TYPES_ENTITIES_CHEST, "object"));
        stringToKindsMap.put("firepotion", new Pair<>(Constant.TYPES_ENTITIES_FIREPOTION, "object"));

        stringToKindsMap.put("guard", new Pair<>(Constant.TYPES_ENTITIES_GUARD, "npc"));
        stringToKindsMap.put("villagegirl", new Pair<>(Constant.TYPES_ENTITIES_VILLAGEGIRL, "npc"));
        stringToKindsMap.put("villager", new Pair<>(Constant.TYPES_ENTITIES_VILLAGER, "npc"));
        stringToKindsMap.put("coder", new Pair<>(Constant.TYPES_ENTITIES_CODER, "npc"));
        stringToKindsMap.put("scientist", new Pair<>(Constant.TYPES_ENTITIES_SCIENTIST, "npc"));
        stringToKindsMap.put("priest", new Pair<>(Constant.TYPES_ENTITIES_PRIEST, "npc"));
        stringToKindsMap.put("king", new Pair<>(Constant.TYPES_ENTITIES_KING, "npc"));
        stringToKindsMap.put("rick", new Pair<>(Constant.TYPES_ENTITIES_RICK, "npc"));
        stringToKindsMap.put("nyan", new Pair<>(Constant.TYPES_ENTITIES_NYAN, "npc"));
        stringToKindsMap.put("sorcerer", new Pair<>(Constant.TYPES_ENTITIES_SORCERER, "npc"));
        stringToKindsMap.put("agent", new Pair<>(Constant.TYPES_ENTITIES_AGENT, "npc"));
        stringToKindsMap.put("octocat", new Pair<>(Constant.TYPES_ENTITIES_OCTOCAT, "npc"));
        stringToKindsMap.put("beachnpc", new Pair<>(Constant.TYPES_ENTITIES_BEACHNPC, "npc"));
        stringToKindsMap.put("forestnpc", new Pair<>(Constant.TYPES_ENTITIES_FORESTNPC, "npc"));
        stringToKindsMap.put("desertnpc", new Pair<>(Constant.TYPES_ENTITIES_DESERTNPC, "npc"));
        stringToKindsMap.put("lavanpc", new Pair<>(Constant.TYPES_ENTITIES_LAVANPC, "npc"));
    }

    public static final Map<Integer, Integer> rankedWeapons = new HashMap<>();

    static {
        rankedWeapons.put(Constant.TYPES_ENTITIES_SWORD1, 0);
        rankedWeapons.put(Constant.TYPES_ENTITIES_SWORD2, 1);
        rankedWeapons.put(Constant.TYPES_ENTITIES_AXE, 2);
        rankedWeapons.put(Constant.TYPES_ENTITIES_MORNINGSTAR, 3);
        rankedWeapons.put(Constant.TYPES_ENTITIES_BLUESWORD, 4);
        rankedWeapons.put(Constant.TYPES_ENTITIES_REDSWORD, 5);
        rankedWeapons.put(Constant.TYPES_ENTITIES_GOLDENSWORD, 6);
    }

    public static final Map<Integer, Integer> rankedArmors = new HashMap<>();

    static {
        rankedArmors.put(Constant.TYPES_ENTITIES_CLOTHARMOR, 0);
        rankedArmors.put(Constant.TYPES_ENTITIES_LEATHERARMOR, 1);
        rankedArmors.put(Constant.TYPES_ENTITIES_MAILARMOR, 2);
        rankedArmors.put(Constant.TYPES_ENTITIES_PLATEARMOR, 3);
        rankedArmors.put(Constant.TYPES_ENTITIES_REDARMOR, 4);
        rankedArmors.put(Constant.TYPES_ENTITIES_GOLDENARMOR, 5);
    }

    // eg: Constant.TYPES_ENTITIES_WARRIOR -> "warrior"
    public static final Map<Integer, String> kindsToStringMap = new HashMap<>();

    static {
        for (Map.Entry<String, Pair<Integer, String>> entry : stringToKindsMap.entrySet()) {
            kindsToStringMap.put(entry.getValue().getKey(), entry.getKey());
        }
    }

    // eg: Constant.TYPES_ENTITIES_WARRIOR -> "player"
    public static final Map<Integer, String> kindsToTypesMap = new HashMap<>();

    static {
        for (Map.Entry<String, Pair<Integer, String>> entry : stringToKindsMap.entrySet()) {
            kindsToTypesMap.put(entry.getValue().getKey(), entry.getValue().getValue());
        }
    }

    public static List<Integer> randomItemKind = new ArrayList<>();

    static {
        for (Map.Entry<Integer, Integer> entry : Types.rankedWeapons.entrySet()) {
            if (entry.getKey() == Constant.TYPES_ENTITIES_SWORD1) {
                continue;
            }
            randomItemKind.add(entry.getKey());
        }
        for (Map.Entry<Integer, Integer> entry : Types.rankedArmors.entrySet()) {
            if (entry.getKey() == Constant.TYPES_ENTITIES_CLOTHARMOR) {
                continue;
            }
            randomItemKind.add(entry.getKey());
        }
    }

    public static Integer getRandomItemKind() {
        int index = new Random().nextInt(Types.randomItemKind.size());
        return Types.randomItemKind.get(index);
    }

    // eg: Constant.TYPES_ENTITIES_WARRIOR -> "player"
    public static String getType(Integer kind) {
        return Types.kindsToTypesMap.get(kind);
    }

    public static Integer getWeaponRank(Integer weaponKind) {
        return Types.rankedWeapons.get(weaponKind);
    }

    public static Integer getArmorRank(Integer armorKind) {
        return Types.rankedArmors.get(armorKind);
    }

    public static boolean isPlayer(Integer kind) {
        return Types.getType(kind).equals("player");
    }

    public static boolean isMob(Integer kind) {
        return Types.getType(kind).equals("mob");
    }

    public static boolean isNpc(Integer kind) {
        return Types.getType(kind).equals("npc");
    }

    public static boolean isCharacter(Integer kind) {
        return Types.isPlayer(kind) || Types.isNpc(kind) || Types.isMob(kind);
    }

    public static boolean isArmor(Integer kind) {
        return Types.getType(kind).equals("armor");
    }

    public static boolean isWeapon(Integer kind) {
        return Types.getType(kind).equals("weapon");
    }

    public static boolean isObject(Integer kind) {
        return Types.getType(kind).equals("object");
    }

    public static boolean isChest(Integer kind) {
        return kind == Constant.TYPES_ENTITIES_CHEST;
    }

    public static boolean isItem(Integer kind) {
        return Types.isWeapon(kind) || Types.isArmor(kind) || Types.isObject(kind) || Types.isChest(kind);
    }

    public static boolean isHealingItem(Integer kind) {
        return kind == Constant.TYPES_ENTITIES_FLASK || kind == Constant.TYPES_ENTITIES_BURGER;
    }

    public static boolean isExpendableItem(Integer kind) {
        return Types.isHealingItem(kind) || kind == Constant.TYPES_ENTITIES_FIREPOTION || kind == Constant.TYPES_ENTITIES_CAKE;
    }

    // eg: "warrior" -> Constant.TYPES_ENTITIES_WARRIOR
    public static Integer getKindFromString(String kind) {
        return Types.stringToKindsMap.get(kind).getKey();
    }

    // eg: Constant.TYPES_ENTITIES_WARRIOR -> "player"
    public static String getKindAsString(Integer kind) {
        return Types.kindsToStringMap.get(kind);
    }

    public static void forEachKind(Consumer<Pair<Integer, String>> callback) {
        for (Map.Entry<String, Pair<Integer, String>> entry : stringToKindsMap.entrySet()) {
            callback.accept(new Pair<>(entry.getValue().getKey(), entry.getKey()));
        }
    }

    public static void forEachArmorKind(Consumer<Pair<Integer, String>> callback) {
        Types.forEachKind(integerStringPair -> {
            if (Types.isArmor(integerStringPair.getKey())) {
                callback.accept(integerStringPair);
            }
        });
    }

    public static void forEachMobOrNpcKind(Consumer<Pair<Integer, String>> callback) {
        Types.forEachKind(integerStringPair -> {
            if (Types.isMob(integerStringPair.getKey()) || Types.isNpc(integerStringPair.getKey())) {
                callback.accept(integerStringPair);
            }
        });
    }

    public static String getOrientationAsString(Integer orientation) {
        switch (orientation) {
            case Constant.TYPES_ORIENTATIONS_LEFT:
                return "left";
            case Constant.TYPES_ORIENTATIONS_RIGHT:
                return "right";
            case Constant.TYPES_ORIENTATIONS_UP:
                return "up";
            case Constant.TYPES_ORIENTATIONS_DOWN:
                return "down";
            default:
                return "";
        }
    }

    public static String getMessageTypeAsString(Integer type) {
        return Types.typesToString.get("Messages").getOrDefault(type, "UNKNOWN");
    }

    /**
     * Test
     *
     * @param args
     */
    public static void main(String[] args) {
        // Types.forEachKind(integerStringPair -> System.out.println(integerStringPair.getKey() + ":" + integerStringPair.getValue()));
        // Types.forEachArmorKind(integerStringPair -> System.out.println(integerStringPair.getKey() + ":" + integerStringPair.getValue()));
        // Types.forEachMobOrNpcKind(integerStringPair -> System.out.println(integerStringPair.getKey() + ":" + integerStringPair.getValue()));
        System.out.println(Types.getMessageTypeAsString(1));
    }

}
