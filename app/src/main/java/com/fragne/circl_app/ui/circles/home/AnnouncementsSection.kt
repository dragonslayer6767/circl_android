package com.fragne.circl_app.ui.circles.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Announcements Section Component
 * Translated from AnnouncementsSection.swift
 */
@Composable
fun AnnouncementsSection(
    announcements: List<AnnouncementData>,
    onShowCreateAnnouncement: () -> Unit,
    isModeratorOrCreator: Boolean = false
) {
    val primaryBlue = Color(0xFF004AAD)
    val lightBlue = Color(0xFF0066FF)
    var showAllAnnouncements by remember { mutableStateOf(false) }

    if (announcements.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            // Section Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        primaryBlue.copy(alpha = 0.8f),
                                        lightBlue.copy(alpha = 0.6f)
                                    )
                                ),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.Campaign,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Text(
                        text = "Announcements",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                if (isModeratorOrCreator) {
                    Button(
                        onClick = onShowCreateAnnouncement,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        contentPadding = PaddingValues(0.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(primaryBlue, lightBlue)
                                    ),
                                    RoundedCornerShape(20.dp)
                                )
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Filled.AddCircle,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "Create",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Announcements List - Show top 3
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                announcements.take(3).forEach { announcement ->
                    AnnouncementCard(
                        announcement = announcement,
                        onDelete = { /* TODO: Delete announcement */ }
                    )
                }

                // Show All button if more than 3
                if (announcements.size > 3) {
                    TextButton(
                        onClick = { showAllAnnouncements = true },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "Show All Announcements",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = primaryBlue
                        )
                    }
                }
            }
        }
    } else {
        // Empty State
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        primaryBlue.copy(alpha = 0.1f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Campaign,
                    contentDescription = null,
                    tint = primaryBlue.copy(alpha = 0.6f),
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "No Announcements Yet",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Text(
                    text = "Stay tuned for important updates from circle moderators",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }

            if (isModeratorOrCreator) {
                Button(
                    onClick = onShowCreateAnnouncement,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(primaryBlue, lightBlue)
                                ),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 20.dp, vertical = 12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Create First Announcement",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }

    // Show All Dialog
    if (showAllAnnouncements) {
        AllAnnouncementsDialog(
            announcements = announcements,
            onDismiss = { showAllAnnouncements = false }
        )
    }
}

/**
 * Announcement Card - Collapsible
 */
@Composable
private fun AnnouncementCard(
    announcement: AnnouncementData,
    onDelete: () -> Unit
) {
    val primaryBlue = Color(0xFF004AAD)
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }
            .animateContentSize(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = primaryBlue
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row - Always visible
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar and Title
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // User Avatar
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                Color.White.copy(alpha = 0.3f),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = announcement.user.take(1).uppercase(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // Title and subtitle
                    Column {
                        Text(
                            text = announcement.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "By ${announcement.user} • ${announcement.announcedAt}",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                // Expand/Collapse Icon
                Icon(
                    if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Expandable Content
            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))

                HorizontalDivider(
                    color = Color.White.copy(alpha = 0.3f),
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = announcement.content,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.95f),
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Posted timestamp at bottom
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Schedule,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Posted ${announcement.announcedAt}",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

/**
 * All Announcements Dialog
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AllAnnouncementsDialog(
    announcements: List<AnnouncementData>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth(0.95f)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "All Announcements",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Filled.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(announcements) { announcement ->
                        AnnouncementCard(
                            announcement = announcement,
                            onDelete = { /* TODO */ }
                        )
                    }
                }
            }
        }
    }
}

