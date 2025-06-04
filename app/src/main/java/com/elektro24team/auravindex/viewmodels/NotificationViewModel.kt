package com.elektro24team.auravindex.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elektro24team.auravindex.model.Notification
import com.elektro24team.auravindex.retrofit.NotificationClient
import com.elektro24team.auravindex.viewmodels.base.BaseViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException

class NotificationViewModel() : BaseViewModel() {

    private val _notifications = MutableLiveData<List<Notification>?>()
    private val _userNotifications = MutableLiveData<List<Notification>?>()
    private val _notification = MutableLiveData<Notification?>()
    val notifications: LiveData<List<Notification>?> = _notifications
    val userNotifications: LiveData<List<Notification>?> = _userNotifications
    val notification: LiveData<Notification?> = _notification

    fun loadNotifications(token: String) {
        viewModelScope.launch {
            val result = try {
                val remote = NotificationClient.apiService.getNotifications(token = "Bearer $token", sort = "desc", sortBy = "createdAt")
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                _notifications.value = result.getOrNull()?.data
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
    fun loadNotificationById(token: String, notificationId: String) {
        viewModelScope.launch {
            val result = try {
                val remote = NotificationClient.apiService.getNotificationById(token = "Bearer $token", notificationId)
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                _notification.value = result.getOrNull()
            } else {
                val error = result.exceptionOrNull()
                if (error is HttpException) {
                    when (error.code()) {
                        401 -> notifyTokenExpired()
                        403 -> notifyInsufficentPermissions()
                        404 -> notifyError("Notification not found")
                        else -> notifyError("HTTP error: ${error.code()}")
                    }
                } else {
                    notifyError("Network error: ${error?.message}")
                }
            }
        }
    }
    fun loadUserNotifications(token: String, userId: String) {
        viewModelScope.launch {
            val result = try {
                val remote = NotificationClient.apiService.getUserNotifications(token = "Bearer $token", filterValue =  userId, sort = "desc", sortBy = "createdAt")
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                _userNotifications.value = result.getOrNull()?.data
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
    fun markNotificationAsRead(token: String, notificationId: String) {
        viewModelScope.launch {
            val result = try {
                val remote = NotificationClient.apiService.markNotificationAsRead(token = "Bearer $token", notificationId)
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                notifySuccess("Notification marked as read")
            } else {
                val error = result.exceptionOrNull()
                if (error is HttpException) {
                    when (error.code()) {
                        401 -> notifyTokenExpired()
                        403 -> notifyInsufficentPermissions()
                        404 -> notifyError("Notification not found")
                        else -> notifyError("HTTP error: ${error.code()}")
                    }
                } else {
                    notifyError("Network error: ${error?.message}")
                }
            }
        }
    }
    override fun clearViewModelData() {
        _notifications.value = null
        _notification.value = null
    }
}