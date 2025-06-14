package com.elektro24team.auravindex.viewmodels

import android.content.Context
import android.net.http.HttpException
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elektro24team.auravindex.model.BookList
import com.elektro24team.auravindex.model.api.BookListRequest
import com.elektro24team.auravindex.retrofit.BookListClient
import com.elektro24team.auravindex.viewmodels.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException


class BookListViewModel(
) :BaseViewModel() {
    private val _bookLists = MutableStateFlow<List<BookList>?>(null)
    val bookLists: StateFlow<List<BookList>?> = _bookLists.asStateFlow()

    fun loadUserLists(token: String, userId: String){
        viewModelScope.launch {
            val result = try {
                val remote = BookListClient.apiService.getUserBookLists(token = "Bearer $token", filterValue = userId)
                Result.success(remote)
            }catch (e: retrofit2.HttpException){
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("BookListViewModel", "HTTP error: ${e.code()} ${e.message()}")
                Log.e("BookListViewModel", "Error body: $errorBody")
                Result.failure(e)
            }catch (e: IOException) {
                Log.e("BookListViewModel", "Network error: ${e.localizedMessage}", e)
                Result.failure(e)
            } catch (e: Exception) {
                Log.e("BookListViewModel", "Unexpected error: ${e.localizedMessage}", e)
                Result.failure(e)
            }
            if (result.isSuccess){
                val lists = result.getOrNull()?.data
                Log.d("BookListViewModel", "Lists: $lists")
                _bookLists.value = lists
            }else{
                _bookLists.value = null
            }
        }
    }

    fun add(bookId: String, listId: String, token: String, context: Context) {
        viewModelScope.launch {
            Log.d("BookListViewModel", "Añadiendo libro: bookId=$bookId, listId=$listId, token=$token")

            val result = try {
                val remote = BookListClient.apiService.addBookToList(
                    token = "Bearer $token",
                    bookListId = listId,
                    bookId = bookId
                )
                Result.success(remote)
            } catch (e: retrofit2.HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("BookListViewModel", "Error HTTP: ${e.code()} ${e.message()}")
                Log.e("BookListViewModel", "error body: $errorBody")
                Result.failure(e)
            } catch (e: Exception) {
                Log.e("BookListViewModel", "Error adding book to list", e)
                Result.failure(e)
            }

            if (result.isSuccess) {
                Toast.makeText(context, "Book added to list", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to add book", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun createList(token: String, bookListInfo : BookListRequest, context: Context){
        viewModelScope.launch {
            val result = try{
                val remote = BookListClient.apiService.createBookList(token = "Bearer $token",bookListInfo)
                Result.success(remote)
            }catch (e: retrofit2.HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("BookListViewModel", "Error HTTP: ${e.code()} ${e.message()}")
                Log.e("BookListViewModel", "error body: $errorBody")
                Result.failure(e)
            } catch (e: Exception) {
                Log.e("BookListViewModel", "Error creating list", e)
                Result.failure(e)
            }

            if (result.isSuccess) {
                Toast.makeText(context, "List created", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to create List", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun deleteList(bookList: String, context: Context, token: String){
        viewModelScope.launch {
            val result = try {
                val remote = BookListClient.apiService.deleteList(bookListId = bookList, token = "Bearer $token")
                Result.success(remote)
            }catch (e: retrofit2.HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("BookListViewModel", "Error HTTP: ${e.code()} ${e.message()}")
                Log.e("BookListViewModel", "error body: $errorBody")
                Result.failure(e)
            } catch (e: Exception) {
                Log.e("BookListViewModel", "Error deleting list", e)
                Result.failure(e)
            }

            if (result.isSuccess) {
                Toast.makeText(context, "List deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to delete List", Toast.LENGTH_SHORT).show()
            }
        }
    }


}