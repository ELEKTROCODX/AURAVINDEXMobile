package com.elektro24team.auravindex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.elektro24team.auravindex.model.AuditLog
import com.elektro24team.auravindex.model.LogAction
import com.elektro24team.auravindex.model.User
import com.elektro24team.auravindex.model.local.AuthorEntity
import com.elektro24team.auravindex.model.local.AuditLogEntity
import com.elektro24team.auravindex.model.local.LogActionEntity
import com.elektro24team.auravindex.model.local.UserEntity
import com.elektro24team.auravindex.model.local.relations.AuditLogWithRelations


@Dao
interface AuditLogDao {

    @Transaction
    suspend fun insertAuditLogWithRelations(
        auditLog: AuditLogEntity,
        user: UserEntity,
        action: LogActionEntity
    ) {
        insertAuditLog(auditLog)
        insertUser(user)
        insertLogAction(action)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuditLog(book: AuditLogEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(editorial: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogAction(status: LogActionEntity)


    @Transaction
    @Query("SELECT * FROM auditlogs WHERE _id = :auditLogId")
    suspend fun getAuditLogWithRelations(auditLogId: String): AuditLogWithRelations

    @Transaction
    @Query("SELECT * FROM auditlogs")
    suspend fun getAllAuditLogsWithRelations(): List<AuditLogWithRelations>

}
