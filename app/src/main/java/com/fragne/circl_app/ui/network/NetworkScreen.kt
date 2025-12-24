package com.fragne.circl_app.ui.network

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

/**
 * Main Network Screen - Unified networking with entrepreneurs, mentors, and connections
 * Translated from PageUnifiedNetworking.swift
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkScreen(
    onNavigateToProfile: () -> Unit = {},
    onNavigateToMessages: () -> Unit = {},
    onNavigateToUserProfile: (Int) -> Unit = {},
    viewModel: NetworkViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val primaryBlue = Color(0xFF004AAD)


    // Show snackbar for messages
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.dismissError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header with gradient background
            NetworkHeader(
                userProfileImageUrl = uiState.userProfileImageUrl,
                unreadMessageCount = uiState.unreadMessageCount,
                selectedTab = uiState.selectedTab,
                onTabSelected = { viewModel.selectTab(it) },
                onProfileClick = onNavigateToProfile,
                onMessagesClick = onNavigateToMessages
            )

            // Content area
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            ) {
                when (uiState.selectedTab) {
                    NetworkTab.ENTREPRENEURS -> EntrepreneursContent(
                        entrepreneurs = uiState.entrepreneurs,
                        declinedUserIds = uiState.declinedUserIds,
                        isLoading = uiState.isLoading,
                        onProfileClick = onNavigateToUserProfile,
                        onConnectClick = { userId, email ->
                            viewModel.sendConnectionRequest(userId, email)
                        }
                    )
                    NetworkTab.MENTORS -> MentorsContent(
                        mentors = uiState.mentors,
                        declinedUserIds = uiState.declinedUserIds,
                        isLoading = uiState.isLoading,
                        onProfileClick = onNavigateToUserProfile,
                        onConnectClick = { userId, email ->
                            viewModel.sendConnectionRequest(userId, email)
                        }
                    )
                    NetworkTab.MY_NETWORK -> MyNetworkContent(
                        connections = uiState.myNetwork,
                        pendingRequests = uiState.pendingRequests,
                        connectionCount = uiState.connectionCount,
                        activeCount = uiState.activeCount,
                        isLoading = uiState.isLoading,
                        onProfileClick = onNavigateToUserProfile,
                        onAcceptRequest = { requestId, senderId ->
                            viewModel.acceptFriendRequest(requestId, senderId)
                        },
                        onDeclineRequest = { requestId ->
                            viewModel.declineFriendRequest(requestId)
                        },
                        onFindConnectionsClick = {
                            viewModel.selectTab(NetworkTab.ENTREPRENEURS)
                        }
                    )
                }
            }
        }
    }
}

/**
 * Network Header - Top bar with tabs
 */
@Composable
private fun NetworkHeader(
    userProfileImageUrl: String,
    unreadMessageCount: Int,
    selectedTab: NetworkTab,
    onTabSelected: (NetworkTab) -> Unit,
    onProfileClick: () -> Unit,
    onMessagesClick: () -> Unit
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
            .statusBarsPadding() // Extend to status bar
    ) {
        // Top bar with profile, logo, and messages
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile button
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

                // Online indicator
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = 2.dp, y = 2.dp)
                        .background(Color.Green, CircleShape)
                        .then(
                            Modifier.padding(1.dp)
                        )
                )
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

            // Messages button with badge
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clickable(onClick = onMessagesClick)
            ) {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = "Messages",
                    tint = Color.White,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp)
                )

                if (unreadMessageCount > 0) {
                    Badge(
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Text(
                            text = if (unreadMessageCount > 99) "99+" else unreadMessageCount.toString(),
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }

        // Tab selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            NetworkTab.entries.forEach { tab ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onTabSelected(tab) },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = tab.compactTitle,
                        fontSize = 16.sp,
                        fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Medium,
                        color = Color.White.copy(alpha = if (selectedTab == tab) 1f else 0.7f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Underline indicator
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(if (selectedTab == tab) 3.dp else 0.dp)
                            .background(Color.White)
                    )
                }
            }
        }
    }
}

