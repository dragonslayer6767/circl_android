package com.fragne.circl_app.ui.circles.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.*

/**
 * Dashboard Member List Screen
 * Admin-only view for managing member payments and contact
 * Translated from DashboardMemberListPage.swift
 */
data class DashboardMember(
    val id: Int,
    val fullName: String,
    val profileImage: String?,
    val userId: Int,
    val isModerator: Boolean,
    val hasPaid: Boolean?,
    val paymentDate: String?,
    val paymentAmount: Double?,
    val email: String?,
    val phoneNumber: String?
)

enum class MemberTab(val title: String) {
    PAID("Paid Members"),
    FREE("Free Members")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardMemberListScreen(
    circleId: Int,
    circleName: String,
    currentUserId: Int,
    onNavigateBack: () -> Unit = {},
    onViewProfile: (Int) -> Unit = {}
) {
    var members by remember { mutableStateOf<List<DashboardMember>>(emptyList()) }
    var selectedTab by remember { mutableStateOf(MemberTab.PAID) }
    var isLoading by remember { mutableStateOf(true) }

    val primaryBlue = Color(0xFF004AAD)
    val lightBlue = Color(0xFF0066FF)

    // Calculate stats
    val paidMembers = members.filter { it.hasPaid == true }
    val freeMembers = members.filter { it.hasPaid != true }
    val totalRevenue = members.mapNotNull { it.paymentAmount }.sum()

    LaunchedEffect(circleId) {
        fetchDashboardMembers(circleId) { fetchedMembers ->
            members = fetchedMembers
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Dashboard Members",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryBlue
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = primaryBlue)
                }
            } else {
                // Header Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5))
                        .padding(20.dp)
                ) {
                    // Circle Name
                    Text(
                        text = circleName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Stats Card
                    StatsCard(
                        paidCount = paidMembers.size,
                        freeCount = freeMembers.size,
                        totalRevenue = totalRevenue
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tab Selector
                    TabSelector(
                        selectedTab = selectedTab,
                        onTabSelected = { selectedTab = it }
                    )
                }

                // Members List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(horizontal = 20.dp),
                    contentPadding = PaddingValues(vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val displayMembers = if (selectedTab == MemberTab.PAID) paidMembers else freeMembers

                    items(displayMembers) { member ->
                        if (selectedTab == MemberTab.PAID) {
                            PaidMemberCard(
                                member = member,
                                currentUserId = currentUserId,
                                onViewProfile = { onViewProfile(member.userId) },
                                onPromoteToModerator = {
                                    promoteToModerator(circleId, member.userId, currentUserId) {
                                        fetchDashboardMembers(circleId) { members = it }
                                    }
                                }
                            )
                        } else {
                            FreeMemberCard(
                                member = member,
                                currentUserId = currentUserId,
                                onViewProfile = { onViewProfile(member.userId) },
                                onMarkAsPaid = {
                                    markAsPaid(circleId, member.userId) {
                                        fetchDashboardMembers(circleId) { members = it }
                                    }
                                },
                                onSendReminder = {
                                    sendPaymentReminder(circleId, member.userId)
                                },
                                onPromoteToModerator = {
                                    promoteToModerator(circleId, member.userId, currentUserId) {
                                        fetchDashboardMembers(circleId) { members = it }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatsCard(
    paidCount: Int,
    freeCount: Int,
    totalRevenue: Double
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 30.dp, horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(
                value = paidCount.toString(),
                label = "Paid Members",
                color = Color(0xFF4CAF50)
            )

            StatItem(
                value = freeCount.toString(),
                label = "Free Members",
                color = Color(0xFFFFA500)
            )

            StatItem(
                value = "$${totalRevenue.toInt()}",
                label = "Total Revenue",
                color = Color(0xFF0066FF)
            )
        }
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = value,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
private fun TabSelector(
    selectedTab: MemberTab,
    onTabSelected: (MemberTab) -> Unit
) {
    val primaryBlue = Color(0xFF004AAD)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFF5F5F5),
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        MemberTab.values().forEach { tab ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = if (selectedTab == tab) primaryBlue else Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { onTabSelected(tab) }
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tab.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (selectedTab == tab) Color.White else Color.Black
                )
            }
        }
    }
}

@Composable
private fun PaidMemberCard(
    member: DashboardMember,
    currentUserId: Int,
    onViewProfile: () -> Unit,
    onPromoteToModerator: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onViewProfile),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Image
            AsyncImage(
                model = member.profileImage,
                contentDescription = "Profile",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Member Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = member.fullName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                member.paymentDate?.let { date ->
                    Text(
                        text = "Paid on ${formatDate(date)}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            // Payment Status
            Column(horizontalAlignment = Alignment.End) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Paid",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF4CAF50)
                    )
                }

                member.paymentAmount?.let { amount ->
                    Text(
                        text = "$$${amount.toInt()}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF4CAF50)
                    )
                }
            }

            // Menu
            if (member.userId != currentUserId && !member.isModerator) {
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            Icons.Filled.MoreVert,
                            contentDescription = "More",
                            tint = Color.Gray
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Make Moderator") },
                            onClick = {
                                showMenu = false
                                onPromoteToModerator()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("View Profile") },
                            onClick = {
                                showMenu = false
                                onViewProfile()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FreeMemberCard(
    member: DashboardMember,
    currentUserId: Int,
    onViewProfile: () -> Unit,
    onMarkAsPaid: () -> Unit,
    onSendReminder: () -> Unit,
    onPromoteToModerator: () -> Unit
) {
    val context = LocalContext.current
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onViewProfile),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Image
            AsyncImage(
                model = member.profileImage,
                contentDescription = "Profile",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Member Info with Contact
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = member.fullName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                member.email?.let { email ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Email,
                            contentDescription = null,
                            tint = Color(0xFF0066FF),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = email,
                            fontSize = 10.sp,
                            color = Color(0xFF0066FF)
                        )
                    }
                }

                member.phoneNumber?.let { phone ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Phone,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = phone,
                            fontSize = 10.sp,
                            color = Color(0xFF4CAF50)
                        )
                    }
                }
            }

            // Pending Status
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.Warning,
                    contentDescription = null,
                    tint = Color(0xFFFFA500),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Pending",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFFFA500)
                )
            }

