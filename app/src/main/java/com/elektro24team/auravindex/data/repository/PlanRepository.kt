package com.elektro24team.auravindex.data.repository

import android.util.Log
import com.elektro24team.auravindex.data.local.PlanDao
import com.elektro24team.auravindex.model.Plan
import com.elektro24team.auravindex.model.local.PlanEntity
import com.elektro24team.auravindex.retrofit.PlanClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class PlanRepository(
    private val planDao: PlanDao
) {
    private val CACHE_EXPIRY_MS = 24 * 60 * 60 * 1000L // 1 día

    private var lastCacheTime: Long = 0

    suspend fun getPlans(): List<PlanEntity> {
        val currentTime = System.currentTimeMillis()
        val isCacheExpired = (currentTime - lastCacheTime) > CACHE_EXPIRY_MS

        return if (planDao.getAllPlans().isEmpty() || isCacheExpired) {
            try {
                val response = PlanClient.apiService.getPlans()
                val planEntities = response.data.map { it.toEntity() }
                planDao.clearPlans()
                planDao.insertPlans(planEntities)
                lastCacheTime = currentTime
                planEntities
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("PlanRepository", "Error fetching plans: ${e.message}")
                // Fall back to local data if API fails
                planDao.getAllPlans()
            }
        } else {
            planDao.getAllPlans()
        }
    }

    private fun Plan.toEntity(): PlanEntity {
        return PlanEntity(
            _id = this._id,
            name = this.name,
            fixed_price = this.fixed_price,
            monthly_price = this.monthly_price,
            max_simultaneous_loans = this.max_simultaneous_loans,
            max_return_days = this.max_return_days,
            max_renovations_per_loan = this.max_renovations_per_loan,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            __v = this.__v
        )
    }
}