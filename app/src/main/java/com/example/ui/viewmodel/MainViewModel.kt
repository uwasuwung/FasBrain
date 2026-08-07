package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.model.Question
import com.example.data.model.TestCategoryInfo
import com.example.data.model.TestResultEntity
import com.example.data.model.UserProfile
import com.example.data.repository.QuestionRepository
import com.example.data.repository.ResultRepository
import com.example.utils.ScoreUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

data class TestState(
    val categoryId: String = "all",
    val categoryTitle: String = "Tes IQ Komprehensif",
    val questions: List<Question> = emptyList(),
    val currentIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val userAnswers: MutableMap<Int, Int> = mutableMapOf(), // questionId -> selectedIndex
    val timeLeftSeconds: Int = 45,
    val isTimerActive: Boolean = false,
    val isTestFinished: Boolean = false,
    val totalTimeTakenSeconds: Int = 0,
    val isPracticeMode: Boolean = false,
    val isAnswerSubmittedInPractice: Boolean = false,
    val latestResultId: Long? = null
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val questionRepository = QuestionRepository(application, db.questionDao())
    private val resultRepository = ResultRepository(db.testResultDao(), db.userProfileDao())

    private val prefs = application.getSharedPreferences("fasbrain_settings", Context.MODE_PRIVATE)

    private val _isDarkMode = MutableStateFlow<Boolean?>(
        if (prefs.contains("dark_mode")) prefs.getBoolean("dark_mode", false) else null
    )
    val isDarkMode: StateFlow<Boolean?> = _isDarkMode.asStateFlow()

    private val _notificationsEnabled = MutableStateFlow(prefs.getBoolean("notifications_enabled", true))
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    private val _dailyReminderEnabled = MutableStateFlow(prefs.getBoolean("daily_reminder", true))
    val dailyReminderEnabled: StateFlow<Boolean> = _dailyReminderEnabled.asStateFlow()

    private val _milestoneNotificationEnabled = MutableStateFlow(prefs.getBoolean("milestone_notification", true))
    val milestoneNotificationEnabled: StateFlow<Boolean> = _milestoneNotificationEnabled.asStateFlow()

    val historyList: StateFlow<List<TestResultEntity>> = resultRepository.allResults
        .stateInScope(emptyList())

    val userProfile: StateFlow<UserProfile?> = resultRepository.userProfile
        .stateInScope(UserProfile(name = "Peserta FasBrain"))

    private val _testState = MutableStateFlow(TestState())
    val testState: StateFlow<TestState> = _testState.asStateFlow()

    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            questionRepository.ensureQuestionsLoaded()
        }
    }

    private fun <T> kotlinx.coroutines.flow.Flow<T>.stateInScope(initialValue: T): StateFlow<T> {
        val flowState = MutableStateFlow(initialValue)
        viewModelScope.launch {
            this@stateInScope.collect { flowState.value = it }
        }
        return flowState.asStateFlow()
    }

    fun updateUserProfile(name: String, age: Int) {
        viewModelScope.launch {
            resultRepository.saveUserProfile(name, age)
        }
    }

    fun startTest(categoryInfo: TestCategoryInfo, isPractice: Boolean = false) {
        timerJob?.cancel()
        viewModelScope.launch {
            val questions = questionRepository.getQuestionsForCategory(categoryInfo.id, categoryInfo.questionCount)
            val firstTimeLimit = questions.firstOrNull()?.timeLimit ?: categoryInfo.defaultTimeLimit

            _testState.value = TestState(
                categoryId = categoryInfo.id,
                categoryTitle = categoryInfo.title,
                questions = questions,
                currentIndex = 0,
                selectedOptionIndex = null,
                userAnswers = mutableMapOf(),
                timeLeftSeconds = firstTimeLimit,
                isTimerActive = !isPractice,
                isTestFinished = false,
                totalTimeTakenSeconds = 0,
                isPracticeMode = isPractice,
                isAnswerSubmittedInPractice = false
            )

            if (!isPractice) {
                startTimer()
            }
        }
    }

    fun selectOption(optionIndex: Int) {
        val current = _testState.value
        if (current.isPracticeMode && current.isAnswerSubmittedInPractice) return

        _testState.value = current.copy(selectedOptionIndex = optionIndex)
        current.userAnswers[current.currentIndex] = optionIndex
    }

    fun submitPracticeAnswer() {
        val current = _testState.value
        if (current.isPracticeMode && current.selectedOptionIndex != null) {
            _testState.value = current.copy(isAnswerSubmittedInPractice = true)
        }
    }

    fun nextQuestion() {
        timerJob?.cancel()
        val current = _testState.value
        val nextIdx = current.currentIndex + 1

        if (nextIdx >= current.questions.size) {
            finishTest()
        } else {
            val nextQuestion = current.questions[nextIdx]
            val nextTimeLimit = nextQuestion.timeLimit

            _testState.value = current.copy(
                currentIndex = nextIdx,
                selectedOptionIndex = current.userAnswers[nextIdx],
                timeLeftSeconds = nextTimeLimit,
                isAnswerSubmittedInPractice = false
            )

            if (!current.isPracticeMode) {
                startTimer()
            }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_testState.value.timeLeftSeconds > 0 && !_testState.value.isTestFinished) {
                delay(1000)
                val state = _testState.value
                val newTime = state.timeLeftSeconds - 1
                val totalTime = state.totalTimeTakenSeconds + 1

                if (newTime <= 0) {
                    _testState.value = state.copy(
                        timeLeftSeconds = 0,
                        totalTimeTakenSeconds = totalTime
                    )
                    // Auto advance to next question when timer expires
                    nextQuestion()
                    break
                } else {
                    _testState.value = state.copy(
                        timeLeftSeconds = newTime,
                        totalTimeTakenSeconds = totalTime
                    )
                }
            }
        }
    }

    private fun finishTest() {
        timerJob?.cancel()
        val current = _testState.value

        var correctCount = 0
        current.questions.forEachIndexed { index, question ->
            val userAnswer = current.userAnswers[index]
            if (userAnswer == question.correctIndex) {
                correctCount++
            }
        }

        val totalQuestions = current.questions.size
        val iqScore = ScoreUtils.calculateIqScore(correctCount, totalQuestions)
        val interp = ScoreUtils.getInterpretation(iqScore)

        _testState.value = current.copy(
            isTestFinished = true,
            isTimerActive = false
        )

        // Save result to Room DB if not practice mode
        if (!current.isPracticeMode && totalQuestions > 0) {
            viewModelScope.launch {
                val entity = TestResultEntity(
                    testCategory = current.categoryId,
                    categoryName = current.categoryTitle,
                    score = iqScore,
                    categoryLabel = interp.categoryLabel,
                    correctCount = correctCount,
                    totalQuestions = totalQuestions,
                    timeTakenSeconds = current.totalTimeTakenSeconds
                )
                val newId = resultRepository.saveResult(entity)
                _testState.value = _testState.value.copy(latestResultId = newId)
            }
        }
    }

    fun deleteHistoryItem(id: Long) {
        viewModelScope.launch {
            resultRepository.deleteResult(id)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            resultRepository.clearHistory()
        }
    }

    fun setDarkMode(isDark: Boolean?) {
        _isDarkMode.value = isDark
        if (isDark == null) {
            prefs.edit().remove("dark_mode").apply()
        } else {
            prefs.edit().putBoolean("dark_mode", isDark).apply()
        }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        _notificationsEnabled.value = enabled
        prefs.edit().putBoolean("notifications_enabled", enabled).apply()
    }

    fun setDailyReminderEnabled(enabled: Boolean) {
        _dailyReminderEnabled.value = enabled
        prefs.edit().putBoolean("daily_reminder", enabled).apply()
    }

    fun setMilestoneNotificationEnabled(enabled: Boolean) {
        _milestoneNotificationEnabled.value = enabled
        prefs.edit().putBoolean("milestone_notification", enabled).apply()
    }

    fun clearLocalCachedTestData() {
        viewModelScope.launch {
            resultRepository.clearHistory()
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
