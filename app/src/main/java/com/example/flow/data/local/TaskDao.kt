package com.example.flow.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for TaskEntity
 * Provides methods to interact with the tasks table
 */
@Dao
interface TaskDao {
    
    /**
     * Get the current active task (first incomplete task)
     * Returns Flow for reactive updates
     */
    @Query("SELECT * FROM tasks WHERE isCompleted = 0 LIMIT 1")
    fun getCurrentTask(): Flow<TaskEntity?>
    
    /**
     * Insert a new task or replace if conflict occurs
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)
    
    /**
     * Mark a task as completed with timestamp
     */
    @Query("UPDATE tasks SET isCompleted = 1, completedAt = :timestamp WHERE id = :id")
    suspend fun completeTask(id: Int, timestamp: Long)

    @Query("SELECT * FROM tasks WHERE isCompleted = 1 ORDER BY completedAt DESC")
    fun getCompletedTasks(): Flow<List<TaskEntity>>
}

