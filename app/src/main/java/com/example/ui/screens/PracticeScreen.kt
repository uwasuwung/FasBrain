package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Question
import com.example.ui.components.FasBrainBottomNavigation
import com.example.ui.components.IshiharaPlateView
import com.example.ui.components.SpatialPatternView
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.ScoreGreen
import com.example.ui.theme.ScoreRed
import com.example.ui.viewmodel.TestState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeScreen(
    testState: TestState,
    onOptionSelected: (Int) -> Unit,
    onSubmitAnswer: () -> Unit,
    onNextClicked: () -> Unit,
    onBackClicked: () -> Unit,
    onNavigateTab: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val currentQuestion: Question? = testState.questions.getOrNull(testState.currentIndex)
    val totalQuestions = testState.questions.size
    val targetProgress = if (totalQuestions > 0) (testState.currentIndex + 1).toFloat() / totalQuestions else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "practice_progress_animation"
    )

    Scaffold(
        modifier = modifier.testTag("practice_screen"),
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Column {
                            Text("Mode Latihan", fontWeight = FontWeight.Bold)
                            Text(
                                "Soal ${testState.currentIndex + 1} dari $totalQuestions • Bebas Tanpa Waktu",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClicked) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = NavyPrimary,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.fillMaxWidth().height(6.dp),
                    color = CyanAccent,
                    trackColor = NavyPrimary.copy(alpha = 0.3f)
                )
            }
        },
        bottomBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    if (!testState.isAnswerSubmittedInPractice) {
                        Button(
                            onClick = onSubmitAnswer,
                            enabled = testState.selectedOptionIndex != null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("button_submit_practice"),
                            colors = ButtonDefaults.buttonColors(containerColor = CyanAccent),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Periksa Jawaban", fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                    } else {
                        Button(
                            onClick = onNextClicked,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("button_next_practice"),
                            colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = if (testState.currentIndex == totalQuestions - 1) "Selesai Latihan" else "Soal Berikutnya",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White)
                        }
                    }
                }

                FasBrainBottomNavigation(
                    selectedTab = "start",
                    onTabSelected = onNavigateTab
                )
            }
        }
    ) { innerPadding ->
        if (currentQuestion != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Visual Diagram if applicable
                item {
                    when (currentQuestion.category) {
                        "buta_warna" -> IshiharaPlateView(questionId = currentQuestion.id)
                        "spasial" -> SpatialPatternView(questionId = currentQuestion.id)
                    }
                }

                // Question Text
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = currentQuestion.question,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Options with Feedback
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        val options = currentQuestion.getOptions()
                        val optionLabels = listOf("A", "B", "C", "D")

                        options.forEachIndexed { index, optionText ->
                            val isSelected = testState.selectedOptionIndex == index
                            val isCorrect = currentQuestion.correctIndex == index

                            val cardBg = when {
                                !testState.isAnswerSubmittedInPractice -> {
                                    if (isSelected) NavyPrimary else MaterialTheme.colorScheme.surface
                                }
                                isCorrect -> ScoreGreen.copy(alpha = 0.2f)
                                isSelected && !isCorrect -> ScoreRed.copy(alpha = 0.2f)
                                else -> MaterialTheme.colorScheme.surface
                            }

                            val borderColor = when {
                                !testState.isAnswerSubmittedInPractice -> {
                                    if (isSelected) CyanAccent else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                }
                                isCorrect -> ScoreGreen
                                isSelected && !isCorrect -> ScoreRed
                                else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                            }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .border(2.dp, borderColor, RoundedCornerShape(14.dp))
                                    .clickable {
                                        if (!testState.isAnswerSubmittedInPractice) {
                                            onOptionSelected(index)
                                        }
                                    }
                                    .testTag("practice_option_$index"),
                                colors = CardDefaults.cardColors(containerColor = cardBg)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(
                                                when {
                                                    testState.isAnswerSubmittedInPractice && isCorrect -> ScoreGreen
                                                    testState.isAnswerSubmittedInPractice && isSelected && !isCorrect -> ScoreRed
                                                    isSelected -> CyanAccent
                                                    else -> MaterialTheme.colorScheme.primaryContainer
                                                }
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = optionLabels.getOrElse(index) { "?" },
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected || (testState.isAnswerSubmittedInPractice && (isCorrect || isSelected))) Color.White else NavyPrimary
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Text(
                                        text = optionText,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = if (isSelected || isCorrect) FontWeight.Bold else FontWeight.Normal,
                                        modifier = Modifier.weight(1f)
                                    )

                                    if (testState.isAnswerSubmittedInPractice) {
                                        if (isCorrect) {
                                            Icon(Icons.Default.Check, contentDescription = "Benar", tint = ScoreGreen)
                                        } else if (isSelected) {
                                            Icon(Icons.Default.Close, contentDescription = "Salah", tint = ScoreRed)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Explanation Box after submit
                if (testState.isAnswerSubmittedInPractice && currentQuestion.explanation.isNotEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(modifier = Modifier.padding(16.dp)) {
                                Icon(
                                    Icons.Default.Info,
                                    contentDescription = "Pembahasan",
                                    tint = NavyPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Pembahasan Soal:",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = NavyPrimary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = currentQuestion.explanation,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
