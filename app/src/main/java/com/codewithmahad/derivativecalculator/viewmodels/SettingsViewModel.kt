package com.codewithmahad.derivativecalculator.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.codewithmahad.derivativecalculator.HistoryAutoClear
import com.codewithmahad.derivativecalculator.SettingsRepository
import com.codewithmahad.derivativecalculator.ThemeOption
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SettingsRepository(application)

    // --- UI State ---
    var isThemeLoading by mutableStateOf(true)
        private set

    val themeOption: StateFlow<ThemeOption> = repository.themeOptionFlow
        .onEach { isThemeLoading = false } // Set loading to false once the first value is emitted
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThemeOption.SYSTEM
        )

    val hapticFeedbackEnabled: StateFlow<Boolean> = repository.hapticFeedbackFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    val keepScreenOn: StateFlow<Boolean> = repository.keepScreenOnFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val precision: StateFlow<Int> = repository.precisionFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 4 // Default precision
        )

    val historyAutoClear: StateFlow<HistoryAutoClear> = repository.historyAutoClearFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HistoryAutoClear.NEVER
        )

    fun setThemeOption(themeOption: ThemeOption) {
        viewModelScope.launch {
            repository.saveThemeOption(themeOption)
        }
    }

    fun setHapticFeedback(isEnabled: Boolean) {
        viewModelScope.launch {
            repository.saveHapticFeedback(isEnabled)
        }
    }

    fun setKeepScreenOn(enabled: Boolean) {
        viewModelScope.launch {
            repository.saveKeepScreenOn(enabled)
        }
    }

    fun setPrecision(precision: Int) {
        viewModelScope.launch {
            repository.savePrecision(precision)
        }
    }

    fun setHistoryAutoClear(option: HistoryAutoClear) {
        viewModelScope.launch {
            repository.saveHistoryAutoClear(option)
        }
    }
}