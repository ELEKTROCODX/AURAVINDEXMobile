package com.elektro24team.auravindex.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elektro24team.auravindex.model.LoanStatus
import com.elektro24team.auravindex.retrofit.LoanStatusClient
import com.elektro24team.auravindex.viewmodels.base.BaseViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
class LoanStatusViewModel() : BaseViewModel() {

    private val _loanStatuses = MutableLiveData<List<LoanStatus>?>()
    private val _loanStatus = MutableLiveData<LoanStatus?>()
    val loanStatuses: LiveData<List<LoanStatus>?> = _loanStatuses
    val loanStatus: LiveData<LoanStatus?> = _loanStatus

    fun loadLoanStatuses() {
        viewModelScope.launch {
            val result = try {
                val remote = LoanStatusClient.apiService.getLoanStatuses()
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                _loanStatuses.value = result.getOrNull()?.data
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
    fun loadLoanStatus(loanId: String) {
        viewModelScope.launch {
            val result = try {
                val remote = LoanStatusClient.apiService.getLoanStatusById(loanId)
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                _loanStatus.value = result.getOrNull()
            } else {
                val error = result.exceptionOrNull()
                if (error is HttpException) {
                    when (error.code()) {
                        401 -> notifyTokenExpired()
                        403 -> notifyInsufficentPermissions()
                        404 -> notifyError("Loan status not found")
                        else -> notifyError("HTTP error: ${error.code()}")
                    }
                } else {
                    notifyError("Network error: ${error?.message}")
                }
            }
        }
    }
    fun loadLoanStatusByName(loanName: String) {
        viewModelScope.launch {
            val result = try {
                val remote = LoanStatusClient.apiService.getLoanStatusByName(filterValue =  loanName)
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                if(result.getOrNull()?.data?.size == 0) {
                    notifyError("Loan status not found")
                } else {
                    _loanStatus.value = result.getOrNull()?.data?.get(0)
                }
            } else {
                val error = result.exceptionOrNull()
                if (error is HttpException) {
                    when (error.code()) {
                        401 -> notifyTokenExpired()
                        403 -> notifyInsufficentPermissions()
                        404 -> notifyError("Loan status not found")
                        else -> notifyError("HTTP error: ${error.code()}")
                    }
                } else {
                    notifyError("Network error: ${error?.message}")
                }
            }
        }
    }
    override fun clearViewModelData() {
        _loanStatuses.value = null
        _loanStatus.value = null

    }
}