package alg;


/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 */
public class Utils {
    public static <T extends Comparable<T>> T min(T a, T b) {
        if(a.compareTo(b) < 0)
            return a;
        return b;
    }

    public static long hashString(String s) {
        final long Bit = 10007, Mod = 1000000000 + 9;
        long ret = 0;
        for(int i = 0;i < s.length();i ++) {
            ret *= Bit;
            ret += s.charAt(i);
            ret %= Mod;
        }

        return ret;
    }
}
