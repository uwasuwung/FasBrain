package com.example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.data.model.TestCategories
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.CategorySelectScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.InstructionScreen
import com.example.ui.screens.PracticeScreen
import com.example.ui.screens.ResultScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.TestScreen
import com.example.ui.screens.UserDashboardScreen
import com.example.ui.theme.FasBrainTheme
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = viewModel()
            val isDarkMode by viewModel.isDarkMode.collectAsState()
            val darkThemeToUse = isDarkMode ?: isSystemInDarkTheme()

            FasBrainTheme(darkTheme = darkThemeToUse) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FasBrainApp(viewModel = viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun FasBrainApp(viewModel: MainViewModel = viewModel()) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val testState by viewModel.testState.collectAsState()
    val historyList by viewModel.historyList.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    val navigateTab: (String) -> Unit = { tab ->
        when (tab) {
            "home" -> {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            }
            "history" -> {
                if (navController.currentDestination?.route != "history") {
                    navController.navigate("history")
                }
            }
            "start" -> {
                if (navController.currentDestination?.route != "category_select") {
                    navController.navigate("category_select")
                }
            }
            "result" -> {
                if (navController.currentDestination?.route != "history") {
                    navController.navigate("history")
                }
            }
            "profile" -> {
                if (navController.currentDestination?.route != "user_dashboard") {
                    navController.navigate("user_dashboard")
                }
            }
        }
    }

    SharedTransitionLayout {
        NavHost(navController = navController, startDestination = "splash") {

            composable("splash") {
                SplashScreen(
                    onSplashFinished = {
                        navController.navigate("home") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                )
            }

            composable("home") {
                HomeScreen(
                    userProfile = userProfile,
                    historyList = historyList,
                    onStartTestClicked = {
                        val fullCategory = TestCategories.getById("all")
                        viewModel.startTest(fullCategory, isPractice = false)
                        navController.navigate("instruction")
                    },
                    onCategorySelected = { category ->
                        viewModel.startTest(category, isPractice = false)
                        navController.navigate("instruction")
                    },
                    onPracticeClicked = {
                        val intent = Intent(context, PracticeActivity::class.java)
                        context.startActivity(intent)
                    },
                    onHistoryClicked = { navController.navigate("history") },
                    onAboutClicked = { navController.navigate("user_dashboard") },
                    onSettingsClicked = { navController.navigate("settings") },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable
                )
            }

            composable("user_dashboard") {
                UserDashboardScreen(
                    userProfile = userProfile,
                    historyList = historyList,
                    onUpdateProfile = { name, age -> viewModel.updateUserProfile(name, age) },
                    onStartTestClicked = {
                        val fullCategory = TestCategories.getById("all")
                        viewModel.startTest(fullCategory, isPractice = false)
                        navController.navigate("instruction")
                    },
                    onHistoryClicked = { navController.navigate("history") },
                    onBackClicked = { navController.popBackStack() },
                    onSettingsClicked = { navController.navigate("settings") },
                    onNavigateTab = navigateTab,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable
                )
            }

            composable("category_select") {
                CategorySelectScreen(
                    onCategoryChosen = { category ->
                        viewModel.startTest(category, isPractice = false)
                        navController.navigate("instruction")
                    },
                    onBackClicked = { navController.popBackStack() },
                    onNavigateTab = navigateTab,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable
                )
            }

            composable("instruction") {
                val categoryInfo = TestCategories.getById(testState.categoryId)
                InstructionScreen(
                    categoryInfo = categoryInfo,
                    onStartTest = {
                        navController.navigate("test") {
                            popUpTo("instruction") { inclusive = true }
                        }
                    },
                    onBackClicked = { navController.popBackStack() },
                    onNavigateTab = navigateTab,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable
                )
            }

            composable("test") {
                TestScreen(
                    testState = testState,
                    onOptionSelected = { index -> viewModel.selectOption(index) },
                    onNextClicked = {
                        val total = testState.questions.size
                        if (testState.currentIndex + 1 >= total) {
                            navController.navigate("result") {
                                popUpTo("test") { inclusive = true }
                            }
                        } else {
                            viewModel.nextQuestion()
                        }
                    },
                    onNavigateTab = navigateTab,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable
                )
            }

            composable("result") {
                ResultScreen(
                    testState = testState,
                    userProfile = userProfile,
                    onHomeClicked = {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    onNavigateTab = navigateTab,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable
                )
            }

            composable("history") {
                HistoryScreen(
                    historyList = historyList,
                    onDeleteResult = { id -> viewModel.deleteHistoryItem(id) },
                    onClearHistory = { viewModel.clearHistory() },
                    onBackClicked = { navController.popBackStack() },
                    onNavigateTab = navigateTab
                )
            }

            composable("practice") {
                PracticeScreen(
                    testState = testState,
                    onOptionSelected = { index -> viewModel.selectOption(index) },
                    onSubmitAnswer = { viewModel.submitPracticeAnswer() },
                    onNextClicked = {
                        val total = testState.questions.size
                        if (testState.currentIndex + 1 >= total) {
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        } else {
                            viewModel.nextQuestion()
                        }
                    },
                    onBackClicked = { navController.popBackStack() },
                    onNavigateTab = navigateTab,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable
                )
            }

            composable("about") {
                AboutScreen(
                    userProfile = userProfile,
                    onSaveProfile = { name, age -> viewModel.updateUserProfile(name, age) },
                    onBackClicked = { navController.popBackStack() },
                    onNavigateTab = navigateTab
                )
            }

            composable("settings") {
                val isDarkMode by viewModel.isDarkMode.collectAsState()
                val isNotificationsEnabled by viewModel.notificationsEnabled.collectAsState()
                val isDailyReminderEnabled by viewModel.dailyReminderEnabled.collectAsState()
                val isMilestoneEnabled by viewModel.milestoneNotificationEnabled.collectAsState()

                SettingsScreen(
                    userProfile = userProfile,
                    historyList = historyList,
                    isDarkMode = isDarkMode,
                    isNotificationsEnabled = isNotificationsEnabled,
                    isDailyReminderEnabled = isDailyReminderEnabled,
                    isMilestoneNotificationEnabled = isMilestoneEnabled,
                    onSetDarkMode = { viewModel.setDarkMode(it) },
                    onSetNotificationsEnabled = { viewModel.setNotificationsEnabled(it) },
                    onSetDailyReminderEnabled = { viewModel.setDailyReminderEnabled(it) },
                    onSetMilestoneNotificationEnabled = { viewModel.setMilestoneNotificationEnabled(it) },
                    onClearLocalCachedTestData = { viewModel.clearLocalCachedTestData() },
                    onNavigateToProfile = { navController.navigate("user_dashboard") },
                    onBackClicked = { navController.popBackStack() },
                    onNavigateTab = navigateTab
                )
            }
        }
    }
}
