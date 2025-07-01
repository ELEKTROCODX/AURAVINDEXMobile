package com.elektrocodx.auravindex.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.elektrocodx.auravindex.data.repository.PlanRepository
import com.elektrocodx.auravindex.viewmodels.PlanViewModel

class PlanViewModelFactory(
    private val repository: PlanRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlanViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlanViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}