package com.fragne.circl_app.ui.circles.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

/**
 * Manage Channels Screen
 * Translated from ManageChannelsView.swift
 *
 * Features:
 * - Create/delete categories
 * - Add channels to categories
 * - Inline channel creation within categories
 * - Drag and drop to reorder categories
 * - Toggle moderator-only visibility for channels
 * - Delete channels and categories
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageChannelsScreen(
    circleId: Int,
    initialChannels: List<Channel>,
    initialCategories: List<ChannelCategory>,
    onNavigateBack: () -> Unit = {},
    onSave: (List<Channel>, List<ChannelCategory>) -> Unit = { _, _ -> }
) {
    var localChannels by remember { mutableStateOf(initialChannels.sortedBy { it.position }) }
    var categories by remember { mutableStateOf(initialCategories) }
    var newCategoryName by remember { mutableStateOf("") }
    var isCreatingCategory by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var selectedCategoryId by remember { mutableStateOf<Int?>(null) }
    var channelNameForCategory by remember { mutableStateOf(mapOf<Int, String>()) }

    val primaryBlue = Color(0xFF004AAD)

    // Calculate uncategorized channels
    val uncategorizedChannels = remember(localChannels, categories) {
        val categorizedIds = categories.flatMap { it.channels.map { ch -> ch.id } }.toSet()
        localChannels.filter { it.id !in categorizedIds }
    }

    Scaffold(
        topBar = {
            ManageChannelsHeader(
                onNavigateBack = onNavigateBack,
                onSave = { onSave(localChannels, categories) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFF5F5F5),
                                primaryBlue.copy(alpha = 0.02f)
                            )
                        )
                    ),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Add New Category Section
                item {
                    AddCategorySection(
                        newCategoryName = newCategoryName,
                        onCategoryNameChange = { newCategoryName = it },
                        onCreateCategory = {
                            isCreatingCategory = true
                            // TODO: Call API to create category
                            // For now, add locally
                            val newCategory = ChannelCategory(
                                id = (categories.maxOfOrNull { it.id } ?: 0) + 1,
                                name = newCategoryName,
                                position = categories.size + 1,
                                channels = emptyList()
                            )
                            categories = categories + newCategory
                            newCategoryName = ""
                            isCreatingCategory = false
                        },
                        isLoading = isCreatingCategory
                    )
                }

                // Instructions
                item {
                    InstructionsCard()
                }

                // Categories List
                if (categories.isEmpty() && uncategorizedChannels.isEmpty()) {
                    item {
                        EmptyCategoriesState()
                    }
                } else {
                    items(categories.sortedBy { it.position }) { category ->
                        CategoryCard(
                            category = category,
                            selectedCategoryId = selectedCategoryId,
                            channelName = channelNameForCategory[category.id] ?: "",
                            onChannelNameChange = { name ->
                                channelNameForCategory = channelNameForCategory + (category.id to name)
                            },
                            onToggleExpand = {
                                selectedCategoryId = if (selectedCategoryId == category.id) null else category.id
                            },
                            onAddChannel = { channelName ->
                                // TODO: Call API to add channel
                                val newChannel = Channel(
                                    id = (localChannels.maxOfOrNull { it.id } ?: 0) + 1,
                                    name = if (channelName.startsWith("#")) channelName else "#$channelName",
                                    category = category.name,
                                    circleId = circleId,
                                    position = localChannels.size + 1,
                                    isModeratorOnly = false
                                )
                                localChannels = localChannels + newChannel

                                // Add to category
                                val updatedCategory = category.copy(
                                    channels = category.channels + newChannel
                                )
                                categories = categories.map { if (it.id == category.id) updatedCategory else it }

                                channelNameForCategory = channelNameForCategory - category.id
                                selectedCategoryId = null
                            },
                            onDeleteChannel = { channel ->
                                localChannels = localChannels.filter { it.id != channel.id }
                                val updatedCategory = category.copy(
                                    channels = category.channels.filter { it.id != channel.id }
                                )
                                categories = categories.map { if (it.id == category.id) updatedCategory else it }
                            },
                            onToggleModeratorOnly = { channel ->
                                val channelIndex = localChannels.indexOfFirst { it.id == channel.id }
                                if (channelIndex != -1) {
                                    val updatedChannel = localChannels[channelIndex].copy(
                                        isModeratorOnly = !channel.isModeratorOnly
                                    )
                                    localChannels = localChannels.toMutableList().apply {
                                        set(channelIndex, updatedChannel)
                                    }

                                    // Update in category
                                    val updatedCategory = category.copy(
                                        channels = category.channels.map {
                                            if (it.id == channel.id) updatedChannel else it
                                        }
                                    )
                                    categories = categories.map { if (it.id == category.id) updatedCategory else it }
                                }
                            },
                            onDeleteCategory = {
                                categories = categories.filter { it.id != category.id }
                            }
                        )
                    }

                    // Uncategorized Channels
                    if (uncategorizedChannels.isNotEmpty()) {
                        item {
                            UncategorizedSection(
                                channels = uncategorizedChannels.sortedBy { it.position },
                                onDeleteChannel = { channel ->
                                    localChannels = localChannels.filter { it.id != channel.id }
                                },
                                onToggleModeratorOnly = { channel ->
                                    val channelIndex = localChannels.indexOfFirst { it.id == channel.id }
                                    if (channelIndex != -1) {
                                        val updatedChannel = localChannels[channelIndex].copy(
                                            isModeratorOnly = !channel.isModeratorOnly
                                        )
                                        localChannels = localChannels.toMutableList().apply {
                                            set(channelIndex, updatedChannel)
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }

            // Loading Overlay
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                        .zIndex(10f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = primaryBlue)
                }
            }
        }
    }

    // Error Dialog
    if (showError) {
        AlertDialog(
            onDismissRequest = { showError = false },
            title = { Text("Error") },
            text = { Text(errorMessage) },
            confirmButton = {
                Button(onClick = { showError = false }) {
                    Text("OK")
                }
            }
        )
    }
}

/**
 * Header with Cancel and Save buttons
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ManageChannelsHeader(
    onNavigateBack: () -> Unit,
    onSave: () -> Unit
) {
    val primaryBlue = Color(0xFF004AAD)

    TopAppBar(
        title = {
            Text(
                text = "Manage Channels",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        },
        navigationIcon = {
            TextButton(onClick = onNavigateBack) {
                Text(
                    text = "Cancel",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        },
        actions = {
            TextButton(onClick = onSave) {
                Text(
                    text = "Save",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = primaryBlue
        )
    )
}

/**
 * Add Category Section
 */
