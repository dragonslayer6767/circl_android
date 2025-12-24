package com.fragne.circl_app.ui.settings

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.fragne.circl_app.tutorial.TutorialManager
import com.fragne.circl_app.tutorial.models.UserType as TutorialUserType
import com.fragne.circl_app.ui.subscription.SubscriptionManager
import com.fragne.circl_app.ui.subscription.SubscriptionPaywallDialog
import com.fragne.circl_app.ui.subscription.UserType

/**
 * Settings Screen
 * Translated from PageSettings.swift
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateToProfile: () -> Unit = {},
    onNavigateToMessages: () -> Unit = {},
    onNavigateToBecomeMentor: () -> Unit = {},
    onNavigateToChangePassword: () -> Unit = {},
    onNavigateToBlockedUsers: () -> Unit = {},
    onNavigateToDeleteAccount: () -> Unit = {},
    onNavigateToSuggestFeature: () -> Unit = {},
    onNavigateToReportProblem: () -> Unit = {},
    onNavigateToTerms: () -> Unit = {},
    onNavigateToPrivacy: () -> Unit = {},
    onNavigateToGuidelines: () -> Unit = {},
    onNavigateToTutorial: () -> Unit = {},
    onNavigateToSupport: () -> Unit = {},
    onLogout: () -> Unit = {},
    userProfileImageUrl: String = "",
    unreadMessageCount: Int = 0
) {
    val context = LocalContext.current
    val tutorialManager = remember { TutorialManager.getInstance(context) }
    val currentUserType by tutorialManager.userType.collectAsState()

    var showLogoutDialog by remember { mutableStateOf(false) }
    var showTutorialDialog by remember { mutableStateOf(false) }
    var selectedTutorialType by remember { mutableStateOf(currentUserType) }
    var showTutorialTypeDropdown by remember { mutableStateOf(false) }

    val primaryBlue = Color(0xFF004AAD)

    var accountSettingsTapCount by remember { mutableIntStateOf(0) }

    // Subscription Manager
    val subscriptionManager = viewModel<SubscriptionManager>()

    // Show subscription paywall dialog
    SubscriptionPaywallDialog(
        subscriptionManager = subscriptionManager
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header
        SettingsHeader(
            userProfileImageUrl = userProfileImageUrl,
            unreadMessageCount = unreadMessageCount,
            onProfileClick = onNavigateToProfile,
            onMessagesClick = onNavigateToMessages
        )

        // Content
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Animated background
            AnimatedBackground()

            // White overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.95f))
            )

            // Settings content
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Account Settings Section (Easter egg!)
                item {
                    SettingsSection(
                        title = "Account Settings",
                        icon = Icons.Filled.AccountCircle,
                        onHeaderClick = {
                            accountSettingsTapCount++
                            if (accountSettingsTapCount >= 10) {
                                // Easter egg triggered!
                                accountSettingsTapCount = 0
                                // TODO: Show easter egg video
                            }
                        }
                    ) {
                        SettingsOption(
                            title = "Become a Mentor",
                            icon = Icons.Filled.School,
                            onClick = onNavigateToBecomeMentor
                        )
                        SettingsOption(
                            title = "Change Password",
                            icon = Icons.Filled.Lock,
                            onClick = onNavigateToChangePassword
                        )
                        SettingsOption(
                            title = "Blocked Users",
                            icon = Icons.Filled.Block,
                            onClick = onNavigateToBlockedUsers
                        )
                        SettingsOption(
                            title = "Delete Account",
                            icon = Icons.Filled.Delete,
                            onClick = onNavigateToDeleteAccount,
                            isDestructive = true,
                            subtitle = "This action cannot be undone"
                        )
                    }
                }

                // Subscription Section
                item {
                    SettingsSection(
                        title = "Subscription",
                        icon = Icons.Filled.Stars
                    ) {
                        SettingsOption(
                            title = "Upgrade to Premium",
                            subtitle = "Unlock exclusive features and benefits",
                            icon = Icons.Filled.Upgrade,
                            onClick = {
                                // Detect user type - TODO: Get from actual user profile
                                val userType = UserType.ENTREPRENEUR
                                subscriptionManager.showPaywall(userType)
                            },
                            trailingContent = {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color(0xFFFFD700).copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "PRO",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFFD700),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        )
                    }
                }

                // Feedback & Suggestions Section
                item {
                    SettingsSection(
                        title = "Feedback & Suggestions",
                        icon = Icons.Filled.Lightbulb
                    ) {
                        SettingsOption(
                            title = "Suggest a Feature",
                            icon = Icons.Filled.Lightbulb,
                            onClick = onNavigateToSuggestFeature
                        )
                        SettingsOption(
                            title = "Report a Problem",
                            icon = Icons.Filled.Warning,
                            onClick = onNavigateToReportProblem
                        )
                    }
                }

                // Legal & Policies Section
                item {
                    SettingsSection(
                        title = "Legal & Policies",
                        icon = Icons.Filled.Description
                    ) {
                        SettingsOption(
                            title = "Terms of Service",
                            icon = Icons.Filled.Description,
                            onClick = onNavigateToTerms
                        )
                        SettingsOption(
                            title = "Privacy Policy",
                            icon = Icons.Filled.Security,
                            onClick = onNavigateToPrivacy
                        )
                        SettingsOption(
                            title = "Community Guidelines",
                            icon = Icons.Filled.Groups,
                            onClick = onNavigateToGuidelines
                        )
                    }
                }

                // Tutorial & Help Section
                item {
                    SettingsSection(
                        title = "Tutorial & Help",
                        icon = Icons.AutoMirrored.Filled.Help
                    ) {
                        SettingsOption(
                            title = "Start Tutorial",
                            subtitle = "${currentUserType.displayName} Tutorial",
                            icon = Icons.Filled.School,
                            onClick = { showTutorialDialog = true }
                        )
                    }
                }

                // Help & Support Section
                item {
                    SettingsSection(
                        title = "Help & Support",
                        icon = Icons.Filled.Headset
                    ) {
                        SettingsOption(
                            title = "Contact Support",
                            icon = Icons.Filled.Headset,
                            onClick = onNavigateToSupport
                        )
                    }
                }

                // Logout Button
                item {
                    Button(
                        onClick = { showLogoutDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            Color.Red,
                                            Color.Red.copy(alpha = 0.8f)
                                        )
                                    ),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Logout,
                                    contentDescription = "Logout",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "Logout",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                // Bottom padding for navigation
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }

    // Tutorial selection dialog
    if (showTutorialDialog) {
        AlertDialog(
            onDismissRequest = { showTutorialDialog = false },
            title = {
                Text(
                    text = "Start Tutorial",
                    fontWeight = FontWeight.Bold,
                    color = primaryBlue
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Choose which tutorial you'd like to experience:",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    // Tutorial type dropdown
                    ExposedDropdownMenuBox(
                        expanded = showTutorialTypeDropdown,
                        onExpandedChange = { showTutorialTypeDropdown = it }
                    ) {
                        OutlinedTextField(
                            value = selectedTutorialType.displayName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Tutorial Type") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = showTutorialTypeDropdown)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryBlue,
                                focusedLabelColor = primaryBlue
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = showTutorialTypeDropdown,
                            onDismissRequest = { showTutorialTypeDropdown = false }
                        ) {
                            TutorialUserType.entries.forEach { userType ->
                                DropdownMenuItem(
                                    text = { Text(userType.displayName) },
                                    onClick = {
                                        selectedTutorialType = userType
                                        showTutorialTypeDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    // Tutorial description
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = primaryBlue.copy(alpha = 0.1f)
                        )
                    ) {
                        Text(
                            text = getTutorialDescription(selectedTutorialType),
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            color = primaryBlue,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showTutorialDialog = false
                        tutorialManager.restartTutorial(selectedTutorialType)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryBlue
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Start Tutorial")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showTutorialDialog = false }
                ) {
                    Text("Cancel", color = primaryBlue)
                }
            }
        )
    }

    // Logout confirmation dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Log out of your account?") },
            text = { Text("You'll need to sign in again to access your account.") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text("Log Out")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

/**
 * Settings Header
 */
