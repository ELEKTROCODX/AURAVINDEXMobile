package com.elektro24team.auravindex.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elektro24team.auravindex.data.repository.AuthRepository
import com.elektro24team.auravindex.viewmodels.base.BaseViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AuthViewModel: BaseViewModel() {
    private val repository = AuthRepository()
    private val _loginResult = MutableLiveData<String>()
    val loginResult: MutableLiveData<String> = _loginResult

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
}