package com.fragne.circl_app.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.*

/**
 * Dynamic Profile Preview Screen
 * Shows detailed user profile when tapping on a network card
 * Translated from DynamicProfilePreview.swift
 */

data class FullProfile(
    val userId: Int,
    val firstName: String?,
    val lastName: String?,
    val profileImage: String?,
    val bio: String?,
    val birthday: String?,
    val educationLevel: String?,
    val institutionAttended: String?,
    val locations: List<String>?,
    val personalityType: String?,
    val skillsets: List<String>?,
    val certificates: List<String>?,
    val yearsOfExperience: Int?,
    val clubs: List<String>?,
    val hobbies: List<String>?,
    val entrepreneurialHistory: String?,
    val connectionsCount: Int?,
    val circs: Int?
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynamicProfilePreviewScreen(
    profileData: FullProfile,
    isInNetwork: Boolean,
    currentUserId: Int,
    onNavigateBack: () -> Unit,
    onRemoveFriend: () -> Unit = {},
    onBlockUser: () -> Unit = {}
) {
    var showRemoveDialog by remember { mutableStateOf(false) }
    var showBlockDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF5F5F5).copy(alpha = 0.3f),
                        Color(0xFFE0E0E0).copy(alpha = 0.5f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Top bar with close button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp, end = 20.dp, start = 20.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFBBBBBB), CircleShape)
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = Color.Black,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Scrollable content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {
                // Header Card with gradient
                ProfileHeaderCard(
                    profileData = profileData,
                    currentUserId = currentUserId,
                    isInNetwork = isInNetwork,
                    onMenuClick = { action ->
                        when (action) {
                            "remove" -> showRemoveDialog = true
                            "block" -> showBlockDialog = true
                        }
                    }
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Bio Section
                if (!profileData.bio.isNullOrEmpty()) {
                    ModernCard(title = "Bio") {
                        Text(
                            text = profileData.bio,
                            fontSize = 15.sp,
                            color = Color.Black,
                            lineHeight = 22.sp
                        )

                        if (!profileData.personalityType.isNullOrEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Column {
                                Text(
                                    text = "Personality Type",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = profileData.personalityType,
                                    fontSize = 15.sp,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // About Section
                ModernCard(title = "About ${profileData.firstName} ${profileData.lastName}") {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        profileData.birthday?.let {
                            ProfileFieldDisplay(
                                label = "Age",
                                value = calculateAge(it)
                            )
                        }

                        profileData.educationLevel?.let {
                            ProfileFieldDisplay(label = "Education Level", value = it)
                        }

                        profileData.institutionAttended?.let {
                            ProfileFieldDisplay(label = "Institution", value = it)
                        }

                        profileData.locations?.takeIf { it.isNotEmpty() }?.let {
                            ProfileFieldDisplay(
                                label = "Location(s)",
                                value = it.joinToString(", ")
                            )
                        }

                        profileData.personalityType?.let {
                            ProfileFieldDisplay(label = "Personality Type", value = it)
                        }

                        if (profileData.educationLevel.isNullOrEmpty() &&
                            profileData.institutionAttended.isNullOrEmpty() &&
                            profileData.locations.isNullOrEmpty() &&
                            profileData.personalityType.isNullOrEmpty()
                        ) {
                            Text(
                                text = "No additional information listed yet.",
                                fontSize = 15.sp,
                                color = Color.Gray,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Technical Side Section
                ModernCard(title = "Technical Side") {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        profileData.skillsets?.takeIf { it.isNotEmpty() }?.let {
                            ProfileFieldDisplay(
                                label = "Skills",
                                value = it.joinToString(", ")
                            )
                        }

                        profileData.certificates?.takeIf { it.isNotEmpty() }?.let {
                            ProfileFieldDisplay(
                                label = "Certificates",
                                value = it.joinToString(", ")
                            )
                        }

                        profileData.yearsOfExperience?.let {
                            ProfileFieldDisplay(
                                label = "Experience",
                                value = "$it years"
                            )
                        }

                        if (profileData.skillsets.isNullOrEmpty() &&
                            profileData.certificates.isNullOrEmpty() &&
                            profileData.yearsOfExperience == null
                        ) {
                            Text(
                                text = "No technical information listed yet.",
                                fontSize = 15.sp,
                                color = Color.Gray,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Interests Section
                ModernCard(title = "Interests") {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        profileData.clubs?.takeIf { it.isNotEmpty() }?.let {
                            ProfileFieldDisplay(label = "Clubs", value = it.joinToString(", "))
                        }

                        profileData.hobbies?.takeIf { it.isNotEmpty() }?.let {
                            ProfileFieldDisplay(label = "Hobbies", value = it.joinToString(", "))
                        }

                        if (profileData.clubs.isNullOrEmpty() && profileData.hobbies.isNullOrEmpty()) {
                            Text(
                                text = "No interests listed yet.",
                                fontSize = 15.sp,
                                color = Color.Gray,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Entrepreneurial History Section
                ModernCard(title = "Entrepreneurial History") {
                    if (!profileData.entrepreneurialHistory.isNullOrEmpty()) {
                        Text(
                            text = profileData.entrepreneurialHistory,
                            fontSize = 15.sp,
                            color = Color.Black,
                            lineHeight = 22.sp
                        )
                    } else {
                        Text(
                            text = "No entrepreneurial history listed yet.",
                            fontSize = 15.sp,
                            color = Color.Gray,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }

    // Dialogs
    if (showRemoveDialog) {
        AlertDialog(
            onDismissRequest = { showRemoveDialog = false },
            title = { Text("Remove from Network") },
            text = { Text("Remove this user from your network?") },
            confirmButton = {
                Button(
                    onClick = {
                        showRemoveDialog = false
                        onRemoveFriend()
                        onNavigateBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Remove")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRemoveDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showBlockDialog) {
        AlertDialog(
            onDismissRequest = { showBlockDialog = false },
            title = { Text("Block User") },
            text = { Text("Block this user?") },
            confirmButton = {
                Button(
                    onClick = {
                        showBlockDialog = false
                        onBlockUser()
                        onNavigateBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Block")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBlockDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun ProfileHeaderCard(
    profileData: FullProfile,
    currentUserId: Int,
    isInNetwork: Boolean,
    onMenuClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF001A3D),
                            Color(0xFF004AAD),
                            Color(0xFF0066FF)
                        )
                    )
                )
        ) {
            // Menu button (if not own profile)
            if (currentUserId != profileData.userId) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    var showMenu by remember { mutableStateOf(false) }

                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                Icons.Filled.MoreVert,
                                contentDescription = "Menu",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            if (isInNetwork) {
                                DropdownMenuItem(
                                    text = { Text("Remove user", color = Color.Red) },
                                    onClick = {
                                        showMenu = false
                                        onMenuClick("remove")
                                    },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Filled.PersonRemove,
                                            contentDescription = null,
                                            tint = Color.Red
                                        )
                                    }
                                )
                            }

                            DropdownMenuItem(
                                text = { Text("Block user", color = Color.Red) },
                                onClick = {
                                    showMenu = false
                                    onMenuClick("block")
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Filled.Block,
                                        contentDescription = null,
                                        tint = Color.Red
                                    )
                                }
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 30.dp, horizontal = 25.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Image with gradient border
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    // Outer glow
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFF0066FF).copy(alpha = 0.3f),
                                        Color(0xFF004AAD).copy(alpha = 0.4f),
                                        Color.Transparent
                                    )
                                ),
                                CircleShape
                            )
                    )

                    // Gradient border ring
                    Box(
                        modifier = Modifier
                            .size(134.dp)
                            .background(
                                Brush.sweepGradient(
                                    colors = listOf(
                                        Color(0xFF0066FF).copy(alpha = 0.9f),
                                        Color(0xFFFFDE59).copy(alpha = 0.9f),
                                        Color(0xFF004AAD).copy(alpha = 0.8f),
                                        Color(0xFF003D7A).copy(alpha = 0.6f),
                                        Color(0xFF0066FF).copy(alpha = 0.9f)
                                    )
                                ),
                                CircleShape
                            )
                    )

                    // Profile image with null check
                    if (!profileData.profileImage.isNullOrEmpty()) {
                        AsyncImage(
                            model = profileData.profileImage,
                            contentDescription = "Profile",
                            modifier = Modifier
                                .size(126.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // Fallback for null/empty image
                        Box(
                            modifier = Modifier
                                .size(126.dp)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            Color.Gray.copy(alpha = 0.3f),
                                            Color.Gray.copy(alpha = 0.1f)
                                        )
                                    ),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.Person,
                                contentDescription = "No Profile Image",
                                tint = Color.Gray.copy(alpha = 0.6f),
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(25.dp))

                // Name
                Text(
                    text = "${profileData.firstName ?: "Unknown"} ${profileData.lastName ?: "User"}",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                // Location
                profileData.locations?.takeIf { it.isNotEmpty() }?.let { locations ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.LocationOn,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = locations.joinToString(", "),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(25.dp))

                // Stats Cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
                ) {
                    StatCard(
                        icon = Icons.Filled.People,
                        value = "${profileData.connectionsCount ?: 0}",
                        label = "Connections",
                        gradientColors = listOf(
                            Color(0xFF1E40AF).copy(alpha = 0.9f),
                            Color(0xFF3B82F6).copy(alpha = 0.8f),
                            Color(0xFF60A5FA).copy(alpha = 0.7f)
                        )
                    )

                    StatCard(
                        icon = Icons.Filled.Circle,
                        value = "0",
                        label = "Circles",
                        gradientColors = listOf(
                            Color(0xFF7C3AED).copy(alpha = 0.9f),
                            Color(0xFFA855F7).copy(alpha = 0.8f),
                            Color(0xFFC084FC).copy(alpha = 0.7f)
                        )
                    )

                    StatCard(
                        icon = Icons.Filled.Star,
                        value = "${profileData.circs ?: 0}",
                        label = "Circs",
                        gradientColors = listOf(
                            Color(0xFF059669).copy(alpha = 0.9f),
                            Color(0xFF10B981).copy(alpha = 0.8f),
                            Color(0xFF34D399).copy(alpha = 0.7f)
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String,
    gradientColors: List<Color>
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .background(
                    Brush.linearGradient(colors = gradientColors),
                    RoundedCornerShape(20.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = value,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White.copy(alpha = 0.9f)
        )
    }
}

@Composable
private fun ModernCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
private fun ProfileFieldDisplay(
    label: String,
    value: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray
        )
        Text(
            text = if (value.isEmpty()) "Not set" else value,
            fontSize = 15.sp,
            color = if (value.isEmpty()) Color.Gray else Color.Black
        )
    }
}

// Helper function
private fun calculateAge(birthday: String): String {
    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val birthDate = formatter.parse(birthday) ?: return "N/A"
        val calendar = Calendar.getInstance()
        val today = calendar.time
        calendar.time = birthDate
        val birthYear = calendar.get(Calendar.YEAR)
        calendar.time = today
        val currentYear = calendar.get(Calendar.YEAR)
        "${currentYear - birthYear}"
    } catch (e: Exception) {
        "N/A"
    }
}

