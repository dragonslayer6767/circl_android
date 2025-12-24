package com.fragne.circl_app.ui.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.*

/**
 * Messages Screen - List of conversations
 * Translated from PageMessages.swift
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(
    onNavigateToProfile: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToChat: (NetworkUser, List<Message>) -> Unit = { _, _ -> },
    onNavigateToUserProfile: (String) -> Unit = {},
    onNavigateToNetwork: () -> Unit = {},
    onNavigateToCircles: () -> Unit = {},
    onNavigateToGrowthHub: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    viewModel: MessagesViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.dismissError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            MessagesBottomNavigationBar(
                onNavigateToHome = onNavigateToHome,
                onNavigateToNetwork = onNavigateToNetwork,
                onNavigateToCircles = onNavigateToCircles,
                onNavigateToGrowthHub = onNavigateToGrowthHub,
                onNavigateToSettings = onNavigateToSettings
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header
            MessagesHeader(
                userProfileImageUrl = uiState.userProfileImageUrl,
                onProfileClick = onNavigateToProfile,
                onHomeClick = onNavigateToHome
            )

            // Search bar and suggested users
            SearchSection(
                searchText = uiState.searchText,
                onSearchTextChange = { viewModel.updateSearchText(it) },
                onClearSearch = { viewModel.clearSearch() },
                suggestedUsers = uiState.suggestedUsers,
                selectedUser = uiState.selectedUser,
                onUserSelected = { user ->
                    viewModel.selectUser(user)
                },
                onNavigateToChat = { user ->
                    val messages = uiState.conversations
                        .find { it.user.id == user.id }
                        ?.messages ?: emptyList()
                    onNavigateToChat(user, messages)
                    viewModel.clearSearch()
                }
            )

            // Conversations list
            ConversationsList(
                conversations = uiState.conversations,
                isLoading = uiState.isLoading,
                onConversationClick = { conversation ->
                    onNavigateToChat(conversation.user, conversation.messages)
                    viewModel.markAsRead(conversation.user.id)
                },
                onProfileClick = { userId ->
                    onNavigateToUserProfile(userId)
                },
                onRefresh = { viewModel.refreshMessages() }
            )
        }
    }
}

/**
 * Messages Header - Blue gradient with profile and home
 */
