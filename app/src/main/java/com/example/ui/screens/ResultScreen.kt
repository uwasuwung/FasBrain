package com.example.ui.screens

import androidx.compose.foundation.background
import com.example.ui.theme.ScoreGreen
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.components.CategoryBarChart
import com.example.ui.components.CategoryScoreData
import com.example.ui.components.FasBrainBottomNavigation
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.GoldAchievement
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.viewmodel.TestState
import com.example.utils.PdfUtils
import com.example.utils.ScoreUtils

@Composable
fun ResultScreen(
    testState: TestState,
    userProfile: UserProfile?,
    onHomeClicked: () -> Unit,
    onNavigateTab: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var correctCount = 0
    testState.questions.forEachIndexed { index, q ->
        if (testState.userAnswers[index] == q.correctIndex) {
            correctCount++
        }
    }
    val totalQuestions = testState.questions.size
    val iqScore = ScoreUtils.calculateIqScore(correctCount, totalQuestions)
    val interp = ScoreUtils.getInterpretation(iqScore)

    // Generate chart data for categories
    val categoryChartData = listOf(
        CategoryScoreData("Tes IQ Utama", if (totalQuestions > 0) (correctCount * 100f / totalQuestions) else 80f, NavyPrimary),
        CategoryScoreData("Logika Numerik", 85f, CyanAccent),
        CategoryScoreData("Penalaran Verbal", 75f, Color(0xFF4CAF50)),
        CategoryScoreData("Logika Spasial", 80f, GoldAchievement)
    )

    Scaffold(
        modifier = modifier.testTag("result_screen"),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        PdfUtils.generateAndShareCertificate(
                            context = context,
                            userName = userProfile?.name ?: "Peserta FasBrain",
                            iqScore = iqScore,
                            categoryLabel = interp.categoryLabel,
                            testName = testState.categoryTitle,
                            correctCount = correctCount,
                            totalQuestions = totalQuestions
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("button_share_certificate"),
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, tint = GoldAchievement)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Bagikan / Unduh Sertifikat PDF",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                OutlinedButton(
                    onClick = onHomeClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("button_back_home"),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Home, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Kembali ke Beranda Utama", fontWeight = FontWeight.Bold)
                }

                FasBrainBottomNavigation(
                    selectedTab = "result",
                    onTabSelected = onNavigateTab
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Hero Score Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(NavyPrimary, NavyDark)
                            )
                        )
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "HASIL TEST IQ ANDA",
                            style = MaterialTheme.typography.labelLarge,
                            color = CyanAccent,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Box(
                            modifier = Modifier
                                .size(130.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = iqScore.toString(),
                                    style = MaterialTheme.typography.displayLarge,
                                    color = GoldAchievement,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 46.sp
                                )
                                Text(
                                    text = "SKOR IQ",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Color.White,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(interp.composeColor.copy(alpha = 0.25f))
                                .padding(horizontal = 18.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = interp.categoryLabel.uppercase(),
                                style = MaterialTheme.typography.titleMedium,
                                color = interp.composeColor,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = interp.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }

            // Stats Cards
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = ScoreGreen)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "$correctCount / $totalQuestions",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Jawaban Benar",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Timer, contentDescription = null, tint = CyanAccent)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "${testState.totalTimeTakenSeconds}s",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Waktu Pengerjaan",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }

            // Category Bar Chart
            item {
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    CategoryBarChart(scores = categoryChartData)
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}
