package com.example.flow.ui.screens.focusmode

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.example.flow.data.local.TaskDao
import com.example.flow.ui.theme.BrightGreen
import com.example.flow.ui.theme.FlowTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random

// --- MANTIQ: Zarrachalar (Particles) ---
data class Particle(
    val id: Int,
    var x: Float,
    var y: Float,
    val color: Color,
    val angle: Double,
    val speed: Float,
    var alpha: Float
)

@Composable
fun FocusModeScreen(
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: FocusViewModel = hiltViewModel()
) {
    val currentTask by viewModel.currentTask.collectAsState()
    val timerText by viewModel.timerText.collectAsState()
    val haptic = LocalHapticFeedback.current

    // --- STATE ---
    var isCompleted by remember { mutableStateOf(false) }
    val particles = remember { mutableStateListOf<Particle>() }
    // MUHIM: Ekranni majburiy yangilash uchun vaqt hisoblagich
    var frameTime by remember { mutableLongStateOf(0L) }

    val density = LocalDensity.current

    // UI Fade Out
    val contentAlpha by animateFloatAsState(
        targetValue = if (isCompleted) 0f else 1f,
        animationSpec = tween(500),
        label = "contentFade"
    )

    // Task Fade In
    val taskAlpha = remember { Animatable(0f) }
    LaunchedEffect(currentTask) {
        if (currentTask != null) {
            taskAlpha.animateTo(1f, animationSpec = tween(800))
        }
    }

    // --- LOGIKA: Portlash va O'yin sikli ---
    LaunchedEffect(isCompleted) {
        if (isCompleted) {
            // 1. Zarrachalarni yaratish
            repeat(100) { id ->
                // Burchakni biroz yuqoriga yo'naltiramiz (yarim doira)
                val angle = Random.nextDouble(Math.PI, Math.PI * 2)
                val speed = Random.nextFloat() * 15f + 5f
                val color = if (Random.nextBoolean()) Color.White else BrightGreen

                particles.add(
                    Particle(
                        id = id,
                        x = 0f,
                        y = 0f,
                        color = color,
                        angle = angle,
                        speed = speed,
                        alpha = 1f
                    )
                )
            }

            // 2. Harakatlantirish (Loop)
            val startTime = System.nanoTime()
            while (true) {
                val nanos = System.nanoTime() - startTime
                if (nanos > 2_000_000_000L) { // 1.5 soniya
                    currentTask?.let { task ->
                        viewModel.completeTask(task.id)
                        onNavigateToHome()
                    }
                    break
                }

                // HAR BIR KADRDA:
                withFrameNanos { time ->
                    frameTime = time // <--- BU JUDA MUHIM: Canvasni uyg'otadi

                    particles.forEach { p ->
                        p.x += (cos(p.angle) * p.speed).toFloat()
                        p.y += (sin(p.angle) * p.speed).toFloat()
                        p.alpha = (p.alpha - 0.015f).coerceAtLeast(0f)
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF3E6655),
                        Color(0xFF1A2C24)
                    ),
                    center = Offset(0.5f, 0.4f),
                    radius = 1000f
                )
            )
    ) {
        // LAYER 1: Asosiy Kontent
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .alpha(contentAlpha),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = timerText,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontFamily = FontFamily.Monospace
                ),
                color = Color.White.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = currentTask?.description ?: "No task",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Light
                ),
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(taskAlpha.value)
            )

            Spacer(modifier = Modifier.weight(1f))

            SwipeableSlider(
                isCompleted = isCompleted,
                onComplete = {
                    if (!isCompleted) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        isCompleted = true
                    }
                },
                modifier = Modifier.padding(bottom = 48.dp)
            )
        }

        // LAYER 2: Zarrachalar (Canvas)
        if (isCompleted) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // frameTime ni o'qish Canvasni qayta chizishga majbur qiladi
                val trigger = frameTime

                val canvasWidth = size.width
                val canvasHeight = size.height
                val startX = canvasWidth / 2
                val startY = canvasHeight - 200f

                particles.forEach { p ->
                    if (p.alpha > 0f) {
                        drawCircle(
                            color = p.color.copy(alpha = p.alpha),
                            radius = 8f, // Zarracha kattaligi
                            center = Offset(startX + p.x, startY + p.y)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SwipeableSlider(
    isCompleted: Boolean,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val thumbSize = 64.dp
    val trackHeight = 72.dp
    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(trackHeight)
    ) {
        val maxOffset = with(density) {
            (constraints.maxWidth - thumbSize.toPx()).coerceAtLeast(0f)
        }
        val completionThreshold = maxOffset * 0.85f

        Box(
            modifier = Modifier.fillMaxWidth().height(trackHeight),
            contentAlignment = Alignment.CenterStart
        ) {
            // Track
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(trackHeight)
                    .background(Color.Black.copy(alpha = 0.2f), RoundedCornerShape(36.dp))
                    .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)), RoundedCornerShape(36.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Slide to Melt",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }

            // Thumb
            Box(
                modifier = Modifier
                    .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                    .size(thumbSize)
                    .shadow(8.dp, CircleShape)
                    .background(Color.White, CircleShape)
                    .pointerInput(isCompleted) {
                        if (!isCompleted) {
                            detectDragGestures(
                                onDragEnd = {
                                    if (offsetX.value >= completionThreshold) {
                                        onComplete()
                                    } else {
                                        scope.launch {
                                            offsetX.animateTo(0f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                                        }
                                    }
                                }
                            ) { change, dragAmount ->
                                val newOffset = (offsetX.value + dragAmount.x).coerceIn(0f, maxOffset)
                                scope.launch { offsetX.snapTo(newOffset) }
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Complete",
                    tint = BrightGreen,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@HiltViewModel
class FocusViewModel @Inject constructor(
    private val taskDao: TaskDao
) : androidx.lifecycle.ViewModel() {

    val currentTask = taskDao.getCurrentTask()
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _timerText = MutableStateFlow("00:00")
    val timerText: StateFlow<String> = _timerText.asStateFlow()

    private var startTime = System.currentTimeMillis()

    init {
        startTime = System.currentTimeMillis()
        viewModelScope.launch {
            while (true) {
                val elapsed = (System.currentTimeMillis() - startTime) / 1000
                val minutes = (elapsed / 60).toInt()
                val seconds = (elapsed % 60).toInt()
                _timerText.value = String.format("%02d:%02d", minutes, seconds)
                delay(1000)
            }
        }
    }

    fun completeTask(taskId: Int) {
        viewModelScope.launch {
            val timestamp = Date().time
            taskDao.completeTask(taskId, timestamp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FocusModeScreenPreview() {
    FlowTheme {
        FocusModeScreen(
            onNavigateBack = {},
            onNavigateToHome = {}
        )
    }
}