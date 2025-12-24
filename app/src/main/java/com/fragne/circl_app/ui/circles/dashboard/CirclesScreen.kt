package com.fragne.circl_app.ui.circles.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage

/**
 * Circles Screen - Explore and manage circles
 * Translated from PageCircles.swift
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CirclesScreen(
    onNavigateToProfile: () -> Unit = {},
    onNavigateToMessages: () -> Unit = {},
    onNavigateToCircleChat: (CircleData) -> Unit = {}
) {
    var currentTab by remember { mutableStateOf("explore") } // "explore" or "my_circles"
    var searchText by remember { mutableStateOf("") }
    var showCreateDialog by remember { mutableStateOf(false) }
    val primaryBlue = Color(0xFF004AAD)


    // Mock data - TODO: Replace with ViewModel
    val myCircles = remember {
        listOf(
            CircleData(
                id = 1,
                name = "Tech Entrepreneurs",
                industry = "Technology",
                memberCount = 234,
                pricing = "Free",
                description = "Connect with tech founders and innovators",
                isPrivate = false,
                profileImageUrl = ""
            ),
            CircleData(
                id = 2,
                name = "Marketing Masterminds",
                industry = "Marketing",
                memberCount = 156,
                pricing = "Premium",
                description = "Share marketing strategies and growth hacks",
                isPrivate = false,
                profileImageUrl = ""
            )
        )
    }

    val exploreCircles = remember {
        listOf(
            CircleData(
                id = 3,
                name = "Finance & Investment",
                industry = "Finance",
                memberCount = 189,
                pricing = "Free",
                description = "Discuss investment strategies and financial planning",
                isPrivate = false,
                profileImageUrl = ""
            ),
            CircleData(
                id = 4,
                name = "Startup Founders",
                industry = "Entrepreneurship",
                memberCount = 312,
                pricing = "Free",
                description = "Support network for early-stage founders",
                isPrivate = false,
                profileImageUrl = ""
            ),
            CircleData(
                id = 5,
                name = "Design Thinking",
                industry = "Design",
                memberCount = 98,
                pricing = "Premium",
                description = "UX/UI designers sharing best practices",
                isPrivate = true,
                profileImageUrl = ""
            )
        )
    }


    Scaffold(
        topBar = {
            Column {
                // Top Bar
                CirclesHeader(
                    userProfileImageUrl = "",
                    unreadMessageCount = 0,
                    onProfileClick = onNavigateToProfile,
                    onMessagesClick = onNavigateToMessages
                )

                // Tab Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(primaryBlue)
                        .padding(horizontal = 18.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Explore Tab
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { currentTab = "explore" },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Explore",
                            fontSize = 16.sp,
                            fontWeight = if (currentTab == "explore") FontWeight.Bold else FontWeight.Medium,
                            color = Color.White.copy(alpha = if (currentTab == "explore") 1f else 0.7f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(3.dp)
                                .background(if (currentTab == "explore") Color.White else Color.Transparent)
                        )
                    }

                    // My Circles Tab
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { currentTab = "my_circles" },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "My Circles",
                            fontSize = 16.sp,
                            fontWeight = if (currentTab == "my_circles") FontWeight.Bold else FontWeight.Medium,
                            color = Color.White.copy(alpha = if (currentTab == "my_circles") 1f else 0.7f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(3.dp)
                                .background(if (currentTab == "my_circles") Color.White else Color.Transparent)
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            // Search Section
            SearchSection(
                searchText = searchText,
                onSearchTextChange = { searchText = it },
                onSearchClick = { /* TODO: Search */ }
            )

            // Content
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(18.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                if (currentTab == "my_circles") {
                    // My Circles Header
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "My Circles",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Text(
                                    text = "${myCircles.size} joined circles",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Gray
                                )
                            }

                            if (myCircles.isNotEmpty()) {
                                Text(
                                    text = "${myCircles.size}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = primaryBlue,
                                    modifier = Modifier
                                        .background(
                                            primaryBlue.copy(alpha = 0.1f),
                                            RoundedCornerShape(50)
                                        )
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    // My Circles List
                    if (myCircles.isEmpty()) {
                        item {
                            EmptyState(
                                icon = Icons.Filled.People,
                                title = "No circles joined yet",
                                description = "Explore and join circles to see them here",
                                accentColor = primaryBlue
                            )
                        }
                    } else {
                        items(myCircles) { circle ->
                            CircleCard(
                                circle = circle,
                                isMember = true,
                                onJoinClick = {},
                                onOpenClick = { onNavigateToCircleChat(circle) }
                            )
                        }
                    }
                } else {
                    // Explore Header
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Explore",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Text(
                                    text = "Discover new circles",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Gray
                                )
                            }

                            Button(
                                onClick = { showCreateDialog = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF34C759) // Darker bright green
                                ),
                                shape = RoundedCornerShape(50),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Add,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "Create",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }

                    // Explore Circles List
                    if (exploreCircles.isEmpty()) {
                        item {
                            EmptyState(
                                icon = Icons.Filled.Search,
                                title = "No circles to explore",
                                description = "Create a new circle to get started!",
                                accentColor = Color(0xFF34C759) // Darker bright green
                            )
                        }
                    } else {
                        items(exploreCircles) { circle ->
                            CircleCard(
                                circle = circle,
                                isMember = false,
                                onJoinClick = { /* TODO: Join circle */ },
                                onOpenClick = {}
                            )
                        }
                    }
                }
            }
        }
    }

    // Create Circle Dialog
    if (showCreateDialog) {
        CreateCircleDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { _, _, _ ->
                // TODO: Create circle
                showCreateDialog = false
            }
        )
    }
}

