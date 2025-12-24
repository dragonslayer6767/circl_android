package com.fragne.circl_app

import android.app.Application
// import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for Circl App
 * Annotated with @HiltAndroidApp to enable Hilt dependency injection
 */
// @HiltAndroidApp
class CirclApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize any app-wide configurations here
    }
}

