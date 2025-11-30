package com.example.flow.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.flow.di.EntryPointAccessors
import com.example.flow.ui.screens.focusmode.FocusModeScreen
import com.example.flow.ui.screens.home.HomeScreen
import com.example.flow.ui.screens.onboarding.OnboardingScreen
import com.example.flow.ui.screens.settings.SettingsScreen
import com.example.flow.ui.screens.splash.SplashScreen
import com.example.flow.ui.screens.stats.StatsScreen
import com.example.flow.ui.screens.taskinput.TaskInputScreen

/**
 * Navigation routes for the Flow app
 */
object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val TASK_INPUT = "task_input"
    const val FOCUS_MODE = "focus_mode"
    const val SETTINGS = "settings"
    const val STATS = "stats"
}

/**
 * Main navigation graph for the Flow app
 * Start destination: SPLASH (Always start here to check user state)
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Routes.SPLASH
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // 1. SPLASH SCREEN (Kirish eshigi)
        composable(Routes.SPLASH) {
            // Hilt EntryPoint orqali UserPreferences ni olamiz
            val context = LocalContext.current
            val userPreferences = EntryPointAccessors.getUserPreferences(context)

            SplashScreen(
                userPreferences = userPreferences,
                onNavigateToHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true } // Splashni tarixda qoldirmaymiz
                    }
                },
                onNavigateToOnboarding = {
                    navController.navigate(Routes.ONBOARDING) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        // 2. ONBOARDING (Birinchi marta kirganda)
        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onNavigateToHome = {
                    navController.navigate(Routes.HOME) {
                        // Onboardingdan o'tgach, ortga qaytib bo'lmasligi kerak
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

        // 3. HOME SCREEN (Markaz)
        composable(Routes.HOME) {
            HomeScreen(
                onNavigateToTaskInput = { navController.navigate(Routes.TASK_INPUT) },
                onNavigateToFocusMode = { navController.navigate(Routes.FOCUS_MODE) },
                onNavigateToStats = { navController.navigate(Routes.STATS) },
                onNavigateToSettings = { navController.navigate(Routes.SETTINGS) }
            )
        }

        // 4. TASK INPUT (Vazifa kiritish)
        composable(Routes.TASK_INPUT) {
            TaskInputScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToFocusMode = {
                    navController.navigate(Routes.FOCUS_MODE) {
                        // Input ekranini tarixdan o'chiramiz, toki ortga qaytganda yana input chiqmasin
                        popUpTo(Routes.TASK_INPUT) { inclusive = true }
                    }
                }
            )
        }

        // 5. FOCUS MODE (Ish jarayoni)
        composable(Routes.FOCUS_MODE) {
            FocusModeScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate(Routes.HOME) {
                        // Vazifa tugagach, to'g'ri Home ga qaytamiz va Fokusni yopamiz
                        popUpTo(Routes.FOCUS_MODE) { inclusive = true }
                    }
                }
            )
        }

        // 6. SETTINGS
        composable(Routes.SETTINGS) {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // 7. STATS (Statistika)
        composable(Routes.STATS) {
            StatsScreen()
        }
    }
}