package com.fragne.circl_app.ui.forum

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Verified
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
 * ForumPost - Individual post item (Twitter/X style)
 */
@Composable
fun ForumPostItem(
    post: ForumPostModel,
    isCurrentUser: Boolean,
    onCommentClick: () -> Unit,
    onLikeClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onReportClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Profile Image
            AsyncImage(
                model = post.profileImage?.takeIf { it.isNotEmpty() }
                    ?: "https://via.placeholder.com/150",
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable { onProfileClick() },
                contentScale = ContentScale.Crop
            )

            // Main content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Header row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Username
                    Text(
                        text = post.user,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )

                    // Mentor badge
                    if (post.isMentor) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified mentor",
                            tint = Color(0xFF004AAD),
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    // Separator
                    Text(
                        text = "·",
                        fontSize = 15.sp,
                        color = Color.Gray
                    )

                    // Timestamp
                    Text(
                        text = timeAgo(post.createdAt),
                        fontSize = 15.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Menu button
                    Box {
                        IconButton(
                            onClick = { showMenu = true },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options",
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            if (isCurrentUser) {
                                DropdownMenuItem(
                                    text = { Text("Delete Post") },
                                    onClick = {
                                        showMenu = false
                                        showDeleteDialog = true
                                    }
                                )
                            } else {
                                DropdownMenuItem(
                                    text = { Text("Report Post") },
                                    onClick = {
                                        showMenu = false
                                        showReportDialog = true
                                    }
                                )
                            }
                        }
                    }
                }

                // Post content
                Text(
                    text = post.content,
                    fontSize = 15.sp,
                    lineHeight = 20.sp,
                    color = Color.Black
                )

                // Category tag
                if (post.category.isNotBlank() && post.category != "Category") {
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = Color(0xFF004AAD).copy(alpha = 0.1f),
                        modifier = Modifier.wrapContentWidth()
                    ) {
                        Text(
                            text = post.category,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF004AAD),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    // Comment button
                    TextButton(
                        onClick = onCommentClick,
                        modifier = Modifier.padding(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ChatBubbleOutline,
                            contentDescription = "Comments",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = post.commentCount.toString(),
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Like button
                    TextButton(
                        onClick = onLikeClick,
                        modifier = Modifier.padding(0.dp)
                    ) {
                        Icon(
                            imageVector = if (post.likedByUser) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (post.likedByUser) Color.Red else Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = post.likeCount.toString(),
                            fontSize = 13.sp,
                            color = if (post.likedByUser) Color.Red else Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        // Divider
        HorizontalDivider(
            thickness = 1.dp,
            color = Color.Gray.copy(alpha = 0.3f)
        )
    }

    // Delete confirmation dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Post?") },
            text = { Text("Are you sure you want to delete this post? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteClick()
                    }
                ) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Report dialog
    if (showReportDialog) {
        ReportPostDialog(
            postId = post.id,
            onDismiss = { showReportDialog = false },
            onSubmit = {
                showReportDialog = false
                onReportClick()
            }
        )
    }
}

@Composable
fun ReportPostDialog(
    postId: Int,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit
) {
    var selectedReason by remember { mutableStateOf<ReportReason?>(null) }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Report Post") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Why are you reporting this post?")

                Box {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(selectedReason?.displayName ?: "Select reason")
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        ReportReason.values().forEach { reason ->
                            DropdownMenuItem(
                                text = { Text(reason.displayName) },
                                onClick = {
                                    selectedReason = reason
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onSubmit,
                enabled = selectedReason != null
            ) {
                Text("Submit")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

