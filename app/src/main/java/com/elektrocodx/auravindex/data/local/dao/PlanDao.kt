package com.elektrocodx.auravindex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elektrocodx.auravindex.model.local.PlanEntity

@Dao
interface PlanDao {
    @Query("SELECT * FROM plans")
    suspend fun getAllPlans(): List<PlanEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlans(plans: List<PlanEntity>)

    @Query("SELECT * FROM plans WHERE _id = :planId")
    suspend fun getPlanById(planId: String): PlanEntity

    @Query("DELETE FROM plans")
    suspend fun clearPlans()
}