            // Contact Menu
            if (member.userId != currentUserId && !member.isModerator) {
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            Icons.Filled.MoreVert,
                            contentDescription = "More",
                            tint = Color.Gray
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        member.email?.let { email ->
                            DropdownMenuItem(
                                text = { Text("Email Member") },
                                onClick = {
                                    showMenu = false
                                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                                        data = Uri.parse("mailto:$email")
                                    }
                                    context.startActivity(intent)
                                },
                                leadingIcon = {
                                    Icon(Icons.Filled.Email, contentDescription = null)
                                }
                            )
                        }

                        member.phoneNumber?.let { phone ->
                            DropdownMenuItem(
                                text = { Text("Call Member") },
                                onClick = {
                                    showMenu = false
                                    val intent = Intent(Intent.ACTION_DIAL).apply {
                                        data = Uri.parse("tel:${phone.replace(Regex("[^0-9]"), "")}")
                                    }
                                    context.startActivity(intent)
                                },
                                leadingIcon = {
                                    Icon(Icons.Filled.Phone, contentDescription = null)
                                }
                            )

                            DropdownMenuItem(
                                text = { Text("Text Member") },
                                onClick = {
                                    showMenu = false
                                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                                        data = Uri.parse("sms:${phone.replace(Regex("[^0-9]"), "")}")
                                    }
                                    context.startActivity(intent)
                                },
                                leadingIcon = {
                                    Icon(Icons.AutoMirrored.Filled.Message, contentDescription = null)
                                }
                            )
                        }

                        DropdownMenuItem(
                            text = { Text("Mark as Paid") },
                            onClick = {
                                showMenu = false
                                onMarkAsPaid()
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Send Payment Reminder") },
                            onClick = {
                                showMenu = false
                                onSendReminder()
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Make Moderator") },
                            onClick = {
                                showMenu = false
                                onPromoteToModerator()
                            }
                        )
                    }
                }
            }
        }
    }
}

// Helper Functions
private fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        date?.let { outputFormat.format(it) } ?: dateString
    } catch (e: Exception) {
        dateString
    }
}

// Mock API calls - TODO: Replace with actual implementation
private fun fetchDashboardMembers(
    circleId: Int,
    onResult: (List<DashboardMember>) -> Unit
) {
    // Mock data
    val mockMembers = listOf(
        DashboardMember(
            id = 1,
            fullName = "Sarah Johnson",
            profileImage = null,
            userId = 101,
            isModerator = false,
            hasPaid = true,
            paymentDate = "2025-01-15",
            paymentAmount = 75.00,
            email = "sarah.johnson@example.com",
            phoneNumber = "+1-555-123-4567"
        ),
        DashboardMember(
            id = 2,
            fullName = "Mike Davis",
            profileImage = null,
            userId = 102,
            isModerator = false,
            hasPaid = false,
            paymentDate = null,
            paymentAmount = null,
            email = "mike.davis@example.com",
            phoneNumber = "+1-555-987-6543"
        )
    )
    onResult(mockMembers)
}

private fun markAsPaid(circleId: Int, userId: Int, onSuccess: () -> Unit) {
    // TODO: PUT /api/circles/{circle_id}/members/{user_id}/mark_paid/
    onSuccess()
}

private fun sendPaymentReminder(circleId: Int, userId: Int) {
    // TODO: POST /api/circles/{circle_id}/members/{user_id}/payment_reminder/
}

