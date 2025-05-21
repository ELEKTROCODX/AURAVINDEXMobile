package com.elektro24team.auravindex.model.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.elektro24team.auravindex.model.local.AuditLogEntity
import com.elektro24team.auravindex.model.local.GenderEntity
import com.elektro24team.auravindex.model.local.LogActionEntity
import com.elektro24team.auravindex.model.local.RoleEntity
import com.elektro24team.auravindex.model.local.UserEntity

class AuditLogWithRelations (
    @Embedded val auditLog: AuditLogEntity,

    @Relation(
        parentColumn = "user_id",
        entityColumn = "_id"
    )
    val user: UserEntity?,

    @Relation(
        parentColumn = "gender_id",
        entityColumn = "_id"
    )
    val gender: GenderEntity?,

    @Relation(
        parentColumn = "role_id",
        entityColumn = "_id"
    )
    val role: RoleEntity?,

    @Relation(
        parentColumn = "action_id",
        entityColumn = "_id"
    )
    val action: LogActionEntity,
)