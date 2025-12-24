package com.fragne.circl_app.ui.circles.dashboard

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fragne.circl_app.ui.circles.home.Channel
import com.fragne.circl_app.ui.circles.home.ChannelCategory
import java.text.SimpleDateFormat
import java.util.*

/**
 * Circle Channel Messages Screen
 * Translated from PageCircleMessages.swift
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CircleChannelMessagesScreen(
    channel: Channel,
    circleName: String,
    onNavigateBack: () -> Unit = {},
    onNavigateToMembers: () -> Unit = {},
    onNavigateToProfile: (Int) -> Unit = {}
) {
    var newMessage by remember { mutableStateOf("") }
    var currentChannel by remember { mutableStateOf(channel) }
    var showCircleMenu by remember { mutableStateOf(false) }
    var showCategoryMenu by remember { mutableStateOf(false) }
    var showLeaveDialog by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

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

    var selectedCategory by remember {
        mutableStateOf(
            categories.firstOrNull { cat ->
                cat.channels.any { it.id == channel.id }
            } ?: categories.firstOrNull()
        )
    }

    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                id = 1,
                sender = "John Doe",
                content = "Welcome to the channel!",
                isCurrentUser = false,
                timestamp = Date(),
                mediaURL = null
            ),
            ChatMessage(
                id = 2,
                sender = "You",
                content = "Thanks! Excited to be here.",
                isCurrentUser = true,
                timestamp = Date(),
                mediaURL = null
            ),
            ChatMessage(
                id = 3,
                sender = "Jane Smith",
                content = "Let's collaborate on the new project!",
                isCurrentUser = false,
                timestamp = Date(),
                mediaURL = null
            )
        )
    }

    val members = remember {
        listOf(
            CircleMember(1, "John Doe", "", 101),
            CircleMember(2, "Jane Smith", "", 102),
            CircleMember(3, "Mike Johnson", "", 103)
        )
    }

    val primaryBlue = Color(0xFF004AAD)
    val lightBlue = Color(0xFF0052CC)

    Scaffold(
        topBar = {
            ChannelMessagesHeader(
                circleName = circleName,
                currentChannel = currentChannel,
                selectedCategory = selectedCategory,
                categories = categories,
                memberCount = members.size,
                showCircleMenu = showCircleMenu,
                showCategoryMenu = showCategoryMenu,
                onBackClick = onNavigateBack,
                onCircleMenuToggle = { showCircleMenu = !showCircleMenu },
                onCategoryMenuToggle = { showCategoryMenu = !showCategoryMenu },
                onChannelSelected = { newChannel ->
                    currentChannel = newChannel
                    // TODO: Fetch messages for new channel
                }
            )
        },
        bottomBar = {
            MessageInputBar(
                message = newMessage,
                onMessageChange = { newMessage = it },
                onSendClick = {
                    if (newMessage.isNotBlank()) {
                        messages.add(
                            ChatMessage(
                                id = messages.size + 1,
                                sender = "You",
                                content = newMessage,
                                isCurrentUser = true,
                                timestamp = Date(),
                                mediaURL = null
                            )
                        )
                        newMessage = ""
                    }
                },
                onAttachClick = { /* TODO: Attach media */ }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Messages List
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages) { message ->
                    ChatBubble(
                        message = message,
                        onProfileClick = { onNavigateToProfile(1) }
                    )
                }
            }

            // Circle Menu Overlay
            if (showCircleMenu) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                        .clickable { showCircleMenu = false }
                ) {
                    CircleMenu(
                        onNavigateToMembers = {
                            showCircleMenu = false
                            onNavigateToMembers()
                        },
                        onLeaveCircle = {
                            showCircleMenu = false
                            showLeaveDialog = true
                        }
                    )
                }
            }

            // Category Menu Overlay
            if (showCategoryMenu) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                        .clickable { showCategoryMenu = false }
                ) {
                    CategoryMenu(
                        categories = categories,
                        selectedCategory = selectedCategory,
                        onCategorySelected = { category ->
                            selectedCategory = category
                            showCategoryMenu = false
                            // Switch to first channel in category
                            category.channels.firstOrNull()?.let { firstChannel ->
                                currentChannel = firstChannel
                                // TODO: Fetch messages
                            }
                        }
                    )
                }
            }
        }
    }

    // Leave Circle Dialog
    if (showLeaveDialog) {
        AlertDialog(
            onDismissRequest = { showLeaveDialog = false },
            title = { Text("Leave Circle?") },
            text = { Text("Are you sure you want to leave this circle? You'll lose access to all channels.") },
            confirmButton = {
                Button(
                    onClick = {
                        showLeaveDialog = false
                        onNavigateBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Leave")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLeaveDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Auto-scroll to bottom when new messages arrive
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }
}

/**
 * Channel Messages Header
 */
@Composable
private fun ChannelMessagesHeader(
    circleName: String,
    currentChannel: Channel,
    selectedCategory: ChannelCategory?,
    categories: List<ChannelCategory>,
    memberCount: Int,
    showCircleMenu: Boolean,
    showCategoryMenu: Boolean,
    onBackClick: () -> Unit,
    onCircleMenuToggle: () -> Unit,
    onCategoryMenuToggle: () -> Unit,
    onChannelSelected: (Channel) -> Unit
) {
    val primaryBlue = Color(0xFF004AAD)
    val lightBlue = Color(0xFF0052CC)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    colors = listOf(primaryBlue, lightBlue)
                )
            )
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // Top row with back button, circle name, and menu
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button
            Button(
                onClick = onBackClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.15f)
                ),
                shape = RoundedCornerShape(18.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(17.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Back",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }

            // Circle Name and Category Selector
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Circle Name with Dropdown
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = circleName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1
                    )
                    IconButton(
                        onClick = onCircleMenuToggle,
                        modifier = Modifier.size(18.dp)
                    ) {
                        Icon(
                            imageVector = if (showCircleMenu) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                            contentDescription = "Menu",
                            tint = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Category and Channel Info Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Category Selector
                    Button(
                        onClick = onCategoryMenuToggle,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(14.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Icon(
                            Icons.Filled.Tag,
                            contentDescription = null,
                            tint = primaryBlue,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = selectedCategory?.name ?: "Select",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = primaryBlue
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = if (showCategoryMenu) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                            contentDescription = null,
                            tint = primaryBlue,
                            modifier = Modifier.size(9.dp)
                        )
                    }

                    // Member Count
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.People,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "$memberCount",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // Online Indicator
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color.Green, CircleShape)
                        )
                        Text(
                            text = "Online",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(80.dp)) // Balance the back button
        }

        // Channel Tabs (if category selected)
        selectedCategory?.let { category ->
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(category.channels) { ch ->
                    ChannelTab(
                        channel = ch,
                        isSelected = currentChannel.id == ch.id,
                        onClick = { onChannelSelected(ch) }
                    )
                }
            }
        }
    }
}

