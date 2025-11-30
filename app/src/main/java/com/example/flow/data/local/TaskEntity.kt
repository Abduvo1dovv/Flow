package com.example.flow.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Task entity for Room database
 * Represents a single task in the Flow app
 */
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val description: String,
    val createdAt: Long,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null
)

