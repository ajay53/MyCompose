package com.goazzi.mycompose.util

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesCustom {

    private const val PREFS_NAME = "very_safe_file"

    private var preferences: SharedPreferences? = null

    private fun getPref(context: Context): SharedPreferences {
        if (preferences == null) {
            preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }

        return preferences!!
    }

    fun putString(context: Context, key: String, value: String) {
        if (key.isBlank()) {
            return
        }
        with(getPref(context).edit()) {
            putString(key, value)
            apply()
        }
    }

    fun putInt(context: Context, key: String, value: Int) {
        if (key.isBlank()) {
            return
        }
        with(getPref(context).edit()) {
            putInt(key, value)
            apply()
        }
    }

    fun getString(context: Context, key: String): String {
        if (key.isBlank()) {
            return ""
        }
        return getPref(context).getString(key, "") ?: ""
    }



    fun getInt(context: Context, key: String): Int {
        if (key.isBlank()) {
            return 0
        }
        return getPref(context).getInt(key, 0)
    }

    fun putBoolean(context: Context, key: String, value: Boolean) {
        if (key.isBlank()) {
            return
        }
        with(getPref(context).edit()) {
            putBoolean(key, value)
            apply()
        }
    }

    fun getBoolean(context: Context, key: String): Boolean {
        if (key.isBlank()) {
            return false
        }
        return getPref(context).getBoolean(key, false)
    }

    fun clear(context: Context) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            apply()
        }
    }

}