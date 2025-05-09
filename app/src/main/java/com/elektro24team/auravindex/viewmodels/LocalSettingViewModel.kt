package com.elektro24team.auravindex.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elektro24team.auravindex.data.repository.LocalSettingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocalSettingViewModel(
    private val repository: LocalSettingRepository
) : ViewModel() {
    private val _settings = MutableStateFlow<Map<String, String>>(emptyMap())
    val settings: StateFlow<Map<String, String>> = _settings.asStateFlow()

    fun loadSetting(keySetting: String) {
        viewModelScope.launch {
            val result = repository.getSetting(keySetting)
            _settings.update { it + (keySetting to (result ?: "")) }
        }
    }

    fun saveSetting(keySetting: String, keyValue: String) {
        viewModelScope.launch {
            repository.setSetting(keySetting, keyValue)
            _settings.update { it + (keySetting to keyValue) }
        }
    }
}