/**
 * Entrepreneurs Tab Content
 */
@Composable
private fun EntrepreneursContent(
    entrepreneurs: List<EntrepreneurProfile>,
    declinedUserIds: Set<Int>,
    isLoading: Boolean,
    onProfileClick: (Int) -> Unit,
    onConnectClick: (Int, String) -> Unit
) {
    val filteredEntrepreneurs = entrepreneurs.filter { it.userId !in declinedUserIds }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        if (isLoading) {
            item {
                LoadingCard("Discovering entrepreneurs...")
            }
        } else if (filteredEntrepreneurs.isEmpty()) {
            item {
                EmptyStateCard(
                    icon = Icons.Outlined.Group,
                    title = "No entrepreneurs found",
                    subtitle = "Check back later for new connections and growth partners",
                    iconTint = Color(0xFF004AAD)
                )
            }
        } else {
            items(filteredEntrepreneurs) { entrepreneur ->
                EntrepreneurCard(
                    entrepreneur = entrepreneur,
                    onProfileClick = { onProfileClick(entrepreneur.userId) },
                    onConnectClick = { onConnectClick(entrepreneur.userId, entrepreneur.email) }
                )
            }
        }
    }
}

/**
 * Mentors Tab Content
 */
@Composable
private fun MentorsContent(
    mentors: List<MentorProfile>,
    declinedUserIds: Set<Int>,
    isLoading: Boolean,
    onProfileClick: (Int) -> Unit,
    onConnectClick: (Int, String) -> Unit
) {
    val filteredMentors = mentors.filter { it.userId !in declinedUserIds }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        if (isLoading) {
            item {
                LoadingCard("Finding expert mentors...")
            }
        } else if (filteredMentors.isEmpty()) {
            item {
                EmptyStateCard(
                    icon = Icons.Outlined.School,
                    title = "No mentors available",
                    subtitle = "Expert mentors will appear here when available to guide your journey",
                    iconTint = Color(0xFFFFA500)
                )
            }
        } else {
            items(filteredMentors) { mentor ->
                MentorCard(
                    mentor = mentor,
                    onProfileClick = { onProfileClick(mentor.userId) },
                    onConnectClick = { onConnectClick(mentor.userId, mentor.email) }
                )
            }
        }
    }
}

/**
 * My Network Tab Content
 */
@Composable
private fun MyNetworkContent(
    connections: List<NetworkConnection>,
    pendingRequests: List<FriendRequest>,
    connectionCount: Int,
    activeCount: Int,
    isLoading: Boolean,
    onProfileClick: (Int) -> Unit,
    onAcceptRequest: (Int, Int) -> Unit,
    onDeclineRequest: (Int) -> Unit,
    onFindConnectionsClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        if (isLoading) {
            item {
                LoadingCard("Loading your network...")
            }
        } else {
            // Network stats
            item {
                NetworkStats(
                    connectionCount = connectionCount,
                    activeCount = activeCount
                )
            }

            // Pending requests
            if (pendingRequests.isNotEmpty()) {
                item {
                    Text(
                        text = "Pending Requests",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(pendingRequests) { request ->
                    FriendRequestCard(
                        request = request,
                        onAccept = { onAcceptRequest(request.requestId, request.senderId) },
                        onDecline = { onDeclineRequest(request.requestId) },
                        onProfileClick = { onProfileClick(request.senderId) }
                    )
                }
            }

            // Connections
            if (connections.isNotEmpty()) {
                item {
                    Text(
                        text = "My Connections",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(connections) { connection ->
                    NetworkConnectionCard(
                        connection = connection,
                        onProfileClick = { onProfileClick(connection.userId) }
                    )
                }
            } else {
                item {
                    EmptyNetworkState(onFindConnectionsClick = onFindConnectionsClick)
                }
            }
        }
    }
}