@Composable
private fun AddCategorySection(
    newCategoryName: String,
    onCategoryNameChange: (String) -> Unit,
    onCreateCategory: () -> Unit,
    isLoading: Boolean
) {
    val primaryBlue = Color(0xFF004AAD)
    val lightBlue = Color(0xFF0066FF)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = primaryBlue.copy(alpha = 0.05f),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = primaryBlue.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Add New Category",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newCategoryName,
                onValueChange = onCategoryNameChange,
                placeholder = { Text("Enter category name") },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Folder,
                        contentDescription = null,
                        tint = primaryBlue
                    )
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryBlue.copy(alpha = 0.2f),
                    unfocusedBorderColor = primaryBlue.copy(alpha = 0.2f),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true
            )

            IconButton(
                onClick = onCreateCategory,
                enabled = newCategoryName.trim().isNotEmpty() && !isLoading,
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        brush = if (newCategoryName.trim().isNotEmpty()) {
                            Brush.linearGradient(
                                colors = listOf(primaryBlue, lightBlue)
                            )
                        } else {
                            Brush.linearGradient(
                                colors = listOf(Color.Gray, Color.Gray)
                            )
                        },
                        shape = CircleShape
                    )
                    .shadow(4.dp, CircleShape)
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add Category",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/**
 * Instructions Card
 */
@Composable
private fun InstructionsCard() {
    val primaryBlue = Color(0xFF004AAD)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = primaryBlue.copy(alpha = 0.05f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.Menu,
                contentDescription = null,
                tint = primaryBlue,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = "Hold and drag the ≡ handle to reorder categories",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
        }
    }
}

/**
 * Empty Categories State
 */
