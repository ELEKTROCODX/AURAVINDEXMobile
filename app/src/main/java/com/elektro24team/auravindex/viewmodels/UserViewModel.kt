package com.elektro24team.auravindex.viewmodels

import retrofit2.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elektro24team.auravindex.data.repository.BookRepository
import com.elektro24team.auravindex.data.repository.UserRepository
import com.elektro24team.auravindex.model.User
import com.elektro24team.auravindex.viewmodels.base.BaseViewModel
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository

): BaseViewModel() {
    private val _users = MutableLiveData<List<User>>()
    private val _user = MutableLiveData<User>()
    val users: MutableLiveData<List<User>> = _users
    val user: MutableLiveData<User> = _user

    fun getUser(token: String, email: String) {
        viewModelScope.launch {
            val result = repository.getUser(token, email)
            if (result.isSuccess) {
                _user.value = result.getOrNull()
            } else {
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

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getUsers(token: String){
        viewModelScope.launch {
            val result = repository.getUsers(token)
            when {
                result.isSuccess -> _users.value = result.getOrNull()
                result.isFailure -> {
                    val error = result.exceptionOrNull()
                    Log.e("UserViewModel", "Error loading users", error)
                    when (error) {
                        is HttpException -> {
                            Log.e("UserViewModel", "HTTP error: ${error.code()}")
                            when (error.code()) {
                                401 -> notifyTokenExpired()
                                403 -> notifyInsufficentPermissions()
                                else -> notifyError("Error loading users")
                            }
                        }
                        else -> notifyError("Network error: ${error?.message}")
                    }
                }
            }
        }
    }
}