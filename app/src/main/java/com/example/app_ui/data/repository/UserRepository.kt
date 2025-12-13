package com.example.app_ui.data.repository

import com.example.app_ui.data.dao.UserDao
import com.example.app_ui.data.entity.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    fun getCurrentUser(): Flow<User?> = userDao.getCurrentUser()
    
    suspend fun getUserById(userId: String): User? = userDao.getUserById(userId)
    
    suspend fun getUserByEmail(email: String): User? = userDao.getUserByEmail(email)
    
    suspend fun insertUser(user: User) = userDao.insertUser(user)
    
    suspend fun updateUser(user: User) = userDao.updateUser(user)
    
    suspend fun deleteUser(user: User) = userDao.deleteUser(user)
    
    suspend fun deleteAllUsers() = userDao.deleteAllUsers()
    
    suspend fun getUserCount(): Int = userDao.getUserCount()
}

