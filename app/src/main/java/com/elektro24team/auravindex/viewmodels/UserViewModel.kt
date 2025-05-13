package com.elektro24team.auravindex.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elektro24team.auravindex.data.repository.UserRepository
import com.elektro24team.auravindex.model.User
import kotlinx.coroutines.launch

class UserViewModel: ViewModel() {
    private val repository = UserRepository()
    private val _user = MutableLiveData<User>()
    val user: MutableLiveData<User> = _user

    fun getUser(token: String, email: String){
        viewModelScope.launch {
            val result = repository.getUser("Bearer $token", email)
            _user.value = result.getOrNull()
            Log.d("UserViewModel","Usuario cargado: ${_user.value?.name}")
        }
    }
}