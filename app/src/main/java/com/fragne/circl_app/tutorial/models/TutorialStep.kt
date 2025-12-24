package com.fragne.circl_app.tutorial.models

import androidx.compose.ui.geometry.Rect
import java.util.UUID

/**
 * Tutorial Step Model
 * Represents a single step in the tutorial flow
 */
data class TutorialStep(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val targetView: String, // Identifier for the view/component to highlight
    val message: String, // Detailed explanation for this user type
    val navigationDestination: String? = null, // Where to navigate (if needed)
    val highlightRect: Rect? = null, // Area to highlight (optional)
    val tooltipAlignment: TooltipAlignment = TooltipAlignment.CENTER,
    val duration: Long? = null, // Duration in milliseconds
    val isInteractive: Boolean = false // Whether user needs to interact or just view
) {
    /**
     * Tooltip Alignment Options
     */
    enum class TooltipAlignment {
        TOP,
        BOTTOM,
        LEADING,
        TRAILING,
        CENTER
    }
}