@Composable
private fun MessagesHeader(
    userProfileImageUrl: String,
    onProfileClick: () -> Unit,
    onHomeClick: () -> Unit
) {
    val primaryBlue = Color(0xFF004AAD)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        primaryBlue,
                        primaryBlue.copy(alpha = 0.95f)
                    )
                )
            )
            .padding(top = 50.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile
            Box(
                modifier = Modifier
                    .size(36.dp)
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
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.weight(1f))

            // Home icon
            IconButton(onClick = onHomeClick) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Home",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

/**
 * Search Section with suggestions dropdown
 */
@Composable
private fun SearchSection(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    suggestedUsers: List<NetworkUser>,
    selectedUser: NetworkUser?,
    onUserSelected: (NetworkUser) -> Unit,
    onNavigateToChat: (NetworkUser) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Search bar with arrow button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Search input
            Row(
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(25.dp))
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                BasicTextField(
                    value = searchText,
                    onValueChange = onSearchTextChange,
                    modifier = Modifier.weight(1f),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        if (searchText.isEmpty()) {
                            Text(
                                "Search for users in your network...",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    color = Color.Gray
                                )
                            )
                        }
                        innerTextField()
                    }
                )

                if (searchText.isNotEmpty()) {
                    IconButton(
                        onClick = onClearSearch,
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Clear",
                            tint = Color.Gray
                        )
                    }
                }
            }

            // Arrow button to navigate to chat
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = if (selectedUser != null) Color(0xFF004AAD) else Color(0xFF004AAD).copy(alpha = 0.3f),
                        shape = CircleShape
                    )
                    .clickable(enabled = selectedUser != null) {
                        selectedUser?.let { onNavigateToChat(it) }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowRight,
                    contentDescription = "Go to chat",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Suggested users dropdown
        if (suggestedUsers.isNotEmpty() && searchText.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column {
                    suggestedUsers.take(5).forEachIndexed { index, user ->
                        SuggestedUserItem(
                            user = user,
                            onClick = { onUserSelected(user) }
                        )

                        if (index < suggestedUsers.take(5).size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = Color.LightGray.copy(alpha = 0.3f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SuggestedUserItem(
    user: NetworkUser,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        if (user.profileImage != null && user.profileImage.isNotEmpty()) {
            AsyncImage(
                model = user.profileImage,
                contentDescription = "Profile",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFE0E0E0), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = user.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = user.company.ifEmpty { "Network Connection" },
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
}

/**
 * Conversations List
 */
@Composable
private fun ConversationsList(
    conversations: List<Conversation>,
    isLoading: Boolean,
    onConversationClick: (Conversation) -> Unit,
    onProfileClick: (String) -> Unit,
    onRefresh: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        Color(0xFF004AAD).copy(alpha = 0.02f)
                    )
                )
            )
    ) {
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            conversations.isEmpty() -> {
                EmptyMessagesState(onRefresh = onRefresh)
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(conversations) { conversation ->
                        ConversationCard(
                            conversation = conversation,
                            onClick = { onConversationClick(conversation) },
                            onProfileClick = { onProfileClick(conversation.user.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ConversationCard(
    conversation: Conversation,
    onClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (conversation.hasUnread) {
                Color(0xFFE3F2FD) // Light blue shade for unread
            } else {
                Color.White
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile image with online indicator
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clickable(onClick = onProfileClick)
            ) {
                if (conversation.user.profileImage != null && conversation.user.profileImage.isNotEmpty()) {
                    AsyncImage(
                        model = conversation.user.profileImage,
                        contentDescription = "Profile",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF004AAD).copy(alpha = 0.3f),
                                        Color(0xFF004AAD).copy(alpha = 0.1f)
                                    )
                                ),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null,
                            tint = Color(0xFF004AAD),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                // Online indicator
                if (conversation.user.isOnline) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.BottomEnd)
                            .background(Color.Green, CircleShape)
                            .then(
                                Modifier.padding(2.dp)
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Conversation details
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = conversation.user.name,
                        fontSize = 17.sp,
                        fontWeight = if (conversation.hasUnread) FontWeight.SemiBold else FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = formatTimestamp(conversation.timestamp),
                        fontSize = 14.sp,
                        color = if (conversation.hasUnread) Color(0xFF004AAD) else Color.Gray,
                        fontWeight = if (conversation.hasUnread) FontWeight.Medium else FontWeight.Normal
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = conversation.lastMessage,
                        fontSize = 15.sp,
                        color = if (conversation.hasUnread) MaterialTheme.colorScheme.onSurface else Color.Gray,
                        fontWeight = if (conversation.hasUnread) FontWeight.Medium else FontWeight.Normal,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    if (conversation.unreadCount > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Badge(
                            containerColor = Color(0xFF004AAD)
                        ) {
                            Text(
                                text = if (conversation.unreadCount > 99) "99+" else conversation.unreadCount.toString(),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = Color.Gray.copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun EmptyMessagesState(onRefresh: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.padding(40.dp)
        ) {
            // Animated icon
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF004AAD).copy(alpha = 0.15f),
                                Color(0xFF004AAD).copy(alpha = 0.05f),
                                Color.Transparent
                            )
                        ),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Chat,
                    contentDescription = null,
                    tint = Color(0xFF004AAD),
                    modifier = Modifier.size(48.dp)
                )
            }

            // Text content
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Your Conversations",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "Connect with your network and start meaningful conversations",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }

            // Refresh button
            Button(
                onClick = onRefresh,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF004AAD)
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Refresh Messages",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

private fun formatTimestamp(timestamp: String): String {
    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.US)
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val date = formatter.parse(timestamp) ?: return ""

        val now = Date()
        val timeInterval = (now.time - date.time) / 1000 // in seconds

        when {
            timeInterval < 60 -> "Just now"
            timeInterval < 3600 -> "${timeInterval / 60}m"
            timeInterval < 86400 -> "${timeInterval / 3600}h"
            timeInterval < 604800 -> "${timeInterval / 86400}d"
            else -> {
                val displayFormatter = SimpleDateFormat("MMM d", Locale.US)
                displayFormatter.format(date)
            }
        }
    } catch (e: Exception) {
        ""
    }
}

/**
 * Bottom Navigation Bar for Messages Screen
 */
@Composable
private fun MessagesBottomNavigationBar(
    onNavigateToHome: () -> Unit,
    onNavigateToNetwork: () -> Unit,
    onNavigateToCircles: () -> Unit,
    onNavigateToGrowthHub: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    NavigationBar {
        // Home
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("Home") },
            selected = false,
            onClick = onNavigateToHome
        )

        // Network
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = "Network"
                )
            },
            label = { Text("Network") },
            selected = false,
            onClick = onNavigateToNetwork
        )

        // Circles
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.People,
                    contentDescription = "Circles"
                )
            },
            label = { Text("Circles") },
            selected = false,
            onClick = onNavigateToCircles
        )

        // Growth Hub
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.AttachMoney,
                    contentDescription = "Growth Hub"
                )
            },
            label = { Text("Growth Hub") },
            selected = false,
            onClick = onNavigateToGrowthHub
        )

        // Settings
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings"
                )
            },
            label = { Text("Settings") },
            selected = false,
            onClick = onNavigateToSettings
        )
    }
}
