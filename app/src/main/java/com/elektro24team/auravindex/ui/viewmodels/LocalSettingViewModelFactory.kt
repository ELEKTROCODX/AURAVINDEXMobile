
package com.elektro24team.auravindex.ui.viemodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.elektro24team.auravindex.ui.viewmodels.LocalSettingViewModel


class LocalSettingViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocalSettingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LocalSettingViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
