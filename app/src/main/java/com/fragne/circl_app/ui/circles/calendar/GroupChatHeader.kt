package com.fragne.circl_app.ui.circles.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Group Chat Header Component
 * Translated from GroupChatHeader.swift
 */
@Composable
fun GroupChatHeader(
    hasDashboard: Boolean,
    selectedTab: GroupTab,
    onTabSelected: (GroupTab) -> Unit,
    onNavigateBack: () -> Unit
) {
    val primaryBlue = Color(0xFF004AAD)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(primaryBlue)
    ) {
        // Top padding for status bar
        Spacer(modifier = Modifier.height(48.dp))

        // Top Header with Back Button and Logo
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button
            IconButton(onClick = onNavigateBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Logo
            Text(
                text = "Circl.",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            // Spacer to keep logo centered
            Spacer(modifier = Modifier.width(48.dp))
        }

        // Tab Navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Dashboard Tab (if enabled)
            if (hasDashboard) {
                TabItem(
                    icon = Icons.Filled.PieChart,
                    label = "Dashboard",
                    isSelected = selectedTab == GroupTab.DASHBOARD,
                    onClick = { onTabSelected(GroupTab.DASHBOARD) }
                )
            }

            // Home Tab
            TabItem(
                icon = Icons.Filled.Home,
                label = "Home",
                isSelected = selectedTab == GroupTab.HOME,
                onClick = { onTabSelected(GroupTab.HOME) }
            )

            // Calendar Tab
            TabItem(
                icon = Icons.Filled.CalendarMonth,
                label = "Calendar",
                isSelected = selectedTab == GroupTab.CALENDAR,
                onClick = { onTabSelected(GroupTab.CALENDAR) }
            )
        }
    }
}

/**
 * Tab Item Component
 */
@Composable
private fun TabItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White.copy(alpha = if (isSelected) 1f else 0.6f),
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White.copy(alpha = if (isSelected) 1f else 0.6f)
        )
    }
}

/**
 * Group Tab Enum
 */
enum class GroupTab {
    DASHBOARD,
    HOME,
    CALENDAR
}