/**
 * Circles Header
 */
@Composable
private fun CirclesHeader(
    userProfileImageUrl: String,
    unreadMessageCount: Int,
    onProfileClick: () -> Unit,
    onMessagesClick: () -> Unit
) {
    val primaryBlue = Color(0xFF004AAD)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(primaryBlue)
            .statusBarsPadding() // Extend to status bar
            .padding(horizontal = 18.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile
        Box(
            modifier = Modifier
                .size(34.dp)
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
        }

        Spacer(modifier = Modifier.weight(1f))

        // Logo
        Text(
            text = "Circl.",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.weight(1f))

        // Messages
        Box(
            modifier = Modifier
                .size(34.dp)
                .clickable(onClick = onMessagesClick)
        ) {
            Icon(
                imageVector = Icons.Filled.Email,
                contentDescription = "Messages",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )

            if (unreadMessageCount > 0) {
                Badge(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 6.dp, y = (-6).dp),
                    containerColor = Color.Red
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

/**
 * Search Section
 */
@Composable
private fun SearchSection(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    val primaryBlue = Color(0xFF004AAD)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Search for a Circle (keywords or name)...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                    tint = Color.Gray
                )
            },
            shape = RoundedCornerShape(25.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF5F5F5),
                focusedContainerColor = Color(0xFFF5F5F5),
                unfocusedBorderColor = primaryBlue.copy(alpha = 0.1f),
                focusedBorderColor = primaryBlue
            ),
            singleLine = true
        )

        IconButton(
            onClick = onSearchClick,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Search",
                tint = primaryBlue,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

/**
 * Circle Card
 */
@Composable
private fun CircleCard(
    circle: CircleData,
    isMember: Boolean,
    onJoinClick: () -> Unit,
    onOpenClick: () -> Unit
) {
    val primaryBlue = Color(0xFF004AAD)
    var showAbout by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Circle Image
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(primaryBlue.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (circle.profileImageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = circle.profileImageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.People,
                        contentDescription = null,
                        tint = Color.Gray.copy(alpha = 0.6f),
                        modifier = Modifier.size(50.dp)
                    )
                }
            }

            // Content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = circle.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Business,
                            contentDescription = null,
                            tint = primaryBlue,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "Industry: ${circle.industry}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.People,
                            contentDescription = null,
                            tint = primaryBlue,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "${circle.memberCount} Members",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = { showAbout = true }) {
                        Text(
                            text = "About",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = primaryBlue,
                            textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
                        )
                    }

                    if (!isMember) {
                        Button(
                            onClick = onJoinClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF34C759) // Darker bright green
                            ),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = "Join Now",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // Open arrow for members
            if (isMember) {
                IconButton(onClick = onOpenClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Open",
                        tint = primaryBlue,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }

    // About Dialog
    if (showAbout) {
        AlertDialog(
            onDismissRequest = { showAbout = false },
            title = { Text(circle.name) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Industry: ${circle.industry}")
                    Text("Members: ${circle.memberCount}")
                    Text(circle.description)
                    if (circle.isPrivate) {
                        Text(
                            text = "🔒 Private Circle",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAbout = false }) {
                    Text("Close")
                }
            }
        )
    }
}

/**
 * Empty State
 */
@Composable
private fun EmptyState(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    accentColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                accentColor.copy(alpha = 0.05f),
                RoundedCornerShape(16.dp)
            )
            .padding(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = accentColor.copy(alpha = 0.4f),
            modifier = Modifier.size(48.dp)
        )

        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )

        Text(
            text = description,
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}


