package com.elektro24team.auravindex.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elektro24team.auravindex.data.repository.PlanRepository
import com.elektro24team.auravindex.model.Plan
import com.elektro24team.auravindex.model.local.PlanEntity
import com.elektro24team.auravindex.retrofit.PlanClient
import kotlinx.coroutines.launch

//Manage plans data
class PlanViewModel(
    private val repository: PlanRepository
) : ViewModel() {

    private val _plans = MutableLiveData<List<PlanEntity>>()
    val plans: LiveData<List<PlanEntity>> = _plans

    fun loadPlans() {
        viewModelScope.launch {
            val result = repository.getPlans()
            _plans.postValue(result)
        }
    }
}