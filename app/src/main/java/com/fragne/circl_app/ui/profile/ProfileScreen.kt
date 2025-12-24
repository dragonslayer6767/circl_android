package com.fragne.circl_app.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import com.fragne.circl_app.ui.subscription.SubscriptionManager
import com.fragne.circl_app.ui.subscription.SubscriptionPaywallDialog
import com.fragne.circl_app.ui.subscription.UserType

/**
 * Profile Page - User's personal profile
 * Translated from ProfilePage.swift
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToBusinessProfile: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToPremium: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToNetwork: () -> Unit = {},
    onNavigateToCircles: () -> Unit = {},
    onNavigateToGrowthHub: () -> Unit = {}
) {
    var currentTab by remember { mutableStateOf("profile") } // "profile" or "business"
    var isEditing by remember { mutableStateOf(false) }

    // Subscription Manager
    val subscriptionManager = viewModel<SubscriptionManager>()

    // Mock data - TODO: Replace with ViewModel
    var bio by remember { mutableStateOf("Passionate entrepreneur building the future of tech.") }
    var firstName by remember { mutableStateOf("John") }
    var birthday by remember { mutableStateOf("1995-06-15") }
    var institution by remember { mutableStateOf("Stanford University") }
    var location by remember { mutableStateOf("San Francisco, CA") }
    var personalityType by remember { mutableStateOf("ENTJ") }
    var skills by remember { mutableStateOf("Product Management, UX Design, Python") }
    var experience by remember { mutableStateOf("5") }
    var clubs by remember { mutableStateOf("Tech Entrepreneurs Club, Innovation Lab") }
    var hobbies by remember { mutableStateOf("Hiking, Photography, Coding") }

    val primaryBlue = Color(0xFF004AAD)
    val lightBlue = Color(0xFF0066FF)

    // Show subscription paywall dialog
    SubscriptionPaywallDialog(
        subscriptionManager = subscriptionManager
    )

    Scaffold(
        topBar = {

            Column {
                // Top Bar - Blue background
                TopAppBar(
                    title = { Text("Profile", color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                "Back",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        // Edit/Save button
                        IconButton(onClick = { isEditing = !isEditing }) {
                            Icon(
                                imageVector = if (isEditing) Icons.Filled.Check else Icons.Filled.Edit,
                                contentDescription = if (isEditing) "Save" else "Edit",
                                tint = Color.White
                            )
                        }
                        // Settings button
                        IconButton(onClick = onNavigateToSettings) {
                            Icon(Icons.Filled.Settings, "Settings", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = primaryBlue,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                        actionIconContentColor = Color.White
                    )
                )

                // Tab Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(primaryBlue)
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Your Profile Tab
                    Column(
                        modifier = Modifier
                            .width(100.dp)
                            .clickable { currentTab = "profile" },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Your Profile",
                            fontSize = 15.sp,
                            fontWeight = if (currentTab == "profile") FontWeight.SemiBold else FontWeight.Normal,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(3.dp)
                                .background(if (currentTab == "profile") Color.White else Color.Transparent)
                        )
                    }

                    // Business Profile Tab
                    Column(
                        modifier = Modifier
                            .width(130.dp)
                            .clickable {
                                currentTab = "business"
                                onNavigateToBusinessProfile()
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Business Profile",
                            fontSize = 15.sp,
                            fontWeight = if (currentTab == "business") FontWeight.SemiBold else FontWeight.Normal,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(3.dp)
                                .background(if (currentTab == "business") Color.White else Color.Transparent)
                        )
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar {
                // Home
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Home,
                            contentDescription = "Home"
                        )
                    },
                    label = { Text("Home") },
                    selected = false,
                    onClick = onNavigateToHome
                )

                // Network
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.AccountCircle,
                            contentDescription = "Network"
                        )
                    },
                    label = { Text("Network") },
                    selected = false,
                    onClick = onNavigateToNetwork
                )

                // Circles
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.People,
                            contentDescription = "Circles"
                        )
                    },
                    label = { Text("Circles") },
                    selected = false,
                    onClick = onNavigateToCircles
                )

                // Growth Hub
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.AttachMoney,
                            contentDescription = "Growth Hub"
                        )
                    },
                    label = { Text("Growth Hub") },
                    selected = false,
                    onClick = onNavigateToGrowthHub
                )

                // Settings
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings"
                        )
                    },
                    label = { Text("Settings") },
                    selected = false,
                    onClick = onNavigateToSettings
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5)),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Profile Header Card
            item {
                ProfileHeaderCard(
                    firstName = firstName,
                    profileImageUrl = "",
                    connectionCount = 150,
                    isEditing = isEditing
                )
            }

            // Premium Button
            item {
                PremiumButton(
                    onClick = {
                        // Detect user type based on profile data
                        // TODO: This should come from actual user data/preferences
                        val userType = UserType.detectUserType(
                            usageInterests = "entrepreneur",
                            industryInterests = "technology"
                        )
                        subscriptionManager.showPaywall(userType)
                    }
                )
            }

            // Bio Section
            item {
                ProfileCard(title = "Bio") {
                    if (isEditing) {
                        OutlinedTextField(
                            value = bio,
                            onValueChange = { bio = it },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 4
                        )
                    } else {
                        Text(
                            text = bio,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }
                }
            }

            // About Section
            item {
                ProfileCard(title = "About $firstName") {
                    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                        ProfileField("Age", calculateAge(birthday), isEditing, { birthday = it }, birthday)
                        ProfileField("Institution", institution, isEditing, { institution = it })
                        ProfileField("Location(s)", location, isEditing, { location = it })
                        if (personalityType.isNotEmpty() || isEditing) {
                            ProfileField("Personality Type", personalityType, isEditing, { personalityType = it })
                        }
                    }
                }
            }

            // Technical Side Section
            item {
                ProfileCard(title = "Technical Side") {
                    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                        ProfileField("Skills", skills, isEditing, { skills = it })
                        ProfileField("Experience", "$experience years", isEditing, { experience = it })
                    }
                }
            }

            // Interests Section
            item {
                ProfileCard(title = "Interests") {
                    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                        ProfileField("Clubs", clubs, isEditing, { clubs = it })
                        ProfileField("Hobbies", hobbies, isEditing, { hobbies = it })
                    }
                }
            }

            // Bottom padding
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun ProfileHeaderCard(
    firstName: String,
    profileImageUrl: String,
    connectionCount: Int,
    isEditing: Boolean
) {
    val primaryBlue = Color(0xFF004AAD)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Image
            Box(contentAlignment = Alignment.BottomEnd) {
                if (profileImageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = profileImageUrl,
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(4.dp, primaryBlue, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .background(primaryBlue.copy(alpha = 0.1f), CircleShape)
                            .border(4.dp, primaryBlue, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null,
                            tint = primaryBlue,
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }

                // Camera button when editing
                if (isEditing) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(primaryBlue, CircleShape)
                            .clickable { /* TODO: Upload image */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CameraAlt,
                            contentDescription = "Upload",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Name
            Text(
                text = firstName,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            // Connection Count
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.People,
                    contentDescription = null,
                    tint = primaryBlue,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "$connectionCount Connections",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
private fun PremiumButton(onClick: () -> Unit) {
    val primaryBlue = Color(0xFF004AAD)
    val lightBlue = Color(0xFF0066FF)

    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(colors = listOf(primaryBlue, lightBlue)),
                    RoundedCornerShape(16.dp)
                )
                .border(
                    2.dp,
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Yellow.copy(alpha = 0.8f),
                            Color(0xFFFFA500).copy(alpha = 0.6f),
                            Color.Yellow.copy(alpha = 0.4f)
                        )
                    ),
                    RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Color.Yellow,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Upgrade to Premium",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun ProfileCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            content()
        }
    }
}

@Composable
private fun ProfileField(
    label: String,
    value: String,
    isEditing: Boolean,
    onValueChange: (String) -> Unit,
    editValue: String = value
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray
        )

        if (isEditing) {
            OutlinedTextField(
                value = editValue,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Text(
                text = value,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}

private fun calculateAge(birthday: String): String {
    return try {
        val parts = birthday.split("-")
        if (parts.size == 3) {
            val birthYear = parts[0].toInt()
            val currentYear = 2024
            "${currentYear - birthYear}"
        } else {
            "N/A"
        }
    } catch (e: Exception) {
        "N/A"
    }
}

