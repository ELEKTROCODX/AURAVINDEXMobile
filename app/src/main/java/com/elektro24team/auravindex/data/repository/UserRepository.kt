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
    
    suspend fun getUsers(token: String): List<User> {
        val currentTime = System.currentTimeMillis()
        val isCacheExpired = (currentTime - lastCacheTime) > CACHE_EXPIRY_MS
        val local = userDao.getAllUsersWithRelations()
        return if (local.isEmpty() || isCacheExpired) {
            val remote = UserClient.apiService.getUsers(token = token)
            remote.data.map { it }
        } else {
            local.map { it.toDomain() }
        }
    }
    suspend fun getUserById(token: String, userId: String): User {
        val local = userDao.getUserWithRelations(userId)
        if (local != null) {
            return local.toDomain()
        }

        val remote = UserClient.apiService.getUserById(token = token, id = userId)
        saveUserToCache(remote)

        return remote
    }
    suspend fun saveUserToCache(user: User) {
        userDao.insertUser(listOf(user.toEntity()))
    }
    
    suspend fun getUser(token: String, email: String): Result<User>{
        return try {
            val response = UserClient.apiService.getUser(token = token,filterField = "email",filterValue = email)
            Log.d("UserRepository", "Usuario recibido: ${response}")
            val user = response.data?.firstOrNull()
            user?.let {
                Log.d("UserRepository", "Usuario encontrado: ${it.name}")
                Result.success(it)
            } ?: run {
                Result.failure(Exception("No se encontró un usuario"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}

