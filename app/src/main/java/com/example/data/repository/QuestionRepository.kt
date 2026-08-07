package com.example.data.repository

import android.content.Context
import com.example.data.database.QuestionDao
import com.example.data.model.Question
import com.example.data.model.QuestionJsonDto
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class QuestionRepository(
    private val context: Context,
    private val questionDao: QuestionDao
) {
    val allQuestions: Flow<List<Question>> = questionDao.getAllQuestions()

    suspend fun ensureQuestionsLoaded() = withContext(Dispatchers.IO) {
        val count = questionDao.getQuestionCount()
        if (count < 500) {
            loadQuestionsFromJson()
        }
    }

    private suspend fun loadQuestionsFromJson() {
        try {
            val jsonString = context.assets.open("soal.json").bufferedReader().use { it.readText() }
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val listType = Types.newParameterizedType(List::class.java, QuestionJsonDto::class.java)
            val adapter = moshi.adapter<List<QuestionJsonDto>>(listType)
            val dtoList = adapter.fromJson(jsonString) ?: emptyList()

            val entities = dtoList.map { dto ->
                val opts = dto.options
                Question(
                    id = dto.id,
                    category = dto.realCategory,
                    question = dto.question,
                    optionA = opts.getOrNull(0) ?: "",
                    optionB = opts.getOrNull(1) ?: "",
                    optionC = opts.getOrNull(2) ?: "",
                    optionD = opts.getOrNull(3) ?: "",
                    correctIndex = dto.realCorrectIndex,
                    timeLimit = dto.realTimeLimit,
                    explanation = dto.explanation,
                    domain = dto.domain ?: "",
                    type = dto.type ?: "",
                    difficulty = dto.difficulty ?: "medium"
                )
            }
            questionDao.insertAll(entities)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getQuestionsForCategory(categoryId: String, limit: Int = 30): List<Question> = withContext(Dispatchers.IO) {
        ensureQuestionsLoaded()
        if (categoryId == "all") {
            questionDao.getRandomQuestions(limit)
        } else {
            val categoryQuestions = questionDao.getRandomQuestionsByCategory(categoryId, limit)
            if (categoryQuestions.isEmpty()) {
                questionDao.getRandomQuestions(limit)
            } else {
                categoryQuestions
            }
        }
    }
}