/**
 * Channel Tab
 */
@Composable
private fun ChannelTab(
    channel: Channel,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val primaryBlue = Color(0xFF004AAD)

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color.White else Color.White.copy(alpha = 0.2f)
        ),
        shape = RoundedCornerShape(50),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = "#${channel.name}",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) primaryBlue else Color.White
        )
    }
}

/**
 * Chat Bubble
 */
@Composable
private fun ChatBubble(
    message: ChatMessage,
    onProfileClick: () -> Unit
) {
    val primaryBlue = Color(0xFF004AAD)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = if (message.isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        if (!message.isCurrentUser) {
            // Sender Avatar
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(primaryBlue.copy(alpha = 0.2f), CircleShape)
                    .clickable(onClick = onProfileClick),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = message.sender.first().toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryBlue
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            modifier = Modifier.widthIn(max = 280.dp),
            horizontalAlignment = if (message.isCurrentUser) Alignment.End else Alignment.Start
        ) {
            if (!message.isCurrentUser) {
                Text(
                    text = message.sender,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = primaryBlue,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Card(
                shape = RoundedCornerShape(
                    topStart = if (message.isCurrentUser) 16.dp else 4.dp,
                    topEnd = if (message.isCurrentUser) 4.dp else 16.dp,
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = if (message.isCurrentUser) primaryBlue else Color(0xFFF0F0F0)
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = message.content,
                        fontSize = 15.sp,
                        color = if (message.isCurrentUser) Color.White else Color.Black,
                        lineHeight = 20.sp
                    )

                    message.mediaURL?.let { url ->
                        Spacer(modifier = Modifier.height(8.dp))
                        AsyncImage(
                            model = url,
                            contentDescription = "Attached media",
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 200.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formatTimestamp(message.timestamp),
                        fontSize = 11.sp,
                        color = if (message.isCurrentUser) Color.White.copy(alpha = 0.7f) else Color.Gray
                    )
                }
            }
        }
    }
}

/**
 * Message Input Bar
 */
@Composable
private fun MessageInputBar(
    message: String,
    onMessageChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onAttachClick: () -> Unit
) {
    val primaryBlue = Color(0xFF004AAD)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Attach Button
            IconButton(
                onClick = onAttachClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Attach",
                    tint = primaryBlue
                )
            }

            // Text Field
            OutlinedTextField(
                value = message,
                onValueChange = onMessageChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message...") },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryBlue,
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f)
                ),
                maxLines = 4
            )

            // Send Button
            IconButton(
                onClick = onSendClick,
                enabled = message.isNotBlank(),
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        if (message.isNotBlank()) primaryBlue else Color.Gray.copy(alpha = 0.3f),
                        CircleShape
                    )
            ) {
                Icon(
                    Icons.Filled.Send,
                    contentDescription = "Send",
                    tint = Color.White
                )
            }
        }
    }
}

