package com.fragne.circl_app.ui.circles.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fragne.circl_app.ui.circles.dashboard.CircleData
import java.text.SimpleDateFormat
import java.util.*

/**
 * Circle Calendar Screen
 * Translated from CalenderView.swift
 * Full implementation with events, check-ins, and create/delete
 */
@Composable
fun CircleCalendarScreen(
    circle: CircleData,
    onNavigateBack: () -> Unit = {}
) {
    var selectedDate by remember { mutableStateOf(Date()) }
    var showAllEvents by remember { mutableStateOf(false) }
    var showCreateEvent by remember { mutableStateOf(false) }
    var events by remember { mutableStateOf<List<CalendarEvent>>(emptyList()) }
    var checkedInEvents by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var isLoading by remember { mutableStateOf(true) }

    val primaryBlue = Color(0xFF004AAD)

    // Mock events - TODO: Replace with API
    LaunchedEffect(Unit) {
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val today = Date()

            val calendar2 = Calendar.getInstance()
            calendar2.time = today
            calendar2.add(Calendar.DAY_OF_MONTH, 2)

            val calendar5 = Calendar.getInstance()
            calendar5.time = today
            calendar5.add(Calendar.DAY_OF_MONTH, 5)

            events = listOf(
                CalendarEvent(
                    id = 1,
                    title = "Tech Workshop",
                    description = "Learn about new technologies",
                    eventType = "Workshop",
                    date = dateFormat.format(today),
                    startTime = "10:00",
                    endTime = "12:00",
                    points = 10,
                    revenue = 0,
                    circleId = circle.id
                ),
                CalendarEvent(
                    id = 2,
                    title = "Networking Mixer",
                    description = "Connect with fellow entrepreneurs",
                    eventType = "Social",
                    date = dateFormat.format(calendar2.time),
                    startTime = "18:00",
                    endTime = "20:00",
                    points = 5,
                    revenue = 25,
                    circleId = circle.id
                ),
                CalendarEvent(
                    id = 3,
                    title = "Speaker Series",
                    description = "Industry leader talk",
                    eventType = "Speaker",
                    date = dateFormat.format(calendar5.time),
                    startTime = "14:00",
                    endTime = "16:00",
                    points = 15,
                    revenue = 50,
                    circleId = circle.id
                )
            )
        } catch (e: Exception) {
            // Handle initialization error gracefully
            events = emptyList()
        } finally {
            isLoading = false
        }
    }

    val filteredEvents = remember(showAllEvents, selectedDate, events) {
        if (showAllEvents) {
            events
        } else {
            try {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val selectedDateStr = dateFormat.format(selectedDate)
                events.filter { it.date == selectedDateStr }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = primaryBlue)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "${circle.name} Calendar",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Manage events and check-ins",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            if (circle.isModerator) {
                IconButton(onClick = { showCreateEvent = true }) {
                    Icon(
                        Icons.Filled.AddCircle,
                        contentDescription = "Create Event",
                        tint = primaryBlue,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Calendar Picker Card
            item {
                CalendarPickerCard(
                    selectedDate = selectedDate,
                    onDateSelected = { selectedDate = it }
                )
            }

            // Events Header with Toggle
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = try {
                            if (showAllEvents) {
                                "All Events (${events.size})"
                            } else {
                                "Events for ${SimpleDateFormat("MMM dd", Locale.getDefault()).format(selectedDate)} (${filteredEvents.size})"
                            }
                        } catch (e: Exception) {
                            "Events (${filteredEvents.size})"
                        },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    TextButton(onClick = { showAllEvents = !showAllEvents }) {
                        Text(
                            text = if (showAllEvents) "Back to Date" else "Show All",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = primaryBlue
                        )
                    }
                }
            }

            // Events List
            if (filteredEvents.isEmpty()) {
                item {
                    EmptyEventsState(
                        isModerator = circle.isModerator,
                        onCreateClick = { showCreateEvent = true }
                    )
                }
            } else {
                items(filteredEvents) { event ->
                    EventCard(
                        event = event,
                        isCheckedIn = checkedInEvents.contains(event.id),
                        isModerator = circle.isModerator,
                        onCheckIn = {
                            checkedInEvents = checkedInEvents + event.id
                            // TODO: POST check-in to API
                        },
                        onDelete = {
                            events = events.filter { it.id != event.id }
                            // TODO: DELETE request to API
                        },
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            }
        }
    }

    // Create Event Dialog
    if (showCreateEvent) {
        CreateEventDialog(
            circle = circle,
            onDismiss = { showCreateEvent = false },
            onCreate = { newEvent ->
                events = events + newEvent
                showCreateEvent = false
                // TODO: POST to API
            }
        )
    }
}

/**
 * Calendar Picker Card - iOS Style Full Month View
 */
@Composable
private fun CalendarPickerCard(
    selectedDate: Date,
    onDateSelected: (Date) -> Unit
) {
    val primaryBlue = Color(0xFF004AAD)
    val calendar = remember { Calendar.getInstance() }
    var currentMonth by remember { mutableStateOf(Calendar.getInstance().apply { time = selectedDate }) }

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
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Month/Year Header with Navigation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(currentMonth.time),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = {
                            currentMonth = Calendar.getInstance().apply {
                                time = currentMonth.time
                                add(Calendar.MONTH, -1)
                            }
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            Icons.Filled.ChevronLeft,
                            contentDescription = "Previous Month",
                            tint = primaryBlue
                        )
                    }

                    IconButton(
                        onClick = {
                            currentMonth = Calendar.getInstance().apply {
                                time = currentMonth.time
                                add(Calendar.MONTH, 1)
                            }
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            Icons.Filled.ChevronRight,
                            contentDescription = "Next Month",
                            tint = primaryBlue
                        )
                    }
                }
            }

            // Day Names Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT").forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                }
            }

            // Calendar Grid
            val monthCalendar = remember(currentMonth) {
                generateCalendarDays(currentMonth.time)
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                monthCalendar.chunked(7).forEach { week ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        week.forEach { day ->
                            CalendarDayCell(
                                day = day,
                                selectedDate = selectedDate,
                                currentMonth = currentMonth,
                                primaryBlue = primaryBlue,
                                onDateSelected = onDateSelected
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Individual Calendar Day Cell
 */
@Composable
private fun RowScope.CalendarDayCell(
    day: CalendarDay,
    selectedDate: Date,
    currentMonth: Calendar,
    primaryBlue: Color,
    onDateSelected: (Date) -> Unit
) {
    val isSelected = remember(selectedDate, day) {
        if (day.date != null) {
            val selectedCal = Calendar.getInstance().apply { time = selectedDate }
            val dayCal = Calendar.getInstance().apply { time = day.date }
            selectedCal.get(Calendar.YEAR) == dayCal.get(Calendar.YEAR) &&
                    selectedCal.get(Calendar.DAY_OF_YEAR) == dayCal.get(Calendar.DAY_OF_YEAR)
        } else false
    }

    val isToday = remember(day) {
        if (day.date != null) {
            val today = Calendar.getInstance()
            val dayCal = Calendar.getInstance().apply { time = day.date }
            today.get(Calendar.YEAR) == dayCal.get(Calendar.YEAR) &&
                    today.get(Calendar.DAY_OF_YEAR) == dayCal.get(Calendar.DAY_OF_YEAR)
        } else false
    }

    Box(
        modifier = Modifier
            .weight(1f)
            .aspectRatio(1f)
            .clickable(enabled = day.date != null && day.isCurrentMonth) {
                day.date?.let { onDateSelected(it) }
            },
        contentAlignment = Alignment.Center
    ) {
        if (day.dayNumber > 0) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = when {
                            isToday -> primaryBlue
                            isSelected -> primaryBlue.copy(alpha = 0.2f)
                            else -> Color.Transparent
                        },
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day.dayNumber.toString(),
                    fontSize = 16.sp,
                    fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = when {
                        isToday -> Color.White
                        !day.isCurrentMonth -> Color.Gray.copy(alpha = 0.4f)
                        else -> Color.Black
                    },
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Generate calendar days for a month
 */
private fun generateCalendarDays(date: Date): List<CalendarDay> {
    val calendar = Calendar.getInstance()
    calendar.time = date

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)

    // First day of the month
    calendar.set(year, month, 1)
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1 // 0 = Sunday

    // Days in current month
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    // Days in previous month
    calendar.add(Calendar.MONTH, -1)
    val daysInPrevMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    val days = mutableListOf<CalendarDay>()

    // Previous month's trailing days
    for (i in (daysInPrevMonth - firstDayOfWeek + 1)..daysInPrevMonth) {
        calendar.set(year, month - 1, i)
        days.add(CalendarDay(i, false, Date(calendar.timeInMillis)))
    }

    // Current month's days
    for (i in 1..daysInMonth) {
        calendar.set(year, month, i)
        days.add(CalendarDay(i, true, Date(calendar.timeInMillis)))
    }

    // Next month's leading days to fill the grid (6 weeks = 42 cells)
    val remainingCells = 42 - days.size
    for (i in 1..remainingCells) {
        calendar.set(year, month + 1, i)
        days.add(CalendarDay(i, false, Date(calendar.timeInMillis)))
    }

    return days
}

/**
 * Calendar Day Data Class
 */
private data class CalendarDay(
    val dayNumber: Int,
    val isCurrentMonth: Boolean,
    val date: Date?
)

/**
 * Event Card
 */
@Composable
private fun EventCard(
    event: CalendarEvent,
    isCheckedIn: Boolean,
    isModerator: Boolean,
    onCheckIn: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryBlue = Color(0xFF004AAD)
    val eventTypeColor = when (event.eventType.lowercase()) {
        "workshop" -> Color(0xFF4CAF50)
        "social" -> Color(0xFFFF9800)
        "meeting" -> Color(0xFF2196F3)
        "speaker" -> Color(0xFF9C27B0)
        "conference" -> Color(0xFF00BCD4)
        else -> primaryBlue
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Title and Type Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = event.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    event.description?.let { desc ->
                        Text(
                            text = desc,
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Event Type Badge
                    Text(
                        text = event.eventType,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier
                            .background(eventTypeColor, RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    )

                    // Delete button for moderators
                    if (isModerator) {
                        IconButton(
                            onClick = onDelete,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = "Delete",
                                tint = Color.Red,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // Date
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.CalendarToday,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = try {
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(event.date)
                            ?.let { SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault()).format(it) }
                            ?: event.date
                    } catch (e: Exception) {
                        event.date
                    },
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // Time
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.AccessTime,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "${formatTime(event.startTime)} - ${formatTime(event.endTime)}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // Points and Revenue
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (event.points > 0) {
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
                            text = "${event.points} pts",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }
                }

                if (event.revenue > 0) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.AttachMoney,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${'$'}${event.revenue}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }
                }
            }

            // Check-in Button
            Button(
                onClick = onCheckIn,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCheckedIn) Color(0xFF4CAF50) else primaryBlue
                ),
                enabled = !isCheckedIn
            ) {
                Icon(
                    if (isCheckedIn) Icons.Filled.CheckCircle else Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isCheckedIn) "Checked In" else "Check In")
            }
        }
    }
}

/**
 * Empty Events State
 */
@Composable
private fun EmptyEventsState(
    isModerator: Boolean,
    onCreateClick: () -> Unit
) {
    val primaryBlue = Color(0xFF004AAD)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        colors = CardDefaults.cardColors(
            containerColor = primaryBlue.copy(alpha = 0.05f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                Icons.Filled.EventBusy,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = "No events scheduled",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
            if (isModerator) {
                Text(
                    text = "Tap the + button to create an event",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Create Event Dialog
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateEventDialog(
    circle: CircleData,
    onDismiss: () -> Unit,
    onCreate: (CalendarEvent) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var eventType by remember { mutableStateOf("Workshop") }
    var points by remember { mutableStateOf("10") }
    var revenue by remember { mutableStateOf("0") }
    var selectedDate by remember { mutableStateOf(Date()) }
    var startTime by remember { mutableStateOf("10:00") }
    var endTime by remember { mutableStateOf("12:00") }
    var expanded by remember { mutableStateOf(false) }

    val eventTypes = listOf("Workshop", "Speaker", "Social", "Meeting", "Conference")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Event") },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Event Name *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )
                }

                item {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = eventType,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Event Type") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            eventTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type) },
                                    onClick = {
                                        eventType = type
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = points,
                            onValueChange = { if (it.all { char -> char.isDigit() }) points = it },
                            label = { Text("Points") },
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = revenue,
                            onValueChange = { if (it.all { char -> char.isDigit() }) revenue = it },
                            label = { Text("Revenue ($)") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = startTime,
                            onValueChange = { startTime = it },
                            label = { Text("Start Time") },
                            placeholder = { Text("HH:MM") },
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = endTime,
                            onValueChange = { endTime = it },
                            label = { Text("End Time") },
                            placeholder = { Text("HH:MM") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onCreate(
                            CalendarEvent(
                                id = System.currentTimeMillis().toInt(),
                                title = title,
                                description = description.takeIf { it.isNotBlank() },
                                eventType = eventType,
                                date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate),
                                startTime = startTime,
                                endTime = endTime,
                                points = points.toIntOrNull() ?: 0,
                                revenue = revenue.toIntOrNull() ?: 0,
                                circleId = circle.id
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
 * Format time from HH:mm to HH:MM AM/PM
 */
private fun formatTime(time: String): String {
    return try {
        val parts = time.split(":")
        val hour = parts[0].toInt()
        val minute = parts[1]
        val amPm = if (hour >= 12) "PM" else "AM"
        val displayHour = when {
            hour == 0 -> 12
            hour > 12 -> hour - 12
            else -> hour
        }
        "$displayHour:$minute $amPm"
    } catch (e: Exception) {
        time
    }
}

// Data Model

data class CalendarEvent(
    val id: Int,
    val title: String,
    val description: String?,
    val eventType: String,
    val date: String,  // yyyy-MM-dd
    val startTime: String,  // HH:mm
    val endTime: String,  // HH:mm
    val points: Int,
    val revenue: Int,
    val circleId: Int
)

