package com.elektro24team.auravindex.viewmodels

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elektro24team.auravindex.model.Loan
import com.elektro24team.auravindex.model.Notification
import com.elektro24team.auravindex.model.api.LoanRequest
import com.elektro24team.auravindex.model.api.NotificationRequest
import com.elektro24team.auravindex.model.local.NotificationEntity
import com.elektro24team.auravindex.retrofit.LoanClient
import com.elektro24team.auravindex.utils.functions.formatUtcToLocalWithDate
import com.elektro24team.auravindex.utils.objects.NotificationHandler
import com.elektro24team.auravindex.viewmodels.base.BaseViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException


class LoanViewModel() : BaseViewModel() {

    private val _loan = MutableLiveData<Loan?>()
    private val _loans = MutableLiveData<List<Loan>?>()
    private val _userLoans = MutableLiveData<List<Loan>?>()
    private val _bookLoans = MutableLiveData<List<Loan>?>()
    val loan: LiveData<Loan?> = _loan
    val loans: LiveData<List<Loan>?> = _loans
    val userLoans: LiveData<List<Loan>?> = _userLoans
    val bookLoans: LiveData<List<Loan>?> = _bookLoans

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
                        400 -> notifyError(error.message())
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
    fun loadUserLoans(token: String, userId: String) {
        viewModelScope.launch {
            val result = try {
                val remote = LoanClient.apiService.getObjectLoans(token = "Bearer $token", filterField = "user", filterValue = userId)
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                _userLoans.value = result.getOrNull()?.data
            } else {
                val error = result.exceptionOrNull()
                if (error is HttpException) {
                    when (error.code()) {
                        400 -> notifyError(error.message())
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
    fun loadBookLoans(token: String, bookId: String) {
        viewModelScope.launch {
            val result = try {
                val remote = LoanClient.apiService.getObjectLoans(token = "Bearer $token", filterField = "book", filterValue = bookId)
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                _bookLoans.value = result.getOrNull()?.data
            } else {
                val error = result.exceptionOrNull()
                if (error is HttpException) {
                    when (error.code()) {
                        400 -> notifyError(error.message())
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
                        400 -> notifyError(error.message())
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
    fun createLoan(token: String, loan: LoanRequest, notificationViewModel: NotificationViewModel) {
        viewModelScope.launch {
            val result = try {
                val remote = LoanClient.apiService.createLoan(token = "Bearer $token", loan)
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                notificationViewModel.createNotification(token,
                    NotificationRequest(
                        receiver = loan.user,
                        title = "Your loan has been sent",
                        message = "You can come to our library and pick up the book.",
                        notification_type = "LOAN_REQUEST",
                        is_read = false
                    )
                )
                notifySuccess("The loan request has been sent successfully")
            } else {
                val error = result.exceptionOrNull()
                if (error is HttpException) {
                    when (error.code()) {
                        400 -> notifyError(error.message())
                        401 -> notifyTokenExpired()
                        403 -> notifyInsufficentPermissions()
                        404 -> notifyError(error.message())
                        409 -> notifyError(error.message())
                        else -> notifyError("HTTP error: ${error.code()}")
                    }
                } else {
                    notifyError("Network error: ${error?.message}")
                }
            }
        }
    }
    fun approveLoan(token: String, loan: Loan, notificationViewModel: NotificationViewModel) {
        viewModelScope.launch {
            val result = try {
                val remote = LoanClient.apiService.approveLoan(token = "Bearer $token", loan._id)
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                notificationViewModel.createNotification(token,
                    NotificationRequest(
                        receiver = loan.user._id.toString(),
                        title = "Your loan has been confirmed",
                        message = "Your loan for the book \"${loan.book.title}\" has been approved. Remember to return it by ${formatUtcToLocalWithDate(loan.return_date)}.",
                        notification_type = "LOAN_APPROVED",
                        is_read = false
                    )
                )
                notifySuccess("The loan has been approved successfully")
            } else {
                val error = result.exceptionOrNull()
                if (error is HttpException) {
                    when (error.code()) {
                        400 -> notifyError(error.message())
                        401 -> notifyTokenExpired()
                        403 -> notifyInsufficentPermissions()
                        404 -> notifyError(error.message())
                        409 -> notifyError(error.message())
                        else -> notifyError("HTTP error: ${error.code()}")
                    }
                } else {
                    notifyError("Network error: ${error?.message}")
                }
            }
        }
    }
    fun renewLoan(token: String, loan: Loan, notificationViewModel: NotificationViewModel) {
        viewModelScope.launch {
            val result = try {
                val remote = LoanClient.apiService.renewLoan(token = "Bearer $token", loan._id)
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                notificationViewModel.createNotification(token,
                    NotificationRequest(
                        receiver = loan.user._id.toString(),
                        title = "Your loan has been renewed",
                        message = "Your loan for the book \"${loan.book.title}\" has been renewed. Remember to return it by ${formatUtcToLocalWithDate(loan.return_date)}.",
                        notification_type = "LOAN_RENEWED",
                        is_read = false
                    )
                )
                notifySuccess("The loan has been renewed successfully")
            } else {
                val error = result.exceptionOrNull()
                if (error is HttpException) {
                    when(error.code()) {
                        400 -> notifyError(error.message())
                        401 -> notifyTokenExpired()
                        403 -> notifyInsufficentPermissions()
                        404 -> notifyError(error.message())
                        409 -> notifyError(error.message())
                        else -> notifyError("HTTP error: ${error.code()}")
                    }
                }
            }
        }
    }
    fun finishLoan(token: String, loan: Loan, notificationViewModel: NotificationViewModel) {
        viewModelScope.launch {
            val result = try {
                val remote = LoanClient.apiService.finishLoan(token = "Bearer $token", loan._id)
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                notificationViewModel.createNotification(token,
                    NotificationRequest(
                        receiver = loan.user._id.toString(),
                        title = "Your loan has been finished",
                        message = "Your loan for the book \"${loan.book.title}\" has been finished.",
                        notification_type = "LOAN_FINISHED",
                        is_read = false
                    )
                )
                notifySuccess("The loan has been finished successfully")
            } else {
                val error = result.exceptionOrNull()
                if (error is HttpException) {
                    when(error.code()) {
                        400 -> notifyError(error.message())
                        401 -> notifyTokenExpired()
                        403 -> notifyInsufficentPermissions()
                        404 -> notifyError(error.message())
                        409 -> notifyError(error.message())
                        else -> notifyError("HTTP error: ${error.code()}")
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