package com.example.flow

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for Flow app
 * Required for Hilt dependency injection
 */
@HiltAndroidApp
class FlowApplication : Application()

