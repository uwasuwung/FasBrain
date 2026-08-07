package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.model.Question
import com.example.data.model.TestCategories
import com.example.data.repository.QuestionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PracticeState(
    val selectedDomainId: String = "all",
    val domainTitle: String = "Tes IQ Komprehensif",
    val questions: List<Question> = emptyList(),
    val currentIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isAnswerSubmitted: Boolean = false,
    val isCorrect: Boolean? = null,
    val sessionCorrectCount: Int = 0,
    val sessionTotalAnswered: Int = 0,
    val isLoading: Boolean = false
)

class PracticeViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val questionRepository = QuestionRepository(application, db.questionDao())

    private val _practiceState = MutableStateFlow(PracticeState())
    val practiceState: StateFlow<PracticeState> = _practiceState.asStateFlow()

    init {
        viewModelScope.launch {
            questionRepository.ensureQuestionsLoaded()
            loadDomainQuestions("all")
        }
    }

    fun loadDomainQuestions(domainId: String) {
        viewModelScope.launch {
            _practiceState.value = _practiceState.value.copy(isLoading = true)
            val categoryInfo = TestCategories.getById(domainId)
            val questions = questionRepository.getQuestionsForCategory(domainId, limit = 20)

            _practiceState.value = PracticeState(
                selectedDomainId = domainId,
                domainTitle = categoryInfo.title,
                questions = questions,
                currentIndex = 0,
                selectedOptionIndex = null,
                isAnswerSubmitted = false,
                isCorrect = null,
                sessionCorrectCount = 0,
                sessionTotalAnswered = 0,
                isLoading = false
            )
        }
    }

    fun selectOption(optionIndex: Int) {
        val current = _practiceState.value
        if (current.isAnswerSubmitted) return
        _practiceState.value = current.copy(selectedOptionIndex = optionIndex)
    }

    fun submitAnswer() {
        val current = _practiceState.value
        val currentQuestion = current.questions.getOrNull(current.currentIndex) ?: return
        val selected = current.selectedOptionIndex ?: return

        if (!current.isAnswerSubmitted) {
            val isCorrect = selected == currentQuestion.correctIndex
            _practiceState.value = current.copy(
                isAnswerSubmitted = true,
                isCorrect = isCorrect,
                sessionCorrectCount = if (isCorrect) current.sessionCorrectCount + 1 else current.sessionCorrectCount,
                sessionTotalAnswered = current.sessionTotalAnswered + 1
            )
        }
    }

    fun nextQuestion() {
        val current = _practiceState.value
        val nextIdx = current.currentIndex + 1

        if (nextIdx < current.questions.size) {
            _practiceState.value = current.copy(
                currentIndex = nextIdx,
                selectedOptionIndex = null,
                isAnswerSubmitted = false,
                isCorrect = null
            )
        } else {
            // Re-fetch random questions for continuous practice without storing results
            loadDomainQuestions(current.selectedDomainId)
        }
    }

    fun resetSession() {
        val current = _practiceState.value
        loadDomainQuestions(current.selectedDomainId)
    }
}
