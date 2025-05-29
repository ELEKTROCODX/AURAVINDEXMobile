package com.elektro24team.auravindex.mapper

import com.elektro24team.auravindex.model.LogAction
import com.elektro24team.auravindex.model.local.LogActionEntity

fun LogAction.toEntity(): LogActionEntity {
    return LogActionEntity(
        __v = this.__v,
        _id = this._id,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        action_code = this.action_code
    )
}

fun LogActionEntity.toDomain(): LogAction {
    return LogAction(
        __v = this.__v,
        _id = this._id,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        action_code = this.action_code
    )
}