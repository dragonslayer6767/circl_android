package com.fragne.circl_app.ui.circles.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Circle Dashboard Screen
 * Translated from DashboardView.swift
 * Full implementation with stats, kanban, projects, and leaderboard
 */
@Composable
fun CircleDashboardScreen(
    circle: CircleData,
    onNavigateBack: () -> Unit = {}
) {
    var selectedTimeframe by remember { mutableStateOf("all") }
    var viewMode by remember { mutableStateOf(TaskViewMode.KANBAN) }
    var showCreateMenu by remember { mutableStateOf(false) }
    var showCreateTask by remember { mutableStateOf(false) }
    var showCreateProject by remember { mutableStateOf(false) }

    val primaryBlue = Color(0xFF004AAD)

    // Mock data - TODO: Replace with ViewModel/API
    val summary = remember {
        DashboardSummary(
            totalEvents = 12,
            totalPoints = 450,
            totalMembers = circle.memberCount,
            totalRevenue = 2500
        )
    }

    val leaderboard = remember {
        listOf(
            LeaderboardEntry(1, "John Doe", "john@example.com", 150),
            LeaderboardEntry(2, "Jane Smith", "jane@example.com", 120),
            LeaderboardEntry(3, "Mike Johnson", "mike@example.com", 95)
        )
    }

    val projects = remember {
        mutableStateListOf(
            Project(
                id = 1,
                name = "Website Redesign",
                description = "Modernize company website",
                color = ProjectColor.BLUE,
                progress = 60,
                tasks = listOf()
            ),
            Project(
                id = 2,
                name = "Marketing Campaign",
                description = "Q4 social media push",
                color = ProjectColor.GREEN,
                progress = 30,
                tasks = listOf()
            )
        )
    }

    val standaloneTasks = remember {
        mutableStateListOf(
            TaskItem(
                id = 1,
                title = "Review designs",
                description = "Check mockups",
                status = TaskStatus.IN_PROGRESS,
                priority = TaskPriority.HIGH
            ),
            TaskItem(
                id = 2,
                title = "Update documentation",
                description = "API docs",
                status = TaskStatus.NOT_STARTED,
                priority = TaskPriority.MEDIUM
            )
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        contentPadding = PaddingValues(vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${circle.name} Dashboard",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Overview of circle activity and analytics",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }

        // Circle Info Card
        item {
            CircleInfoCard(circle = circle)
        }

        // Summary Statistics
        item {
            SummaryStatsGrid(summary = summary)
        }

        // Task Manager Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                // Header with view mode toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Task Manager",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // View mode toggle
                        FilterChip(
                            selected = viewMode == TaskViewMode.KANBAN,
                            onClick = { viewMode = TaskViewMode.KANBAN },
                            label = { Text("Kanban") }
                        )
                        FilterChip(
                            selected = viewMode == TaskViewMode.PROJECTS,
                            onClick = { viewMode = TaskViewMode.PROJECTS },
                            label = { Text("Projects") }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Create button
                Button(
                    onClick = { showCreateMenu = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryBlue
                    )
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Create Task or Project")
                }
            }
        }

        // Kanban Board or Projects Grid
        item {
            if (viewMode == TaskViewMode.KANBAN) {
                KanbanBoardView(
                    tasks = standaloneTasks + projects.flatMap { it.tasks },
                    onTaskClick = { /* TODO */ }
                )
            } else {
                ProjectsGridView(
                    projects = projects,
                    onProjectClick = { /* TODO */ }
                )
            }
        }

        // Leaderboard Section
        item {
            LeaderboardSection(
                leaderboard = leaderboard,
                selectedTimeframe = selectedTimeframe,
                onTimeframeChange = { selectedTimeframe = it }
            )
        }
    }

    // Create Task Dialog
    if (showCreateTask) {
        CreateTaskDialog(
            onDismiss = { showCreateTask = false },
            onCreate = { task ->
                standaloneTasks.add(task)
                showCreateTask = false
            }
        )
    }

    // Create Project Dialog
    if (showCreateProject) {
        CreateProjectDialog(
            onDismiss = { showCreateProject = false },
            onCreate = { project ->
                projects.add(project)
                showCreateProject = false
            }
        )
    }

    // Create Menu
    if (showCreateMenu) {
        AlertDialog(
            onDismissRequest = { showCreateMenu = false },
            title = { Text("Create New") },
            text = {
                Column {
                    TextButton(
                        onClick = {
                            showCreateMenu = false
                            showCreateTask = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.Task, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Task")
                    }
                    TextButton(
                        onClick = {
                            showCreateMenu = false
                            showCreateProject = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.Folder, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Project")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCreateMenu = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

/**
 * Circle Info Card
 */
@Composable
private fun CircleInfoCard(circle: CircleData) {
    val primaryBlue = Color(0xFF004AAD)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = circle.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = primaryBlue
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.Business,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = circle.industry,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            Text(
                text = circle.description,
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.8f),
                lineHeight = 20.sp
            )
        }
    }
}

/**
 * Summary Stats Grid
 */
@Composable
private fun SummaryStatsGrid(summary: DashboardSummary) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Events",
                value = summary.totalEvents.toString(),
                icon = Icons.Filled.Event,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Points",
                value = summary.totalPoints.toString(),
                icon = Icons.Filled.Star,
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Members",
                value = summary.totalMembers.toString(),
                icon = Icons.Filled.People,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Revenue",
                value = "${'$'}${summary.totalRevenue}",
                icon = Icons.Filled.AttachMoney,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Stat Card
 */
@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    val primaryBlue = Color(0xFF004AAD)

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = primaryBlue,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = title,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

/**
 * Kanban Board View
 */
@Composable
private fun KanbanBoardView(
    tasks: List<TaskItem>,
    onTaskClick: (TaskItem) -> Unit
) {
    val primaryBlue = Color(0xFF004AAD)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Kanban Board",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Not Started Column
                item {
                    KanbanColumn(
                        title = "Not Started",
                        tasks = tasks.filter { it.status == TaskStatus.NOT_STARTED },
                        color = Color.Gray,
                        onTaskClick = onTaskClick
                    )
                }

                // In Progress Column
                item {
                    KanbanColumn(
                        title = "In Progress",
                        tasks = tasks.filter { it.status == TaskStatus.IN_PROGRESS },
                        color = primaryBlue,
                        onTaskClick = onTaskClick
                    )
                }

                // Done Column
                item {
                    KanbanColumn(
                        title = "Done",
                        tasks = tasks.filter { it.status == TaskStatus.DONE },
                        color = Color(0xFF4CAF50),
                        onTaskClick = onTaskClick
                    )
                }
            }
        }
    }
}

/**
 * Kanban Column
 */
@Composable
private fun KanbanColumn(
    title: String,
    tasks: List<TaskItem>,
    color: Color,
    onTaskClick: (TaskItem) -> Unit
) {
    Column(
        modifier = Modifier
            .width(250.dp)
            .background(color.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, CircleShape)
            )
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "(${tasks.size})",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        tasks.forEach { task ->
            TaskCard(task = task, onClick = { onTaskClick(task) })
        }

        if (tasks.isEmpty()) {
            Text(
                text = "No tasks",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Task Card
 */
@Composable
private fun TaskCard(
    task: TaskItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = task.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            task.description?.let { desc ->
                Text(
                    text = desc,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 2
                )
            }

            // Priority badge
            val priorityColor = when (task.priority) {
                TaskPriority.HIGH -> Color.Red
                TaskPriority.MEDIUM -> Color(0xFFFFA500)
                TaskPriority.LOW -> Color.Green
            }

            Text(
                text = task.priority.name,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier
                    .background(priorityColor, RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}

/**
 * Projects Grid View
 */
@Composable
private fun ProjectsGridView(
    projects: List<Project>,
    onProjectClick: (Project) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        projects.forEach { project ->
            ProjectCard(project = project, onClick = { onProjectClick(project) })
        }
    }
}

/**
 * Project Card
 */
@Composable
private fun ProjectCard(
    project: Project,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = project.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    project.description?.let { desc ->
                        Text(
                            text = desc,
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(
                            when (project.color) {
                                ProjectColor.BLUE -> Color(0xFF2196F3)
                                ProjectColor.GREEN -> Color(0xFF4CAF50)
                                ProjectColor.ORANGE -> Color(0xFFFF9800)
                                ProjectColor.PURPLE -> Color(0xFF9C27B0)
                                ProjectColor.RED -> Color(0xFFF44336)
                            },
                            CircleShape
                        )
                )
            }

            // Progress bar
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Progress",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "${project.progress}%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
                LinearProgressIndicator(
                    progress = project.progress / 100f,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF004AAD)
                )
            }

            // Task count
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.Task,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.Gray
                )
                Text(
                    text = "${project.tasks.size} tasks",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

/**
 * Leaderboard Section
 */
@Composable
private fun LeaderboardSection(
    leaderboard: List<LeaderboardEntry>,
    selectedTimeframe: String,
    onTimeframeChange: (String) -> Unit
) {
    val primaryBlue = Color(0xFF004AAD)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Circle Leaderboard",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            // Timeframe selector (simplified)
            Text(
                text = "All Time",
                fontSize = 12.sp,
                color = primaryBlue
            )
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                leaderboard.forEachIndexed { index, entry ->
                    LeaderboardItem(entry = entry, rank = index + 1)
                    if (index < leaderboard.size - 1) {
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

/**
 * Leaderboard Item
 */
@Composable
private fun LeaderboardItem(entry: LeaderboardEntry, rank: Int) {
    val medalColor = when (rank) {
        1 -> Color(0xFFFFD700) // Gold
        2 -> Color(0xFFC0C0C0) // Silver
        3 -> Color(0xFFCD7F32) // Bronze
        else -> Color.Gray
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            // Rank
            Text(
                text = "#$rank",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = medalColor
            )

            // User info
            Column {
                Text(
                    text = entry.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Text(
                    text = entry.email,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        // Points
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.Star,
                contentDescription = null,
                tint = Color(0xFFFFC107),
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "${entry.points} pts",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

/**
 * Create Task Dialog
 */
@Composable
private fun CreateTaskDialog(
    onDismiss: () -> Unit,
    onCreate: (TaskItem) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Task") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onCreate(
                            TaskItem(
                                id = System.currentTimeMillis().toInt(),
                                title = title,
                                description = description,
                                status = TaskStatus.NOT_STARTED,
                                priority = TaskPriority.MEDIUM
                            )
                        )
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

/**
 * Create Project Dialog
 */
@Composable
private fun CreateProjectDialog(
    onDismiss: () -> Unit,
    onCreate: (Project) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Project") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onCreate(
                            Project(
                                id = System.currentTimeMillis().toInt(),
                                name = name,
                                description = description,
                                color = ProjectColor.BLUE,
                                progress = 0,
                                tasks = emptyList()
                            )
                        )
                    }
                },
                enabled = name.isNotBlank()
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// Data Models

data class DashboardSummary(
    val totalEvents: Int,
    val totalPoints: Int,
    val totalMembers: Int,
    val totalRevenue: Int
)

data class LeaderboardEntry(
    val userId: Int,
    val name: String,
    val email: String,
    val points: Int
)

data class TaskItem(
    val id: Int,
    val title: String,
    val description: String?,
    val status: TaskStatus,
    val priority: TaskPriority
)

data class Project(
    val id: Int,
    val name: String,
    val description: String?,
    val color: ProjectColor,
    val progress: Int,
    val tasks: List<TaskItem>
)

enum class TaskStatus {
    NOT_STARTED,
    IN_PROGRESS,
    DONE
}

enum class TaskPriority {
    LOW,
    MEDIUM,
    HIGH
}

enum class ProjectColor {
    BLUE,
    GREEN,
    ORANGE,
    PURPLE,
    RED
}

enum class TaskViewMode {
    KANBAN,
    PROJECTS
}

