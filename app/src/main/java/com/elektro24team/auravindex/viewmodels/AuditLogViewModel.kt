package com.elektro24team.auravindex.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elektro24team.auravindex.data.repository.AuditLogRepository
import com.elektro24team.auravindex.model.AuditLog
import com.elektro24team.auravindex.viewmodels.base.BaseViewModel
import kotlinx.coroutines.launch

class AuditLogViewModel(
    private val repository: AuditLogRepository

): BaseViewModel() {
    private val _auditLogs = MutableLiveData<List<AuditLog>>()
    private val _auditLog = MutableLiveData<AuditLog>()
    val auditLogs: MutableLiveData<List<AuditLog>> = _auditLogs
    val auditLog: MutableLiveData<AuditLog> = _auditLog

    fun loadAuditLog(token: String, auditLogId: String) {
        viewModelScope.launch {
            if( _auditLogs.value?.find{ it._id == auditLogId } == null) {
                val result = repository.getAuditLogById(token, auditLogId)
                _auditLogs.postValue(listOf(result))
                _auditLog.postValue(result)
            } else {
                _auditLog.postValue(_auditLogs.value?.find{ it._id == auditLogId })
            }
        }
    }
    fun getAuditLogById(token: String, auditLogId: String){
        viewModelScope.launch {
            val response = repository.getAuditLogById(token, auditLogId)
            _auditLog.value = response
        }
    }
    fun getAuditLogs(token: String){
        viewModelScope.launch {
            val result = repository.getAllAuditLogs(token)
            _auditLogs.value = result
        }
    }
}