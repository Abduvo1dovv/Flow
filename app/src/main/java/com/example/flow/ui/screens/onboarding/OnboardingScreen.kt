package com.example.flow.ui.screens.onboarding

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.flow.ui.theme.FlowTheme

@Composable
fun OnboardingScreen(
    onNavigateToHome: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F2EB))
    ) {
        // The "Aura" - Background Glow
        AuraGlow(
            modifier = Modifier
                .align(Alignment.Center)
                .size(400.dp)
        )
        
        // Typography - Foreground
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to Flow",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 32.sp,
                color = Color(0xFF1A1C19),
                letterSpacing = (-1).sp,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Find your focus, gently.",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = Color(0xFF1A1C19).copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }
        
        // The Button - Bottom
        Button(
            onClick = {
                viewModel.completeOnboarding() // BAZAGA YOZILDI
                onNavigateToHome()
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 48.dp)
                .height(56.dp)
                .shadow(
                    elevation = 8.dp,
                    spotColor = Color(0xFF2D4A3E).copy(alpha = 0.3f),
                    shape = RoundedCornerShape(28.dp)
                ),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2D4A3E),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Begin Journey",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
    }
}

/**
 * Breathing aura glow effect
 * Uses Canvas to draw a radial gradient with animated scale and alpha
 */
@Composable
fun AuraGlow(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "aura_animation")
    
    // Scale animation: oscillates between 0.8f and 1.2f (slowly, 4000ms)
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    // Alpha animation: oscillates between 0.5f and 0.8f
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    
    Canvas(modifier = modifier) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val radius = (size.minDimension / 2) * scale
        
        // Create radial gradient from Bright Lime/Mint to Transparent
        val gradient = Brush.radialGradient(
            colors = listOf(
                Color(0xFFB8E6A8).copy(alpha = 0.6f * alpha),
                Color.Transparent
            ),
            center = Offset(centerX, centerY),
            radius = radius
        )
        
        // Draw the breathing aura circle
        drawCircle(
            brush = gradient,
            radius = radius,
            center = Offset(centerX, centerY)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    FlowTheme {
        OnboardingScreen(
            onNavigateToHome = {}
        )
    }
}
