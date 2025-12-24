package com.fragne.circl_app.ui.circles.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.fragne.circl_app.ui.circles.dashboard.CircleData

/**
 * Circle Popup Card - Detail view for a circle
 * Translated from CirclPopupCard.swift
 */
@Composable
fun CirclePopupCard(
    circle: CircleData,
    isMember: Boolean = false,
    onJoinPressed: (() -> Unit)? = null,
    onOpenCircle: (() -> Unit)? = null,
    onDismiss: () -> Unit = {}
) {
    var showMediaPicker by remember { mutableStateOf(false) }
    var showInviteCopiedToast by remember { mutableStateOf(false) }

    val primaryBlue = Color(0xFF004AAD)

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Box {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Close Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = onDismiss) {
                            Icon(
                                Icons.Filled.Cancel,
                                contentDescription = "Close",
                                tint = Color.Gray
                            )
                        }
                    }

                    // Circle Image
                    if (!circle.profileImageUrl.isNullOrEmpty()) {
                        AsyncImage(
                            model = circle.profileImageUrl,
                            contentDescription = "Circle Image",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            Icons.Filled.Image,
                            contentDescription = "Circle Image",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            tint = Color.Gray.copy(alpha = 0.5f)
                        )
                    }

                    // Upload Photo Button (Moderators only)
                    if (circle.isModerator) {
                        TextButton(onClick = { showMediaPicker = true }) {
                            Text(
                                text = "Upload Circl Photo",
                                fontSize = 14.sp,
                                color = primaryBlue
                            )
                        }
                    }

                    // Circle Name
                    Text(
                        text = circle.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    // Industry
                    Text(
                        text = "Industry: ${circle.industry}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )


                    // Member Count
                    Text(
                        text = "${circle.memberCount} Members",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Divider(modifier = Modifier.padding(horizontal = 16.dp))

                    // About Section
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "About This Circl",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Text(
                            text = circle.description,
                            fontSize = 15.sp,
                            color = Color.Black,
                            lineHeight = 22.sp
                        )
                    }

                    // Access Code (Moderators only, if exists)
                    if (circle.isModerator && !circle.accessCode.isNullOrBlank()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Access Code",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                            Text(
                                text = circle.accessCode ?: "",
                                fontSize = 15.sp,
                                color = Color.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Action Button
                    if (isMember) {
                        // Open Circle Button
                        Button(
                            onClick = {
                                onDismiss()
                                // Delay to allow dialog to close
                                onOpenCircle?.invoke()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF5F5F5)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                Icons.Filled.ArrowForward,
                                contentDescription = null,
                                tint = primaryBlue,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Open Circl",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryBlue
                            )
                        }
                    } else {
                        // Join Button
                        Button(
                            onClick = {
                                onJoinPressed?.invoke()
                                onDismiss()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF34C759) // Darker bright green
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Join Now",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                // Toast Overlay for Invite Copied
                if (showInviteCopiedToast) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            elevation = CardDefaults.cardElevation(10.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Filled.CheckCircle,
                                    contentDescription = null,
                                    tint = primaryBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                                Column {
                                    Text(
                                        text = "Invite Link Copied to Your Clipboard!",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.Black
                                    )
                                    Text(
                                        text = "Paste it into your Messages to invite your network!",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Media Picker Dialog (TODO: Implement)
    if (showMediaPicker) {
        AlertDialog(
            onDismissRequest = { showMediaPicker = false },
            title = { Text("Upload Photo") },
            text = { Text("Photo picker coming soon") },
            confirmButton = {
                TextButton(onClick = { showMediaPicker = false }) {
                    Text("OK")
                }
            }
        )
    }
}

