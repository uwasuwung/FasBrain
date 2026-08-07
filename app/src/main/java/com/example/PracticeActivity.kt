package com.example

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.Question
import com.example.data.model.TestCategories
import com.example.ui.components.FasBrainBottomNavigation
import com.example.ui.components.IshiharaPlateView
import com.example.ui.components.SpatialPatternView
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.FasBrainTheme
import com.example.ui.theme.HeaderGradientStart
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.ScoreGreen
import com.example.ui.theme.ScoreRed
import com.example.ui.viewmodel.PracticeState
import com.example.ui.viewmodel.PracticeViewModel

class PracticeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FasBrainTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PracticeActivityScreen(onFinishActivity = { finish() })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeActivityScreen(
    onFinishActivity: () -> Unit,
    practiceViewModel: PracticeViewModel = viewModel()
) {
    val state by practiceViewModel.practiceState.collectAsState()
    val currentQuestion: Question? = state.questions.getOrNull(state.currentIndex)
    val totalQuestions = state.questions.size
    val targetProgress = if (totalQuestions > 0) (state.currentIndex + 1).toFloat() / totalQuestions else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "practice_activity_progress_animation"
    )

    Scaffold(
        modifier = Modifier.testTag("practice_activity_screen"),
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "Latihan Mandiri",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Latihan Tanpa Menyimpan Hasil • DB Acak",
                                style = MaterialTheme.typography.labelSmall,
                                color = CyanAccent
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onFinishActivity) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Kembali",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { practiceViewModel.resetSession() }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reset Sesi",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = HeaderGradientStart)
                )
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.fillMaxWidth().height(6.dp),
                    color = CyanAccent,
                    trackColor = HeaderGradientStart.copy(alpha = 0.3f)
                )
            }
        },
        bottomBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (currentQuestion != null && !state.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        if (!state.isAnswerSubmitted) {
                            Button(
                                onClick = { practiceViewModel.submitAnswer() },
                                enabled = state.selectedOptionIndex != null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .testTag("button_submit_practice_activity"),
                                colors = ButtonDefaults.buttonColors(containerColor = CyanAccent),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("Periksa Jawaban", fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        } else {
                            Button(
                                onClick = { practiceViewModel.nextQuestion() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .testTag("button_next_practice_activity"),
                                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    text = if (state.currentIndex >= state.questions.size - 1) "Muat Soal Acak Lagi" else "Soal Berikutnya",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }

                val activity = LocalContext.current as? Activity
                FasBrainBottomNavigation(
                    selectedTab = "start",
                    onTabSelected = { _ ->
                        activity?.finish()
                    }
                )
            }
        },
        containerColor = Color(0xFFF8FAFC)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Domain Selection Chip Row
            DomainSelectionRow(
                selectedDomainId = state.selectedDomainId,
                onDomainSelected = { domainId -> practiceViewModel.loadDomainQuestions(domainId) }
            )

            // Session Feedback Banner (In-memory stats, zero DB writes)
            SessionStatsHeader(state = state)

            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = NavyPrimary)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Memuat Soal dari Database...", color = Color(0xFF64748B))
                    }
                }
            } else if (currentQuestion != null) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item { Spacer(modifier = Modifier.height(8.dp)) }

                    // Custom Visual Views for Spatial or Ishihara if applicable
                    item {
                        when (currentQuestion.category) {
                            "buta_warna" -> IshiharaPlateView(questionId = currentQuestion.id)
                            "spasial" -> SpatialPatternView(questionId = currentQuestion.id)
                        }
                    }

                    // Question Card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = NavyPrimary.copy(alpha = 0.1f)
                                    ) {
                                        Text(
                                            text = "Soal #${state.currentIndex + 1} (${currentQuestion.category})",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = NavyPrimary,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }

                                    Text(
                                        text = "Domain: ${currentQuestion.domain.ifEmpty { state.domainTitle }}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFF64748B)
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Text(
                                    text = currentQuestion.question,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                            }
                        }
                    }

                    // Options List
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            val options = currentQuestion.getOptions()
                            val optionLabels = listOf("A", "B", "C", "D")

                            options.forEachIndexed { index, optionText ->
                                val isSelected = state.selectedOptionIndex == index
                                val isCorrectOption = currentQuestion.correctIndex == index

                                val cardBg = when {
                                    !state.isAnswerSubmitted -> {
                                        if (isSelected) NavyPrimary else Color.White
                                    }
                                    isCorrectOption -> ScoreGreen.copy(alpha = 0.15f)
                                    isSelected && !isCorrectOption -> ScoreRed.copy(alpha = 0.15f)
                                    else -> Color.White
                                }

                                val borderColor = when {
                                    !state.isAnswerSubmitted -> {
                                        if (isSelected) CyanAccent else Color(0xFFE2E8F0)
                                    }
                                    isCorrectOption -> ScoreGreen
                                    isSelected && !isCorrectOption -> ScoreRed
                                    else -> Color(0xFFE2E8F0)
                                }

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .border(2.dp, borderColor, RoundedCornerShape(16.dp))
                                        .clickable {
                                            if (!state.isAnswerSubmitted) {
                                                practiceViewModel.selectOption(index)
                                            }
                                        }
                                        .testTag("practice_activity_option_$index"),
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
                                                        state.isAnswerSubmitted && isCorrectOption -> ScoreGreen
                                                        state.isAnswerSubmitted && isSelected && !isCorrectOption -> ScoreRed
                                                        isSelected -> CyanAccent
                                                        else -> Color(0xFFF1F5F9)
                                                    }
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = optionLabels.getOrElse(index) { "?" },
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected || (state.isAnswerSubmitted && (isCorrectOption || isSelected))) Color.White else NavyPrimary
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(16.dp))

                                        Text(
                                            text = optionText,
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = if (isSelected || isCorrectOption) FontWeight.Bold else FontWeight.Normal,
                                            color = if (!state.isAnswerSubmitted && isSelected) Color.White else Color(0xFF0F172A),
                                            modifier = Modifier.weight(1f)
                                        )

                                        if (state.isAnswerSubmitted) {
                                            if (isCorrectOption) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Benar",
                                                    tint = ScoreGreen
                                                )
                                            } else if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = "Salah",
                                                    tint = ScoreRed
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Explanation Card after submission
                    if (state.isAnswerSubmitted && currentQuestion.explanation.isNotEmpty()) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F9FF)),
                                shape = RoundedCornerShape(16.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBAE6FD))
                            ) {
                                Row(modifier = Modifier.padding(16.dp)) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "Pembahasan",
                                        tint = Color(0xFF0284C7),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Pembahasan Jawaban:",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0369A1)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = currentQuestion.explanation,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color(0xFF334155)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(24.dp)) }
                }
            }
        }
    }
}

@Composable
private fun DomainSelectionRow(
    selectedDomainId: String,
    onDomainSelected: (String) -> Unit
) {
    val domains = rememberDomainList()

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(domains) { categoryInfo ->
            val isSelected = selectedDomainId == categoryInfo.id
            FilterChip(
                selected = isSelected,
                onClick = { onDomainSelected(categoryInfo.id) },
                label = {
                    Text(
                        text = categoryInfo.title,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 12.sp
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = NavyPrimary,
                    selectedLabelColor = Color.White,
                    containerColor = Color(0xFFF1F5F9),
                    labelColor = Color(0xFF334155)
                ),
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

@Composable
private fun SessionStatsHeader(state: PracticeState) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFE2E8F0).copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = null,
                    tint = NavyPrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = state.domainTitle,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
            }

            Text(
                text = "Benar ${state.sessionCorrectCount} / ${state.sessionTotalAnswered} Sesi Ini",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = ScoreGreen
            )
        }
    }
}

@Composable
private fun rememberDomainList() = androidx.compose.runtime.remember {
    TestCategories.ALL
}
