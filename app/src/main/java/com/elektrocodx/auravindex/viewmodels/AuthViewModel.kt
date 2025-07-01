package com.elektrocodx.auravindex.viewmodels

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
    private val _registerResult = MutableLiveData<Boolean>()
    val loginResult: MutableLiveData<String?> = _loginResult
    val registerResult: MutableLiveData<Boolean> = _registerResult

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
    fun register(userData: RegisterInfo){
        viewModelScope.launch {
            val result = repository.register(userData)
            if(result.isSuccess){
                _registerResult.value = true
                notifySuccess("Registration successful.")
            } else {
                _registerResult.value = false
                val error = result.exceptionOrNull()
                if(error is HttpException) {
                    when(error.code()) {
                        409 -> notifyError("This email has already been used.")
                        else -> notifyError("HTTP error: ${error.code()}")
                    }
                } else {
                    notifyError("Network error: ${error?.message}")
                }
            }
        }
    }
    override fun clearViewModelData() {
        _loginResult.value = null
        _registerResult.value = false
        repository = AuthRepository()
    }
}