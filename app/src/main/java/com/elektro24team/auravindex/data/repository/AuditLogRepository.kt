package com.elektro24team.auravindex.data.repository

import android.util.Log
import com.elektro24team.auravindex.data.local.dao.AuditLogDao
import com.elektro24team.auravindex.mapper.toDomain
import com.elektro24team.auravindex.mapper.toEntity
import com.elektro24team.auravindex.model.AuditLog
import com.elektro24team.auravindex.retrofit.AuditLogClient


class AuditLogRepository(
    private val auditLogDao: AuditLogDao
) {
    private val CACHE_EXPIRY_MS = 1 * 60 * 1000L // 1 min

    @Volatile
    private var lastCacheTime: Long = 0

    suspend fun getAllAuditLogs(token: String): Result<List<AuditLog>> {
        return try {
            val currentTime = System.currentTimeMillis()
            val isCacheExpired = (currentTime - lastCacheTime) > CACHE_EXPIRY_MS
            val local = auditLogDao.getAllAuditLogsWithRelations()
            if(local.isNotEmpty() && !isCacheExpired) {
                Result.success(local.map { it.toDomain() })
            } else {
                val remote = AuditLogClient.apiService.getAuditLogs("Bearer $token")
                val auditLogs = remote.data ?: emptyList()
                auditLogDao.clearAuditLogs()
                auditLogs.forEach { auditLog ->
                    val auditLogEntity = auditLog.toEntity()
                    val userEntity = auditLog.user?.toEntity()
                    val actionEntity = auditLog.action.toEntity()
                    auditLogDao.insertAuditLogWithRelations(auditLogEntity, actionEntity, userEntity)
                }
                lastCacheTime = System.currentTimeMillis()
                Result.success(auditLogs)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAuditLogById(token: String, auditLogId: String): Result<AuditLog> {
        return try {
            val local = auditLogDao.getAuditLogWithRelations(auditLogId)
            if (local != null) {
                return Result.success(local.toDomain())
            }
            val remote = AuditLogClient.apiService.getAuditLogById(token = "Bearer $token", auditLogId)
            saveAuditLogToCache(remote)
            Result.success(remote)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveAuditLogToCache(auditLog: AuditLog) {
        val auditLogEntity = auditLog.toEntity()
        val userEntity = auditLog.user?.toEntity()
        val actionEntity = auditLog.action.toEntity()

        auditLogDao.insertAuditLogWithRelations(auditLogEntity, actionEntity, userEntity)
    }
}