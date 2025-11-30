package com.example.flow.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val ONBOARDING_KEY = booleanPreferencesKey("onboarding_completed")
    private val NOTIFICATIONS_KEY = booleanPreferencesKey("notifications_enabled")

    // Onboarding holatini o'qish
    val isOnboardingCompleted: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[ONBOARDING_KEY] ?: false
        }

    // Onboardingni "ko'rildi" deb belgilash
    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ONBOARDING_KEY] = completed
        }
    }
}