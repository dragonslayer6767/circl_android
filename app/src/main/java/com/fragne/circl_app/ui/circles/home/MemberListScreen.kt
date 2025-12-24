package com.fragne.circl_app.ui.circles.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

/**
 * Member List Screen
 * Shows all members of a circle with basic information
 * Translated from MemberListPage.swift
 */
data class CircleMember(
    val id: Int,
    val fullName: String,
    val profileImage: String?,
    val userId: Int,
    val isModerator: Boolean,
    val email: String,
    val hasPaid: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberListScreen(
    circleId: Int,
    circleName: String,
    currentUserId: Int,
    onNavigateBack: () -> Unit = {},
    onViewProfile: (Int) -> Unit = {}
) {
    var members by remember { mutableStateOf<List<CircleMember>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedMember by remember { mutableStateOf<CircleMember?>(null) }

    val primaryBlue = Color(0xFF004AAD)

    LaunchedEffect(circleId) {
        // TODO: Replace with actual API call
        fetchMembers(circleId) { fetchedMembers ->
            members = fetchedMembers
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Members",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.MoreVert, // Use back arrow in production
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryBlue
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = primaryBlue
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Header
                    Text(
                        text = "Members in $circleName",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Members List
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(members) { member ->
                            MemberListItem(
                                member = member,
                                currentUserId = currentUserId,
                                onMemberClick = {
                                    selectedMember = member
                                    onViewProfile(member.userId)
                                },
                                onPromoteToModerator = {
                                    promoteToModerator(circleId, member.userId, currentUserId) {
                                        // Refresh list
                                        fetchMembers(circleId) { fetchedMembers ->
                                            members = fetchedMembers
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MemberListItem(
    member: CircleMember,
    currentUserId: Int,
    onMemberClick: () -> Unit,
    onPromoteToModerator: () -> Unit
) {
    val primaryBlue = Color(0xFF004AAD)
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onMemberClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Image
            AsyncImage(
                model = member.profileImage,
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Member Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = member.fullName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Payment Status
                Text(
                    text = if (member.hasPaid) "Paid ✅" else "Not Paid ❌",
                    fontSize = 12.sp,
                    color = if (member.hasPaid) Color(0xFF4CAF50) else Color(0xFFF44336)
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Email
                Text(
                    text = member.email,
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            // Menu (only show for non-current user and non-moderators)
            if (member.userId != currentUserId && !member.isModerator) {
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            Icons.Filled.MoreVert,
                            contentDescription = "More Options",
                            tint = Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Make Moderator") },
                            onClick = {
                                showMenu = false
                                onPromoteToModerator()
                            }
                        )
                    }
                }
            }
        }
    }
}

// Mock fetch function - TODO: Replace with actual API call
private fun fetchMembers(
    circleId: Int,
    onResult: (List<CircleMember>) -> Unit
) {
    // Simulate API call
    val mockMembers = listOf(
        CircleMember(
            id = 1,
            fullName = "John Doe",
            profileImage = null,
            userId = 101,
            isModerator = false,
            email = "john.doe@example.com",
            hasPaid = true
        ),
        CircleMember(
            id = 2,
            fullName = "Jane Smith",
            profileImage = null,
            userId = 102,
            isModerator = false,
            email = "jane.smith@example.com",
            hasPaid = false
        )
    )
    onResult(mockMembers)
}

// TODO: Implement actual API call
internal fun promoteToModerator(
    circleId: Int,
    userId: Int,
    requestingUserId: Int,
    onSuccess: () -> Unit
) {
    // POST /api/circles/make_moderator/
    // For now, just call success
    onSuccess()
}

