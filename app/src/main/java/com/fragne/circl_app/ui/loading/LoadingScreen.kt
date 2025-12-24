package com.fragne.circl_app.ui.loading

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fragne.circl_app.R
import kotlinx.coroutines.delay

/**
 * Loading Screen
 * Shows a random background image with CIRCL logo and animated progress bar
 * Translated from LoadingScreen.swift
 */
@Composable
fun LoadingScreen(
    onLoadingComplete: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Array of available loading screens
    val loadingScreens = remember {
        listOf(
            R.drawable.test_loading_screen_1,
            R.drawable.test_loading_screen_2,
            R.drawable.test_loading_screen_3,
            R.drawable.test_loading_screen_4
        )
    }

    // Randomly select a loading screen
    val selectedLoadingScreen = remember {
        loadingScreens.randomOrNull()
    }

    // Animation states
    var logoScale by remember { mutableFloatStateOf(0.8f) }
    var logoOpacity by remember { mutableFloatStateOf(0f) }
    var progressValue by remember { mutableFloatStateOf(0f) }

    // Animate logo scale
    val animatedLogoScale by animateFloatAsState(
        targetValue = logoScale,
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing
        ),
        label = "logoScale"
    )

    // Animate logo opacity
    val animatedLogoOpacity by animateFloatAsState(
        targetValue = logoOpacity,
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing
        ),
        label = "logoOpacity"
    )

    // Animate progress bar
    val animatedProgress by animateFloatAsState(
        targetValue = progressValue,
        animationSpec = tween(
            durationMillis = 3000,
            easing = FastOutSlowInEasing
        ),
        label = "progress"
    )

    // Start animations on composition
    LaunchedEffect(Unit) {
        // Animate logo appearance
        logoScale = 1.0f
        logoOpacity = 1.0f

        // Delay slightly, then animate progress bar
        delay(300)
        progressValue = 1.0f

        // Wait for loading to complete (3 seconds total)
        delay(3000)
        onLoadingComplete()
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Fullscreen background loading image (randomly selected)
        selectedLoadingScreen?.let { drawableId ->
            Image(
                painter = painterResource(id = drawableId),
                contentDescription = "Loading background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Dark overlay for better logo visibility
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )

        // Centered CIRCL logo
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            // CIRCL Logo
            Image(
                painter = painterResource(id = R.drawable.circl_logo),
                contentDescription = "CIRCL Logo",
                modifier = Modifier
                    .width(200.dp)
                    .height(300.dp)
                    .scale(animatedLogoScale)
                    .alpha(animatedLogoOpacity)
                    .shadow(
                        elevation = 20.dp,
                        spotColor = Color.Black.copy(alpha = 0.5f)
                    ),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.weight(1f))

            // Loading indicator at the bottom
            Column(
                modifier = Modifier.padding(bottom = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // "LOADING" text
                Text(
                    text = "LOADING",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 3.sp,
                    modifier = Modifier
                        .alpha(0.9f)
                        .shadow(
                            elevation = 2.dp,
                            spotColor = Color.Black.copy(alpha = 0.5f)
                        )
                )

                // Custom progress bar
                Box(
                    modifier = Modifier
                        .width(250.dp)
                        .height(6.dp)
                ) {
                    // Background bar
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = Color.Black.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(10.dp)
                            )
                    )

                    // Progress bar with gradient
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(animatedProgress)
                            .height(6.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Color.White, Color(0xFFFFA500)) // White to Orange
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .shadow(
                                elevation = 3.dp,
                                spotColor = Color.Black.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(10.dp)
                            )
                    )
                }
            }
        }
    }
}

