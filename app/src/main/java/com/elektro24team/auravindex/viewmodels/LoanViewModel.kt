package com.elektro24team.auravindex.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elektro24team.auravindex.data.repository.AuditLogRepository
import com.elektro24team.auravindex.model.AuditLog
import com.elektro24team.auravindex.model.Loan
import com.elektro24team.auravindex.model.LoanRequest
import com.elektro24team.auravindex.retrofit.LoanClient
import com.elektro24team.auravindex.retrofit.RecentBookClient
import com.elektro24team.auravindex.viewmodels.base.BaseViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException


class LoanViewModel() : BaseViewModel() {

    private val _loan = MutableLiveData<Loan?>()
    private val _loans = MutableLiveData<List<Loan>?>()
    val loan: LiveData<Loan?> = _loan
    val loans: LiveData<List<Loan>?> = _loans

    fun loadLoanById(token: String, loanId: String) {
        viewModelScope.launch {
            val result = try {
                val remote = LoanClient.apiService.getLoanById(token = "Bearer $token", loanId)
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                _loan.value = result.getOrNull()
            } else {
                val error = result.exceptionOrNull()
                if (error is HttpException) {
                    when (error.code()) {
                        401 -> notifyTokenExpired()
                        403 -> notifyInsufficentPermissions()
                        404 -> notifyError("Loan not found")
                        else -> notifyError("HTTP error: ${error.code()}")
                    }
                } else {
                    notifyError("Network error: ${error?.message}")
                }
            }
        }
    }

    fun loadLoans(token: String) {
        viewModelScope.launch {
            val result = try {
                val remote = LoanClient.apiService.getLoans(token = "Bearer $token")
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                _loans.value = result.getOrNull()?.data
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

        fun createLoan(token: String, loan: LoanRequest) {
            viewModelScope.launch {
                val result = try {
                    val remote = LoanClient.apiService.createLoan(token = "Bearer $token", loan)
                    Result.success(remote)
                } catch (e: Exception) {
                    Result.failure(e)
                }
                if (result.isSuccess) {
                    notifySuccess("The loan request has been sent successfully")
                } else {
                    val error = result.exceptionOrNull()
                    if (error is HttpException) {
                        when (error.code()) {
                            401 -> notifyTokenExpired()
                            403 -> notifyInsufficentPermissions()
                            404 -> notifyError(error.message())
                            else -> notifyError("HTTP error: ${error.code()}")
                        }
                    } else {
                        notifyError("Network error: ${error?.message}")
                    }
                }
            }
        }

    }
    override fun clearViewModelData() {
        _loan.value = null
        _loans.value = null
    }
}