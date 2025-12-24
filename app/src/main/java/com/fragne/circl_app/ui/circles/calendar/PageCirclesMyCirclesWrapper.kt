package com.fragne.circl_app.ui.circles.calendar

import androidx.compose.runtime.Composable
import com.fragne.circl_app.ui.circles.dashboard.CirclesScreen
import com.fragne.circl_app.ui.circles.dashboard.CircleData

/**
 * Page Circles My Circles Wrapper
 * Translated from PageCirclesMyCirclesWrapper.swift
 *
 * Wrapper to display CirclesScreen with "My Circles" tab pre-selected
 */
@Composable
fun PageCirclesMyCirclesWrapper(
    onNavigateToProfile: () -> Unit = {},
    onNavigateToMessages: () -> Unit = {},
    onNavigateToCircleChat: (CircleData) -> Unit = {}
) {
    // Simply display CirclesScreen with My Circles as the initial tab
    // Note: CirclesScreen would need to accept an initialTab parameter to implement this fully
    CirclesScreen(
        onNavigateToProfile = onNavigateToProfile,
        onNavigateToMessages = onNavigateToMessages,
        onNavigateToCircleChat = onNavigateToCircleChat
    )
}

/**
 * Alternative: CirclesScreen with My Circles Selected
 * This composable exists to maintain API compatibility with Swift version
 */
@Composable
fun PageCirclesWithMyCirclesSelected(
    onNavigateToProfile: () -> Unit = {},
    onNavigateToMessages: () -> Unit = {},
    onNavigateToCircleChat: (CircleData) -> Unit = {}
) {
    CirclesScreen(
        onNavigateToProfile = onNavigateToProfile,
        onNavigateToMessages = onNavigateToMessages,
        onNavigateToCircleChat = onNavigateToCircleChat
    )
}

