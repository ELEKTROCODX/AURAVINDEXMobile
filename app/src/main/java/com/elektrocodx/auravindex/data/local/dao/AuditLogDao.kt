package com.elektrocodx.auravindex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.elektrocodx.auravindex.model.local.AuditLogEntity
import com.elektrocodx.auravindex.model.local.LogActionEntity
import com.elektrocodx.auravindex.model.local.UserEntity
import com.elektrocodx.auravindex.model.local.relations.AuditLogWithRelations


@Dao
interface AuditLogDao {

    @Transaction
    suspend fun insertAuditLogWithRelations(
        auditLog: AuditLogEntity,
        action: LogActionEntity,
        user: UserEntity?
    ) {
        insertAuditLog(auditLog)
        insertLogAction(action)
        if(user != null) insertUser(user)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuditLog(auditLog: AuditLogEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogAction(action: LogActionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuditLogs(auditLogs: List<AuditLogEntity>)

    @Transaction
    @Query("SELECT * FROM auditlogs WHERE _id = :auditLogId")
    suspend fun getAuditLogWithRelations(auditLogId: String): AuditLogWithRelations

    @Transaction
    @Query("SELECT * FROM auditlogs")
    suspend fun getAllAuditLogsWithRelations(): List<AuditLogWithRelations>

    @Query("DELETE FROM auditlogs")
    suspend fun clearAuditLogs()

}
