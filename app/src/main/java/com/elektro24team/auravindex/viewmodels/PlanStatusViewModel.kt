package com.elektro24team.auravindex.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elektro24team.auravindex.model.PlanStatus
import com.elektro24team.auravindex.retrofit.ActivePlanClient
import com.elektro24team.auravindex.retrofit.PlanStatusClient
import com.elektro24team.auravindex.viewmodels.base.BaseViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException

//Manage plam statuses data
class PlanStatusViewModel() : BaseViewModel() {

    private val _planStatuses = MutableLiveData<List<PlanStatus>?>()
    private val _planStatus = MutableLiveData<PlanStatus?>()
    val planStatuses: LiveData<List<PlanStatus>?> = _planStatuses
    val planStatus: LiveData<PlanStatus?> = _planStatus

    fun loadPlanStatuses() {
        viewModelScope.launch {
            val result = try {
                val remote = PlanStatusClient.apiService.getPlanStatuses()
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                _planStatuses.value = result.getOrNull()?.data
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
    fun loadPlanStatus(planId: String) {
        viewModelScope.launch {
            val result = try {
                val remote = PlanStatusClient.apiService.getPlanStatusById(planId)
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                _planStatus.value = result.getOrNull()
            } else {
                val error = result.exceptionOrNull()
                if (error is HttpException) {
                    when (error.code()) {
                        401 -> notifyTokenExpired()
                        403 -> notifyInsufficentPermissions()
                        404 -> notifyError("Plan status not found")
                        else -> notifyError("HTTP error: ${error.code()}")
                    }
                } else {
                    notifyError("Network error: ${error?.message}")
                }
            }
        }
    }
    fun loadPlanStatusByName(planName: String) {
        viewModelScope.launch {
            val result = try {
                val remote = PlanStatusClient.apiService.getPlanStatusByName(filterValue =  planName)
                Result.success(remote)
            } catch (e: Exception) {
                Result.failure(e)
            }
            if (result.isSuccess) {
                if(result.getOrNull()?.data?.size == 0) {
                    notifyError("Plan status not found")
                } else {
                    _planStatus.value = result.getOrNull()?.data?.get(0)
                }
            } else {
                val error = result.exceptionOrNull()
                if (error is HttpException) {
                    when (error.code()) {
                        401 -> notifyTokenExpired()
                        403 -> notifyInsufficentPermissions()
                        404 -> notifyError("Plan status not found")
                        else -> notifyError("HTTP error: ${error.code()}")
                    }
                } else {
                    notifyError("Network error: ${error?.message}")
                }
            }
        }
    }
    override fun clearViewModelData() {
        _planStatuses.value = null
        _planStatus.value = null

    }
}