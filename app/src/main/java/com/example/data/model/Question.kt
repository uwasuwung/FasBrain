package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "questions")
data class Question(
    @PrimaryKey val id: Int,
    val category: String,
    val question: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctIndex: Int,
    val timeLimit: Int = 45,
    val explanation: String = "",
    val domain: String = "",
    val type: String = "",
    val difficulty: String = "medium"
) {
    fun getOptions(): List<String> = listOf(optionA, optionB, optionC, optionD)
}

data class QuestionJsonDto(
    val id: Int,
    val category: String? = null,
    val domain: String? = null,
    val type: String? = null,
    val difficulty: String? = "medium",
    val question: String,
    val options: List<String>,
    @Json(name = "correct_answer") val correct_answer: Int? = null,
    val correctIndex: Int? = null,
    @Json(name = "time_limit") val time_limit: Int? = null,
    val timeLimit: Int? = null,
    val explanation: String = ""
) {
    val realCorrectIndex: Int get() = correct_answer ?: correctIndex ?: 0
    val realTimeLimit: Int get() = time_limit ?: timeLimit ?: 35
    val realCategory: String get() {
        if (!category.isNullOrEmpty()) return category
        return when (domain) {
            "Penalaran Verbal" -> "verbal"
            "Penalaran Kuantitatif/Numerik" -> "deret_angka"
            "Penalaran Spasial & Abstrak" -> "spasial"
            "Memori Jangka Pendek & Kecepatan Pemrosesan" -> "memori"
            else -> "iq"
        }
    }
}
