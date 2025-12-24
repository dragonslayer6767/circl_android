package com.fragne.circl_app.ui.settings.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
 * Become a Mentor Page
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BecomeMentorPage(
    onNavigateBack: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var industry by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }

    val primaryBlue = Color(0xFF004AAD)
    val lightBlue = Color(0xFF0066FF)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Become a Mentor") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(0.dp))

            // Header
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.School,
                    contentDescription = null,
                    tint = primaryBlue,
                    modifier = Modifier.size(48.dp)
                )

                Text(
                    text = "Mentor Application",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Text(
                    text = "Share your expertise and help fellow entrepreneurs grow",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }

            // Form Card
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
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Full Name
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Full Name",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            placeholder = { Text("Enter your full name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Industry
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Industry",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )

                        OutlinedTextField(
                            value = industry,
                            onValueChange = { industry = it },
                            placeholder = { Text("e.g., Technology, Finance, Healthcare") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Reason
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Why do you want to become a mentor?",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )

                        OutlinedTextField(
                            value = reason,
                            onValueChange = { reason = it },
                            placeholder = { Text("Share your motivation...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            maxLines = 5
                        )
                    }

                    // Submit Button
                    Button(
                        onClick = { isSubmitted = true },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = name.isNotEmpty() && industry.isNotEmpty() && reason.isNotEmpty(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            disabledContainerColor = Color.Gray
                        ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (isSubmitted) {
                                        Brush.horizontalGradient(
                                            colors = listOf(Color.Green, Color.Green.copy(alpha = 0.8f))
                                        )
                                    } else {
                                        Brush.horizontalGradient(
                                            colors = listOf(primaryBlue, lightBlue)
                                        )
                                    },
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (isSubmitted) {
                                    Icon(
                                        imageVector = Icons.Filled.CheckCircle,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Text(
                                    text = if (isSubmitted) "Application Submitted!" else "Submit Application",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    if (isSubmitted) {
                        Text(
                            text = "We'll review your application and get back to you within 2-3 business days.",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

/**
 * Change Password Page
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordPage(
    onNavigateBack: () -> Unit = {}
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showSuccess by remember { mutableStateOf(false) }

    val primaryBlue = Color(0xFF004AAD)
    val lightBlue = Color(0xFF0066FF)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Change Password") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Icon and Title
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = null,
                    tint = primaryBlue,
                    modifier = Modifier.size(48.dp)
                )

                Text(
                    text = "Update Your Password",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

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
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    OutlinedTextField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        label = { Text("Current Password") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("New Password") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm New Password") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = { showSuccess = true },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = currentPassword.isNotEmpty() &&
                                  newPassword.isNotEmpty() &&
                                  confirmPassword == newPassword,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(primaryBlue, lightBlue)
                                    ),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Update Password",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }

                    if (showSuccess) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Green.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = Color.Green
                            )
                            Text(
                                text = "Password updated successfully!",
                                color = Color.Green,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Generic Placeholder Page
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericSettingsPage(
    title: String,
    description: String,
    onNavigateBack: () -> Unit = {}
) {
    val primaryBlue = Color(0xFF004AAD)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Construction,
                    contentDescription = null,
                    tint = primaryBlue,
                    modifier = Modifier.size(64.dp)
                )

                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = description,
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

