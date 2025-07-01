package com.elektrocodx.auravindex.mapper

import com.elektrocodx.auravindex.model.AuditLog
import com.elektrocodx.auravindex.model.LogAction
import com.elektrocodx.auravindex.model.User
import com.elektrocodx.auravindex.model.local.AuditLogEntity


fun AuditLog.toEntity(): AuditLogEntity {
    return AuditLogEntity(
        __v = this.__v,
        _id = this._id,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        user_id = this.user?._id,
        action_id = this.action._id,
        affectedObject = this.affected_object,
        gender_id = this.user?.gender?._id,
        role_id = this.user?.role?._id
    )
}

fun AuditLogEntity.toDomain(
    user: User,
    action: LogAction,
): AuditLog {
    return AuditLog(
        __v = this.__v,
        _id = this._id,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        user = user,
        action = action,
        affected_object = this.affectedObject
    )
}