package com.example.shoppinglist.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

object ThemeUtil {
    private const val THEME_PREFS = "theme_preferences"
    private const val KEY_THEME_MODE = "theme_mode"

    const val MODE_SYSTEM = 0
    const val MODE_LIGHT = 1
    const val MODE_DARK = 2

    private fun getThemePreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(THEME_PREFS, Context.MODE_PRIVATE)
    }

    fun getCurrentThemeMode(context: Context): Int {
        return getThemePreferences(context).getInt(KEY_THEME_MODE, MODE_SYSTEM)
    }

    fun setThemeMode(context: Context, mode: Int) {
        getThemePreferences(context).edit().putInt(KEY_THEME_MODE, mode).apply()
        applyTheme(mode)
    }

    fun applyTheme(mode: Int) {
        when (mode) {
            MODE_LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            MODE_DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}