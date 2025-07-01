package com.elektrocodx.auravindex.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.elektrocodx.auravindex.data.repository.AuditLogRepository
import com.elektrocodx.auravindex.viewmodels.AuditLogViewModel

class AuditLogViewModelFactory(
    private val repository: AuditLogRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuditLogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuditLogViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}