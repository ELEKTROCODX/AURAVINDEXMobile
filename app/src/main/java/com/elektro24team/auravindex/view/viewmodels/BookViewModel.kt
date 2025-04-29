package com.elektro24team.auravindex.view.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elektro24team.auravindex.model.Book
import com.elektro24team.auravindex.retrofit.BookClient
import kotlinx.coroutines.launch

class BookViewModel: ViewModel(){
    var posts = mutableStateOf<List<Book>>(emptyList())
        private set
    init {
        fetchBooks(showDuplicates = false, showLents = false, page = 1, limit = 10)
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
}