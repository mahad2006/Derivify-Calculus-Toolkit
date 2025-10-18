package com.codewithmahad.derivativecalculator.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.codewithmahad.derivativecalculator.data.HistoryItem
import com.codewithmahad.derivativecalculator.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SettingsRepository(application)
    private var lastDeleted: HistoryItem? = null

    val history: StateFlow<List<HistoryItem>> = repository.historyFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun clearAndSaveHistory(history: List<HistoryItem>) {
        viewModelScope.launch {
            repository.saveHistory(history)
        }
    }

    fun deleteHistoryItem(item: HistoryItem) {
        viewModelScope.launch {
            val currentList = history.first()
            val newList = currentList.toMutableList().apply { remove(item) }
            lastDeleted = item
            repository.saveHistory(newList)
        }
    }

    fun undoDelete() {
        viewModelScope.launch {
            lastDeleted?.let {
                val currentList = history.first()
                val newList = currentList.toMutableList().apply { add(it) }.sortedByDescending { it.timestamp }
                repository.saveHistory(newList)
                lastDeleted = null
            }
        }
    }
}