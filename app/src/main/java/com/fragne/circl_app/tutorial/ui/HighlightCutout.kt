package com.fragne.circl_app.tutorial.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Highlight Cutout
 * Creates a highlighted area with pulsing animation
 */
@Composable
fun HighlightCutout(
    rect: Rect,
    isShowing: Boolean,
    modifier: Modifier = Modifier
) {
    // Pulsing animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")

    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    if (isShowing) {
        Box(
            modifier = modifier
                .offset(x = rect.left.dp, y = rect.top.dp)
                .size(width = rect.width.dp, height = rect.height.dp)
        ) {
            // White border
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(
                        width = 3.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(12.dp)
                    )
            )

            // Pulsing yellow accent
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp)
                    .border(
                        BorderStroke(
                            width = 2.dp,
                            color = Color(0xFFFFDE59).copy(alpha = pulseAlpha)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
            )
        }
    }
}

/**
 * Tutorial Progress Indicator
 * Shows current step progress
 */
@Composable
fun TutorialProgressIndicator(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Progress bar
        LinearProgressIndicator(
            progress = { (currentStep + 1).toFloat() / totalSteps.toFloat() },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = Color(0xFF004AAD),
            trackColor = Color(0xFFE0E0E0)
        )

        // Percentage text
        Text(
            text = "${((currentStep + 1).toFloat() / totalSteps * 100).toInt()}% complete",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End
        )
    }
}

