package com.elektro24team.auravindex.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elektro24team.auravindex.data.repository.BookRepository
import com.elektro24team.auravindex.model.Book
import com.elektro24team.auravindex.model.local.BookEntity
import kotlinx.coroutines.launch

class BookViewModel(
    private val repository: BookRepository
) : ViewModel() {

    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> = _books

    fun loadBooks() {
        viewModelScope.launch {
            val result = repository.getAllBooks()
            _books.postValue(result)
        }
    }
    fun loadBook(bookId: String) {
        viewModelScope.launch {
            if( _books.value?.find{ it._id == bookId } == null) {
                val result = repository.getBookById(bookId)
                _books.postValue(listOf(result))
            }
        }

    }
    
}