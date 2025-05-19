package com.elektro24team.auravindex.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elektro24team.auravindex.data.repository.PlanRepository
import com.elektro24team.auravindex.model.Plan
import kotlinx.coroutines.launch

//Manage plans data
class PlanViewModel(
    private val repository: PlanRepository
) : ViewModel() {

    private val _plans = MutableLiveData<List<Plan>>()
    private val _plan = MutableLiveData<Plan>()
    val plans: LiveData<List<Plan>> = _plans
    val plan: LiveData<Plan> = _plan

    fun loadPlans() {
        viewModelScope.launch {
            val result = repository.getPlans()
            _plans.postValue(result)
        }
    }
    fun loadPlan(planId: String) {
        viewModelScope.launch {
            if( _plans.value?.find{ it._id == planId } == null) {
                val result = repository.getPlanById(planId)
                _plans.postValue(listOf(result))
                _plan.postValue(result)
            } else {
                _plan.postValue(_plans.value?.find{ it._id == planId })
            }
        }
    }
}