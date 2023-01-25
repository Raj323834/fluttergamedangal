package in.glg.rummy.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class RummyPrefManagerTracker {
    public static void saveString(Context context, String key, String value) {
        if(context != null)
        {
            SharedPreferences.Editor editor = context.getSharedPreferences(RummyConstants.PREFS_FILE_NAME_TRACK, 0).edit();
            editor.putString(key, value);
            editor.commit();
        }

    }

    public static void saveInt(Context context, String key, int value) {
        if(context != null)
        {
            SharedPreferences.Editor editor = context.getSharedPreferences(RummyConstants.PREFS_FILE_NAME_TRACK, 0).edit();
            editor.putInt(key, value);
            editor.commit();
        }

    }

    public static void saveBool(Context context, String key, boolean value) {
        if(context != null)
        {
            SharedPreferences.Editor editor = context.getSharedPreferences(RummyConstants.PREFS_FILE_NAME_TRACK, 0).edit();
            editor.putBoolean(key, value);
            editor.commit();
        }

    }

    public static String getString(Context context, String key, String defValue) {
        if(context != null)
        {
            return context.getSharedPreferences(RummyConstants.PREFS_FILE_NAME_TRACK, 0).getString(key, defValue);
        }
        else
        {
            return  defValue;
        }

    }

    public static int getInt(Context context, String key, int defValue) {
        if(context != null)
        {
            return context.getSharedPreferences(RummyConstants.PREFS_FILE_NAME_TRACK, 0).getInt(key, defValue);
        }
        else
        {
            return defValue;
        }

    }

    public static boolean getBool(Context context, String key, boolean defValue) {
        if(context != null)
        {
            return context.getSharedPreferences(RummyConstants.PREFS_FILE_NAME_TRACK, 0).getBoolean(key, defValue);
        }
        else
        {
            return defValue;
        }

    }

    public static void createSharedPreferencesTracker(Context context)
    {

        SharedPreferences sharedPreferencesTracker = context.getSharedPreferences(RummyConstants.PREFS_FILE_NAME_TRACK, 0); // 0 - for private mode

    }
    public static void clearPreferences(Context context) {
        if(context != null)
        {
            context.getSharedPreferences(RummyConstants.PREFS_FILE_NAME_TRACK, 0).edit().clear().commit();
        }


    }
}
