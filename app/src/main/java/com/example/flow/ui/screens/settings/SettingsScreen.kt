package com.example.flow.ui.screens.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flow.ui.theme.DarkGrey
import com.example.flow.ui.theme.ForestGreen
import com.example.flow.ui.theme.WarmSand

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmSand)
            .padding(24.dp)
    ) {
        Column {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable(onClick = onNavigateBack),
                    tint = DarkGrey
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineMedium,
                    color = DarkGrey,
                    fontWeight = FontWeight.Bold
                )
            }

            // Settings Items
            SettingsItem(
                icon = Icons.Rounded.Notifications,
                title = "Notifications",
                subtitle = "Daily reminders (Coming Soon)",
                onClick = { /* Hozircha bo'sh */ }
            )

            SettingsItem(
                icon = Icons.Rounded.Info,
                title = "About Flow",
                subtitle = "Version 1.0.0",
                onClick = { }
            )

            SettingsItem(
                icon = Icons.Rounded.Info,
                title = "Privacy Policy",
                subtitle = "Read our terms",
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com")) // O'zgartirish kerak
                    context.startActivity(intent)
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Danger Zone
            Text(
                text = "DANGER ZONE",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Red.copy(alpha = 0.6f),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            SettingsItem(
                icon = Icons.Rounded.Delete,
                title = "Reset Progress",
                subtitle = "Delete all history & stats",
                iconColor = Color.Red,
                onClick = { /* Bazani tozalash funksiyasi kerak */ }
            )
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    iconColor: Color = ForestGreen,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = DarkGrey,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = DarkGrey.copy(alpha = 0.6f)
            )
        }

        Icon(
            imageVector = Icons.Rounded.ArrowForward,
            contentDescription = null,
            tint = DarkGrey.copy(alpha = 0.3f),
            modifier = Modifier.size(16.dp)
        )
    }
}