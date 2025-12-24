package com.fragne.circl_app.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Business Profile Page
 * Translated from PageBusinessProfile.swift
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessProfileScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToNetwork: () -> Unit = {},
    onNavigateToCircles: () -> Unit = {},
    onNavigateToGrowthHub: () -> Unit = {}
) {
    var currentTab by remember { mutableStateOf("business") }
    var isEditing by remember { mutableStateOf(false) }

    // Mock business data - TODO: Replace with ViewModel
    var companyName by remember { mutableStateOf("TechVenture Inc") }
    var about by remember { mutableStateOf("We're building the future of entrepreneurial connectivity.") }
    var vision by remember { mutableStateOf("To revolutionize connectivity among entrepreneurs.") }
    var mission by remember { mutableStateOf("To empower entrepreneurs by fostering meaningful connections.") }
    var companyCulture by remember { mutableStateOf("Innovative, inclusive, and results-driven.") }
    var productService by remember { mutableStateOf("Platform connecting entrepreneurs, mentors, and investors.") }
    var usp by remember { mutableStateOf("AI-powered matching algorithm for meaningful connections.") }
    var traction by remember { mutableStateOf("10,000+ users, 50,000+ connections made.") }
    var revenueStreams by remember { mutableStateOf("Subscription plans, premium features, partnerships.") }
    var pricingStrategy by remember { mutableStateOf("Freemium model with premium tiers.") }
    var coFounders by remember { mutableStateOf("John Doe (CEO), Jane Smith (CTO)") }
    var keyHires by remember { mutableStateOf("Head of Growth, Lead Engineer") }
    var advisors by remember { mutableStateOf("Industry veterans from top tech companies.") }
    var fundingStage by remember { mutableStateOf("Series A") }
    var amountRaised by remember { mutableStateOf("$2M") }
    var useOfFunds by remember { mutableStateOf("Product development, marketing, team expansion.") }
    var financialProjections by remember { mutableStateOf("Break-even in 18 months, profitability in 24 months.") }
    var rolesNeeded by remember { mutableStateOf("Senior Developer, Marketing Manager") }
    var mentorship by remember { mutableStateOf("Looking for mentors in scaling and fundraising.") }
    var investment by remember { mutableStateOf("Seeking Series B investment.") }
    var other by remember { mutableStateOf("Open to strategic partnerships.") }

    // Company details
    var industry by remember { mutableStateOf("Technology") }
    var type by remember { mutableStateOf("Startup") }
    var stage by remember { mutableStateOf("Seed") }
    var revenue by remember { mutableStateOf("Pre-revenue") }
    var location by remember { mutableStateOf("San Francisco, CA") }

    val primaryBlue = Color(0xFF004AAD)

    Scaffold(
        topBar = {
            Column {
                // Top Bar - Blue background
                TopAppBar(
                    title = {
                        Text(
                            "Circl.",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },
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
                            .clickable {
                                currentTab = "profile"
                                onNavigateToProfile()
                            },
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
                            .clickable { currentTab = "business" },
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
            // Company Header Card
            item {
                CompanyHeaderCard(
                    companyName = companyName,
                    companyLogoUrl = "",
                    isEditing = isEditing,
                    onNameChange = { companyName = it }
                )
            }

            // About Section
            item {
                BusinessCard(title = "About") {
                    EditableField("", about, isEditing) { about = it }
                }
            }

            // Company Details
            item {
                BusinessCard(title = "Company Details") {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        DetailRow("Industry", industry, isEditing) { industry = it }
                        DetailRow("Type", type, isEditing) { type = it }
                        DetailRow("Stage", stage, isEditing) { stage = it }
                        DetailRow("Revenue", revenue, isEditing) { revenue = it }
                        DetailRow("Location", location, isEditing) { location = it }
                    }
                }
            }

            // Values
            item {
                BusinessCard(title = "Values") {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        EditableField("Vision", vision, isEditing) { vision = it }
                        EditableField("Mission", mission, isEditing) { mission = it }
                        EditableField("Company Culture", companyCulture, isEditing) { companyCulture = it }
                    }
                }
            }

            // Solution
            item {
                BusinessCard(title = "Solution") {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        EditableField("Product/Service", productService, isEditing) { productService = it }
                        EditableField("Unique Selling Proposition", usp, isEditing) { usp = it }
                        EditableField("Traction/Progress", traction, isEditing) { traction = it }
                    }
                }
            }

            // Business Model
            item {
                BusinessCard(title = "Business Model") {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        EditableField("Revenue Streams", revenueStreams, isEditing) { revenueStreams = it }
                        EditableField("Pricing Strategy", pricingStrategy, isEditing) { pricingStrategy = it }
                    }
                }
            }

            // Team
            item {
                BusinessCard(title = "Team") {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        EditableField("CoFounders", coFounders, isEditing) { coFounders = it }
                        EditableField("Key Hires", keyHires, isEditing) { keyHires = it }
                        EditableField("Advisors/Mentors", advisors, isEditing) { advisors = it }
                    }
                }
            }

            // Financials
            item {
                BusinessCard(title = "Financials") {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        EditableField("Funding Stage", fundingStage, isEditing) { fundingStage = it }
                        EditableField("Amount Raised", amountRaised, isEditing) { amountRaised = it }
                        EditableField("Use of Funds", useOfFunds, isEditing) { useOfFunds = it }
                        EditableField("Financial Projections", financialProjections, isEditing) { financialProjections = it }
                    }
                }
            }

            // Looking For
            item {
                BusinessCard(title = "Looking For") {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        EditableField("Roles Needed", rolesNeeded, isEditing) { rolesNeeded = it }
                        EditableField("Mentorship", mentorship, isEditing) { mentorship = it }
                        EditableField("Investment", investment, isEditing) { investment = it }
                        EditableField("Other", other, isEditing) { other = it }
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
private fun CompanyHeaderCard(
    companyName: String,
    companyLogoUrl: String,
    isEditing: Boolean,
    onNameChange: (String) -> Unit
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
            // Company Logo
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(primaryBlue.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Business,
                    contentDescription = null,
                    tint = primaryBlue,
                    modifier = Modifier.size(50.dp)
                )
            }

            // Company Name
            if (isEditing) {
                OutlinedTextField(
                    value = companyName,
                    onValueChange = onNameChange,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            } else {
                Text(
                    text = companyName,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
private fun BusinessCard(
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
private fun EditableField(
    label: String,
    value: String,
    isEditing: Boolean,
    onValueChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray
            )
        }

        if (isEditing) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                minLines = if (value.length > 50) 3 else 1
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

@Composable
private fun DetailRow(
    label: String,
    value: String,
    isEditing: Boolean,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray,
            modifier = Modifier.weight(0.4f)
        )

        if (isEditing) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(0.6f),
                singleLine = true
            )
        } else {
            Text(
                text = value,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.weight(0.6f)
            )
        }
    }
}

