package in.glg.rummy.utils;

/**
 * Created by GridLogic on 31/8/17.
 */


import android.util.Log;

public class RummyTLog {
    private static final boolean LOG_ENABLED = false;

    public static void d(String var0, String var1) {
        Log.d(var0, String.format("%s", new Object[]{var1}));
    }

    public static void e(String var0, Exception var1) {
        Log.e(var0, var1.getMessage());
    }

    public static void e(String var0, String var1) {
        Log.e(var0, String.format("%s", new Object[]{var1}));
    }

    public static void i(String var0, String var1) {
        Log.i(var0, String.format("%s", new Object[]{var1}));
    }

    public static void v(String var0, String var1) {
        Log.v(var0, String.format("%s", new Object[]{var1}));
    }

    public static void w(String var0, String var1) {
        Log.w(var0, var1);
    }
}
