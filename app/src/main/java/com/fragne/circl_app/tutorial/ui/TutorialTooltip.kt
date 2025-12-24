package com.fragne.circl_app.tutorial.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fragne.circl_app.tutorial.models.TutorialStep

/**
 * Tutorial Tooltip Component
 * Shows tutorial step information with navigation controls
 */
@Composable
fun TutorialTooltip(
    step: TutorialStep,
    currentStepIndex: Int,
    totalSteps: Int,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showContent by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(100)
        showContent = true
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = when (step.tooltipAlignment) {
            TutorialStep.TooltipAlignment.TOP -> Alignment.TopCenter
            TutorialStep.TooltipAlignment.BOTTOM -> Alignment.BottomCenter
            TutorialStep.TooltipAlignment.LEADING -> Alignment.CenterStart
            TutorialStep.TooltipAlignment.TRAILING -> Alignment.CenterEnd
            TutorialStep.TooltipAlignment.CENTER -> Alignment.Center
        }
    ) {
        AnimatedVisibility(
            visible = showContent,
            enter = slideInVertically(
                initialOffsetY = { if (step.tooltipAlignment == TutorialStep.TooltipAlignment.TOP) -it else it }
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { if (step.tooltipAlignment == TutorialStep.TooltipAlignment.TOP) -it else it }
            ) + fadeOut()
        ) {
            TooltipContent(
                step = step,
                currentStepIndex = currentStepIndex,
                totalSteps = totalSteps,
                onNext = onNext,
                onPrevious = onPrevious,
                onSkip = onSkip
            )
        }
    }
}

@Composable
private fun TooltipContent(
    step: TutorialStep,
    currentStepIndex: Int,
    totalSteps: Int,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onSkip: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with step counter and skip button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = step.title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF004AAD)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Step ${currentStepIndex + 1} of $totalSteps",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                }

                // Skip button
                IconButton(
                    onClick = onSkip,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Skip tutorial",
                        tint = Color.Gray
                    )
                }
            }

            // Description
            Text(
                text = step.description,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF004AAD)
            )

            // Main message
            Text(
                text = step.message,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = Color(0xFF333333)
            )

            // Progress indicator
            TutorialProgressIndicator(
                currentStep = currentStepIndex,
                totalSteps = totalSteps,
                modifier = Modifier.fillMaxWidth()
            )

            // Navigation buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Previous button (if not first step)
                if (currentStepIndex > 0) {
                    OutlinedButton(
                        onClick = onPrevious,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF004AAD)
                        )
                    ) {
                        Text("Previous")
                    }
                }

                // Next/Done button
                Button(
                    onClick = onNext,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF004AAD)
                    )
                ) {
                    Text(
                        text = if (currentStepIndex < totalSteps - 1) "Next" else "Done",
                        color = Color.White
                    )
                }
            }
        }
    }
}

