package com.elektrocodx.auravindex.viewmodels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elektrocodx.auravindex.data.repository.AuthRepository
import com.elektrocodx.auravindex.retrofit.RegisterInfo
import com.elektrocodx.auravindex.viewmodels.base.BaseViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AuthViewModel: BaseViewModel() {
    private var repository = AuthRepository()
    private val _loginResult = MutableLiveData<String?>()
    private val _registerResult = MutableLiveData<String?>()
    val loginResult: MutableLiveData<String?> = _loginResult
    val registerResult: MutableLiveData<String?> = _registerResult

    fun login(email: String, password: String){
        viewModelScope.launch {
            val result = repository.login(email,password)
            if(result.isSuccess){
                _loginResult.value = result.getOrNull()
            } else {
                val error = result.exceptionOrNull()
                if (error is HttpException) {
                    when (error.code()) {
                        400 -> notifyError("Invalid email or password")
                        401 -> notifyError("Wrong credentials")
                        else -> notifyError("HTTP error: ${error.code()}")
                    }
                } else {
                    notifyError("Network error: ${error?.message}")
                }
            }
        }
    }
    fun register(userData: RegisterInfo, imageUri: Uri?, context: Context){
        viewModelScope.launch {
            val result = repository.register(userData,imageUri, context = context)
            if(result.isSuccess){
                _registerResult.value = result.toString()
                notifySuccess("You've successfully registered.")
            } else {
                val error = result.exceptionOrNull()
                if(error is HttpException) {
                    when (error.code()) {
                        409 -> notifyError("That email is already registered.")
                        413 -> notifyError("Your image size is too big.")
                        else -> notifyError("HTTP error: ${error.code()}")
                    }
                } else {
                }
            }
        }
    }
    override fun clearViewModelData() {
        _loginResult.value = null
        _registerResult.value = null
        repository = AuthRepository()
    }
}