/**
 * Create Circle Dialog
 * Translated from PageCircles.swift - Complete circle creation form
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateCircleDialog(
    onDismiss: () -> Unit,
    onCreate: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var industry by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isPrivate by remember { mutableStateOf(false) }
    var accessCode by remember { mutableStateOf("") }

    // Channel selection - #Welcome is mandatory
    val allChannelOptions = listOf("#Welcome", "#Chats", "#Moderators", "#News")
    var selectedChannels by remember { mutableStateOf(setOf("#Welcome")) }

    val primaryBlue = Color(0xFF004AAD)
    val brightGreen = Color(0xFF34C759)

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Text(
                    text = "Create a Circl",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                // Circle Name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Circle Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryBlue,
                        focusedLabelColor = primaryBlue,
                        cursorColor = primaryBlue,
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedContainerColor = Color(0xFFF5F5F5)
                    )
                )

                // Industry
                OutlinedTextField(
                    value = industry,
                    onValueChange = { industry = it },
                    label = { Text("Industry") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryBlue,
                        focusedLabelColor = primaryBlue,
                        cursorColor = primaryBlue,
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedContainerColor = Color(0xFFF5F5F5)
                    )
                )

                // Category (Optional)
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryBlue,
                        focusedLabelColor = primaryBlue,
                        cursorColor = primaryBlue,
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedContainerColor = Color(0xFFF5F5F5)
                    )
                )

                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    shape = RoundedCornerShape(10.dp),
                    maxLines = 4,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryBlue,
                        focusedLabelColor = primaryBlue,
                        cursorColor = primaryBlue,
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedContainerColor = Color(0xFFF5F5F5)
                    )
                )

                // Make Circle Private Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Make Circle Private",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    Switch(
                        checked = isPrivate,
                        onCheckedChange = { isPrivate = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = primaryBlue,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color.Gray
                        )
                    )
                }

                // Access Code (if private)
                if (isPrivate) {
                    OutlinedTextField(
                        value = accessCode,
                        onValueChange = { accessCode = it },
                        label = { Text("Access Code") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryBlue,
                            focusedLabelColor = primaryBlue,
                            cursorColor = primaryBlue,
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedContainerColor = Color(0xFFF5F5F5)
                        )
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                // Select Channels
                Text(
                    text = "Select Channels",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                // Channel toggles
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    allChannelOptions.forEach { channel ->
                        val isWelcome = channel == "#Welcome"
                        val isSelected = selectedChannels.contains(channel)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = channel,
                                fontSize = 16.sp,
                                color = if (isWelcome) Color.Gray else Color.Black
                            )
                            Switch(
                                checked = isSelected,
                                onCheckedChange = { checked ->
                                    if (!isWelcome) { // Can't toggle #Welcome
                                        selectedChannels = if (checked) {
                                            selectedChannels + channel
                                        } else {
                                            selectedChannels - channel
                                        }
                                    }
                                },
                                enabled = !isWelcome,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = brightGreen, // Dark green instead of blue
                                    uncheckedThumbColor = Color.White,
                                    uncheckedTrackColor = Color.Gray,
                                    disabledCheckedThumbColor = Color.White,
                                    disabledCheckedTrackColor = Color.Gray
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Create Button
                Button(
                    onClick = {
                        if (name.isNotEmpty() && industry.isNotEmpty()) {
                            onCreate(name, industry, description)
                            onDismiss()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = brightGreen
                    ),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    enabled = name.isNotEmpty() && industry.isNotEmpty()
                ) {
                    Text(
                        text = "Create Circl",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

/**
 * Circle Data Model
 */
data class CircleData(
    val id: Int,
    val name: String,
    val industry: String,
    val memberCount: Int,
    val pricing: String,
    val description: String,
    val isPrivate: Boolean,
    val profileImageUrl: String,
    val isModerator: Boolean = false,
    val accessCode: String? = null,
    val duesAmount: Int? = null,  // Amount in cents
    val hasStripeAccount: Boolean? = false
)


