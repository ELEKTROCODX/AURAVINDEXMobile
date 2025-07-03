package com.elektrocodx.auravindex.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elektrocodx.auravindex.model.ActivePlan
import com.elektrocodx.auravindex.model.Plan
import com.elektrocodx.auravindex.model.api.ActivePlanRequest
import com.elektrocodx.auravindex.model.api.NotificationRequest
import com.elektrocodx.auravindex.retrofit.ActivePlanClient
import com.elektrocodx.auravindex.utils.functions.formatUtcToLocalWithDate
import com.elektrocodx.auravindex.viewmodels.base.BaseViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException


class ActivePlanViewModel() : BaseViewModel() {

    private val _activePlan = MutableLiveData<ActivePlan?>()
    private val _activePlans = MutableLiveData<List<ActivePlan>?>()
    private val _isActivePlanChecked = MutableLiveData(false)
    val activePlan: LiveData<ActivePlan?> = _activePlan
    val activePlans: LiveData<List<ActivePlan>?> = _activePlans
    val isActivePlanChecked: LiveData<Boolean> get() = _isActivePlanChecked

    fun loadActivePlanById(token: String, activePlanId: String) {
        viewModelScope.launch {
            val result = try {
                val remote = ActivePlanClient.apiService.getActivePlanById(token = "Bearer $token", activePlanId)
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                _activePlan.value = result.getOrNull()
            } else {
                val error = result.exceptionOrNull()
                if (error is HttpException) {
                    when (error.code()) {
                        401 -> notifyTokenExpired()
                        403 -> notifyInsufficentPermissions()
                        404 -> notifyError("Active plan not found")
                        else -> notifyError("HTTP error: ${error.code()}")
                    }
                } else {
                    notifyError("Network error: ${error?.message}")
                }
            }
        }
    }

    fun loadActivePlanByUserId(token: String, userId: String) {
        viewModelScope.launch {
            val result = try {
                val remote = ActivePlanClient.apiService.getActivePlanByUserId(token = "Bearer $token", filterValue = userId, sort = "asc", sortBy = "createdAt")
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                if(result.getOrNull()?.data?.isNotEmpty() == true) {
                    result.getOrNull()?.data?.forEach { ap ->
                        if(ap.plan_status?.plan_status == "ACTIVE" || ap.plan_status?.plan_status == "RENEWED") {
                            _activePlan.value = ap
                        }
                    }
                }
            } else {
                val error = result.exceptionOrNull()
                if (error is HttpException) {
                    when (error.code()) {
                        401 -> notifyTokenExpired()
                        403 -> notifyInsufficentPermissions()
                        404 -> notifyError("Active plan not found")
                        else -> notifyError("HTTP error: ${error.code()}")
                    }
                } else {
                    notifyError("Network error: ${error?.message}")
                }
            }
            _isActivePlanChecked.value = true
        }
    }

    fun loadActivePlans(token: String) {
        viewModelScope.launch {
            val result = try {
                val remote = ActivePlanClient.apiService.getActivePlans(token = "Bearer $token", sort = "desc", sortBy = "createdAt")
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                _activePlans.value = result.getOrNull()?.data
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
    fun createActivePlan(token: String, userId: String, plan: Plan, notificationViewModel: NotificationViewModel) {
        viewModelScope.launch {
            val result = try {
                val activePlanRequest = ActivePlanRequest(userId, plan._id)
                val remote = ActivePlanClient.apiService.createActivePlan(token = "Bearer $token", activePlanRequest)
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                loadActivePlanByUserId(token, userId)
                notificationViewModel.createNotification(token,
                    NotificationRequest(
                        receiver = userId,
                        title = "Your subscription has been confirmed",
                        message = "You've successfully been subscribed to ${plan.name}.",
                        notification_type = "SUBSCRIPTION",
                        is_read = false
                    )
                )
                notificationViewModel.loadUserNotifications(token, userId)
                notifySuccess("You have successfully subscribed to ${plan.name}.")
            } else {
                val error = result.exceptionOrNull()
                if (error is HttpException) {
                    when (error.code()) {
                        401 -> notifyTokenExpired()
                        403 -> notifyInsufficentPermissions()
                        404 -> notifyError(error.message())
                        409 -> notifyError("You cannot subscribe to more than one plan at once.")
                        else -> notifyError("HTTP error: ${error.code()}")
                    }
                } else {
                    notifyError("Network error: ${error?.message}")
                }
            }
        }
    }
    fun renewActivePlan(token: String, activePlan: ActivePlan, notificationViewModel: NotificationViewModel) {
        viewModelScope.launch {
            val result = try {
                val remote = ActivePlanClient.apiService.renewActivePlan(token = "Bearer $token", activePlan._id)
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                loadActivePlanById(token, activePlan._id)
                notificationViewModel.createNotification(token,
                    NotificationRequest(
                        receiver = activePlan.user._id,
                        title = "Your subscription has been renewed",
                        message = "Your subscription to ${activePlan.plan.name} has been renewed and ends on ${formatUtcToLocalWithDate(activePlan.ending_date)}.",
                        notification_type = "SUBSCRIPTION",
                        is_read = false
                    )
                )
                notificationViewModel.loadUserNotifications(token, activePlan.user._id)
                notifySuccess("Your plan has been successfully renewed.")
            } else {
                val error = result.exceptionOrNull()
                if (error is HttpException) {
                    when (error.code()) {
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
    fun finishActivePlan(token: String, activePlan: ActivePlan, notificationViewModel: NotificationViewModel) {
        viewModelScope.launch {
            val result = try {
                val remote = ActivePlanClient.apiService.finishActivePlan(token = "Bearer $token", activePlan._id)
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                loadActivePlanById(token, activePlan._id)
                notificationViewModel.createNotification(token,
                    NotificationRequest(
                        receiver = activePlan.user._id,
                        title = "Your subscription has been finished",
                        message = "Your subscription to ${activePlan.plan.name} has been finished.",
                        notification_type = "SUBSCRIPTION",
                        is_read = false
                    )
                )
                notificationViewModel.loadUserNotifications(token, activePlan.user._id)
                notifySuccess("Your plan has been successfully finished.")
            } else {
                val error = result.exceptionOrNull()
                if (error is HttpException) {
                    when (error.code()) {
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
    fun cancelActivePlan(token: String, activePlan: ActivePlan, notificationViewModel: NotificationViewModel) {
        viewModelScope.launch {
            val result = try {
                val remote = ActivePlanClient.apiService.cancelActivePlan(token = "Bearer $token", activePlan._id)
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                notificationViewModel.createNotification(token,
                    NotificationRequest(
                        receiver = activePlan.user._id,
                        title = "Your subscription has been canceled",
                        message = "Your subscription to ${activePlan.plan.name} has been canceled.",
                        notification_type = "SUBSCRIPTION",
                        is_read = false
                    )
                )
                notificationViewModel.loadUserNotifications(token, activePlan.user._id)
                _activePlan.value = null
                notifySuccess("Your plan has been successfully canceled.")
            } else {
                val error = result.exceptionOrNull()
                if (error is HttpException) {
                    when (error.code()) {
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
    fun resetIsActivePlanChecked() {
        _isActivePlanChecked.value = false
    }
    override fun clearViewModelData() {
        _activePlan.value = null
        _activePlans.value = null
        _isActivePlanChecked.value = false
    }
}