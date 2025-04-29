package com.elektro24team.auravindex.view.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elektro24team.auravindex.model.Plan
import com.elektro24team.auravindex.retrofit.PlanClient
import kotlinx.coroutines.launch

//Manage plans data
class PlanViewModel: ViewModel() {
    //Save plans in a list
    var posts = mutableStateOf<List<Plan>>(emptyList())
        private set

    //Execute the instance of PlanViewModel
    //calls fetchPlans automatic
    init {
        fetchPlans()
    }

    //fetch plans from the server
    private fun fetchPlans(){
        viewModelScope.launch {
            try {
                //Calls the API service to get the plans
                val response = PlanClient.apiService.getPlans()
                //Update the state with the list of plans
                posts.value = response.data
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}