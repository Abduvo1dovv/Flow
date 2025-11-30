package com.example.flow.ui.screens.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flow.data.local.TaskDao
import com.example.flow.data.local.TaskEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.Instant
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

// UI uchun tayyor ma'lumotlar
data class StatsState(
    val currentStreak: Int = 0,
    val totalTasks: Int = 0,
    val weeklyActivity: List<DailyActivity> = emptyList()
)

data class DailyActivity(
    val dayName: String, // "Mon", "Tue"
    val count: Int,      // Nechta vazifa bajarilgan
    val isToday: Boolean
)

@HiltViewModel
class StatsViewModel @Inject constructor(
    taskDao: TaskDao
) : ViewModel() {

    val statsState: StateFlow<StatsState> = taskDao.getCompletedTasks()
        .map { tasks -> calculateStats(tasks) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = StatsState()
        )

    private fun calculateStats(tasks: List<TaskEntity>): StatsState {
        if (tasks.isEmpty()) return StatsState()

        // 1. Total Tasks
        val total = tasks.size

        // 2. Weekly Activity (Oxirgi 7 kun)
        val zoneId = ZoneId.systemDefault()
        val today = java.time.LocalDate.now(zoneId)

        // Oxirgi 7 kunni generatsiya qilamiz
        val activityList = (6 downTo 0).map { offset ->
            val date = today.minusDays(offset.toLong())

            // Shu kunda bajarilgan vazifalar soni
            val count = tasks.count {
                val taskDate = Instant.ofEpochMilli(it.completedAt ?: 0)
                    .atZone(zoneId)
                    .toLocalDate()
                taskDate == date
            }

            DailyActivity(
                dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH).take(1), // M, T, W...
                count = count,
                isToday = offset == 0
            )
        }

        // 3. Streak Logic (Ketma-ketlik)
        // Vazifalar sanasini set qilib olamiz (takrorlanmasligi uchun)
        val activeDates = tasks.mapNotNull {
            it.completedAt?.let { ts ->
                Instant.ofEpochMilli(ts).atZone(zoneId).toLocalDate()
            }
        }.toSet()

        var streak = 0
        var checkDate = today

        // Bugun bajarganmi?
        if (activeDates.contains(today)) {
            streak++
        }

        // Orqaga qarab sanaymiz
        while (true) {
            checkDate = checkDate.minusDays(1)
            if (activeDates.contains(checkDate)) {
                streak++
            } else {
                break // Zanjir uzildi
            }
        }

        return StatsState(
            currentStreak = streak,
            totalTasks = total,
            weeklyActivity = activityList
        )
    }
}