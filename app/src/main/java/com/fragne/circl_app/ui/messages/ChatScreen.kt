package com.fragne.circl_app.ui.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * Chat Screen - Individual conversation view
 * Translated from ChatView.swift
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    user: NetworkUser,
    messages: List<Message>,
    onNavigateBack: () -> Unit = {},
    onNavigateToProfile: (String) -> Unit = {}
) {
    val viewModel: ChatViewModel = viewModel(
        factory = ChatViewModelFactory(user, messages)
    )
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Scroll to bottom when new messages arrive
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(uiState.messages.size - 1)
            }
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.dismissError()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Header
                ChatHeader(
                    user = user,
                    onNavigateBack = onNavigateBack,
                    onProfileClick = { onNavigateToProfile(user.id) },
                    onOptionsClick = { viewModel.toggleOptionsMenu() }
                )

                // Messages
                Box(modifier = Modifier.weight(1f)) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.messages) { message ->
                            MessageBubble(
                                message = message,
                                isCurrentUser = message.senderId == "1" // TODO: Get from user prefs
                            )
                        }
                    }
                }

                // Input bar
                ChatInputBar(
                    messageText = uiState.messageText,
                    onMessageTextChange = { viewModel.updateMessageText(it) },
                    onSendMessage = { viewModel.sendMessage() },
                    isSending = uiState.isSending
                )
            }
        }

        // Options menu overlay
        if (uiState.showOptionsMenu) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable { viewModel.closeOptionsMenu() }
            ) {
                OptionsMenu(
                    onDismiss = { viewModel.closeOptionsMenu() },
                    onViewProfile = {
                        viewModel.closeOptionsMenu()
                        onNavigateToProfile(user.id)
                    }
                )
            }
        }
    }
}

/**
 * Chat Header with back button, profile, and options
 */
@Composable
private fun ChatHeader(
    user: NetworkUser,
    onNavigateBack: () -> Unit,
    onProfileClick: () -> Unit,
    onOptionsClick: () -> Unit
) {
    val primaryBlue = Color(0xFF004AAD)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(primaryBlue)
    ) {
        // Status bar spacer
        Spacer(modifier = Modifier.height(50.dp))

        // Main header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Profile picture
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clickable(onClick = onProfileClick)
            ) {
                if (user.profileImage != null && user.profileImage.isNotEmpty()) {
                    AsyncImage(
                        model = user.profileImage,
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
                            .background(Color.White.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // User info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                if (user.company.isNotEmpty()) {
                    Text(
                        text = user.company,
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            // Options button
            IconButton(onClick = onOptionsClick) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Options",
                    tint = Color.White
                )
            }
        }
    }
}

/**
 * Message Bubble
 */
@Composable
private fun MessageBubble(
    message: ChatMessage,
    isCurrentUser: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isCurrentUser) {
            // Other user's avatar
            if (message.senderImage != null && message.senderImage.isNotEmpty()) {
                AsyncImage(
                    model = message.senderImage,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color(0xFFE0E0E0), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            // Message bubble
            Surface(
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomStart = if (isCurrentUser) 20.dp else 4.dp,
                    bottomEnd = if (isCurrentUser) 4.dp else 20.dp
                ),
                color = if (isCurrentUser) Color(0xFF004AAD) else Color(0xFFF0F0F0),
                shadowElevation = 1.dp
            ) {
                Text(
                    text = message.content,
                    fontSize = 16.sp,
                    color = if (isCurrentUser) Color.White else Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Timestamp
            Text(
                text = formatChatTimestamp(message.timestamp),
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }

        if (isCurrentUser) {
            Spacer(modifier = Modifier.width(8.dp))

            // Current user avatar (optional, can be removed)
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color(0xFF004AAD).copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = Color(0xFF004AAD),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

/**
 * Chat Input Bar
 */
@Composable
private fun ChatInputBar(
    messageText: String,
    onMessageTextChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    isSending: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            // Message input
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFFF5F5F5)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = messageText,
                        onValueChange = onMessageTextChange,
                        modifier = Modifier.weight(1f),
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            color = Color.Black
                        ),
                        decorationBox = { innerTextField ->
                            if (messageText.isEmpty()) {
                                Text(
                                    "Type a message...",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        color = Color.Gray
                                    )
                                )
                            }
                            innerTextField()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Send button
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        if (messageText.isNotEmpty() && !isSending) Color(0xFF004AAD) else Color(0xFF004AAD).copy(alpha = 0.3f),
                        CircleShape
                    )
                    .clickable(enabled = messageText.isNotEmpty() && !isSending) {
                        onSendMessage()
                    },
                contentAlignment = Alignment.Center
            ) {
                if (isSending) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

/**
 * Options Menu
 */
@Composable
private fun BoxScope.OptionsMenu(
    onDismiss: () -> Unit,
    onViewProfile: () -> Unit
) {
    Surface(
        modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(top = 120.dp, end = 16.dp)
            .widthIn(max = 200.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            OptionsMenuItem(
                icon = Icons.Filled.Person,
                text = "View Profile",
                onClick = onViewProfile
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))

            OptionsMenuItem(
                icon = Icons.Filled.Block,
                text = "Block User",
                onClick = { /* TODO */ }
            )

            OptionsMenuItem(
                icon = Icons.Filled.Delete,
                text = "Delete Chat",
                onClick = { /* TODO */ },
                textColor = Color.Red
            )
        }
    }
}

@Composable
private fun OptionsMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit,
    textColor: Color = Color.Black
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            color = textColor
        )
    }
}

private fun formatChatTimestamp(timestamp: String): String {
    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.US)
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val date = formatter.parse(timestamp) ?: return ""

        val displayFormatter = SimpleDateFormat("h:mm a", Locale.US)
        displayFormatter.format(date)
    } catch (e: Exception) {
        ""
    }
}

