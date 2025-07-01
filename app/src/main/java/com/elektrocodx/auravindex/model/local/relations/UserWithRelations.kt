package com.elektrocodx.auravindex.model.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.elektrocodx.auravindex.model.local.GenderEntity
import com.elektrocodx.auravindex.model.local.RoleEntity
import com.elektrocodx.auravindex.model.local.UserEntity

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