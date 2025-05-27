package com.elektro24team.auravindex.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.elektro24team.auravindex.data.repository.GenderRepository
import com.elektro24team.auravindex.viewmodels.GenderViewModel

class GenderViewModelFactory(
    private val repository: GenderRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GenderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GenderViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}