package com.fragne.circl_app.ui.circles.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fragne.circl_app.ui.circles.dashboard.CircleData
import com.fragne.circl_app.ui.circles.dashboard.CircleDashboardScreen
import com.fragne.circl_app.ui.circles.calendar.CircleCalendarScreen
import java.text.SimpleDateFormat
import java.util.*

/**
 * Circle Group Chat Home Screen
 * Translated from PageGroupchats.swift
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CircleGroupChatScreen(
    circle: CircleData,
    onNavigateBack: () -> Unit = {},
    onNavigateToChannel: (Int) -> Unit = {},
    onNavigateToMembers: () -> Unit = {},
    onNavigateToDues: () -> Unit = {},
    onNavigateToManageChannels: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(GroupTab.DASHBOARD) }
    var showCircleSwitcher by remember { mutableStateOf(false) }
    var showCreateThread by remember { mutableStateOf(false) }
    var showCreateAnnouncement by remember { mutableStateOf(false) }
    var showLeaveDialog by remember { mutableStateOf(false) }
    var showCircleSettings by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    // Mock data - TODO: Replace with ViewModel
    val categories = remember {
        listOf(
            ChannelCategory(
                id = 1,
                name = "General",
                channels = listOf(
                    Channel(1, "welcome", "General"),
                    Channel(2, "chats", "General"),
                    Channel(3, "announcements", "General")
                )
            ),
            ChannelCategory(
                id = 2,
                name = "Projects",
                channels = listOf(
                    Channel(4, "design", "Projects"),
                    Channel(5, "development", "Projects")
                )
            )
        )
    }

    val announcements = remember {
        listOf(
            AnnouncementData(
                id = 1,
                user = "Fragnedelgado1",
                title = "Test",
                content = "Testing",
                announcedAt = "Recently"
            )
        )
    }

    val threads = remember {
        listOf(
            ThreadData(
                id = 1,
                author = "Jane Smith",
                content = "What are your thoughts on the new product roadmap?",
                replies = 12,
                createdAt = "2 hours ago"
            ),
            ThreadData(
                id = 2,
                author = "Mike Johnson",
                content = "Looking for feedback on the latest design mockups",
                replies = 8,
                createdAt = "5 hours ago"
            )
        )
    }

    val primaryBlue = Color(0xFF004AAD)
    val lightBlue = Color(0xFF0066FF)

    Scaffold(
        topBar = {
            CircleGroupChatHeader(
                circleName = circle.name,
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                onNavigateBack = onNavigateBack,
                onShowMenu = { /* TODO: Show menu */ }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White,
                            primaryBlue.copy(alpha = 0.02f)
                        )
                    )
                )
        ) {
            when (selectedTab) {
                GroupTab.HOME -> {
                    HomeTabContent(
                        circle = circle,
                        categories = categories,
                        announcements = announcements,
                        threads = threads,
                        onNavigateToChannel = onNavigateToChannel,
                        onShowCircleSwitcher = { showCircleSwitcher = true },
                        onNavigateToManageChannels = onNavigateToManageChannels,
                        onShowCreateThread = { showCreateThread = true },
                        onShowCreateAnnouncement = { showCreateAnnouncement = true },
                        onNavigateToDues = onNavigateToDues,
                        onShowCircleSettings = { showCircleSettings = true }
                    )
                }
                GroupTab.DASHBOARD -> {
                    CircleDashboardScreen(
                        circle = circle,
                        onNavigateBack = onNavigateBack
                    )
                }
                GroupTab.CALENDAR -> {
                    CircleCalendarScreen(
                        circle = circle,
                        onNavigateBack = onNavigateBack
                    )
                }
            }
        }
    }

    // Dialogs
    if (showLeaveDialog) {
        LeaveCircleDialog(
            circleName = circle.name,
            onDismiss = { showLeaveDialog = false },
            onConfirm = {
                // TODO: Leave circle
                showLeaveDialog = false
            }
        )
    }

    // Circle Settings Bottom Sheet
    if (showCircleSettings) {
        CircleSettingsBottomSheet(
            circle = circle,
            onDismiss = { showCircleSettings = false },
            onNavigateToMembers = onNavigateToMembers,
            onNavigateToDues = onNavigateToDues,
            onNavigateToManageChannels = {
                showCircleSettings = false
                onNavigateToManageChannels()
            },
            onLeaveCircle = {
                showCircleSettings = false
                showLeaveDialog = true
            },
            onShowAbout = { showAboutDialog = true }
        )
    }

    // About Circle Dialog
    if (showAboutDialog) {
        CircleAboutDialog(
            circle = circle,
            isMember = true, // TODO: Get actual membership status
            onDismiss = { showAboutDialog = false },
            onJoinCircle = {
                // TODO: Implement join circle
                showAboutDialog = false
            },
            onOpenCircle = {
                // Already in circle, do nothing or refresh
            },
            onUploadPhoto = {
                // TODO: Implement photo upload
            }
        )
    }
}

