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
        fetchBooks()
    }

    private fun fetchBooks(){
        viewModelScope.launch {
            try {
                val response = BookClient.apiService.getBooks()
                posts.value = response.data
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}