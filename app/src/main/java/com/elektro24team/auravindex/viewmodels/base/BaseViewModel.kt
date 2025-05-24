package com.elektro24team.auravindex.viewmodels.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    private val _tokenExpired = MutableLiveData<Boolean>()
    private val _insufficentPermissions = MutableLiveData<Boolean>()
    private val _error = MutableLiveData<String>()
    val tokenExpired: LiveData<Boolean> = _tokenExpired
    val insufficentPermissions: LiveData<Boolean> = _insufficentPermissions
    val error: LiveData<String> = _error

    protected fun notifyTokenExpired() {
        _tokenExpired.postValue(true)
    }

    protected fun notifyInsufficentPermissions() {
        _insufficentPermissions.postValue(true)
    }

    protected fun notifyError(message: String) {
        _error.postValue(message)
    }
}