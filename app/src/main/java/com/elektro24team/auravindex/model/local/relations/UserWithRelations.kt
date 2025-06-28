package com.elektro24team.auravindex.model.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.elektro24team.auravindex.model.local.GenderEntity
import com.elektro24team.auravindex.model.local.RoleEntity
import com.elektro24team.auravindex.model.local.UserEntity

class UserWithRelations (
    @Embedded val user: UserEntity,

    @Relation(
        parentColumn = "gender_id",
        entityColumn = "_id"
    )
    val gender: GenderEntity,

    @Relation(
        parentColumn = "role_id",
        entityColumn = "_id"
    )
    val role: RoleEntity,

)