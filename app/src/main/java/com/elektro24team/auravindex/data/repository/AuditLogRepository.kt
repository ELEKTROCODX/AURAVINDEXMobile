package com.elektro24team.auravindex.data.repository

import com.elektro24team.auravindex.data.local.dao.AuditLogDao
import com.elektro24team.auravindex.mapper.toDomain
import com.elektro24team.auravindex.mapper.toEntity
import com.elektro24team.auravindex.model.AuditLog
import com.elektro24team.auravindex.retrofit.AuditLogClient


class AuditLogRepository(
    private val auditLogDao: AuditLogDao
) {
    private val CACHE_EXPIRY_MS = 10 * 60 * 1000L // 10 min

    @Volatile
    private var lastCacheTime: Long = 0

    suspend fun getAllAuditLogs(token: String): List<AuditLog> {
        val currentTime = System.currentTimeMillis()
        val isCacheExpired = (currentTime - lastCacheTime) > CACHE_EXPIRY_MS
        val local = auditLogDao.getAllAuditLogsWithRelations()
        return if (local.isEmpty() || isCacheExpired) {
            val remote = AuditLogClient.apiService.getAuditLogs(token = "Bearer $token")
            remote.data.map { it }
        } else {
            local.map { it.toDomain() }
        }
    }

    suspend fun getAuditLogById(token: String, auditLogId: String): AuditLog {
        val local = auditLogDao.getAuditLogWithRelations(auditLogId)
        if (local != null) {
            return local.toDomain()
        }

        val remote = AuditLogClient.apiService.getAuditLogById(token = "Bearer $token", auditLogId)
        saveAuditLogToCache(remote)

        return remote
    }

    suspend fun saveAuditLogToCache(auditLog: AuditLog) {
        val auditLogEntity = auditLog.toEntity()
        val userEntity = auditLog.user.toEntity()
        val actionEntity = auditLog.action.toEntity()

        auditLogDao.insertAuditLogWithRelations(auditLogEntity, userEntity, actionEntity)
    }
}