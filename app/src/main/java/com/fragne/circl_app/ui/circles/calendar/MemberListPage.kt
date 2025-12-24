package com.fragne.circl_app.ui.circles.calendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
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
 * Member List Page - Shows all members in a circle
 * Translated from MemberListPage.swift
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberListPage(
    circleName: String,
    circleId: Int,
    currentUserId: Int,
    onNavigateBack: () -> Unit = {},
    onMemberClick: (Member) -> Unit = {}
) {
    var members by remember { mutableStateOf<List<Member>>(emptyList()) }
    var showPromoteDialog by remember { mutableStateOf(false) }
    var selectedMemberToPromote by remember { mutableStateOf<Member?>(null) }

    val primaryBlue = Color(0xFF004AAD)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Members") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title
            Text(
                text = "Members in $circleName",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            // Members List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(members) { member ->
                    MemberItem(
                        member = member,
                        currentUserId = currentUserId,
                        onMemberClick = { onMemberClick(member) },
                        onPromoteModerator = {
                            selectedMemberToPromote = member
                            showPromoteDialog = true
                        }
                    )
                }

                // Empty State
                if (members.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Filled.People,
                                    contentDescription = null,
                                    tint = Color.Gray.copy(alpha = 0.4f),
                                    modifier = Modifier.size(64.dp)
                                )
                                Text(
                                    text = "No members found",
                                    fontSize = 16.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Promote to Moderator Dialog
    if (showPromoteDialog && selectedMemberToPromote != null) {
        AlertDialog(
            onDismissRequest = { showPromoteDialog = false },
            title = { Text("Make Moderator") },
            text = { Text("Are you sure you want to make ${selectedMemberToPromote?.fullName} a moderator?") },
            confirmButton = {
                Button(
                    onClick = {
                        selectedMemberToPromote?.let { member ->
                            // TODO: API call to promote
                            // promoteToModerator(member.userId, circleId, currentUserId)
                        }
                        showPromoteDialog = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPromoteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Load members on appear
    LaunchedEffect(circleId) {
        // TODO: Fetch members from API
        // For now, using mock data
        members = listOf(
            Member(
                id = 1,
                fullName = "John Doe",
                profileImage = "",
                userId = 101,
                isModerator = true,
                email = "john@example.com",
                hasPaid = true
            ),
            Member(
                id = 2,
                fullName = "Jane Smith",
                profileImage = "",
                userId = 102,
                isModerator = false,
                email = "jane@example.com",
                hasPaid = true
            ),
            Member(
                id = 3,
                fullName = "Bob Johnson",
                profileImage = "",
                userId = 103,
                isModerator = false,
                email = "bob@example.com",
                hasPaid = false
            )
        )
    }
}

/**
 * Member Item Component
 */
@Composable
private fun MemberItem(
    member: Member,
    currentUserId: Int,
    onMemberClick: () -> Unit,
    onPromoteModerator: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onMemberClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Image
            if (member.profileImage.isNullOrEmpty()) {
                Icon(
                    Icons.Filled.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    tint = Color.Gray
                )
            } else {
                AsyncImage(
                    model = member.profileImage,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            // Member Info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = member.fullName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )

                // Payment Status
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (member.hasPaid) "Paid ✅" else "Not Paid ❌",
                        fontSize = 12.sp,
                        color = if (member.hasPaid) Color.Green else Color.Red
                    )
                }

                // Email
                Text(
                    text = member.email,
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            // Menu (only for non-moderators and not current user)
            if (member.userId != currentUserId && !member.isModerator) {
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            Icons.Filled.MoreVert,
                            contentDescription = "Options",
                            tint = Color.Gray
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
                                onPromoteModerator()
                            },
                            leadingIcon = {
                                Icon(Icons.Filled.Star, contentDescription = null)
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Member Data Model
 */
data class Member(
    val id: Int,
    val fullName: String,
    val profileImage: String?,
    val userId: Int,
    val isModerator: Boolean,
    val email: String,
    val hasPaid: Boolean
)

