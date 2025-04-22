package com.elektro24team.auravindex.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.asLiveData
import com.elektro24team.auravindex.data.LocalSettingsAppDatabase
import com.elektro24team.auravindex.data.repository.LocalSettingRepository
import com.elektro24team.auravindex.model.LocalSetting

class LocalSettingViewModel(application: Application): AndroidViewModel(application){
    private val repository: LocalSettingRepository
    val allLocalSettings: LiveData<List<LocalSetting>>

    init {
        val localsettingDao = LocalSettingsAppDatabase.getDatabase(application).localsettingDao()
        repository = LocalSettingRepository(localsettingDao)
        allLocalSettings = repository.allLocalSettings.asLiveData()
    }
    fun insert(localsetting: LocalSetting) = viewModelScope.launch {
        repository.insert(localsetting)
    }
    fun update(localsetting: LocalSetting) = viewModelScope.launch {
        repository.update(localsetting)
    }
    fun delete(localsetting: LocalSetting) = viewModelScope.launch {
        repository.delete(localsetting)
    }

}