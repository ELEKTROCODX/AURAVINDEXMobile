package com.elektro24team.auravindex.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elektro24team.auravindex.data.repository.AuditLogRepository
import com.elektro24team.auravindex.model.AuditLog
import com.elektro24team.auravindex.model.ActivePlan
import com.elektro24team.auravindex.model.api.ActivePlanRequest
import com.elektro24team.auravindex.retrofit.ActivePlanClient
import com.elektro24team.auravindex.retrofit.RecentBookClient
import com.elektro24team.auravindex.utils.enums.SettingKey
import com.elektro24team.auravindex.viewmodels.base.BaseViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException


class ActivePlanViewModel() : BaseViewModel() {

    private val _activePlan = MutableLiveData<ActivePlan?>()
    private val _activePlans = MutableLiveData<List<ActivePlan>?>()
    val activePlan: LiveData<ActivePlan?> = _activePlan
    val activePlans: LiveData<List<ActivePlan>?> = _activePlans

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
            //Log.d("AVDEBUG", "Token: $token, UserId: $userId - Result: $result")
            if (result.isSuccess) {
                if(result.getOrNull()?.data?.isNotEmpty() == true) {
                    result.getOrNull()?.data?.forEach { ap ->
                        if(ap.plan_status?.plan_status == "ACTIVE") {
                            //Log.d("AVDEBUG", "Active plan found: $ap")
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
    fun createActivePlan(token: String, userId: String, planId: String) {
        viewModelScope.launch {
            val result = try {
                val activePlanRequest = ActivePlanRequest(userId, planId)
                val remote = ActivePlanClient.apiService.createActivePlan(token = "Bearer $token", activePlanRequest)
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                loadActivePlanByUserId(token, userId)
                notifySuccess("You have successfully subscribed to the plan.")
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
    fun renewActivePlan(token: String, activePlanId: String) {
        viewModelScope.launch {
            val result = try {
                val remote = ActivePlanClient.apiService.renewActivePlan(token = "Bearer $token", activePlanId)
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                loadActivePlanById(token, activePlanId)
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
    fun finishActivePlan(token: String, activePlanId: String) {
        viewModelScope.launch {
            val result = try {
                val remote = ActivePlanClient.apiService.finishActivePlan(token = "Bearer $token", activePlanId)
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                loadActivePlanById(token, activePlanId)
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
    fun cancelActivePlan(token: String, activePlanId: String) {
        viewModelScope.launch {
            val result = try {
                val remote = ActivePlanClient.apiService.cancelActivePlan(token = "Bearer $token", activePlanId)
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
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
    override fun clearViewModelData() {
        _activePlan.value = null
        _activePlans.value = null
    }
}