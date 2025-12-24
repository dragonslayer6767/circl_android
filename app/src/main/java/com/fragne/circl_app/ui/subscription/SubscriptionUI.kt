package com.fragne.circl_app.ui.subscription

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Subscription Paywall Dialog
 * Full-screen dialog showing subscription options with animated transitions
 * Translated from SubscriptionOverlayComponents.swift
 */
@Composable
fun SubscriptionPaywallDialog(
    subscriptionManager: SubscriptionManager = viewModel(),
    onDismiss: () -> Unit = {}
) {
    val isShowingPaywall by subscriptionManager.isShowingPaywall.collectAsState()
    val subscriptionState by subscriptionManager.subscriptionState.collectAsState()
    val currentContent by subscriptionManager.currentContent.collectAsState()

    if (isShowingPaywall && currentContent != null) {
        Dialog(
            onDismissRequest = {
                subscriptionManager.dismissPaywall()
                onDismiss()
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            )
        ) {
            PaywallFullScreenView(
                content = currentContent!!,
                subscriptionState = subscriptionState,
                subscriptionManager = subscriptionManager
            )
        }
    }
}

/**
 * Full Screen Paywall View
 * Shows background image immediately, then animates in content
 */
@Composable
private fun PaywallFullScreenView(
    content: SubscriptionContent,
    subscriptionState: SubscriptionState,
    subscriptionManager: SubscriptionManager
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Background - always visible (no animation)
        PaywallBackgroundView(content = content)

        // Content overlay - appears after delay with animation
        AnimatedVisibility(
            visible = subscriptionState == SubscriptionState.SHOWING_CONTENT,
            enter = fadeIn(animationSpec = tween(400)) + scaleIn(
                initialScale = 0.95f,
                animationSpec = tween(400)
            ),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            PaywallContentView(
                content = content,
                subscriptionManager = subscriptionManager
            )
        }
    }
}

/**
 * Paywall Background View
 * Shows background image with random selection (matches iOS)
 */
