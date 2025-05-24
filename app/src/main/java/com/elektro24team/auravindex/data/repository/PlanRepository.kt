package com.elektro24team.auravindex.data.repository

import com.elektro24team.auravindex.data.local.dao.PlanDao
import com.elektro24team.auravindex.mapper.toDomain
import com.elektro24team.auravindex.mapper.toEntity
import com.elektro24team.auravindex.model.Plan
import com.elektro24team.auravindex.retrofit.PlanClient


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
    suspend fun getPlanById(planId: String): Plan {
        val local = planDao.getPlanById(planId)
        if (local != null) {
            return local.toDomain()
        }

        val remote = PlanClient.apiService.getPlanById(planId)
        savePlanToCache(remote)

        return remote
    }
    suspend fun savePlanToCache(plan: Plan) {
        planDao.insertPlans(listOf(plan.toEntity()))
    }
}