package com.elektrocodx.auravindex.viewmodels

import androidx.lifecycle.viewModelScope
import com.elektrocodx.auravindex.model.Book
import com.elektrocodx.auravindex.model.Loan
import com.elektrocodx.auravindex.model.api.LoanRequest
import com.elektrocodx.auravindex.model.api.NotificationRequest
import com.elektrocodx.auravindex.retrofit.LoanClient
import com.elektrocodx.auravindex.utils.functions.formatUtcToLocalWithDate
import com.elektrocodx.auravindex.viewmodels.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException


class LoanViewModel() : BaseViewModel() {

    private val _loan = MutableStateFlow<Loan?>(null)
    private val _loans = MutableStateFlow<List<Loan>?>(null)
    private val _userLoans = MutableStateFlow<List<Loan>?>(null)
    private val _bookLoans = MutableStateFlow<List<Loan>?>(null)
    private val _createdLoan = MutableStateFlow<Boolean>(false)
    val loan: StateFlow<Loan?> = _loan.asStateFlow()
    val loans: StateFlow<List<Loan>?> = _loans.asStateFlow()
    val userLoans: StateFlow<List<Loan>?> = _userLoans.asStateFlow()
    val bookLoans: StateFlow<List<Loan>?> = _bookLoans.asStateFlow()
    val createdLoan: StateFlow<Boolean> = _createdLoan.asStateFlow()

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
    fun createLoan(token: String, loan: LoanRequest, book: Book, notificationViewModel: NotificationViewModel) {
        viewModelScope.launch {
            val result = try {
                val remote = LoanClient.apiService.createLoan(token = "Bearer $token", loan)
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                _createdLoan.value = true
                notificationViewModel.createNotification(token,
                    NotificationRequest(
                        receiver = loan.user,
                        title = "Your loan request has been sent",
                        message = "You can come to our library and pick up the book \"${book.title}\".",
                        notification_type = "LOAN",
                        is_read = false
                    )
                )
                notificationViewModel.loadUserNotifications(token, loan.user)
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
                        notification_type = "LOAN",
                        is_read = false
                    )
                )
                notificationViewModel.loadUserNotifications(token, loan.user._id)
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
                        notification_type = "LOAN",
                        is_read = false
                    )
                )
                notificationViewModel.loadUserNotifications(token, loan.user._id)
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
                        notification_type = "LOAN",
                        is_read = false
                    )
                )
                notificationViewModel.loadUserNotifications(token, loan.user._id)
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
        _userLoans.value = null
        _bookLoans.value = null
        _createdLoan.value = false
    }
}