@Composable
private fun PaywallBackgroundView(content: SubscriptionContent) {
    // Get random background image for this user type
    val imageRes = remember { getRandomBackgroundImageRes(content.backgroundImage) }

    androidx.compose.foundation.Image(
        painter = painterResource(id = imageRes),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

/**
 * Paywall Content View
 * Main scrollable content with title, plans, and subscribe button
 */
@Composable
private fun PaywallContentView(
    content: SubscriptionContent,
    subscriptionManager: SubscriptionManager
) {
    var showContent by remember { mutableStateOf(false) }
    val selectedPlan by subscriptionManager.selectedPlan.collectAsState()

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(100)
        showContent = true
    }

    val contentScale by animateFloatAsState(
        targetValue = if (showContent) 1f else 0.9f,
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        label = "contentScale"
    )

    val contentAlpha by animateFloatAsState(
        targetValue = if (showContent) 1f else 0f,
        animationSpec = tween(500),
        label = "contentAlpha"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .scale(contentScale)
            .graphicsLayer { alpha = contentAlpha }
            .verticalScroll(rememberScrollState())
    ) {
        // Close Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = { subscriptionManager.dismissPaywall() },
                modifier = Modifier
                    .size(32.dp)
                    .background(Color.Black.copy(alpha = 0.3f), CircleShape)
            ) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        // White Content Container
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .background(
                    Color.White.copy(alpha = 0.92f),
                    RoundedCornerShape(30.dp)
                )
                .padding(top = 40.dp, bottom = 30.dp)
        ) {
            // Title Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = content.title,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = content.subtitle,
                    fontSize = 18.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Subscription Plans (Horizontal Scroll)
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 15.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                items(content.plans) { plan ->
                    SubscriptionPlanCard(
                        plan = plan,
                        isSelected = selectedPlan?.id == plan.id,
                        onSelect = { subscriptionManager.selectPlan(plan) },
                        modifier = Modifier.width(280.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Subscribe Button
            Button(
                onClick = { subscriptionManager.completeSubscription() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
                    .height(56.dp),
                enabled = selectedPlan != null,
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF004AAD),
                    disabledContainerColor = Color(0xFF004AAD).copy(alpha = 0.6f)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 4.dp,
                    disabledElevation = 0.dp
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Start Your Journey",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Terms & Privacy
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(onClick = { /* Handle terms */ }) {
                    Text(
                        text = "Terms of Service",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                TextButton(onClick = { /* Handle privacy */ }) {
                    Text(
                        text = "Privacy Policy",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

/**
 * Subscription Plan Card
 * Individual plan card with features and pricing
 */
@Composable
private fun SubscriptionPlanCard(
    plan: SubscriptionPlan,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1.0f,
        animationSpec = tween(200),
        label = "cardScale"
    )

    Card(
        modifier = modifier
            .scale(scale)
            .clickable(onClick = onSelect),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF004AAD).copy(alpha = 0.15f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 6.dp else 4.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = if (isSelected) 3.dp else 1.dp,
                    brush = if (isSelected) {
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFFFD700),
                                Color(0xFFFFA500),
                                Color(0xFFFFD700)
                            )
                        )
                    } else {
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF004AAD).copy(alpha = 0.3f),
                                Color(0xFF0066FF).copy(alpha = 0.2f)
                            )
                        )
                    },
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header with pricing
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = plan.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF004AAD)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = plan.price,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF004AAD)
                            )
                            Text(
                                text = "/${plan.period}",
                                fontSize = 16.sp,
                                color = Color(0xFF0066FF)
                            )
                        }

                        // Original price and discount
                        if (plan.originalPrice != null && plan.discount != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = plan.originalPrice,
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    style = LocalTextStyle.current.copy(
                                        textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                                    )
                                )

                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color.Green.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = plan.discount,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Green,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Popular badge
                    if (plan.isPopular) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFFFA500)
                        ) {
                            Text(
                                text = "POPULAR",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Features
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    plan.features.forEach { feature ->
                        Row(
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = null,
                                tint = Color.Green,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = feature,
                                fontSize = 14.sp,
                                color = Color(0xFF004AAD).copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Get random background image resource for user type
 * Matches iOS behavior of randomly selecting from available images
 */
private fun getRandomBackgroundImageRes(imageName: String): Int {
    val availableImages = when (imageName) {
        "entrepreneur" -> listOf(
            R.drawable.entrepreneur_1,
            R.drawable.entrepreneur_2
        )
        "student" -> listOf(
            R.drawable.student_1,
            R.drawable.student_2,
            R.drawable.student_3
        )
        "student_entrepreneur" -> listOf(
            R.drawable.student_entrepreneur_1,
            R.drawable.student_entrepreneur_2
        )
        "mentor" -> listOf(
            R.drawable.mentor_1,
            R.drawable.mentor_2,
            R.drawable.mentor_3
        )
        "community_builder" -> listOf(
            R.drawable.community_builder,
            R.drawable.community_builder_2
        )
        "investor" -> listOf(
            R.drawable.investor_1,
            R.drawable.investor_2,
            R.drawable.investor_3
        )
        else -> getAllBackgroundImages() // Fallback: random from all 15 images
    }

    return availableImages.random()
}

/**
 * Get all 15 background images for random fallback selection
 * Used when user type is unknown or OTHER
 */
private fun getAllBackgroundImages(): List<Int> {
    return listOf(
        // Community Builder (2 images)
        R.drawable.community_builder,
        R.drawable.community_builder_2,
        // Entrepreneur (2 images)
        R.drawable.entrepreneur_1,
        R.drawable.entrepreneur_2,
        // Investor (3 images)
        R.drawable.investor_1,
        R.drawable.investor_2,
        R.drawable.investor_3,
        // Mentor (3 images)
        R.drawable.mentor_1,
        R.drawable.mentor_2,
        R.drawable.mentor_3,
        // Student (3 images)
        R.drawable.student_1,
        R.drawable.student_2,
        R.drawable.student_3,
        // Student Entrepreneur (2 images)
        R.drawable.student_entrepreneur_1,
        R.drawable.student_entrepreneur_2
    )
}

