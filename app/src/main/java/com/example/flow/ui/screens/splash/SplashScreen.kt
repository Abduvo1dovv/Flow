package com.example.flow.ui.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.flow.R
import com.example.flow.data.local.UserPreferences
import com.example.flow.ui.theme.WarmSand
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

@Composable
fun SplashScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    userPreferences: UserPreferences // NavGraphdan keladi
) {
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        // 1. Animatsiya (Sekin paydo bo'lish)
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )

        // 2. Biroz kutish (Logo ko'rinishi uchun)
        delay(500)

        // 3. Tekshirish: Oldin kirganmi?
        // .first() oqimdan birinchi qiymatni oladi
        val isCompleted = userPreferences.isOnboardingCompleted.first()

        if (isCompleted) {
            onNavigateToHome()
        } else {
            onNavigateToOnboarding()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmSand),
        contentAlignment = Alignment.Center
    ) {
        // Logo (Launcher icon ishlatyapmiz hozircha)
        Image(
            painter = painterResource(id = R.mipmap.ic_launcher), // Yoki o'zingni logong
            contentDescription = "Logo",
            modifier = Modifier
                .size(120.dp)
                .alpha(alpha.value)
        )
    }
}