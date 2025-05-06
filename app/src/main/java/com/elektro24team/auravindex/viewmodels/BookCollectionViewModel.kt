package com.elektro24team.auravindex.viewmodels

import com.elektro24team.auravindex.retrofit.BookCollectionClient
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elektro24team.auravindex.model.BookCollection
import kotlinx.coroutines.launch

class BookCollectionViewModel: ViewModel(){
    var posts = mutableStateOf<List<BookCollection>>(emptyList())
        private set
    init {
        fetchBookCollections()

    }


    private fun fetchBookCollections(){
        viewModelScope.launch {
            try {
                val response = BookCollectionClient.apiService.getBookCollections()
                posts.value = response.data
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}