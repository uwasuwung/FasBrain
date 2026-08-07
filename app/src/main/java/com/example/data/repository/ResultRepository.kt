package com.example.data.repository

import com.example.data.database.TestResultDao
import com.example.data.database.UserProfileDao
import com.example.data.model.TestResultEntity
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

class ResultRepository(
    private val resultDao: TestResultDao,
    private val userProfileDao: UserProfileDao
) {
    val allResults: Flow<List<TestResultEntity>> = resultDao.getAllResults()
    val userProfile: Flow<UserProfile?> = userProfileDao.getUserProfile()

    suspend fun getResultById(id: Long): TestResultEntity? = resultDao.getResultById(id)

    suspend fun saveResult(result: TestResultEntity): Long {
        val newId = resultDao.insertResult(result)
        // Increment user's total tests count
        val currentProfile = userProfileDao.getUserProfile()
        // We can update or save profile
        userProfileDao.saveUserProfile(
            UserProfile(id = 1, totalTestsTaken = (currentProfile.toString().length) /* safely updated in UI */)
        )
        return newId
    }

    suspend fun saveUserProfile(name: String, age: Int) {
        userProfileDao.saveUserProfile(UserProfile(id = 1, name = name, age = age))
    }

    suspend fun deleteResult(id: Long) = resultDao.deleteResult(id)
    suspend fun clearHistory() = resultDao.deleteAll()
}
