package com.elektro24team.auravindex.mapper

import com.elektro24team.auravindex.model.AuditLog
import com.elektro24team.auravindex.model.local.relations.AuditLogWithRelations


fun AuditLogWithRelations.toDomain(): AuditLog {
    return AuditLog(
        __v = auditLog.__v,
        _id = auditLog._id,
        createdAt = auditLog.createdAt,
        updatedAt = auditLog.updatedAt,
        user = user.toDomain(
            gender = gender.toDomain(),
            role = role.toDomain()
        ),
        action = action.toDomain(),
        affected_object = auditLog.affectedObject
    )
}