package com.elektro24team.auravindex.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.elektro24team.auravindex.model.BookList
import com.elektro24team.auravindex.model.api.BookListRequest
import com.elektro24team.auravindex.retrofit.BookListClient
import com.elektro24team.auravindex.viewmodels.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException


class BookListViewModel(
) :BaseViewModel() {
    private val _bookLists = MutableStateFlow<List<BookList>?>(null)
    val bookLists: StateFlow<List<BookList>?> = _bookLists.asStateFlow()
    private val _myBookList = MutableStateFlow<BookList?>(null)
    val myBookList: StateFlow<BookList?> = _myBookList.asStateFlow()

    fun createList(token: String, bookListInfo : BookListRequest){
        viewModelScope.launch {
            val result = try{
                val remote = BookListClient.apiService.createBookList(token = "Bearer $token", bookListInfo)
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }

            if (result.isSuccess) {
                loadUserLists(token, bookListInfo.owner)
                notifySuccess("Successfully created book list.")
            } else {
                val error = result.exceptionOrNull()
                if (error is HttpException) {
                    when (error.code()) {
                        401 -> notifyTokenExpired()
                        403 -> notifyInsufficentPermissions()
                        409 -> notifyError("You've exceeded the limit of lists per user.")
                        else -> notifyError("HTTP error: ${error.code()}")
                    }
                } else {
                    notifyError("Network error: ${error?.message}")
                }
            }
        }
    }

    fun getBookList(token: String, listId: String){
        viewModelScope.launch {
            val result = try{
                val remote = BookListClient.apiService.getBookListById("Bearer $token",listId)
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                val myList = result.getOrNull()
                _myBookList.value = myList
            } else {
                val error = result.exceptionOrNull()
                if (error is HttpException) {
                    when (error.code()) {
                        401 -> notifyTokenExpired()
                        403 -> notifyInsufficentPermissions()
                        404 -> notifyError("Book list not found.")
                        else -> notifyError("HTTP error: ${error.code()}")
                    }
                } else {
                    notifyError("Network error: ${error?.message}")
                }
            }
        }
    }

    fun loadUserLists(token: String, userId: String){
        viewModelScope.launch {
            val result = try {
                val remote = BookListClient.apiService.getUserBookLists(token = "Bearer $token", filterValue = userId)
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess){
                val lists = result.getOrNull()?.data
                _bookLists.value = lists
            }else{
                val error = result.exceptionOrNull()
                if (error is HttpException) {
                    when (error.code()) {
                        401 -> notifyTokenExpired()
                        403 -> notifyInsufficentPermissions()
                        else -> notifyError("HTTP error: ${error.code()}")
                    }
                } else {
                    notifyError("Network error: ${error?.message}")
                }
            }
        }
    }

    fun deleteBookList(bookList: String, token: String, userId: String){
        viewModelScope.launch {
            val result = try {
                val remote = BookListClient.apiService.deleteList(bookListId = bookList, token = "Bearer $token")
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                loadUserLists(token, userId)
                notifySuccess("Successfully deleted book list.")
            } else {
                val error = result.exceptionOrNull()
                if (error is HttpException) {
                    when (error.code()) {
                        401 -> notifyTokenExpired()
                        403 -> notifyInsufficentPermissions()
                        404 -> notifyError("Book list not found.")
                        else -> notifyError("HTTP error: ${error.code()}")
                    }
                } else {
                    notifyError("Network error: ${error?.message}")
                }
            }
        }
    }

    fun addBookToList(bookId: String, listId: String, token: String) {
        viewModelScope.launch {
            val result = try {
                val remote = BookListClient.apiService.addBookToList(
                    token = "Bearer $token",
                    bookListId = listId,
                    bookId = bookId
                )
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                notifySuccess("Successfully added book to list.")
            } else {
                val error = result.exceptionOrNull()
                if (error is HttpException) {
                    when (error.code()) {
                        401 -> notifyTokenExpired()
                        403 -> notifyInsufficentPermissions()
                        404 -> notifyError(error.message())
                        409 -> notifyError("You've exceeded the limit of books per list.")
                        else -> notifyError("HTTP error: ${error.code()}")
                    }
                } else {
                    notifyError("Network error: ${error?.message}")
                }
            }
        }
    }

    fun removeBookFromList(token: String, bookListId: String, bookId : String){
        viewModelScope.launch {
            val result = try{
                val remote = BookListClient.apiService.removeBookFromList("Bearer $token", bookListId, bookId)
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess){
                getBookList(token, listId = bookListId)
                notifySuccess("Successfully removed book from list.")
            } else {
                val error = result.exceptionOrNull()
                if (error is HttpException) {
                    when (error.code()) {
                        401 -> notifyTokenExpired()
                        403 -> notifyInsufficentPermissions()
                        404 -> notifyError(error.message())
                        else -> notifyError("HTTP error: ${error.code()}")
                    }
                } else {
                    notifyError("Network error: ${error?.message}")
                }
            }
        }
    }
}