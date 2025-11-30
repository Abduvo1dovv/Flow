package com.example.flow.ui.screens.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.flow.ui.theme.DarkGrey
import com.example.flow.ui.theme.ForestGreen
import com.example.flow.ui.theme.WarmSand

@Composable
fun StatsScreen(
    viewModel: StatsViewModel = hiltViewModel()
) {
    val state by viewModel.statsState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmSand)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // HEADER
            Text(
                text = "YOUR MOMENTUM",
                style = MaterialTheme.typography.labelMedium,
                color = DarkGrey.copy(alpha = 0.5f),
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // BIG STREAK NUMBER
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${state.currentStreak}",
                    fontSize = 96.sp,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreen,
                    letterSpacing = (-4).sp
                )
                Text(
                    text = "Day Streak 🔥",
                    style = MaterialTheme.typography.titleLarge,
                    color = DarkGrey
                )
            }

            Spacer(modifier = Modifier.height(56.dp))

            // WEEKLY ACTIVITY CHART (Custom)
            Text(
                text = "Last 7 Days",
                style = MaterialTheme.typography.labelMedium,
                color = DarkGrey.copy(alpha = 0.5f),
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                state.weeklyActivity.forEach { day ->
                    DayBar(day = day)
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // EXTRA STATS
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                StatItem(
                    count = "${state.totalTasks}",
                    label = "Total Melted"
                )

                // Flow Score (Gamification: Har bir task = 10 ball)
                StatItem(
                    count = "${state.totalTasks * 10}",
                    label = "Flow Score"
                )
            }
        }
    }
}

@Composable
fun DayBar(day: DailyActivity) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        // Bar Height calculation (Max 100.dp, Min 8.dp)
        // Agar 0 bo'lsa ham kichkina nuqta ko'rinsin
        val barHeight = if (day.count > 0) (day.count * 20).dp.coerceAtMost(100.dp) else 8.dp

        val barColor = if (day.count > 0) ForestGreen else DarkGrey.copy(alpha = 0.1f)

        // The Bar
        Box(
            modifier = Modifier
                .width(32.dp)
                .height(barHeight)
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .background(barColor)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Day Name (M, T, W...)
        Text(
            text = day.dayName,
            style = MaterialTheme.typography.labelMedium,
            color = if (day.isToday) ForestGreen else DarkGrey.copy(alpha = 0.5f),
            fontWeight = if (day.isToday) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun StatItem(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = DarkGrey
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = DarkGrey.copy(alpha = 0.5f)
        )
    }
}