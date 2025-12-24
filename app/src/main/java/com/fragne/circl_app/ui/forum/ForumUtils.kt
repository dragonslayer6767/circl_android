package com.fragne.circl_app.ui.forum

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Converts ISO8601 timestamp to relative time string (e.g., "2 hours ago")
 */
fun timeAgo(dateString: String): String {
    return try {
        // Parse ISO8601 date string
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("UTC")

        val date = sdf.parse(dateString) ?: return "Just now"
        val now = Date()
        val seconds = (now.time - date.time) / 1000

        when {
            seconds < 10 -> "Just now"
            seconds < 60 -> "${seconds}sec${if (seconds != 1L) "s" else ""} ago"
            seconds < 3600 -> "${seconds / 60} min ago"
            seconds < 86400 -> {
                val hours = seconds / 3600
                "${hours} hour${if (hours > 1) "s" else ""} ago"
            }
            seconds < 604800 -> {
                val days = seconds / 86400
                "${days} day${if (days > 1) "s" else ""} ago"
            }
            seconds < 2592000 -> {
                val weeks = seconds / 604800
                "${weeks} week${if (weeks > 1) "s" else ""} ago"
            }
            seconds < 31536000 -> {
                val months = seconds / 2592000
                "${months} month${if (months > 1) "s" else ""} ago"
            }
            else -> {
                val years = seconds / 31536000
                "${years} year${if (years > 1) "s" else ""} ago"
            }
        }
    } catch (e: Exception) {
        "Just now"
    }
}

