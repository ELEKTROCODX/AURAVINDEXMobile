package com.elektrocodx.auravindex.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elektrocodx.auravindex.data.repository.BookCollectionRepository
import com.elektrocodx.auravindex.model.local.BookCollectionEntity
import com.elektrocodx.auravindex.viewmodels.base.BaseViewModel
import kotlinx.coroutines.launch

class BookCollectionViewModel(
    private val repository: BookCollectionRepository
) : BaseViewModel() {

    private val _bookCollections = MutableLiveData<List<BookCollectionEntity>?>()
    val bookCollections: LiveData<List<BookCollectionEntity>?> = _bookCollections

    fun loadBookCollections() {
        viewModelScope.launch {
            val result = repository.getBookCollections()
            _bookCollections.postValue(result)
        }
    }

    override fun clearViewModelData() {
        _bookCollections.value = null
    }
}