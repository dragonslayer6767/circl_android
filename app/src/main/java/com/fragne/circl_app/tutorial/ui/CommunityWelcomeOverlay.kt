package com.fragne.circl_app.tutorial.ui

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fragne.circl_app.tutorial.models.UserType

/**
 * Community Welcome Overlay
 * Final celebration screen after tutorial completion
 */
@Composable
fun CommunityWelcomeOverlay(
    userType: UserType,
    onJoinCircl: () -> Unit,
    onInviteFriends: () -> Unit,
    onGetStarted: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(200)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut()
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f)),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(20.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Success icon
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = Color(0xFF34C759)
                    )

                    // Title
                    Text(
                        text = "Welcome to Circl!",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF004AAD),
                        textAlign = TextAlign.Center
                    )

                    // Personalized message based on user type
                    Text(
                        text = getWelcomeMessage(userType),
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        color = Color(0xFF333333),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Action buttons
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Join Circl button
                        Button(
                            onClick = onJoinCircl,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF004AAD)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Groups,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Join a Circle",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Invite Friends button
                        OutlinedButton(
                            onClick = onInviteFriends,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF004AAD)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Invite Friends",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Get Started button
                        TextButton(
                            onClick = onGetStarted,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Get Started",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF004AAD)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Get personalized welcome message based on user type
 */
private fun getWelcomeMessage(userType: UserType): String {
    return when (userType) {
        UserType.ENTREPRENEUR ->
            "You're all set to start finding co-founders, connecting with investors, and building your startup. " +
            "Remember: authentic engagement and providing value to others is the key to success on Circl!"

        UserType.STUDENT ->
            "You're ready to connect with mentors, learn from experienced entrepreneurs, and accelerate your learning. " +
            "Be curious, ask great questions, and soak up all the knowledge this community has to offer!"

        UserType.STUDENT_ENTREPRENEUR ->
            "You're equipped to balance building your startup while learning. Connect with fellow student founders, " +
            "find mentors, and leverage your unique position to experiment and grow!"

        UserType.MENTOR ->
            "You're ready to share your wisdom and guide the next generation. Your experience is valuable - " +
            "connect with eager learners and make a lasting impact on their journeys!"

        UserType.INVESTOR ->
            "You're set to discover investment opportunities and connect directly with founders. " +
            "Engage authentically, provide value beyond capital, and build relationships that lead to great deals!"

        UserType.COMMUNITY_BUILDER, UserType.OTHER ->
            "You're ready to engage with an amazing community of entrepreneurs, students, mentors, and innovators. " +
            "Connect authentically, give before you ask, and enjoy the journey!"
    }
}

