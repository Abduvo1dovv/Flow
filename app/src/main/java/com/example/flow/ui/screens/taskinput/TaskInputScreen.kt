package com.example.flow.ui.screens.taskinput

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.example.flow.data.local.TaskDao
import com.example.flow.data.local.TaskEntity
import com.example.flow.ui.theme.DarkGrey
import com.example.flow.ui.theme.ForestGreen
import com.example.flow.ui.theme.FlowTheme
import com.example.flow.ui.theme.WarmSand
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@Composable
fun TaskInputScreen(
    onNavigateBack: () -> Unit,
    onNavigateToFocusMode: () -> Unit,
    viewModel: TaskInputViewModel = hiltViewModel()
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var taskText by remember { mutableStateOf("") }
    
    // Auto-focus and open keyboard when screen opens
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmSand)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Close Button (Top-right)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Close",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable(onClick = onNavigateBack),
                    tint = DarkGrey
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Input Field (Center)
            BasicTextField(
                value = taskText,
                onValueChange = { taskText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                textStyle = TextStyle(
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                    color = DarkGrey,
                    fontFamily = MaterialTheme.typography.headlineMedium.fontFamily,
                    fontWeight = MaterialTheme.typography.headlineMedium.fontWeight
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (taskText.isNotBlank()) {
                            viewModel.saveTask(taskText.trim())
                            onNavigateToFocusMode()
                        }
                    }
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (taskText.isEmpty()) {
                            Text(
                                text = "Just one small step...",
                                style = MaterialTheme.typography.headlineMedium,
                                color = DarkGrey.copy(alpha = 0.4f)
                            )
                        }
                        innerTextField()
                    }
                }
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Suggestion Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SuggestionChip(
                    text = "Read 5 pages",
                    onClick = { taskText = "Read 5 pages" }
                )
                SuggestionChip(
                    text = "Drink water",
                    onClick = { taskText = "Drink water" }
                )
                SuggestionChip(
                    text = "Reply to email",
                    onClick = { taskText = "Reply to email" }
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Start Button (Bottom)
            Button(
                onClick = {
                    if (taskText.isNotBlank()) {
                        viewModel.saveTask(taskText.trim())
                        onNavigateToFocusMode()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = taskText.isNotBlank(),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ForestGreen,
                    contentColor = Color.White,
                    disabledContainerColor = ForestGreen.copy(alpha = 0.5f),
                    disabledContentColor = Color.White.copy(alpha = 0.5f)
                )
            ) {
                Text(
                    text = "Melt This Task",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun SuggestionChip(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = Color.White.copy(alpha = 0.6f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = DarkGrey
        )
    }
}

@HiltViewModel
class TaskInputViewModel @Inject constructor(
    private val taskDao: TaskDao
) : androidx.lifecycle.ViewModel() {
    
    fun saveTask(text: String) {
        viewModelScope.launch {
            val task = TaskEntity(
                description = text,
                createdAt = Date().time,
                isCompleted = false,
                completedAt = null
            )
            taskDao.insertTask(task)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskInputScreenPreview() {
    FlowTheme {
        TaskInputScreen(
            onNavigateBack = {},
            onNavigateToFocusMode = {}
        )
    }
}
