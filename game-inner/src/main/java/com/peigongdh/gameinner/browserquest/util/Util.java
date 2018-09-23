package com.peigongdh.gameinner.browserquest.util;

import com.peigongdh.gameinner.browserquest.common.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class Util {

    public static int randomOrientation() {
        int r = new Random().nextInt(4);
        int orientation = 0;
        switch (r) {
            case 0:
                orientation = Constant.TYPES_ORIENTATIONS_LEFT;
                break;
            case 1:
                orientation = Constant.TYPES_ORIENTATIONS_RIGHT;
                break;
            case 2:
                orientation = Constant.TYPES_ORIENTATIONS_UP;
                break;
            case 3:
                orientation = Constant.TYPES_ORIENTATIONS_DOWN;
                break;
        }
        return orientation;
    }

    public static <T> boolean any(List<T> list, Predicate<T> callback) {
        for (T t : list) {
            if (callback.test(t)) {
                return true;
            }
        }
        return false;
    }

    public static <T> T detect(List<T> list, Predicate<T> callback) {
        for (T t : list) {
            if (callback.test(t)) {
                return t;
            }
        }
        return null;
    }

    public static <T> List<T> reject(List<T> list, Predicate<T> callback) {
        list.removeIf(callback);
        return list;
    }

    public static int distanceTo(int x1, int y1, int x2, int y2) {
        int distX = Math.abs(x1 - x2);
        int distY = Math.abs(y1 - y2);
        return distX > distY ? distX : distY;
    }

    /**
     * Test
     */
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        System.out.println(Util.any(list, s -> s.equals("a")));
        System.out.println(Util.detect(list, s -> s.equals("c")));
        System.out.println(Util.reject(list, s -> s.equals("c")));
        // use stream, more can see: https://www.ibm.com/developerworks/cn/java/j-lo-java8streamapi/index.html
    }
}
