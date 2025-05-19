package com.elektro24team.auravindex.data.repository

import android.util.Log
import com.elektro24team.auravindex.data.local.dao.PlanDao
import com.elektro24team.auravindex.mapper.toDomain
import com.elektro24team.auravindex.model.Plan
import com.elektro24team.auravindex.model.local.PlanEntity
import com.elektro24team.auravindex.retrofit.BookClient
import com.elektro24team.auravindex.retrofit.PlanClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class PlanRepository(
    private val planDao: PlanDao
) {
    private val CACHE_EXPIRY_MS = 24 * 60 * 60 * 1000L // 1 día
    @Volatile
    private var lastCacheTime: Long = 0

    suspend fun getPlans(): List<Plan> {
        val currentTime = System.currentTimeMillis()
        val isCacheExpired = (currentTime - lastCacheTime) > CACHE_EXPIRY_MS
        val local = planDao.getAllPlans()
        return if (local.isEmpty() || isCacheExpired) {
            val remote = PlanClient.apiService.getPlans()
            remote.data.map { it }
        } else {
            local.map { it.toDomain() }
        }
    }
}