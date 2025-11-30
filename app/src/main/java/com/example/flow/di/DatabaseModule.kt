package com.example.flow.di

import android.content.Context
import androidx.room.Room
import com.example.flow.data.local.AppDatabase
import com.example.flow.data.local.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing database dependencies
 * Uses @Singleton to ensure only one database instance exists (critical for performance)
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    /**
     * Provides the AppDatabase instance
     * Singleton ensures only one database instance exists across the app
     */
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "flow_database"
        ).build()
    }
    
    /**
     * Provides the TaskDao instance
     * Retrieved from the database instance
     */
    @Provides
    fun provideTaskDao(database: AppDatabase): TaskDao {
        return database.taskDao()
    }
}

