package com.elektrocodx.auravindex.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elektrocodx.auravindex.model.RecentBook
import com.elektrocodx.auravindex.retrofit.RecentBookClient
import com.elektrocodx.auravindex.viewmodels.base.BaseViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RecentBookViewModel() : BaseViewModel() {

    private val _recentBook = MutableLiveData<RecentBook?>()
    val recentBook: LiveData<RecentBook?> = _recentBook


    fun loadRecentBooks(token: String, userId: String) {
        viewModelScope.launch {
            val result = try {
                val remote = RecentBookClient.apiService.getRecentBooksByUserId(token = "Bearer $token", filterValue = userId)
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                val book = result.getOrNull()?.data
                _recentBook.value = result.getOrNull()?.data?.get(0)
                if (!book.isNullOrEmpty()){
                    _recentBook.value = book[0]
                }else{
                    notifyError("No RECENT")
                }
            } else {
                val error = result.exceptionOrNull()
                if (error is HttpException) {
                    when (error.code()) {
                        401 -> notifyTokenExpired()
                        403 -> notifyInsufficentPermissions()
                        404 -> notifyError("Recent books not found")
                        else -> notifyError("HTTP error: ${error.code()}")
                    }
                } else {
                    notifyError("Network error: ${error?.message}")
                }
            }
        }
    }
    override fun clearViewModelData() {
        _recentBook.value = null
    }
}