/**
 * Header for Circle Group Chat
 */
@Composable
private fun CircleGroupChatHeader(
    circleName: String,
    selectedTab: GroupTab,
    onTabSelected: (GroupTab) -> Unit,
    onNavigateBack: () -> Unit,
    onShowMenu: () -> Unit
) {
    val primaryBlue = Color(0xFF004AAD)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(primaryBlue)
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Text(
                text = circleName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = onShowMenu) {
                Icon(
                    Icons.Filled.MoreVert,
                    contentDescription = "Menu",
                    tint = Color.White
                )
            }
        }

        // Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            GroupTab.values().forEach { tab ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onTabSelected(tab) }
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = tab.title,
                        fontSize = 14.sp,
                        fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal,
                        color = Color.White.copy(alpha = if (selectedTab == tab) 1f else 0.7f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                            .background(if (selectedTab == tab) Color.White else Color.Transparent)
                    )
                }
            }
        }
    }
}

/**
 * Home Tab Content
 */
@Composable
private fun HomeTabContent(
    circle: CircleData,
    categories: List<ChannelCategory>,
    announcements: List<AnnouncementData>,
    threads: List<ThreadData>,
    onNavigateToChannel: (Int) -> Unit,
    onShowCircleSwitcher: () -> Unit,
    onNavigateToManageChannels: () -> Unit,
    onShowCreateThread: () -> Unit,
    onShowCreateAnnouncement: () -> Unit,
    onNavigateToDues: () -> Unit,
    onShowCircleSettings: () -> Unit
) {
    val primaryBlue = Color(0xFF004AAD)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Circle Switcher
        item {
            CircleSwitcherCard(
                circleName = circle.name,
                memberCount = circle.memberCount,
                onClick = onShowCircleSwitcher,
                onSettingsClick = onShowCircleSettings
            )
        }

        // Dues Button
        item {
            DuesButton(onClick = onNavigateToDues)
        }

        // Announcements Section
        item {
            AnnouncementsSection(
                announcements = announcements,
                onShowCreateAnnouncement = onShowCreateAnnouncement
            )
        }

        // Threads Section
        item {
            ThreadsSection(
                threads = threads,
                onShowCreateThread = onShowCreateThread
            )
        }

        // Divider
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                primaryBlue.copy(alpha = 0.2f),
                                Color.Transparent
                            )
                        )
                    )
            )
        }

        // Channels Section Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Channels",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "Join conversations by topic",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                }

                Text(
                    text = "${categories.sumOf { it.channels.size }} channels",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = primaryBlue,
                    modifier = Modifier
                        .background(
                            primaryBlue.copy(alpha = 0.1f),
                            RoundedCornerShape(50)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }

        // Channel Categories
        items(categories) { category ->
            ChannelCategorySection(
                category = category,
                onNavigateToChannel = onNavigateToChannel
            )
        }

        // Manage Channels Button (for moderators)
        item {
            TextButton(
                onClick = onNavigateToManageChannels,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Icon(
                    Icons.Filled.Settings,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = primaryBlue
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Manage Channels",
                    color = primaryBlue,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/**
 * Circle Switcher Card
 */
@Composable
private fun CircleSwitcherCard(
    circleName: String,
    memberCount: Int,
    onClick: () -> Unit,
    onSettingsClick: () -> Unit = {}
) {
    val primaryBlue = Color(0xFF004AAD)
    val lightBlue = Color(0xFF0066FF)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circle Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(primaryBlue, lightBlue)
                        ),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.People,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onClick)
            ) {
                Text(
                    text = circleName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Tap to switch circles",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Icon(
                Icons.Filled.ArrowDropDown,
                contentDescription = "Switch",
                tint = primaryBlue,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = onClick)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Settings Icon
            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    Icons.Filled.Settings,
                    contentDescription = "Circle Settings",
                    tint = primaryBlue,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

/**
 * Dues Button
 */
@Composable
private fun DuesButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFFA500)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Icon(
            Icons.Filled.Payment,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            "View Dues & Payments",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}


/**
 * Circle Settings Bottom Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CircleSettingsBottomSheet(
    circle: CircleData,
    onDismiss: () -> Unit,
    onNavigateToMembers: () -> Unit,
    onNavigateToDues: () -> Unit,
    onNavigateToManageChannels: () -> Unit,
    onLeaveCircle: () -> Unit,
    onShowAbout: () -> Unit = {}
) {
    val primaryBlue = Color(0xFF004AAD)
    var enableDashboard by remember { mutableStateOf(true) }
    var dashboardPrivate by remember { mutableStateOf(true) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            // About This Circle
            SettingsMenuItem(
                icon = Icons.Filled.Info,
                title = "About This Circle",
                onClick = {
                    onDismiss()
                    onShowAbout()
                }
            )

            // Members List
            SettingsMenuItem(
                icon = Icons.Filled.People,
                title = "Members List",
                onClick = {
                    onDismiss()
                    onNavigateToMembers()
                }
            )

            // Dues - Always show this option
            SettingsMenuItem(
                icon = Icons.Filled.Payment,
                title = "Dues",
                onClick = {
                    onDismiss()
                    onNavigateToDues()
                }
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Leave Circle
            SettingsMenuItem(
                icon = Icons.Filled.Logout,
                title = "Leave Circle",
                isDestructive = true,
                onClick = onLeaveCircle
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Moderator Options Header
            Text(
                text = "Moderator Options",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )

            // Enable Dashboard Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.Dashboard,
                    contentDescription = null,
                    tint = primaryBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Enable Dashboard",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = enableDashboard,
                    onCheckedChange = { enableDashboard = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = primaryBlue
                    )
                )
            }

            // Dashboard Privacy Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.Lock,
                    contentDescription = null,
                    tint = primaryBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Dashboard Privacy",
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Private",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFFFA500),
                            modifier = Modifier
                                .background(
                                    Color(0xFFFFA500).copy(alpha = 0.1f),
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        text = "Only admins can see das...",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                Switch(
                    checked = dashboardPrivate,
                    onCheckedChange = { dashboardPrivate = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = primaryBlue
                    )
                )
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Manage Channels
            SettingsMenuItem(
                icon = Icons.Filled.Settings,
                title = "Manage Channels",
                onClick = onNavigateToManageChannels
            )

            // Delete Circle
            SettingsMenuItem(
                icon = Icons.Filled.Delete,
                title = "Delete Circle",
                isDestructive = true,
                onClick = { /* TODO: Delete circle */ }
            )
        }
    }
}

