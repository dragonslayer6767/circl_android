package com.fragne.circl_app.tutorial.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex
import com.fragne.circl_app.tutorial.TutorialManager

/**
 * Tutorial Overlay
 * Main overlay that appears during tutorial
 */
@Composable
fun TutorialOverlay(
    tutorialManager: TutorialManager,
    modifier: Modifier = Modifier
) {
    val isShowingTutorial by tutorialManager.isShowingTutorial.collectAsState()
    val currentFlow by tutorialManager.currentFlow.collectAsState()
    val currentStepIndex by tutorialManager.currentStepIndex.collectAsState()

    val currentStep = currentFlow?.steps?.getOrNull(currentStepIndex)

    // Debug logging
    LaunchedEffect(isShowingTutorial, currentStep) {
        android.util.Log.d(
            "TutorialOverlay",
            "🎭 Overlay check: isShowing=$isShowingTutorial, currentStep=${currentStep?.title}"
        )
    }

    AnimatedVisibility(
        visible = isShowingTutorial && currentStep != null,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(300)),
        modifier = modifier.zIndex(1000f)
    ) {
        currentStep?.let { step ->
            android.util.Log.d("TutorialOverlay", "🎭 SHOWING overlay for step '${step.title}'")

            // Check if this is the community welcome step
            if (step.targetView == "community_welcome") {
                CommunityWelcomeOverlay(
                    userType = tutorialManager.userType.collectAsState().value,
                    onJoinCircl = {
                        tutorialManager.completeTutorial()
                        // TODO: Navigate to circles page
                    },
                    onInviteFriends = {
                        // TODO: Implement share functionality
                    },
                    onGetStarted = {
                        tutorialManager.completeTutorial()
                    }
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Semi-transparent backdrop
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f))
                            .clickable {
                                if (!step.isInteractive) {
                                    tutorialManager.nextStep()
                                }
                            }
                    )

                    // Highlighted area cutout (if specified)
                    step.highlightRect?.let { rect ->
                        HighlightCutout(
                            rect = rect,
                            isShowing = isShowingTutorial
                        )
                    }

                    // Tutorial tooltip
                    TutorialTooltip(
                        step = step,
                        currentStepIndex = currentStepIndex,
                        totalSteps = currentFlow?.steps?.size ?: 0,
                        onNext = { tutorialManager.nextStep() },
                        onPrevious = { tutorialManager.previousStep() },
                        onSkip = { tutorialManager.skipTutorial() }
                    )
                }
            }
        }
    }
}

/**
 * Modifier extension to apply tutorial overlay to a screen
 */
fun Modifier.withTutorialOverlay(tutorialManager: TutorialManager): Modifier {
    return this.then(
        Modifier.wrapContentSize()
    )
}

