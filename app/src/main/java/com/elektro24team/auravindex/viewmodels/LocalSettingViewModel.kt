package com.elektro24team.auravindex.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elektro24team.auravindex.data.repository.LocalSettingRepository
import com.elektro24team.auravindex.utils.enums.SettingKey
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

    suspend fun loadSettings(vararg keys: String): Map<String, String> {
        val results = mutableMapOf<String, String>()
        keys.forEach { key ->
            val value = repository.getSetting(key) ?: ""
            results[key] = value
        }
        _settings.update { it + results }
        return results
    }

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

    fun clearSetting(keySetting: String) {
        viewModelScope.launch {
            repository.clearSetting(keySetting)
            _settings.update { it - keySetting }
        }
    }
    suspend fun clearSettings(vararg keys: String) {
        val results = mutableMapOf<String, String>()
        keys.forEach { key ->
            repository.clearSetting(key)
        }
    }
    fun clearUserSettings() {
        viewModelScope.launch {
            var keys = listOf(
                SettingKey.ID.keySetting,
                SettingKey.EMAIL.keySetting,
                SettingKey.PROFILE_IMAGE.keySetting,
                SettingKey.TOKEN.keySetting,
                SettingKey.ROLE_NAME.keySetting,
                SettingKey.ROLE_ID.keySetting,
            )
            clearSettings(*keys.toTypedArray())
        }
    }
}