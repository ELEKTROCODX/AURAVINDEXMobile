package com.elektrocodx.auravindex.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elektrocodx.auravindex.data.repository.GenderRepository
import com.elektrocodx.auravindex.model.Gender
import kotlinx.coroutines.launch

class GenderViewModel (private val repository: GenderRepository) : ViewModel(){
    private val _genders = MutableLiveData<List<Gender>>()
    val genders: MutableLiveData<List<Gender>> = _genders

    fun getGendersList(){
        viewModelScope.launch {
            val result = repository.getGenders()
            if (result.isSuccess){
                _genders.value = result.getOrThrow()
            }
        }
    }
}