package com.elektro24team.auravindex.viewmodels

import android.util.Log
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

    fun getUser(token: String, email: String){
        viewModelScope.launch {
            try {
                val response = repository.getUser(token, email)
                if (response.isSuccess) {
                    _user.value = response.getOrNull()
                } else {
                    if (response.hashCode() == 401) {
                        notifyTokenExpired()
                    } else if(response.hashCode() == 403){
                        notifyInsufficentPermissions()
                    } else {
                        notifyError("Erorr loading books")
                    }
                }
            } catch (e: Exception) {
                notifyError("Network error: ${e.message}")
            }
        }
    }
    
    fun loadUser(token: String, userId: String) {
        viewModelScope.launch {
            if( _users.value?.find{ it._id == userId } == null) {
                val result = repository.getUserById(token, userId)
                _users.postValue(listOf(result))
                _user.postValue(result)
            } else {
                _user.postValue(_users.value?.find{ it._id == userId })
            }
        }
    }

    fun getUserById(token: String, userId: String){
        viewModelScope.launch {
            val response = repository.getUserById(token, userId)
            _user.value = response
        }
    }

    fun getUsers(token: String){
        viewModelScope.launch {
            val result = repository.getUsers(token)
            _users.value = result
        }
    }


}