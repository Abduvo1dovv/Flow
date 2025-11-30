package com.example.flow.ui.screens.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.sharp.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.flow.ui.theme.DarkGrey
import com.example.flow.ui.theme.ForestGreen
import com.example.flow.ui.theme.WarmSand

@Composable
fun HomeScreen(
    onNavigateToTaskInput: () -> Unit,
    onNavigateToFocusMode: () -> Unit,
    onNavigateToStats: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    // Bazadagi holatni kuzatamiz
    val activeTask by viewModel.activeTask.collectAsState()

    Scaffold(
        containerColor = WarmSand
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Center Content
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Matn vaziyatga qarab o'zgaradi
                Text(
                    text = if (activeTask != null)
                        "Ready to get back in the flow?"
                    else
                        "What is the one thing for today?",
                    style = MaterialTheme.typography.headlineSmall,
                    color = DarkGrey,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Tugma bosilganda qayerga borishi hal qilinadi
                BreathingButton(
                    isActiveTask = activeTask != null,
                    onClick = {
                        if (activeTask != null) {
                            onNavigateToFocusMode() // Vazifa bor -> Fokusga
                        } else {
                            onNavigateToTaskInput() // Vazifa yo'q -> Kiritishga
                        }
                    }
                )

                // Agar vazifa bo'lsa, kichkina eslatma
                if (activeTask != null) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Current: ${activeTask!!.description}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DarkGrey.copy(alpha = 0.6f),
                        maxLines = 1
                    )
                }
            }

            // Floating Bottom Navigation
            FloatingBottomNav(
                currentRoute = "home",
                onNavigateToHome = { },
                onNavigateToStats = onNavigateToStats,
                onNavigateToSettings = onNavigateToSettings,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun BreathingButton(
    isActiveTask: Boolean, // Vazifa bormi?
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val buttonShape = RoundedCornerShape(40.dp)

    Box(modifier = Modifier.size(200.dp)) {
        // Shadow Layer (FIXED: using named arguments for shadow)
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(200.dp)
                .scale(scale * 0.95f)
                .shadow(
                    elevation = 12.dp,
                    shape = buttonShape,
                    spotColor = Color.Gray.copy(alpha = 0.2f)
                )
                .background(Color.Transparent, buttonShape)
        )

        // Main Button (FIXED: using named arguments for shadow)
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(180.dp)
                .scale(scale)
                .shadow(
                    elevation = 8.dp,
                    shape = buttonShape,
                    spotColor = Color.Gray.copy(alpha = 0.15f)
                )
                .background(Color(0xFFFAF8F5), buttonShape)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            // Ikonka vaziyatga qarab o'zgaradi: Plyus yoki Play
            Icon(
                imageVector = if (isActiveTask) Icons.Rounded.PlayArrow else Icons.Rounded.Add,
                contentDescription = "Action",
                tint = ForestGreen,
                modifier = Modifier.size(64.dp)
            )
        }
    }
}

@Composable
fun FloatingBottomNav(
    currentRoute: String,
    onNavigateToHome: () -> Unit,
    onNavigateToStats: () -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 48.dp)
            .padding(bottom = 32.dp)
            .height(72.dp)
            // FIXED: using named arguments for shadow
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(50),
                spotColor = Color.Gray.copy(alpha = 0.15f)
            ),
        shape = RoundedCornerShape(50),
        color = Color(0xFFF0ECE6).copy(alpha = 0.85f)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavIcon(Icons.Outlined.Home, currentRoute == "home", onNavigateToHome)
            NavIcon(Icons.Outlined.DateRange, currentRoute == "stats", onNavigateToStats)
            NavIcon(Icons.Outlined.Settings, currentRoute == "settings", onNavigateToSettings)
        }
    }
}

@Composable
fun NavIcon(icon: ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier.size(56.dp).clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(width = 48.dp, height = 40.dp)
                    .background(ForestGreen.copy(alpha = 0.15f), RoundedCornerShape(50))
            )
        }
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) ForestGreen else DarkGrey.copy(alpha = 0.5f),
            modifier = Modifier.size(28.dp)
        )
    }
}