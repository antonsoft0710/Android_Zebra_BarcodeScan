package code.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class OSettings {

    private static Activity mActivity;

    public OSettings(Activity mActivity) {
        OSettings.mActivity = mActivity;
    }

    /**
     * Clear save local data in App
     *
     * @param FileName : Which file you want to clear use it
     */
    public static void clearSharedPreference(String FileName) {
        SharedPreferences prefs = mActivity.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * Clear save local data in App
     */
    public static void clearSharedPreference() {
        SharedPreferences prefs = mActivity.getSharedPreferences(AppSettings.PREFS_MAIN_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * store data in  string type sharedPreferences
     *
     * @param key
     * @param value
     * @param FileName
     */
    public static void putString(String key, String value, String FileName) {
        SharedPreferences prefs = mActivity.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * store data in  string type sharedPreferences
     *
     * @param key
     * @param value
     */
    public static void putString(String key, String value) {
        SharedPreferences prefs = mActivity.getSharedPreferences(AppSettings.PREFS_MAIN_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * get data in string type sharedPreferences
     *
     * @param key
     * @param FileName
     * @return
     */
    public static String getString(String key, String FileName) {
        SharedPreferences prefs = mActivity.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        return prefs.getString(key, "");
    }

    /**
     * get data in string type sharedPreferences
     *
     * @param key
     * @return
     */
    public static String getString(String key) {
        SharedPreferences prefs = mActivity.getSharedPreferences(AppSettings.PREFS_MAIN_FILE, Context.MODE_PRIVATE);
        return prefs.getString(key, "");
    }


    /**
     * store data in  string type sharedPreferences
     *
     * @param key
     * @param value
     * @param FileName
     */
    public static void putBoolean(String key, boolean value, String FileName) {
        SharedPreferences prefs = mActivity.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * store data in  string type sharedPreferences
     *
     * @param key
     * @param value
     */
    public static void putBoolean(String key, boolean value) {
        SharedPreferences prefs = mActivity.getSharedPreferences(AppSettings.PREFS_MAIN_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * get data in string type sharedPreferences
     *
     * @param key
     * @param FileName
     * @return
     */
    public static Boolean getBoolean(String key, String FileName) {
        SharedPreferences prefs = mActivity.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        return prefs.getBoolean(key, true);
    }

    /**
     * get data in string type sharedPreferences
     *
     * @param key
     * @return
     */
    public static Boolean getBoolean(String key) {
        SharedPreferences prefs = mActivity.getSharedPreferences(AppSettings.PREFS_MAIN_FILE, Context.MODE_PRIVATE);
        return prefs.getBoolean(key, true);
    }


    /**
     * store data in  string type sharedPreferences
     *
     * @param key
     * @param value
     * @param FileName
     */
    public static void putInt(String key, int value, String FileName) {
        SharedPreferences prefs = mActivity.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * store data in  string type sharedPreferences
     *
     * @param key
     * @param value
     */
    public static void putInt(String key, int value) {
        SharedPreferences prefs = mActivity.getSharedPreferences(AppSettings.PREFS_MAIN_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * get data in string type sharedPreferences
     *
     * @param key
     * @param FileName
     * @return
     */
    public static int getInt(String key, String FileName) {
        SharedPreferences prefs = mActivity.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        return prefs.getInt(key, 0);
    }

    /**
     * get data in string type sharedPreferences
     *
     * @param key
     * @return
     */
    public static int getInt(String key) {
        SharedPreferences prefs = mActivity.getSharedPreferences(AppSettings.PREFS_MAIN_FILE, Context.MODE_PRIVATE);
        return prefs.getInt(key, 0);
    }

}
