package com.elektrocodx.auravindex.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.elektrocodx.auravindex.data.repository.GenderRepository
import com.elektrocodx.auravindex.viewmodels.GenderViewModel

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