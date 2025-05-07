package com.elektro24team.auravindex.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elektro24team.auravindex.data.repository.LocalSettingRepository
import kotlinx.coroutines.launch

class LocalSettingViewModel(
    private val repository: LocalSettingRepository
) : ViewModel() {

    private val _settings = MutableLiveData<Map<String, String>>()
    val settings: LiveData<Map<String, String>> = _settings

    fun loadSetting(keySetting: String) {
        viewModelScope.launch {
            val result = repository.getSetting(keySetting)
            _settings.postValue(mapOf(keySetting to (result ?: "")))
        }
    }

    fun saveSetting(keySetting: String, keyValue: String) {
        viewModelScope.launch {
            repository.setSetting(keySetting, keyValue)
            _settings.postValue(mapOf(keySetting to keyValue))
        }
    }
}