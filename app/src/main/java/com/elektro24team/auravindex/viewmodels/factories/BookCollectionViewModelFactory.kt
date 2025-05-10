package com.elektro24team.auravindex.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.elektro24team.auravindex.data.repository.BookCollectionRepository
import com.elektro24team.auravindex.viewmodels.BookCollectionViewModel

class BookCollectionViewModelFactory(
    private val repository: BookCollectionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookCollectionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookCollectionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}