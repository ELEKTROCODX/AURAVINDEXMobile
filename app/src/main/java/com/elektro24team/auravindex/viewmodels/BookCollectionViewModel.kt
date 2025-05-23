package com.elektro24team.auravindex.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elektro24team.auravindex.data.repository.BookCollectionRepository
import com.elektro24team.auravindex.model.local.BookCollectionEntity
import com.elektro24team.auravindex.viewmodels.base.BaseViewModel
import kotlinx.coroutines.launch

class BookCollectionViewModel(
    private val repository: BookCollectionRepository
) : BaseViewModel() {

    private val _bookCollections = MutableLiveData<List<BookCollectionEntity>>()
    val bookCollections: LiveData<List<BookCollectionEntity>> = _bookCollections

    fun loadBookCollections() {
        viewModelScope.launch {
            val result = repository.getBookCollections()
            _bookCollections.postValue(result)
        }
    }
}