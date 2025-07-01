package com.elektrocodx.auravindex.viewmodels

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elektrocodx.auravindex.data.repository.AuditLogRepository
import com.elektrocodx.auravindex.model.AuditLog
import com.elektrocodx.auravindex.viewmodels.base.BaseViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AuditLogViewModel(
    private val repository: AuditLogRepository

): BaseViewModel() {
    private val _auditLogs = MutableLiveData<List<AuditLog>?>()
    private val _auditLog = MutableLiveData<AuditLog?>()
    val auditLogs: MutableLiveData<List<AuditLog>?> = _auditLogs
    val auditLog: MutableLiveData<AuditLog?> = _auditLog

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getAuditLogById(token: String, auditLogId: String){
        viewModelScope.launch {
            val result = repository.getAuditLogById(token, auditLogId)
            if (result.isSuccess) {
                _auditLog.value = result.getOrNull()
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
    fun getAuditLogs(token: String){
        viewModelScope.launch {
            val result = repository.getAllAuditLogs(token, sort = "desc", sortBy = "createdAt")
            when {
                result.isSuccess -> _auditLogs.value = result.getOrNull()
                result.isFailure -> {
                    val error = result.exceptionOrNull()
                    when (error) {
                        is HttpException -> {
                            when(error.code()) {
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
        _auditLog.value = null
        _auditLogs.value = null
    }
}