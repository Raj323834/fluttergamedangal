package in.glg.rummy.utils;

/**
 * Created by GridLogic on 31/8/17.
 */

import android.content.Context;
import android.content.SharedPreferences.Editor;

public class RummyPrefManager {
    public static void saveString(Context context, String key, String value) {
        Editor editor = context.getSharedPreferences(RummyConstants.PREFS_FILE_NAME, 0).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void saveInt(Context context, String key, int value) {
        Editor editor = context.getSharedPreferences(RummyConstants.PREFS_FILE_NAME, 0).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void saveBool(Context context, String key, boolean value) {
        Editor editor = context.getSharedPreferences(RummyConstants.PREFS_FILE_NAME, 0).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key, String defValue) {
        return context.getSharedPreferences(RummyConstants.PREFS_FILE_NAME, 0).getString(key, defValue);
    }

    public static int getInt(Context context, String key, int defValue) {
        return context.getSharedPreferences(RummyConstants.PREFS_FILE_NAME, 0).getInt(key, defValue);
    }

    public static boolean getBool(Context context, String key, boolean defValue) {
        return context.getSharedPreferences(RummyConstants.PREFS_FILE_NAME, 0).getBoolean(key, defValue);
    }

    public static void clearPreferences(Context context) {
        context.getSharedPreferences(RummyConstants.PREFS_FILE_NAME, 0).edit().clear().commit();
    }
}