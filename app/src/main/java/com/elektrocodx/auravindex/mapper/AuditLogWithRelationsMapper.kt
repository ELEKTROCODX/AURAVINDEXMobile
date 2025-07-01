package com.elektrocodx.auravindex.mapper

import com.elektrocodx.auravindex.model.AuditLog
import com.elektrocodx.auravindex.model.local.relations.AuditLogWithRelations


fun AuditLogWithRelations.toDomain(): AuditLog {
    if(user != null && gender != null && role != null) {
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

    } else {
        return AuditLog(
            __v = auditLog.__v,
            _id = auditLog._id,
            createdAt = auditLog.createdAt,
            updatedAt = auditLog.updatedAt,
            user = null,
            action = action.toDomain(),
            affected_object = auditLog.affectedObject
        )
    }

}