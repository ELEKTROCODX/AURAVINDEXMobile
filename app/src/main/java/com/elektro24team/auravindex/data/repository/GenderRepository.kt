package com.elektro24team.auravindex.data.repository

import com.elektro24team.auravindex.model.Gender
import com.elektro24team.auravindex.retrofit.GenderClient

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