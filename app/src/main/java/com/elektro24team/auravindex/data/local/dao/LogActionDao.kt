package com.elektro24team.auravindex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elektro24team.auravindex.model.local.LogActionEntity

@Dao
interface LogActionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogActions(logActions: List<LogActionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogAction(logAction: LogActionEntity)

    @Query("SELECT * FROM logactions")
    suspend fun getAllLogActions(): List<LogActionEntity>

    @Query("SELECT * FROM logactions WHERE _id = :logActionId LIMIT 1")
    suspend fun getLogActionById(logActionId: String): LogActionEntity?

    @Query("DELETE FROM logActions")
    suspend fun clearLogActions()
}