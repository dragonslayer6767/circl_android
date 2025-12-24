package com.fragne.circl_app.ui.forum

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

/**
 * Main Forum/Feed Screen - Twitter/X style
 * This is the home screen users see after login
 */
@Composable
fun ForumScreen(
    onNavigateToProfile: () -> Unit = {},
    onNavigateToMessages: () -> Unit = {},
    onNavigateToUserProfile: (Int) -> Unit = {},
    onNavigateToComments: (Int) -> Unit = {},
    viewModel: ForumViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val primaryBlue = Color(0xFF004AAD)


    Column(modifier = Modifier.fillMaxSize()) {
        // Top bar
        ForumTopBar(
            userProfileImageUrl = uiState.userProfileImageUrl,
            unreadMessageCount = uiState.unreadMessageCount,
            selectedTab = uiState.selectedFilter,
            onTabSelected = { tab ->
                viewModel.fetchPostsWithTabSwitch(tab)
            },
            onProfileClick = onNavigateToProfile,
            onMessagesClick = onNavigateToMessages
        )

        // Post compose area
        PostComposeArea(
            postContent = uiState.postContent,
            onPostContentChange = { viewModel.updatePostContent(it) },
            selectedCategory = uiState.selectedCategory,
            onCategoryClick = { /* Show category dialog */ },
            selectedPrivacy = uiState.selectedPrivacy,
            onPrivacyChange = { viewModel.updateSelectedPrivacy(it) },
            onSubmitPost = { viewModel.submitPost() },
            userProfileImageUrl = uiState.userProfileImageUrl
        )

        HorizontalDivider(
            thickness = 1.dp,
            color = Color.Gray.copy(alpha = 0.2f)
        )

        // Feed content
        Box(modifier = Modifier.weight(1f)) {
            when {
                uiState.isLoading || uiState.isTabSwitchLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                uiState.posts.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No posts yet", color = Color.Gray)
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = uiState.posts,
                            key = { it.id }
                        ) { post ->
                            ForumPostItem(
                                post = post,
                                isCurrentUser = post.user == "Current User", // TODO: Check actual user
                                onCommentClick = { onNavigateToComments(post.id) },
                                onLikeClick = { viewModel.toggleLike(post) },
                                onDeleteClick = { viewModel.deletePost(post.id) },
                                onReportClick = { /* TODO: Report post */ },
                                onProfileClick = { onNavigateToUserProfile(post.userId) }
                            )
                        }
                    }
                }
            }

            // Show error message
            uiState.errorMessage?.let { error ->
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    action = {
                        TextButton(onClick = { viewModel.clearError() }) {
                            Text("Dismiss")
                        }
                    }
                ) {
                    Text(error)
                }
            }
        }
    }
}

@Composable
fun ForumTopBar(
    userProfileImageUrl: String,
    unreadMessageCount: Int,
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    onProfileClick: () -> Unit,
    onMessagesClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF004AAD),
                        Color(0xFF004AAD).copy(alpha = 0.95f)
                    )
                )
            )
            .statusBarsPadding() // Extend to status bar
    ) {
        // Top row with profile, logo, messages
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile image
            Box(
                modifier = Modifier.clickable { onProfileClick() }
            ) {
                AsyncImage(
                    model = userProfileImageUrl.takeIf { it.isNotEmpty() }
                        ?: "https://via.placeholder.com/150",
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                // Online indicator
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color.Green)
                        .align(Alignment.BottomEnd)
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

            // Messages icon
            Box(
                modifier = Modifier.clickable { onMessagesClick() }
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Messages",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )

                if (unreadMessageCount > 0) {
                    Surface(
                        shape = CircleShape,
                        color = Color.Red,
                        modifier = Modifier
                            .size(18.dp)
                            .align(Alignment.TopEnd)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = if (unreadMessageCount > 99) "99+" else unreadMessageCount.toString(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // Tab selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .padding(bottom = 10.dp, top = 16.dp)
        ) {
            listOf("public" to "For you", "my_network" to "Following").forEach { (key, label) ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onTabSelected(key) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = label,
                        fontSize = 16.sp,
                        fontWeight = if (selectedTab == key) FontWeight.Bold else FontWeight.Medium,
                        color = Color.White.copy(alpha = if (selectedTab == key) 1f else 0.7f)
                    )

                    if (selectedTab == key) {
                        Box(
                            modifier = Modifier
                                .width(if (key == "public") 60.dp else 80.dp)
                                .height(3.dp)
                                .background(Color.White)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PostComposeArea(
    postContent: String,
    onPostContentChange: (String) -> Unit,
    selectedCategory: String,
    onCategoryClick: () -> Unit,
    selectedPrivacy: String,
    onPrivacyChange: (String) -> Unit,
    onSubmitPost: () -> Unit,
    userProfileImageUrl: String
) {
    var showPrivacyMenu by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Profile image
        AsyncImage(
            model = userProfileImageUrl.takeIf { it.isNotEmpty() }
                ?: "https://via.placeholder.com/150",
            contentDescription = "Your profile",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Text input
            BasicTextField(
                value = postContent,
                onValueChange = onPostContentChange,
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    color = Color.Black
                ),
                cursorBrush = SolidColor(Color(0xFF004AAD)),
                decorationBox = { innerTextField ->
                    if (postContent.isEmpty()) {
                        Text(
                            "What's happening?",
                            fontSize = 18.sp,
                            color = Color.Gray.copy(alpha = 0.5f)
                        )
                    }
                    innerTextField()
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Bottom action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category button
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = if (selectedCategory == "Category")
                        Color(0xFFE8F2FF)
                    else
                        Color(0xFF004AAD).copy(alpha = 0.1f),
                    modifier = Modifier.clickable { onCategoryClick() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tag,
                            contentDescription = null,
                            tint = Color(0xFF004AAD),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = if (selectedCategory == "Category") "Tags" else selectedCategory,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF004AAD)
                        )
                    }
                }

                // Privacy menu
                Box {
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = Color(0xFFE8F2FF),
                        modifier = Modifier.clickable { showPrivacyMenu = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (selectedPrivacy == "Public") Icons.Default.Public else Icons.Default.Lock,
                                contentDescription = null,
                                tint = Color(0xFF004AAD),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = selectedPrivacy,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF004AAD)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = showPrivacyMenu,
                        onDismissRequest = { showPrivacyMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Public") },
                            onClick = {
                                onPrivacyChange("Public")
                                showPrivacyMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("My Network") },
                            onClick = {
                                onPrivacyChange("My Network")
                                showPrivacyMenu = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Post button
                Button(
                    onClick = onSubmitPost,
                    enabled = postContent.trim().isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF004AAD),
                        disabledContainerColor = Color.Gray.copy(alpha = 0.4f)
                    ),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        "Post",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ForumScreenPreview() {
    MaterialTheme {
        ForumScreen()
    }
}

