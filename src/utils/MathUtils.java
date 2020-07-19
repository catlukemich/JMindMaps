package utils;

public class MathUtils {

    static public int max(int... values) {
        int max = Integer.MIN_VALUE;
        for (int value : values) {
            max = Math.max(value, max);
        }
        return max;
    }
}
