package com.fragne.circl_app.core.data

/**
 * API Configuration for Circl App
 * Manages base URL for different environments
 */
object APIConfig {
    // Use emulator local for debug builds, production for release
    private const val EMULATOR_BASE_URL = "http://10.0.2.2:8000/api/"  // Android emulator
    private const val PRODUCTION_BASE_URL = "https://circlapp.online/api/"

    /**
     * Get the appropriate base URL based on build type
     * For debug builds on emulator: local server
     * For release builds: production server
     *
     * Note: Change to "http://127.0.0.1:8000/api/" for physical device testing
     */
    const val BASE_URL: String = EMULATOR_BASE_URL  // TODO: Switch to PRODUCTION_BASE_URL for release
}

