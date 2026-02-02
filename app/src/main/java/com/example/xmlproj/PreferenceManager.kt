package com.example.xmlproj

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_THEME_MODE = "theme_mode"
    }

    // Save string value
    fun saveString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    // Get string value
    fun getString(key: String, defaultValue: String = ""): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    // Save boolean value
    fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    // Get boolean value
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    // Save int value
    fun saveInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    // Get int value
    fun getInt(key: String, defaultValue: Int = 0): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    // User-specific methods
    fun saveUserEmail(email: String) {
        saveString(KEY_USER_EMAIL, email)
    }

    fun getUserEmail(): String {
        return getString(KEY_USER_EMAIL)
    }

    fun saveUserName(name: String) {
        saveString(KEY_USER_NAME, name)
    }

    fun getUserName(): String {
        return getString(KEY_USER_NAME)
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        saveBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
    }

    fun isLoggedIn(): Boolean {
        return getBoolean(KEY_IS_LOGGED_IN)
    }

    // Clear all data (useful for logout)
    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }

    // Remove specific key
    fun remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }
}
