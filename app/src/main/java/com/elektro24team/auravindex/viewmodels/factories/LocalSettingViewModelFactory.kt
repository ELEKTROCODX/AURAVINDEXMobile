package com.elektro24team.auravindex.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.elektro24team.auravindex.data.repository.LocalSettingRepository
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel

class LocalSettingViewModelFactory(
    private val repository: LocalSettingRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocalSettingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LocalSettingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
