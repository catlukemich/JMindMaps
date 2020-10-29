package utils;

import java.awt.*;

public class MathUtils {

    static public int max(int... values) {
        int max = Integer.MIN_VALUE;
        for (int value : values) {
            max = Math.max(value, max);
        }
        return max;
    }


    static public float distance(Point point1, Point point2) {
        int dx = Math.abs(point1.x - point2.x);
        int dy = Math.abs(point1.y - point2.y);

        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}
