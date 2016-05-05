package com.sickfutre.android.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Created by Alex Dzeshko on 18-Feb-16.
 */
class Preferences(val context: Context) {

    fun preferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun putPreference(key: String, value: String? = null) {
        preferences().edit().putString(key, value).apply()
    }

    fun putPreference(key: String, value: Int = 0) {
        preferences().edit().putInt(key, value).apply()
    }

    fun putPreference(key: String, value: Boolean = false) {
        preferences().edit().putBoolean(key, value).apply()
    }

    fun getPreferenceString(key: String, defValue: String? = null): String? {
        return preferences().getString(key, defValue)
    }

    fun getPreferenceInt(key: String, defValue: Int = 0): Int {
        return preferences().getInt(key, defValue)
    }

    fun getPreferenceBool(key: String, defValue: Boolean = false): Boolean {
        return preferences().getBoolean(key, defValue)
    }

}
