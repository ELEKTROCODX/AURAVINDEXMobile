package com.elektro24team.auravindex.viewmodels

import retrofit2.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elektro24team.auravindex.data.repository.UserRepository
import com.elektro24team.auravindex.model.User
import com.elektro24team.auravindex.viewmodels.base.BaseViewModel
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository

): BaseViewModel() {
    private val _users = MutableLiveData<List<User>?>()
    private val _user = MutableLiveData<User?>()
    private val _myUser = MutableLiveData<User?>()
    val users: MutableLiveData<List<User>?> = _users
    val user: MutableLiveData<User?> = _user
    val myUser: MutableLiveData<User?> = _myUser
    fun getUserByEmail(token: String, email: String) {
        viewModelScope.launch {
            val result = repository.getUser(token, email)
            Log.d("AVDEBUG", "USEREMAIL - Result: $result - Token: $token")
            if (result.isSuccess) {
                _myUser.value = result.getOrNull()
            } else {
                val error = result.exceptionOrNull()
                Log.d("AVDEBUG", "USEREMAIL - Error: $error")
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

    fun getMyUserById(token: String, userId: String) {
        viewModelScope.launch {
            val result = repository.getUserById(token, userId)
            Log.d("AVDEBUG", "USERMYID - Result: $result - Token: $token")
            if(result.isSuccess) {
                _myUser.value = result.getOrNull()
            } else {
                val error = result.exceptionOrNull()
                Log.d("AVDEBUG", "USERMYID - Error: $error")
                if(error is HttpException) {
                    when(error.code()){
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
    fun getUserById(token: String, userId: String) {
        viewModelScope.launch {
            val result = repository.getUserById(token, userId)
            Log.d("AVDEBUG", "USERID - Result: $result - Token: $token")
            if(result.isSuccess) {
                _user.value = result.getOrNull()
            } else {
                val error = result.exceptionOrNull()
                Log.d("AVDEBUG", "USERID - Error: $error")
                if(error is HttpException) {
                    when(error.code()){
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
            Log.d("AVDEBUG", "USERS - Token: $token - Result: $result")
            when {
                result.isSuccess -> _users.value = result.getOrNull()
                result.isFailure -> {
                    val error = result.exceptionOrNull()
                    Log.d("AVDEBUG", "USERS - Error: $error")
                    when (error) {
                        is HttpException -> {
                            when (error.code()) {
                                401 -> notifyTokenExpired()
                                403 -> notifyInsufficentPermissions()
                                else -> notifyError("HTTP error: ${error.code()}")
                            }
                        }
                        else -> notifyError("Network error: ${error?.message}")
                    }
                }
            }
        }
    }
    override fun clearViewModelData() {
        _users.value = null
        _myUser.value = null

    }
}