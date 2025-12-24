package com.fragne.circl_app.ui.settings.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fragne.circl_app.tutorial.TutorialManager
import com.fragne.circl_app.tutorial.models.UserType

/**
 * Tutorial Settings Screen
 * Allows users to restart tutorials and select different user type tutorials
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialSettingsScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val tutorialManager = remember { TutorialManager.getInstance(context) }
    val currentUserType by tutorialManager.userType.collectAsState()

    var selectedUserType by remember { mutableStateOf(currentUserType) }
    var showDropdown by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tutorial & Help") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF004AAD),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header
            Icon(
                imageVector = Icons.Filled.School,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.CenterHorizontally),
                tint = Color(0xFF004AAD)
            )

            Text(
                text = "Interactive Tutorial",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF004AAD),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Text(
                text = "Learn how to use Circl with our personalized guided tour",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

            // Current Status
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF5F5F5)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Current User Type",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray
                    )
                    Text(
                        text = currentUserType.displayName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF004AAD)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Tutorial Status",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray
                    )
                    Text(
                        text = if (tutorialManager.hasTutorialBeenCompleted(currentUserType)) {
                            "Completed ✓"
                        } else {
                            "Not Started"
                        },
                        fontSize = 16.sp,
                        color = if (tutorialManager.hasTutorialBeenCompleted(currentUserType)) {
                            Color(0xFF34C759)
                        } else {
                            Color.Gray
                        }
                    )
                }
            }

            // User Type Selector
            Text(
                text = "Select Tutorial Type",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF004AAD)
            )

            ExposedDropdownMenuBox(
                expanded = showDropdown,
                onExpandedChange = { showDropdown = it }
            ) {
                OutlinedTextField(
                    value = selectedUserType.displayName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tutorial Type") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showDropdown) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = showDropdown,
                    onDismissRequest = { showDropdown = false }
                ) {
                    UserType.values().forEach { userType ->
                        DropdownMenuItem(
                            text = { Text(userType.displayName) },
                            onClick = {
                                selectedUserType = userType
                                showDropdown = false
                            }
                        )
                    }
                }
            }

            // Tutorial Description
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE3F2FD)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = getTutorialDescription(selectedUserType),
                        fontSize = 14.sp,
                        color = Color(0xFF004AAD),
                        lineHeight = 20.sp
                    )
                }
            }

            // Start Tutorial Button
            Button(
                onClick = { showConfirmDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF004AAD)
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Start Tutorial",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Help Text
            Text(
                text = "The tutorial will guide you through the app's features step by step. You can skip or exit at any time.",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        // Confirmation Dialog
        if (showConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmDialog = false },
                title = { Text("Start Tutorial?") },
                text = {
                    Text("This will start the ${selectedUserType.displayName} tutorial. You can skip it at any time.")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showConfirmDialog = false
                            tutorialManager.restartTutorial(selectedUserType)
                            onNavigateBack()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF004AAD)
                        )
                    ) {
                        Text("Start")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showConfirmDialog = false }) {
                        Text("Cancel", color = Color(0xFF004AAD))
                    }
                }
            )
        }
    }
}

/**
 * Get tutorial description for each user type
 */
private fun getTutorialDescription(userType: UserType): String {
    return when (userType) {
        UserType.ENTREPRENEUR ->
            "Learn how to find co-founders, connect with investors, showcase your venture, and leverage Circl's tools to grow your startup."

        UserType.STUDENT ->
            "Discover how to find mentors, learn from experienced entrepreneurs, and accelerate your learning journey on Circl."

        UserType.STUDENT_ENTREPRENEUR ->
            "Get a comprehensive guide for building your startup while in school, finding co-founders, and balancing both worlds."

        UserType.MENTOR ->
            "Learn how to share your knowledge, find mentees, and make an impact by guiding the next generation of entrepreneurs."

        UserType.INVESTOR ->
            "Discover how to find investment opportunities, connect directly with founders, and access quality deal flow on Circl."

        UserType.COMMUNITY_BUILDER ->
            "Get a complete overview of Circl's features and learn how to connect, engage, and grow within the community."

        UserType.OTHER ->
            "Get a complete overview of Circl's features and learn how to make the most of the platform."
    }
}

