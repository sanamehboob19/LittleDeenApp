package com.ccx.islamic.kids.littledeenapp.util


import android.content.Context
import android.content.SharedPreferences
import com.ccx.islamic.kids.littledeenapp.util.AppConstants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class PreferenceHelper @Inject constructor(
    @ApplicationContext context: Context
) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE)

    // ==========================================
    // 🧱 Generic Reusable Getters & Setters
    // ==========================================

    fun putString(key: String, value: String?) {
        prefs.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String = ""): String {
        return prefs.getString(key, defaultValue) ?: defaultValue
    }

    fun putBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return prefs.getBoolean(key, defaultValue)
    }

    fun putInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return prefs.getInt(key, defaultValue)
    }

    fun putFloat(key: String, value: Float) {
        prefs.edit().putFloat(key, value).apply()
    }

    fun getFloat(key: String, defaultValue: Float = 0f): Float {
        return prefs.getFloat(key, defaultValue)
    }

    fun putLong(key: String, value: Long) {
        prefs.edit().putLong(key, value).apply()
    }

    fun getLong(key: String, defaultValue: Long = 0L): Long {
        return prefs.getLong(key, defaultValue)
    }

    fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }

    fun clearAll() {
        prefs.edit().clear().apply()
    }

    // ==========================================
    // 🎯 Project-Specific Convenient Properties
    // (Localizes all keys in one clean place)
    // ==========================================

    var isFirstLaunch: Boolean
        get() = getBoolean(AppConstants.KEY_IS_FIRST_LAUNCH, true)
        set(value) = putBoolean(AppConstants.KEY_IS_FIRST_LAUNCH, value)

    var childName: String
        get() = getString(AppConstants.KEY_CHILD_NAME, AppConstants.DEFAULT_CHILD_NAME)
        set(value) = putString(AppConstants.KEY_CHILD_NAME, value)

    var childAgeGroup: String
        get() = getString(AppConstants.KEY_CHILD_AGE_GROUP, AppConstants.AGE_GROUP_6_TO_8)
        set(value) = putString(AppConstants.KEY_CHILD_AGE_GROUP, value)

    var childAvatarRes: Int
        get() = getInt(AppConstants.KEY_CHILD_AVATAR_RES, 0)
        set(value) = putInt(AppConstants.KEY_CHILD_AVATAR_RES, value)

    var customAvatarUri: String?
        get() = prefs.getString("key_custom_avatar_uri", null)
        set(value) = putString("key_custom_avatar_uri", value)

    var isTtsEnabled: Boolean
        get() = getBoolean(AppConstants.KEY_TTS_ENABLED, true)
        set(value) = putBoolean(AppConstants.KEY_TTS_ENABLED, value)

    var isBgMusicEnabled: Boolean
        get() = getBoolean(AppConstants.KEY_BG_MUSIC_ENABLED, true)
        set(value) = putBoolean(AppConstants.KEY_BG_MUSIC_ENABLED, value)

    var isSoundFxEnabled: Boolean
        get() = getBoolean(AppConstants.KEY_SOUND_FX_ENABLED, true)
        set(value) = putBoolean(AppConstants.KEY_SOUND_FX_ENABLED, value)

    var ttsSpeechRate: Int
        get() = getInt(AppConstants.KEY_TTS_SPEECH_RATE, 45)
        set(value) = putInt(AppConstants.KEY_TTS_SPEECH_RATE, value)

    var ttsPitch: Int
        get() = getInt(AppConstants.KEY_TTS_PITCH, 60)
        set(value) = putInt(AppConstants.KEY_TTS_PITCH, value)

    var appVolume: Int
        get() = getInt(AppConstants.KEY_APP_VOLUME, 80)
        set(value) = putInt(AppConstants.KEY_APP_VOLUME, value)
}