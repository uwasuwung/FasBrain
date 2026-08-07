package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "test_results")
data class TestResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val testCategory: String,
    val categoryName: String,
    val score: Int,
    val categoryLabel: String,
    val correctCount: Int,
    val totalQuestions: Int,
    val timeTakenSeconds: Int,
    val categoryScoresJson: String = "{}",
    val timestamp: Long = System.currentTimeMillis()
)
