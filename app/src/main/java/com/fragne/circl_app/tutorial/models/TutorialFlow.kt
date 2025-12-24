package com.fragne.circl_app.tutorial.models

import java.util.UUID

/**
 * Tutorial Flow Model
 * Represents a complete tutorial flow for a specific user type
 */
data class TutorialFlow(
    val id: String = UUID.randomUUID().toString(),
    val userType: UserType,
    val title: String,
    val description: String,
    val steps: List<TutorialStep>,
    val estimatedDuration: Long, // Duration in milliseconds
    val isRequired: Boolean = true // Some tutorials might be optional
) {
    /**
     * Calculate progress based on completed steps
     */
    fun calculateProgress(completedSteps: Set<String>): Float {
        if (steps.isEmpty()) return 0f
        val completed = steps.count { it.id in completedSteps }
        return completed.toFloat() / steps.size.toFloat()
    }
}

