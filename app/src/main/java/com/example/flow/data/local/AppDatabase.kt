package com.example.flow.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Room database for the Flow app
 * Version 1 - Initial schema
 */
@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}

