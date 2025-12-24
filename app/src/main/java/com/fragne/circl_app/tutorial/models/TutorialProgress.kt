package com.fragne.circl_app.tutorial.models

import java.util.Date

/**
 * Tutorial Progress Model
 * Tracks user's progress through a tutorial flow
 */
data class TutorialProgress(
    val userId: Int,
    val flowId: String,
    val completedSteps: Set<String> = emptySet(),
    val currentStepIndex: Int = 0,
    val isCompleted: Boolean = false,
    val lastAccessed: Date = Date(),
    val startedAt: Date = Date()
)

