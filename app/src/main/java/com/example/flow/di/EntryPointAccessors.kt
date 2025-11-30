package com.example.flow.di

import android.content.Context
import com.example.flow.data.local.UserPreferences
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface UserPreferencesEntryPoint {
    fun userPreferences(): UserPreferences
}

object EntryPointAccessors {
    fun getUserPreferences(context: Context): UserPreferences {
        val appContext = context.applicationContext ?: throw IllegalStateException()
        val entryPoint = EntryPointAccessors.fromApplication(
            appContext,
            UserPreferencesEntryPoint::class.java
        )
        return entryPoint.userPreferences()
    }
}