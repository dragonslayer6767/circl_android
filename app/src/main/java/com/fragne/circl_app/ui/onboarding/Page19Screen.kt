package com.fragne.circl_app.ui.onboarding

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fragne.circl_app.ui.components.CloudBackground
import com.fragne.circl_app.ui.theme.CirclBlue
import com.fragne.circl_app.ui.theme.CirclBlueLight
import com.fragne.circl_app.ui.theme.CirclYellow
import com.fragne.circl_app.ui.theme.CirclWhite
import kotlin.random.Random

/**
 * Page19 - Welcome/Completion Screen
 * Final onboarding screen with confetti animation
 */
@Composable
fun Page19Screen(
    onComplete: () -> Unit
) {
    var showConfetti by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(3000) // Hide confetti after 3 seconds
        showConfetti = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(CirclBlue, CirclBlueLight)
                )
            )
    ) {
        // Cloud Background
        CloudBackground {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(110.dp))

                // Logo
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .shadow(10.dp, CircleShape)
                        .background(CirclWhite, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Circl.",
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold,
                        color = CirclBlue
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Welcome Message
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = "Welcome to Circl!",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = CirclYellow,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "You're all set! Start connecting with entrepreneurs, mentors, and innovators. Let's build something great together!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = CirclWhite,
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Get Started Button
                Button(
                    onClick = onComplete,
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(10.dp, RoundedCornerShape(15.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CirclYellow
                    ),
                    shape = RoundedCornerShape(15.dp),
                    contentPadding = PaddingValues(vertical = 18.dp)
                ) {
                    Text(
                        text = "Get Started",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = CirclBlue
                    )
                }

                Spacer(modifier = Modifier.height(60.dp))
            }
        }

        // Confetti Animation
        if (showConfetti) {
            ConfettiAnimation()
        }
    }
}

@Composable
fun ConfettiAnimation() {
    val confettiCount = 50
    val confettiItems = remember {
        List(confettiCount) {
            ConfettiItem(
                color = listOf(
                    CirclYellow,
                    CirclWhite,
                    Color(0xFFFF6B6B),
                    Color(0xFF4ECDC4),
                    Color(0xFF45B7D1)
                ).random(),
                startX = Random.nextFloat(),
                speed = Random.nextFloat() * 2f + 1f
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "confetti")
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "confetti_progress"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        confettiItems.forEach { item ->
            val yProgress = (animationProgress * item.speed) % 1f
            val x = size.width * item.startX
            val y = size.height * yProgress

            drawCircle(
                color = item.color,
                radius = 8f,
                center = Offset(x, y),
                alpha = 1f - yProgress // Fade out as it falls
            )
        }
    }
}

data class ConfettiItem(
    val color: Color,
    val startX: Float,
    val speed: Float
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Page19ScreenPreview() {
    Page19Screen(onComplete = {})
}