@Composable
private fun SettingsHeader(
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
 * Animated Background
 */
@Composable
private fun AnimatedBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "background")

    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bgAnimation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF001A3D),
                        Color(0xFF004AAD),
                        Color(0xFF0066FF),
                        Color(0xFF003D7A)
                    ),
                    start = androidx.compose.ui.geometry.Offset(
                        x = animationProgress * 1000,
                        y = 0f
                    ),
                    end = androidx.compose.ui.geometry.Offset(
                        x = 1000f - animationProgress * 1000,
                        y = 1000f
                    )
                )
            )
            .then(Modifier.background(Color.White.copy(alpha = 0.9f)))
    )
}

/**
 * Settings Section
 */
@Composable
private fun SettingsSection(
    title: String,
    icon: ImageVector,
    onHeaderClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Section Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (onHeaderClick != null) Modifier.clickable(onClick = onHeaderClick)
                        else Modifier
                    ),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF004AAD),
                    modifier = Modifier.size(20.dp)
                )

                Text(
                    text = title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            // Section Content
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                content()
            }
        }
    }
}

/**
 * Settings Option
 */
@Composable
private fun SettingsOption(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    isDestructive: Boolean = false,
    subtitle: String? = null,
    trailingContent: @Composable (() -> Unit)? = null
) {
    val primaryBlue = Color(0xFF004AAD)
    val lightBlue = Color(0xFF0066FF)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF5F5F5)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        if (isDestructive) {
                            Brush.linearGradient(
                                colors = listOf(
                                    Color.Red.copy(alpha = 0.8f),
                                    Color.Red
                                )
                            )
                        } else {
                            Brush.linearGradient(
                                colors = listOf(primaryBlue, lightBlue)
                            )
                        },
                        RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Text
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )

                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }

            // Trailing content or chevron
            if (trailingContent != null) {
                trailingContent()
            } else {
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

/**
 * Get tutorial description for each user type
 */
private fun getTutorialDescription(userType: TutorialUserType): String {
    return when (userType) {
        TutorialUserType.ENTREPRENEUR ->
            "Learn how to find co-founders, connect with investors, showcase your venture, and leverage Circl's tools to grow your startup."

        TutorialUserType.STUDENT ->
            "Discover how to find mentors, learn from experienced entrepreneurs, and accelerate your learning journey on Circl."

        TutorialUserType.STUDENT_ENTREPRENEUR ->
            "Get a comprehensive guide for building your startup while in school, finding co-founders, and balancing both worlds."

        TutorialUserType.MENTOR ->
            "Learn how to share your knowledge, find mentees, and make an impact by guiding the next generation of entrepreneurs."

        TutorialUserType.INVESTOR ->
            "Discover how to find investment opportunities, connect directly with founders, and access quality deal flow on Circl."

        TutorialUserType.COMMUNITY_BUILDER ->
            "Get a complete overview of Circl's features and learn how to connect, engage, and grow within the community."

        TutorialUserType.OTHER ->
            "Get a complete overview of Circl's features and learn how to make the most of the platform."
    }
}
