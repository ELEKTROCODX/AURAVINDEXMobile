package com.elektro24team.auravindex.view.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elektro24team.auravindex.model.ApiResponse
import com.elektro24team.auravindex.model.Book
import com.elektro24team.auravindex.retrofit.BookClient
import kotlinx.coroutines.launch

class BookViewModel: ViewModel(){
    var posts = mutableStateOf<List<Book>>(emptyList())
        private set
    var latestReleases = mutableStateOf<List<Book>>(emptyList())
        private set
    private val _book = MutableLiveData<Book?>()
    val book: LiveData<Book?> = _book
    var filteredBooks = mutableStateOf<List<Book>>(emptyList())
        private set
    init {
        fetchBooks(showDuplicates = false, showLents = false, page = 1, limit = 10)
        fetchLatestReleases()
        fetchFilteredBooks(filter = "", value = "")
    }


    private fun fetchBooks(showDuplicates: Boolean = true, showLents: Boolean = true, page: Int = 1, limit: Int = 10){
        viewModelScope.launch {
            try {
                val response = BookClient.apiService.getBooks(showDuplicates = showDuplicates, showLents = showLents, page, limit)
                posts.value = response.data
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
    private fun fetchLatestReleases(limit: Int = 10){
        viewModelScope.launch {
            try {
                val response = BookClient.apiService.getLatestReleases(limit)
                latestReleases.value = response.data
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun fetchFilteredBooks(showDuplicates: Boolean = true, showLents: Boolean = true, filter: String, value: String){
        viewModelScope.launch {
            try {
                val response = BookClient.apiService.getFilteredBooks(showDuplicates,showLents,filter,value)
                filteredBooks.value = response.data
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    fun fetchBookById(id: String) {
        viewModelScope.launch {
            try {
                val result = BookClient.apiService.getBookById(id)
                _book.postValue(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getDistinctBooksByTitle(books: List<Book>, query: String): List<Book> {
        return books
            .filter { it.title.contains(query, ignoreCase = true) }
            .distinctBy { it._id }
    }


}