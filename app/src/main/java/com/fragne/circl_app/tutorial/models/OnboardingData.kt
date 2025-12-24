package com.fragne.circl_app.tutorial.models

/**
 * Onboarding Data Model
 * Captures user's responses from the onboarding flow
 */
data class OnboardingData(
    val usageInterests: String = "",
    val industryInterests: String = "",
    val location: String = "",
    val userGoals: String? = null
)

