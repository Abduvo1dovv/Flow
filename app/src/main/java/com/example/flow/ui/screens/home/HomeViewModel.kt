package com.example.flow.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flow.data.local.TaskDao
import com.example.flow.data.local.TaskEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    taskDao: TaskDao
) : ViewModel() {

    // Bazadagi eng oxirgi bajarilmagan vazifani kuzatib turadi
    val activeTask: StateFlow<TaskEntity?> = taskDao.getCurrentTask()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
}