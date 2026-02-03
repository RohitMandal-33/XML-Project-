package com.example.xmlproj.repository

import com.example.xmlproj.database.User
import com.example.xmlproj.database.UserDao
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    val allUsers: Flow<List<User>> = userDao.getAllUsers()

    suspend fun insert(user: User): Long {
        return userDao.insert(user)
    }

    suspend fun update(user: User) {
        userDao.update(user)
    }

    suspend fun delete(user: User) {
        userDao.delete(user)
    }

    suspend fun getUserById(userId: Int): User? {
        return userDao.getUserById(userId)
    }

    suspend fun login(email: String, password: String): User? {
        return userDao.login(email, password)
    }

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    suspend fun deleteAll() {
        userDao.deleteAll()
    }

    suspend fun getUserCount(): Int {
        return userDao.getUserCount()
    }
}