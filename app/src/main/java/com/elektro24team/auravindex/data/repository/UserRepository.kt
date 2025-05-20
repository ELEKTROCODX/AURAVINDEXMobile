package com.elektro24team.auravindex.data.repository

import android.util.Log
import com.elektro24team.auravindex.data.local.dao.UserDao
import com.elektro24team.auravindex.mapper.toDomain
import com.elektro24team.auravindex.mapper.toEntity
import com.elektro24team.auravindex.model.User
import com.elektro24team.auravindex.retrofit.UserClient

class UserRepository(
  private val userDao: UserDao  
){
    private val CACHE_EXPIRY_MS = 24 * 60 * 60 * 1000L // 1 día
    @Volatile
    private var lastCacheTime: Long = 0

    suspend fun getUsers(token: String): Result<List<User>> {
        return try {
            val currentTime = System.currentTimeMillis()
            val isCacheExpired = (currentTime - lastCacheTime) > CACHE_EXPIRY_MS
            val local = userDao.getAllUsersWithRelations()

            if (local.isNotEmpty() && !isCacheExpired) {
                Result.success(local.map { it.toDomain() })
            } else {
                val remote = UserClient.apiService.getUsers("Bearer $token")
                val users = remote.data ?: emptyList()
                userDao.clearUsers()
                userDao.insertUser(users.map { it.toEntity() })
                lastCacheTime = System.currentTimeMillis()
                Result.success(users)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getUserById(token: String, userId: String): Result<User> {
        return try {
            val local = userDao.getUserWithRelations(userId)
            if (local != null) {
                return Result.success(local.toDomain())
            }

            val remote = UserClient.apiService.getUserById("Bearer $token", userId)
            saveUserToCache(remote)
            Result.success(remote)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun saveUserToCache(user: User) {
        userDao.insertUser(listOf(user.toEntity()))
    }

    suspend fun getUser(token: String, email: String): Result<User> {
        return try {
            val response = UserClient.apiService.getUser(
                token = "Bearer $token",
                filterField = "email",
                filterValue = email
            )
            val user = response.data?.firstOrNull()
            user?.let {
                saveUserToCache(it)
                Result.success(it)
            } ?: Result.failure(Exception("No user found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

