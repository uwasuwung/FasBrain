package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import com.example.ui.components.sharedBoundsIf
import com.example.ui.components.sharedElementIf
import com.example.ui.components.FasBrainBottomNavigation
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.TestCategories
import com.example.data.model.TestCategoryInfo
import com.example.data.model.TestResultEntity
import com.example.data.model.UserProfile
import com.example.ui.theme.BadgeNewBg
import com.example.ui.theme.BadgeNewText
import com.example.ui.theme.BadgePopularBg
import com.example.ui.theme.BadgePopularText
import com.example.ui.theme.BadgeRecommendBg
import com.example.ui.theme.BadgeRecommendText
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.CyanDark
import com.example.ui.theme.HeaderGradientEnd
import com.example.ui.theme.HeaderGradientStart
import com.example.ui.theme.QuickAccessBlueBg
import com.example.ui.theme.QuickAccessBlueIcon
import com.example.ui.theme.QuickAccessGreenBg
import com.example.ui.theme.QuickAccessGreenIcon
import com.example.ui.theme.QuickAccessPurpleBg
import com.example.ui.theme.QuickAccessPurpleIcon
import com.example.ui.theme.QuickAccessYellowBg
import com.example.ui.theme.QuickAccessYellowIcon
import com.example.ui.theme.StatsCardNavy

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeScreen(
    userProfile: UserProfile?,
    historyList: List<TestResultEntity>,
    onStartTestClicked: () -> Unit,
    onCategorySelected: (TestCategoryInfo) -> Unit,
    onPracticeClicked: () -> Unit,
    onHistoryClicked: () -> Unit,
    onAboutClicked: () -> Unit,
    onSettingsClicked: () -> Unit = {},
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    modifier: Modifier = Modifier
) {
    val highestScore = historyList.maxOfOrNull { it.score } ?: 1280
    val totalTests = if (historyList.isNotEmpty()) historyList.size else 12
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf("home") }

    Scaffold(
        modifier = modifier.testTag("home_screen"),
        containerColor = Color(0xFFF8FAFC),
        bottomBar = {
            FasBrainBottomNavigation(
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    selectedTab = tab
                    when (tab) {
                        "home" -> { /* stay on home */ }
                        "history" -> onHistoryClicked()
                        "start" -> onStartTestClicked()
                        "result" -> onHistoryClicked()
                        "profile" -> onAboutClicked()
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. Top Navy Header Section
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(HeaderGradientStart, HeaderGradientEnd)
                            )
                        )
                ) {
                    // Futuristic Brain Background Overlay
                    Image(
                        painter = painterResource(id = R.drawable.img_brain_header),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(220.dp)
                            .offset(x = 40.dp, y = (-20).dp)
                            .clip(CircleShape),
                        alpha = 0.4f
                    )

                    Column(
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 28.dp)
                    ) {
                        // Top Bar: Logo + Brand Title + Notification Bell
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFF1E2B6D)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Psychology,
                                        contentDescription = "Logo",
                                        tint = CyanAccent,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "FasBrain",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Tes IQ & Psikotes",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF94A3B8)
                                    )
                                }
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Notification Bell with Badge
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.1f))
                                        .clickable { onSettingsClicked() },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Notifications,
                                        contentDescription = "Notifikasi",
                                        tint = Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFEF4444))
                                            .align(Alignment.TopEnd)
                                            .offset(x = (-2).dp, y = 2.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "3",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                // Settings Button
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.1f))
                                        .clickable { onSettingsClicked() }
                                        .testTag("home_settings_button"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Settings,
                                        contentDescription = "Pengaturan",
                                        tint = Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Greeting
                        Text(
                            text = "Hai, ${userProfile?.name ?: "Andi"} 👋",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.sharedBoundsIf(
                                key = "user_profile_name",
                                sharedTransitionScope = sharedTransitionScope,
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Kenali potensi terbaikmu hari ini!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFCBD5E1)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Search Bar
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .shadow(8.dp, shape = RoundedCornerShape(26.dp)),
                            shape = RoundedCornerShape(26.dp),
                            color = Color.White
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Search,
                                    contentDescription = "Cari",
                                    tint = Color(0xFF94A3B8),
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = if (searchQuery.isEmpty()) "Cari tes atau kategori..." else searchQuery,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF94A3B8)
                                )
                            }
                        }
                    }
                }
            }

            // 2. Hero Banner Card
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .sharedElementIf(
                                key = "category_card_all",
                                sharedTransitionScope = sharedTransitionScope,
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                            .clickable { onStartTestClicked() },
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(Color(0xFF0F1B52), Color(0xFF1E2B70))
                                    )
                                )
                                .padding(20.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1.2f)) {
                                    Text(
                                        text = buildAnnotatedString {
                                            append("Ukur Potensi, Raih\nMasa Depan ")
                                            withStyle(SpanStyle(color = CyanAccent, fontWeight = FontWeight.Bold)) {
                                                append("Cerah")
                                            }
                                        },
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        lineHeight = 24.sp
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "Tes IQ & Psikotes terpercaya\nuntuk masa depan yang lebih baik",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFFCBD5E1),
                                        fontSize = 11.sp,
                                        lineHeight = 16.sp
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Button: MULAI TES SEKARANG
                                    Surface(
                                        shape = RoundedCornerShape(20.dp),
                                        color = CyanDark,
                                        modifier = Modifier.clickable { onStartTestClicked() }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "MULAI TES SEKARANG",
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                fontSize = 11.sp
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Icon(
                                                imageVector = Icons.Default.ArrowForward,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                // Right Illustration Graphic
                                Image(
                                    painter = painterResource(id = R.drawable.img_trophy_banner),
                                    contentDescription = "Trophy",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(115.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Indicator Dots
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(20.dp)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(CyanDark)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFCBD5E1))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFCBD5E1))
                        )
                    }
                }
            }

            // 3. Akses Cepat (Quick Access)
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Akses Cepat",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "Lihat Semua",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF0284C7),
                            modifier = Modifier.clickable { onPracticeClicked() }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Item 1: Tes IQ
                        QuickAccessItem(
                            title = "Tes IQ",
                            subtitle = "Inteligensi",
                            bgColor = QuickAccessPurpleBg,
                            iconColor = QuickAccessPurpleIcon,
                            icon = Icons.Default.Psychology,
                            onClick = { onCategorySelected(TestCategories.getById("iq")) },
                            categoryId = "iq",
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope
                        )

                        // Item 2: Psikotes
                        QuickAccessItem(
                            title = "Psikotes",
                            subtitle = "Kepribadian",
                            bgColor = QuickAccessBlueBg,
                            iconColor = QuickAccessBlueIcon,
                            icon = Icons.Default.Person,
                            onClick = { onCategorySelected(TestCategories.getById("verbal")) },
                            categoryId = "verbal",
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope
                        )

                        // Item 3: Tes Minat Bakat
                        QuickAccessItem(
                            title = "Tes Minat Bakat",
                            subtitle = "Potensi Diri",
                            bgColor = QuickAccessGreenBg,
                            iconColor = QuickAccessGreenIcon,
                            icon = Icons.Default.Work,
                            onClick = { onCategorySelected(TestCategories.getById("deret_angka")) },
                            categoryId = "deret_angka",
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope
                        )

                        // Item 4: Semua Tes
                        QuickAccessItem(
                            title = "Semua Tes",
                            subtitle = "Lihat Semua",
                            bgColor = QuickAccessYellowBg,
                            iconColor = QuickAccessYellowIcon,
                            icon = Icons.Default.Description,
                            onClick = { onPracticeClicked() },
                            categoryId = "all",
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    }
                }
            }

            // 4. Rekomendasi Tes (Recommended Tests)
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Rekomendasi Tes",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "Lihat Semua",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF0284C7),
                            modifier = Modifier.clickable { onPracticeClicked() }
                        )
                    }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp)
                    ) {
                        item {
                            RecommendedTestCard(
                                title = "Tes IQ Standar",
                                duration = "25 Menit",
                                participants = "128K peserta",
                                badgeText = "Populer",
                                badgeBg = BadgePopularBg,
                                badgeTextColor = BadgePopularText,
                                iconBgColor = Color(0xFF2563EB),
                                onClick = { onCategorySelected(TestCategories.getById("iq")) },
                                categoryId = "iq",
                                sharedTransitionScope = sharedTransitionScope,
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                        }
                        item {
                            RecommendedTestCard(
                                title = "Psikotes Kepribadian",
                                duration = "30 Menit",
                                participants = "98K peserta",
                                badgeText = "Populer",
                                badgeBg = BadgePopularBg,
                                badgeTextColor = BadgePopularText,
                                iconBgColor = Color(0xFF0D9488),
                                onClick = { onCategorySelected(TestCategories.getById("verbal")) },
                                categoryId = "verbal",
                                sharedTransitionScope = sharedTransitionScope,
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                        }
                        item {
                            RecommendedTestCard(
                                title = "Tes Minat Bakat",
                                duration = "20 Menit",
                                participants = "75K peserta",
                                badgeText = "Direkomendasikan",
                                badgeBg = BadgeRecommendBg,
                                badgeTextColor = BadgeRecommendText,
                                iconBgColor = Color(0xFFEA580C),
                                onClick = { onCategorySelected(TestCategories.getById("deret_angka")) },
                                categoryId = "deret_angka",
                                sharedTransitionScope = sharedTransitionScope,
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                        }
                        item {
                            RecommendedTestCard(
                                title = "Tes Kerja",
                                duration = "35 Menit",
                                participants = "64K peserta",
                                badgeText = "Baru",
                                badgeBg = BadgeNewBg,
                                badgeTextColor = BadgeNewText,
                                iconBgColor = Color(0xFF7C3AED),
                                onClick = { onCategorySelected(TestCategories.getById("spasial")) },
                                categoryId = "spasial",
                                sharedTransitionScope = sharedTransitionScope,
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                        }
                    }
                }
            }

            // 5. Statistikmu (Your Statistics)
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .sharedElementIf(
                                key = "user_stats_card",
                                sharedTransitionScope = sharedTransitionScope,
                                animatedVisibilityScope = animatedVisibilityScope
                            ),
                        colors = CardDefaults.cardColors(containerColor = StatsCardNavy),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Statistikmu",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Lihat Detail",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = CyanAccent,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.clickable { onHistoryClicked() }
                                )
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Stat 1: Tes Selesai
                                StatItem(
                                    icon = Icons.Outlined.Assignment,
                                    value = totalTests.toString(),
                                    label = "Tes Selesai"
                                )

                                StatDivider()

                                // Stat 2: Skor Tertinggi
                                StatItem(
                                    icon = Icons.Outlined.EmojiEvents,
                                    value = highestScore.toString(),
                                    label = "Skor Tertinggi"
                                )

                                StatDivider()

                                // Stat 3: Kategori Dikuasai
                                StatItem(
                                    icon = Icons.Outlined.TrackChanges,
                                    value = "5",
                                    label = "Kategori Dikuasai"
                                )

                                StatDivider()

                                // Stat 4: Hari Aktif
                                StatItem(
                                    icon = Icons.Outlined.CalendarToday,
                                    value = "15",
                                    label = "Hari Aktif"
                                )
                            }
                        }
                    }
                }
            }

            // 6. Motivational Quote Banner Card
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "“",
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = Color(0xFF93C5FD),
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 20.sp
                                )
                                Text(
                                    text = "Kesuksesan dimulai dari mengenal diri sendiri dengan baik.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF1E293B)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "– FasBrain",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color(0xFF64748B)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Image(
                                painter = painterResource(id = R.drawable.img_mountain_quote),
                                contentDescription = "Quote Mountain",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(RoundedCornerShape(14.dp))
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun QuickAccessItem(
    title: String,
    subtitle: String,
    bgColor: Color,
    iconColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    categoryId: String = "",
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(76.dp)
            .sharedElementIf(
                key = "category_card_$categoryId",
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope
            )
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(bgColor)
                .sharedElementIf(
                    key = "category_icon_$categoryId",
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(26.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A),
            fontSize = 11.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.sharedBoundsIf(
                key = "category_title_$categoryId",
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope
            )
        )

        Text(
            text = subtitle,
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF64748B),
            fontSize = 9.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun RecommendedTestCard(
    title: String,
    duration: String,
    participants: String,
    badgeText: String,
    badgeBg: Color,
    badgeTextColor: Color,
    iconBgColor: Color,
    onClick: () -> Unit,
    categoryId: String = "",
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null
) {
    Card(
        modifier = Modifier
            .width(170.dp)
            .clip(RoundedCornerShape(16.dp))
            .sharedElementIf(
                key = "category_card_$categoryId",
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope
            )
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBgColor)
                    .sharedElementIf(
                        key = "category_icon_$categoryId",
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.sharedBoundsIf(
                    key = "category_title_$categoryId",
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope
                )
            )

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Timer,
                        contentDescription = null,
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = duration,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF64748B),
                        fontSize = 10.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = participants,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF64748B),
                        fontSize = 10.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(badgeBg)
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = badgeText,
                    style = MaterialTheme.typography.labelSmall,
                    color = badgeTextColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.sp
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = CyanAccent,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF94A3B8),
            fontSize = 9.sp
        )
    }
}

@Composable
private fun StatDivider() {
    Box(
        modifier = Modifier
            .height(36.dp)
            .width(1.dp)
            .background(Color.White.copy(alpha = 0.15f))
    )
}
