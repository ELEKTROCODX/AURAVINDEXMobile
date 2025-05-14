package com.elektro24team.auravindex.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elektro24team.auravindex.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private val repository = AuthRepository()
    private val _loginResult = MutableLiveData<Result<String>>()
    val loginResult: LiveData<Result<String>> get() = _loginResult

    fun login(email: String, password: String){
        viewModelScope.launch {
            val result = repository.login(email,password)
            _loginResult.value = result
        }
    }
}