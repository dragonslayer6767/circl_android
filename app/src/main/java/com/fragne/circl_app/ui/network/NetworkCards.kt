package com.fragne.circl_app.ui.network

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

/**
 * Entrepreneur Profile Card
 */
@Composable
fun EntrepreneurCard(
    entrepreneur: EntrepreneurProfile,
    onProfileClick: () -> Unit,
    onConnectClick: () -> Unit
) {
    var showConfirmDialog by remember { mutableStateOf(false) }
    val primaryBlue = Color(0xFF004AAD)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onProfileClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with profile image and basic info
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile image with gradient border
                Box(
                    modifier = Modifier.size(68.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(
                                width = 2.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        primaryBlue.copy(alpha = 0.8f),
                                        primaryBlue.copy(alpha = 0.3f)
                                    )
                                ),
                                shape = CircleShape
                            )
                    )

                    if (entrepreneur.profileImage != null && entrepreneur.profileImage.isNotEmpty()) {
                        AsyncImage(
                            model = entrepreneur.profileImage,
                            contentDescription = "Profile",
                            modifier = Modifier
                                .size(64.dp)
                                .align(Alignment.Center)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .align(Alignment.Center)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFFF5F5F5),
                                            Color(0xFFE0E0E0)
                                        )
                                    ),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = null,
                                modifier = Modifier.size(32.dp),
                                tint = Color.Gray
                            )
                        }
                    }

                    // Verified badge
                    if (entrepreneur.isVerified) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Verified",
                            tint = primaryBlue,
                            modifier = Modifier
                                .size(20.dp)
                                .align(Alignment.BottomEnd)
                                .background(Color.White, CircleShape)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Name and business info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = entrepreneur.name,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (entrepreneur.businessStage.isNotEmpty()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Work,
                                contentDescription = null,
                                tint = primaryBlue,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = entrepreneur.businessStage,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = primaryBlue,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    if (entrepreneur.businessName.isNotEmpty()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Business,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = entrepreneur.businessName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Industry
            if (entrepreneur.businessIndustry.isNotEmpty()) {
                Column {
                    Text(
                        text = "INDUSTRY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = entrepreneur.businessIndustry,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Tags
            if (entrepreneur.tags.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    entrepreneur.tags.take(3).forEach { tag ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = primaryBlue.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = tag,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = primaryBlue,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                    if (entrepreneur.tags.size > 3) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFF5F5F5)
                        ) {
                            Text(
                                text = "+${entrepreneur.tags.size - 3}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Bio
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.FormatQuote,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = entrepreneur.bio.ifEmpty { "Looking to scale innovative solutions and build meaningful partnerships" },
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Pass button
                OutlinedButton(
                    onClick = onProfileClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = primaryBlue
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, primaryBlue)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Pass")
                }

                // Connect button
                Button(
                    onClick = { showConfirmDialog = true },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryBlue
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.PersonAdd,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Connect")
                }
            }
        }
    }

    // Confirmation dialog
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Send Connection Request?") },
            text = { Text("Are you sure you want to connect with ${entrepreneur.name}?") },
            confirmButton = {
                Button(
                    onClick = {
                        onConnectClick()
                        showConfirmDialog = false
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

/**
 * Mentor Profile Card
 */
@Composable
fun MentorCard(
    mentor: MentorProfile,
    onProfileClick: () -> Unit,
    onConnectClick: () -> Unit
) {
    var showConfirmDialog by remember { mutableStateOf(false) }
    val mentorOrange = Color(0xFFFFA500)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onProfileClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile image
                Box(modifier = Modifier.size(68.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(
                                width = 2.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        mentorOrange.copy(alpha = 0.8f),
                                        mentorOrange.copy(alpha = 0.3f)
                                    )
                                ),
                                shape = CircleShape
                            )
                    )

                    if (mentor.profileImage != null && mentor.profileImage.isNotEmpty()) {
                        AsyncImage(
                            model = mentor.profileImage,
                            contentDescription = "Profile",
                            modifier = Modifier
                                .size(64.dp)
                                .align(Alignment.Center)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .align(Alignment.Center)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFFF5F5F5),
                                            Color(0xFFE0E0E0)
                                        )
                                    ),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.School,
                                contentDescription = null,
                                modifier = Modifier.size(32.dp),
                                tint = mentorOrange
                            )
                        }
                    }

                    // Verified badge
                    if (mentor.isVerified) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Verified",
                            tint = mentorOrange,
                            modifier = Modifier
                                .size(20.dp)
                                .align(Alignment.BottomEnd)
                                .background(Color.White, CircleShape)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = mentor.name,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Work,
                            contentDescription = null,
                            tint = mentorOrange,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = mentor.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = mentorOrange,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    if (mentor.company.isNotEmpty()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Business,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = mentor.company,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MentorStat(
                    icon = Icons.Filled.Star,
                    value = String.format("%.1f", mentor.rating),
                    label = "Rating",
                    color = mentorOrange
                )
                MentorStat(
                    icon = Icons.Filled.Schedule,
                    value = "${mentor.sessionsCompleted}",
                    label = "Sessions",
                    color = mentorOrange
                )
                MentorStat(
                    icon = Icons.Filled.AccessTime,
                    value = mentor.experience,
                    label = "Experience",
                    color = mentorOrange
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Expertise tags
            if (mentor.expertise.isNotEmpty()) {
                Text(
                    text = "EXPERTISE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    mentor.expertise.take(3).forEach { skill ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = mentorOrange.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = skill,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = mentorOrange,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                    if (mentor.expertise.size > 3) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFF5F5F5)
                        ) {
                            Text(
                                text = "+${mentor.expertise.size - 3}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Bio
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFF8F0), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.FormatQuote,
                    contentDescription = null,
                    tint = mentorOrange,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = mentor.bio,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onProfileClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = mentorOrange
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, mentorOrange)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Pass")
                }

                Button(
                    onClick = { showConfirmDialog = true },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = mentorOrange
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.PersonAdd,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Connect")
                }
            }
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Request Mentorship?") },
            text = { Text("Would you like to connect with ${mentor.name} for mentorship?") },
            confirmButton = {
                Button(
                    onClick = {
                        onConnectClick()
                        showConfirmDialog = false
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun MentorStat(
    icon: ImageVector,
    value: String,
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color.Gray
        )
    }
}

/**
 * Network Connection Card
 */
@Composable
fun NetworkConnectionCard(
    connection: NetworkConnection,
    onProfileClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onProfileClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile image
            Box(modifier = Modifier.size(56.dp)) {
                if (connection.profileImage != null && connection.profileImage.isNotEmpty()) {
                    AsyncImage(
                        model = connection.profileImage,
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
                            .background(Color(0xFFE0E0E0), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null,
                            modifier = Modifier.size(28.dp),
                            tint = Color.Gray
                        )
                    }
                }

                // Online indicator
                if (connection.isOnline) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .align(Alignment.BottomEnd)
                            .background(Color.Green, CircleShape)
                            .border(2.dp, Color.White, CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = connection.name,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (connection.title != null && connection.company != null) {
                    Text(
                        text = "${connection.title} at ${connection.company}",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Connection type badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when (connection.connectionType) {
                        "mentor" -> Color(0xFFFFA500).copy(alpha = 0.1f)
                        else -> Color(0xFF004AAD).copy(alpha = 0.1f)
                    },
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = connection.connectionType.replaceFirstChar { it.uppercase() },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = when (connection.connectionType) {
                            "mentor" -> Color(0xFFFFA500)
                            else -> Color(0xFF004AAD)
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            // Unread badge
            if (connection.unreadCount > 0) {
                Badge {
                    Text(
                        text = if (connection.unreadCount > 9) "9+" else connection.unreadCount.toString()
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Message button
            IconButton(onClick = { /* TODO: Open chat */ }) {
                Icon(
                    imageVector = Icons.Filled.Chat,
                    contentDescription = "Message",
                    tint = Color(0xFF004AAD)
                )
            }
        }
    }
}

/**
 * Friend Request Card
 */
@Composable
fun FriendRequestCard(
    request: FriendRequest,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    onProfileClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFBF0)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile image (clickable)
                Box(
                    modifier = Modifier.clickable(onClick = onProfileClick)
                ) {
                    if (request.senderProfileImage != null && request.senderProfileImage.isNotEmpty()) {
                        AsyncImage(
                            model = request.senderProfileImage,
                            contentDescription = "Profile",
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color(0xFFE0E0E0), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = Color.Gray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = request.senderName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "wants to connect",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            if (request.message != null && request.message.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = request.message,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onAccept,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF004AAD)
                    )
                ) {
                    Text("Accept")
                }

                OutlinedButton(
                    onClick = onDecline,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Decline")
                }
            }
        }
    }
}

/**
 * Network Stats Card
 */
@Composable
fun NetworkStats(
    connectionCount: Int,
    activeCount: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Connections stat
        Surface(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF004AAD).copy(alpha = 0.1f)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$connectionCount",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF004AAD)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Connections",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
            }
        }

        // Active stat
        Surface(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color.Green.copy(alpha = 0.1f)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$activeCount",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Green
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Active",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
            }
        }
    }
}

/**
 * Loading Card
 */
@Composable
fun LoadingCard(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            CircularProgressIndicator()
            Text(
                text = message,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
        }
    }
}

/**
 * Empty State Card
 */
@Composable
fun EmptyStateCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    iconTint: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                iconTint.copy(alpha = 0.1f),
                                iconTint.copy(alpha = 0.05f)
                            )
                        ),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = iconTint.copy(alpha = 0.6f)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}

/**
 * Empty Network State with CTA
 */
@Composable
fun EmptyNetworkState(
    onFindConnectionsClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color.Green.copy(alpha = 0.1f),
                                Color.Green.copy(alpha = 0.05f)
                            )
                        ),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Group,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = Color.Green.copy(alpha = 0.6f)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "No connections yet",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Start connecting with entrepreneurs and mentors to build your network",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }

            Button(
                onClick = onFindConnectionsClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF004AAD)
                ),
                shape = RoundedCornerShape(25.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.PersonAdd,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Find Connections",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

