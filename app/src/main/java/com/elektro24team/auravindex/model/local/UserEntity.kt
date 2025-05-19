package com.elektro24team.auravindex.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elektro24team.auravindex.model.Gender
import com.elektro24team.auravindex.model.Role

@Entity(tableName = "users")
data class UserEntity(
    val __v: Int,
    @PrimaryKey val _id: String,
    val address: String,
    val biography: String,
    val birthdate: String,
    val createdAt: String,
    val email: String,
    val gender_id: String,
    val last_name: String,
    val name: String,
    val role_id: String,
    val updatedAt: String,
    val user_img: String,
)