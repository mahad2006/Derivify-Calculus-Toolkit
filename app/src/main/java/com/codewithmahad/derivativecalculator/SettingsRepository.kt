package com.codewithmahad.derivativecalculator

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.codewithmahad.derivativecalculator.data.HistoryItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

enum class ThemeOption {
    LIGHT, DARK, SYSTEM
}

enum class HistoryAutoClear {
    NEVER, ONE_DAY, ONE_WEEK, ONE_MONTH
}

class SettingsRepository(context: Context) {

    private val dataStore = context.dataStore
    private val gson = Gson()

    // Keys to identify our settings
    companion object {
        val THEME_OPTION_KEY = stringPreferencesKey("theme_option")
        val HAPTIC_FEEDBACK_KEY = booleanPreferencesKey("haptic_feedback")
        val HISTORY_KEY = stringPreferencesKey("calculation_history") // New key for history
        val KEEP_SCREEN_ON_KEY = booleanPreferencesKey("keep_screen_on")
        val PRECISION_KEY = intPreferencesKey("precision")
        val HISTORY_AUTO_CLEAR_KEY = stringPreferencesKey("history_auto_clear")
    }

    // --- History Auto Clear Preference ---
    val historyAutoClearFlow: Flow<HistoryAutoClear> = dataStore.data.map { preferences ->
        val optionName = preferences[HISTORY_AUTO_CLEAR_KEY] ?: HistoryAutoClear.NEVER.name
        try {
            HistoryAutoClear.valueOf(optionName)
        } catch (e: IllegalArgumentException) {
            HistoryAutoClear.NEVER
        }
    }

    suspend fun saveHistoryAutoClear(option: HistoryAutoClear) {
        dataStore.edit { preferences ->
            preferences[HISTORY_AUTO_CLEAR_KEY] = option.name
        }
    }

    // --- Theme Preference ---
    suspend fun saveThemeOption(themeOption: ThemeOption) {
        dataStore.edit { preferences ->
            preferences[THEME_OPTION_KEY] = themeOption.name
        }
    }

    val themeOptionFlow: Flow<ThemeOption> = dataStore.data.map { preferences ->
        val themeName = preferences[THEME_OPTION_KEY] ?: ThemeOption.SYSTEM.name
        try {
            ThemeOption.valueOf(themeName)
        } catch (e: IllegalArgumentException) {
            // If the saved theme is invalid (e.g., from a previous version), default to System
            ThemeOption.SYSTEM
        }
    }

    // --- Haptic Feedback Preference ---
    val hapticFeedbackFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[HAPTIC_FEEDBACK_KEY] ?: true
    }

    suspend fun saveHapticFeedback(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[HAPTIC_FEEDBACK_KEY] = isEnabled
        }
    }

    // --- History Preference ---
    val historyFlow: Flow<List<HistoryItem>> = dataStore.data.map { preferences ->
        val jsonString = preferences[HISTORY_KEY]
        if (jsonString.isNullOrEmpty()) {
            emptyList()
        } else {
            val type = object : TypeToken<List<HistoryItem>>() {}.type
            gson.fromJson(jsonString, type)
        }
    }

    suspend fun saveHistory(history: List<HistoryItem>) {
        val jsonString = gson.toJson(history)
        dataStore.edit { preferences ->
            preferences[HISTORY_KEY] = jsonString
        }
    }

    // --- Keep Screen On Preference ---
    val keepScreenOnFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[KEEP_SCREEN_ON_KEY] ?: false
    }

    suspend fun saveKeepScreenOn(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEEP_SCREEN_ON_KEY] = enabled
        }
    }

    // --- Precision Preference ---
    val precisionFlow: Flow<Int> = dataStore.data.map { preferences ->
        preferences[PRECISION_KEY] ?: 4 // Default precision
    }

    suspend fun savePrecision(precision: Int) {
        dataStore.edit { preferences ->
            preferences[PRECISION_KEY] = precision
        }
    }
}