/**
 * Settings Menu Item
 */
@Composable
private fun SettingsMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    isDestructive: Boolean = false,
    onClick: () -> Unit
) {
    val primaryBlue = Color(0xFF004AAD)
    val textColor = if (isDestructive) Color.Red else Color.Black
    val iconColor = if (isDestructive) Color.Red else primaryBlue

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            color = textColor
        )
    }
}

/**
 * Leave Circle Dialog
 */
@Composable
private fun LeaveCircleDialog(
    circleName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Leave Circle?") },
        text = {
            Text("Are you sure you want to leave \"$circleName\"? You'll lose access to all channels and content.")
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Leave")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

/**
 * Group Tab Enum
 */
enum class GroupTab(val title: String) {
    DASHBOARD("Dashboard"),
    HOME("Home"),
    CALENDAR("Calendar")
}

/**
 * Data Models
 */
data class ChannelCategory(
    val id: Int,
    val name: String,
    val position: Int = 0,
    val channels: List<Channel>
)

data class Channel(
    val id: Int,
    val name: String,
    val category: String,
    val circleId: Int = 0,
    val position: Int = 0,
    val isModeratorOnly: Boolean = false
)

data class AnnouncementData(
    val id: Int,
    val user: String,
    val title: String,
    val content: String,
    val announcedAt: String
)

data class ThreadData(
    val id: Int,
    val author: String,
    val content: String,
    val replies: Int,
    val createdAt: String
)

