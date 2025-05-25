package com.elektro24team.auravindex.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elektro24team.auravindex.model.RecentBook
import com.elektro24team.auravindex.retrofit.RecentBookClient
import com.elektro24team.auravindex.viewmodels.base.BaseViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import kotlin.collections.filter
import kotlin.text.contains

class RecentBookViewModel() : BaseViewModel() {

    private val _recentBook = MutableLiveData<RecentBook?>()
    val recentBook: LiveData<RecentBook?> = _recentBook


    fun loadRecentBooks(token: String, userId: String) {
        viewModelScope.launch {
            /*val result = RecentBookClient.apiService.getRecentBooksById(token, userId)*/
            val result = try {
                val remote = RecentBookClient.apiService.getRecentBooksByUserId(token = "Bearer $token", filterValue = userId)
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                _recentBook.value = result.getOrNull()?.data?.get(0)
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