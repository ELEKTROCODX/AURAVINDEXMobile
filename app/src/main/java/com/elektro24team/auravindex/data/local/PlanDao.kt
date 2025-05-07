package com.elektro24team.auravindex.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elektro24team.auravindex.model.local.PlanEntity

@Dao
interface PlanDao {
    @Query("SELECT * FROM plans")
    suspend fun getAllPlans(): List<PlanEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlans(plans: List<PlanEntity>)

    @Query("DELETE FROM plans")
    suspend fun clearPlans()
}