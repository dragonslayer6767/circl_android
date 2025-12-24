package com.fragne.circl_app.ui.marketplace

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

/**
 * Growth Hub / Marketplace Screen
 * Translated from PageSkillSellingPlaceholder.swift
 * Note: Bottom navigation is provided by MainScreen, not this screen
 */
@Composable
fun GrowthHubScreen(
    onNavigateToProfile: () -> Unit = {},
    onNavigateToMessages: () -> Unit = {},
    userProfileImageUrl: String = "",
    unreadMessageCount: Int = 0
) {
    val primaryBlue = Color(0xFF004AAD)


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header
        GrowthHubHeader(
            userProfileImageUrl = userProfileImageUrl,
            unreadMessageCount = unreadMessageCount,
            onProfileClick = onNavigateToProfile,
            onMessagesClick = onNavigateToMessages
        )

        // Content
        PlaceholderContent()
    }
}

/**
 * Growth Hub Header
 */
@Composable
private fun GrowthHubHeader(
    userProfileImageUrl: String,
    unreadMessageCount: Int,
    onProfileClick: () -> Unit,
    onMessagesClick: () -> Unit
) {
    val primaryBlue = Color(0xFF004AAD)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(primaryBlue)
            .statusBarsPadding() // Extend to status bar
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile picture
        Box(
            modifier = Modifier
                .size(32.dp)
                .clickable(onClick = onProfileClick)
        ) {
            if (userProfileImageUrl.isNotEmpty()) {
                AsyncImage(
                    model = userProfileImageUrl,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Profile",
                    tint = Color.White,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Logo
        Text(
            text = "Circl.",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.weight(1f))

        // Messages icon with badge
        Box(
            modifier = Modifier
                .size(32.dp)
                .clickable(onClick = onMessagesClick)
        ) {
            Icon(
                imageVector = Icons.Filled.Email,
                contentDescription = "Messages",
                tint = Color.White,
                modifier = Modifier.fillMaxSize()
            )

            if (unreadMessageCount > 0) {
                Badge(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 6.dp, y = (-6).dp),
                    containerColor = Color.Red
                ) {
                    Text(
                        text = if (unreadMessageCount > 99) "99+" else unreadMessageCount.toString(),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

/**
 * Placeholder Content
 */
@Composable
private fun PlaceholderContent() {
    val primaryBlue = Color(0xFF004AAD)
    val lightBlue = Color(0xFF0066FF)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            // Marketplace Icon
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(primaryBlue, lightBlue)
                            ),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.MonetizationOn,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(60.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Coming Soon Message
                Text(
                    text = "The Growth Hub is Almost Here!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        }

        item {
            // Features Preview Header
            Text(
                text = "What's Coming:",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        // Feature Cards
        items(getFeaturesList()) { feature ->
            FeaturePreviewCard(
                icon = feature.icon,
                title = feature.title,
                description = feature.description
            )
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

/**
 * Feature Preview Card
 */
@Composable
private fun FeaturePreviewCard(
    icon: ImageVector,
    title: String,
    description: String
) {
    val primaryBlue = Color(0xFF004AAD)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        primaryBlue.copy(alpha = 0.1f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = primaryBlue,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                Text(
                    text = description,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )
            }
        }
    }
}


/**
 * Feature Data Model
 */
private data class Feature(
    val icon: ImageVector,
    val title: String,
    val description: String
)

/**
 * Get list of features
 */
private fun getFeaturesList(): List<Feature> {
    return listOf(
        Feature(
            icon = Icons.Filled.MonetizationOn,
            title = "Earn Extra Income",
            description = "Turn your skills into cash flow. Set your rates, work on your schedule, and get paid securely through our escrow system."
        ),
        Feature(
            icon = Icons.Filled.Groups,
            title = "Build or Hire Your Team",
            description = "From finding your next co-founder to building your marketing team — everything you need to scale your venture. Connect with the right people and turn your vision into reality."
        ),
        Feature(
            icon = Icons.Filled.Shield,
            title = "Work With Confidence",
            description = "No more payment worries. Our secure escrow system protects both parties until projects are completed to satisfaction."
        ),
        Feature(
            icon = Icons.Filled.TravelExplore,
            title = "Access Hidden Opportunities",
            description = "Discover exclusive projects and collaborations that aren't posted anywhere else - from fellow entrepreneurs who get it."
        ),
        Feature(
            icon = Icons.Filled.Handshake,
            title = "Collaborate on Projects",
            description = "Build your résumé and gain hands-on experience by working closely with real companies. Prove your skills, grow your network, and maybe even land your next job."
        ),
        Feature(
            icon = Icons.Filled.Business,
            title = "Join Companies & Startups",
            description = "Step into the action — join emerging startups or established teams looking for talent like you. Turn your ambition into opportunity and build the career you've been working for."
        )
    )
}

