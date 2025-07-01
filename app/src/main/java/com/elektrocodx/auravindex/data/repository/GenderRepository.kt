package com.elektrocodx.auravindex.data.repository

import com.elektrocodx.auravindex.model.Gender
import com.elektrocodx.auravindex.retrofit.GenderClient

class GenderRepository {
    suspend fun getGenders(): Result<List<Gender>>{
        return try {
            val response = GenderClient.apiService.getGenders()
            val genders = response.data
            Result.success(genders)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}