package com.example.flow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.flow.data.local.UserPreferences
import com.example.flow.navigation.NavGraph
import com.example.flow.navigation.Routes
import com.example.flow.ui.theme.FlowTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userPreferences: UserPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            FlowTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    // Onboarding holatini o'qish
                    // DIQQAT: Bu yerda oddiy yechim qilyapmiz.
                    // Real appda Splash Screen ishlatiladi loading uchun.
                    val isOnboardingCompleted by userPreferences.isOnboardingCompleted
                        .collectAsState(initial = false)

                    // Agar ma'lumot yuklanib bo'lsa
                    val startDest = if (isOnboardingCompleted) Routes.HOME else Routes.ONBOARDING
                    NavGraph(navController = navController, startDestination = startDest)
                }
            }
        }
    }
}