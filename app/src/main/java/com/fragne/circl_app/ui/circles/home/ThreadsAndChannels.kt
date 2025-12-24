package com.fragne.circl_app.ui.circles.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Threads Section Component
 */
@Composable
fun ThreadsSection(
    threads: List<ThreadData>,
    onShowCreateThread: () -> Unit
) {
    val primaryBlue = Color(0xFF004AAD)
    val lightBlue = Color(0xFF0066DD)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Circle Threads",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Share ideas and discussions",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
            }

            Button(
                onClick = onShowCreateThread,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(50)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(primaryBlue, lightBlue)
                            ),
                            RoundedCornerShape(50)
                        )
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "New",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Threads List
        if (threads.isNotEmpty()) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(threads) { thread ->
                    ThreadCard(thread = thread)
                }
            }
        } else {
            // Empty State
            Card(
                modifier = Modifier
                    .width(260.dp)
                    .height(120.dp)
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = primaryBlue.copy(alpha = 0.05f)
                ),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Filled.Forum,
                        contentDescription = null,
                        tint = primaryBlue.copy(alpha = 0.4f),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No threads yet",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                    Text(
                        text = "Be the first to start a discussion!",
                        fontSize = 13.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

/**
 * Thread Card
 */
@Composable
private fun ThreadCard(thread: ThreadData) {
    val primaryBlue = Color(0xFF004AAD)

    Card(
        modifier = Modifier
            .width(300.dp)
            .clickable { /* TODO: Open thread */ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = thread.author,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = primaryBlue
                )
                Text(
                    text = thread.createdAt,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = thread.content,
                fontSize = 15.sp,
                color = Color.Black,
                lineHeight = 22.sp,
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    Icons.Filled.ChatBubble,
                    contentDescription = null,
                    tint = primaryBlue,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "${thread.replies} replies",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

/**
 * Channel Category Section
 */
@Composable
fun ChannelCategorySection(
    category: ChannelCategory,
    onNavigateToChannel: (Int) -> Unit
) {
    val primaryBlue = Color(0xFF004AAD)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Text(
            text = category.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            category.channels.forEach { channel ->
                ChannelItem(
                    channel = channel,
                    onClick = { onNavigateToChannel(channel.id) }
                )
            }
        }
    }
}

/**
 * Channel Item
 */
@Composable
private fun ChannelItem(
    channel: Channel,
    onClick: () -> Unit
) {
    val primaryBlue = Color(0xFF004AAD)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = primaryBlue.copy(alpha = 0.05f)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.Tag,
                    contentDescription = null,
                    tint = primaryBlue,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = channel.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }

            Icon(
                Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