@Composable
private fun EmptyCategoriesState() {
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
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                Icons.Filled.Layers,
                contentDescription = null,
                tint = primaryBlue.copy(alpha = 0.4f),
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = "No categories yet",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
            Text(
                text = "Add a category to organize your channels",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}


/**
 * Category Card with inline channel creation
 */
@Composable
private fun CategoryCard(
    category: ChannelCategory,
    selectedCategoryId: Int?,
    channelName: String,
    onChannelNameChange: (String) -> Unit,
    onToggleExpand: () -> Unit,
    onAddChannel: (String) -> Unit,
    onDeleteChannel: (Channel) -> Unit,
    onToggleModeratorOnly: (Channel) -> Unit,
    onDeleteCategory: () -> Unit
) {
    val primaryBlue = Color(0xFF004AAD)
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Category Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Drag Handle
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.size(20.dp)
                ) {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(
                                    color = Color.Gray.copy(alpha = 0.6f),
                                    shape = RoundedCornerShape(1.dp)
                                )
                        )
                    }
                }

                Text(
                    text = category.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )

                // Menu Button
                var showMenu by remember { mutableStateOf(false) }
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            Icons.Filled.MoreVert,
                            contentDescription = "Menu",
                            tint = Color.Gray
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Delete Category", color = Color.Red) },
                            onClick = {
                                showMenu = false
                                showDeleteDialog = true
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Delete,
                                    contentDescription = null,
                                    tint = Color.Red
                                )
                            }
                        )
                    }
                }

                // Add Channel Label (when not expanded)
                if (selectedCategoryId != category.id) {
                    Text(
                        text = "Add Channel",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(end = 6.dp)
                    )
                }

                // Add/Close Button
                IconButton(onClick = onToggleExpand) {
                    Icon(
                        if (selectedCategoryId == category.id) Icons.Filled.Close else Icons.Filled.AddCircle,
                        contentDescription = if (selectedCategoryId == category.id) "Close" else "Add Channel",
                        tint = if (selectedCategoryId == category.id) Color.Red else primaryBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Inline Channel Creation Box
            if (selectedCategoryId == category.id) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = channelName,
                        onValueChange = onChannelNameChange,
                        placeholder = { Text("Enter channel name") },
                        leadingIcon = {
                            Icon(
                                Icons.Filled.Tag,
                                contentDescription = null,
                                tint = primaryBlue
                            )
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryBlue.copy(alpha = 0.2f),
                            unfocusedBorderColor = primaryBlue.copy(alpha = 0.2f),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        singleLine = true
                    )

                    IconButton(
                        onClick = {
                            if (channelName.trim().isNotEmpty()) {
                                onAddChannel(channelName)
                            }
                        },
                        enabled = channelName.trim().isNotEmpty(),
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                color = if (channelName.trim().isNotEmpty()) primaryBlue else Color.Gray,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Add",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Channels List
            category.channels.forEach { channel ->
                ChannelRowItem(
                    channel = channel,
                    onDelete = { onDeleteChannel(channel) },
                    onToggleModeratorOnly = { onToggleModeratorOnly(channel) }
                )
            }
        }
    }

    // Delete Category Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Category") },
            text = { Text("Are you sure you want to delete this category? Channels will become uncategorized.") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteCategory()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

/**
 * Channel Row Item with delete and moderator-only toggle
 */
@Composable
private fun ChannelRowItem(
    channel: Channel,
    onDelete: () -> Unit,
    onToggleModeratorOnly: () -> Unit
) {
    val primaryBlue = Color(0xFF004AAD)
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Channel Icon
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = primaryBlue.copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Tag,
                    contentDescription = null,
                    tint = primaryBlue,
                    modifier = Modifier.size(14.dp)
                )
            }

            Text(
                text = channel.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )

            // Moderator Only Badge
            if (channel.isModeratorOnly) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFFFFA500).copy(alpha = 0.1f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Lock,
                            contentDescription = null,
                            tint = Color(0xFFFFA500),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "Mod Only",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFFFA500)
                        )
                    }
                }
            }

            // Menu Button
            Box {
                IconButton(
                    onClick = { showMenu = true },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Filled.MoreVert,
                        contentDescription = "Menu",
                        tint = Color.Gray
                    )
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                if (channel.isModeratorOnly) "Make Public" else "Restrict to Moderators"
                            )
                        },
                        onClick = {
                            showMenu = false
                            onToggleModeratorOnly()
                        },
                        leadingIcon = {
                            Icon(
                                if (channel.isModeratorOnly) Icons.Filled.LockOpen else Icons.Filled.Lock,
                                contentDescription = null,
                                tint = primaryBlue
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete Channel", color = Color.Red) },
                        onClick = {
                            showMenu = false
                            showDeleteDialog = true
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = null,
                                tint = Color.Red
                            )
                        }
                    )
                }
            }
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Channel") },
            text = { Text("Are you sure you want to delete '${channel.name}'? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

/**
 * Uncategorized Channels Section
 */
@Composable
private fun UncategorizedSection(
    channels: List<Channel>,
    onDeleteChannel: (Channel) -> Unit,
    onToggleModeratorOnly: (Channel) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Uncategorized",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            channels.forEach { channel ->
                ChannelRowItem(
                    channel = channel,
                    onDelete = { onDeleteChannel(channel) },
                    onToggleModeratorOnly = { onToggleModeratorOnly(channel) }
                )
            }
        }
    }
}