/**
 * Circle Menu
 */
@Composable
private fun CircleMenu(
    onNavigateToMembers: () -> Unit,
    onLeaveCircle: () -> Unit
) {
    val primaryBlue = Color(0xFF004AAD)

    Card(
        modifier = Modifier
            .width(250.dp)
            .padding(start = 16.dp, top = 100.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            MenuItem(icon = Icons.Filled.Info, title = "About This Circle")
            MenuItem(icon = Icons.Filled.PersonAdd, title = "Invite Network")
            MenuItem(
                icon = Icons.Filled.People,
                title = "Members List",
                onClick = onNavigateToMembers
            )
            MenuItem(icon = Icons.Filled.PushPin, title = "Pinned Messages")
            MenuItem(icon = Icons.Filled.Folder, title = "Group Files")
            MenuItem(icon = Icons.Filled.Notifications, title = "Notification Settings")

            Divider()

            MenuItem(
                icon = Icons.Filled.ExitToApp,
                title = "Leave Circle",
                isDestructive = true,
                onClick = onLeaveCircle
            )
        }
    }
}

/**
 * Category Menu
 */
@Composable
private fun CategoryMenu(
    categories: List<ChannelCategory>,
    selectedCategory: ChannelCategory?,
    onCategorySelected: (ChannelCategory) -> Unit
) {
    val primaryBlue = Color(0xFF004AAD)

    Card(
        modifier = Modifier
            .width(270.dp)
            .padding(start = 16.dp, top = 100.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Select Category",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            categories.forEach { category ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCategorySelected(category) }
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = category.name,
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                    if (selectedCategory?.id == category.id) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = "Selected",
                            tint = primaryBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                if (category != categories.last()) {
                    Divider()
                }
            }
        }
    }
}

/**
 * Menu Item
 */
@Composable
private fun MenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    isDestructive: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val primaryBlue = Color(0xFF004AAD)
    val textColor = if (isDestructive) Color.Red else Color.Black
    val iconColor = if (isDestructive) Color.Red else primaryBlue

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = title,
            fontSize = 15.sp,
            color = textColor
        )
    }
}

/**
 * Format Timestamp
 */
private fun formatTimestamp(date: Date): String {
    val formatter = SimpleDateFormat("h:mm a", Locale.getDefault())
    return formatter.format(date)
}

/**
 * Data Models
 */
data class ChatMessage(
    val id: Int,
    val sender: String,
    val content: String,
    val isCurrentUser: Boolean,
    val timestamp: Date,
    val mediaURL: String?
)

data class CircleMember(
    val id: Int,
    val fullName: String,
    val profileImage: String,
    val userId: Int
)

