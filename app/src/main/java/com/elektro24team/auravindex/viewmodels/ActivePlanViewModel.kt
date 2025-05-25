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
import com.elektro24team.auravindex.model.ActivePlanRequest
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
                val remote = ActivePlanClient.apiService.getActivePlanByUserId(token = "Bearer $token", filterValue = userId)
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                if(result.getOrNull()?.data?.isNotEmpty() == true) {
                    _activePlan.value = result.getOrNull()?.data?.get(0)
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
                val remote = ActivePlanClient.apiService.getActivePlans(token = "Bearer $token")
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                Log.d("ActivePlanViewModel", "loadActivePlans: ${result.getOrNull()?.data}")
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
                notifySuccess("The active plan has been created successfully")
            } else {
                val error = result.exceptionOrNull()
                if (error is HttpException) {
                    when (error.code()) {
                        401 -> notifyTokenExpired()
                        403 -> notifyInsufficentPermissions()
                        404 -> notifyError(error.message())
                        409 -> notifyError("You are already subscribed to a plan")
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