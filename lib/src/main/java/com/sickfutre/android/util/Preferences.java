package com.sickfutre.android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Alex Dzeshko on 18-Feb-16.
 */
public class Preferences {

    private static Preferences sInstance;
    private Context context;

    private Preferences(Context context) {
        this.context = context;
    }
    public static Preferences get(Context context) {
        if (sInstance == null) {
            sInstance = new Preferences(context);
        }
        return sInstance;
    }

    public String putPreference(String key, String value) {
        preferences().edit().putString(key, value).apply();
        return value;
    }

    public int putPreference(String key, int value) {
        preferences().edit().putInt(key, value).apply();
        return value;
    }

    public boolean putPreference(String key, boolean value) {
        preferences().edit().putBoolean(key, value).apply();
        return value;
    }

    public SharedPreferences preferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getPreferenceString(String key, String defValue) {
        return preferences().getString(key, defValue);
    }

    public int getPreferenceInt(String key, int defValue) {
        return preferences().getInt(key, defValue);
    }

    public boolean getPreferenceBool(String key, boolean defValue) {
        return preferences().getBoolean(key, defValue);
    }
}
