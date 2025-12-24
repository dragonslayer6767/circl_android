package com.fragne.circl_app.tutorial.models

/**
 * Tutorial State
 * Represents the current state of the tutorial
 */
sealed class TutorialState {
    object NotStarted : TutorialState()
    data class InProgress(val stepIndex: Int) : TutorialState()
    object Completed : TutorialState()
    object Skipped : TutorialState()
}

