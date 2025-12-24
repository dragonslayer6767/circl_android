package com.fragne.circl_app.ui.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fragne.circl_app.ui.components.CloudBackground
import com.fragne.circl_app.ui.theme.CirclBlue
import com.fragne.circl_app.ui.theme.CirclYellow
import com.fragne.circl_app.ui.theme.CirclWhite

/**
 * Page5 - Personal Information Screen
 * Collects birthday, location, gender, availability, and personality type
 */
@Composable
fun Page5Screen(
    onNavigateToNext: () -> Unit
) {
    var birthday by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf<String?>(null) }
    var selectedAvailability by remember { mutableStateOf<String?>(null) }
    var personalityType by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var showGenderDialog by remember { mutableStateOf(false) }
    var showAvailabilityDialog by remember { mutableStateOf(false) }
    var showAvailabilityInfo by remember { mutableStateOf(false) }
    var showPersonalityTypeInfo by remember { mutableStateOf(false) }
    var showAlert by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }

    val genderOptions = listOf("Male", "Female", "Prefer not to say")
    val availabilityOptions = listOf(
        "Full-time (40+ hrs/week)",
        "Part-time (20-40 hrs/week)",
        "Side project (<20 hrs/week)",
        "Weekends only",
        "Flexible hours",
        "Currently employed - exploring options"
    )

    CloudBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            // Title
            Text(
                text = "Create Your Account",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = CirclYellow
            )

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider(color = CirclWhite, thickness = 2.dp)

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Personal Information",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = CirclWhite,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(15.dp))

            // Birthday Field
            OutlinedButton(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = CirclWhite,
                    contentColor = CirclBlue
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "📅 ",
                    fontSize = 18.sp
                )
                Text(
                    text = if (birthday.isEmpty()) "Select your Birthday" else birthday,
                    color = if (birthday.isEmpty()) CirclBlue.copy(alpha = 0.6f) else CirclBlue,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Location Field
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                placeholder = { Text("Location (City, State)", color = CirclBlue.copy(alpha = 0.6f)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CirclWhite,
                    unfocusedContainerColor = CirclWhite,
                    focusedTextColor = CirclBlue,
                    unfocusedTextColor = CirclBlue
                ),
                shape = RoundedCornerShape(10.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Gender Selection
            Text(
                text = "Gender",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = CirclWhite,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { showGenderDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = CirclWhite),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = selectedGender ?: "Select Gender",
                    color = CirclBlue,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Availability
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Availability",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = CirclWhite
                )
                TextButton(onClick = { showAvailabilityInfo = true }) {
                    Text("ℹ️", fontSize = 20.sp)
                }
            }

            Button(
                onClick = { showAvailabilityDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = CirclWhite),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = selectedAvailability ?: "Select Availability",
                    color = CirclBlue,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Personality Type
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Personality Type (MBTI)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = CirclWhite
                )
                TextButton(onClick = { showPersonalityTypeInfo = true }) {
                    Text("ℹ️", fontSize = 20.sp)
                }
            }

            OutlinedTextField(
                value = personalityType,
                onValueChange = { if (it.length <= 4) personalityType = it.uppercase() },
                placeholder = { Text("e.g., INTJ", color = CirclBlue.copy(alpha = 0.6f)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CirclWhite,
                    unfocusedContainerColor = CirclWhite,
                    focusedTextColor = CirclBlue,
                    unfocusedTextColor = CirclBlue
                ),
                shape = RoundedCornerShape(10.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Next Button
            Button(
                onClick = {
                    when {
                        birthday.isEmpty() -> {
                            alertMessage = "Please select your birthday"
                            showAlert = true
                        }
                        location.isBlank() -> {
                            alertMessage = "Please enter your location"
                            showAlert = true
                        }
                        selectedGender == null -> {
                            alertMessage = "Please select your gender"
                            showAlert = true
                        }
                        selectedAvailability == null -> {
                            alertMessage = "Please select your availability"
                            showAlert = true
                        }
                        else -> onNavigateToNext()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(5.dp, RoundedCornerShape(10.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = CirclYellow),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(vertical = 15.dp)
            ) {
                Text(
                    text = "Next",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = CirclBlue
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

        // Gender Dialog
        if (showGenderDialog) {
            AlertDialog(
                onDismissRequest = { showGenderDialog = false },
                title = { Text("Select Gender") },
                text = {
                    Column {
                        genderOptions.forEach { option ->
                            TextButton(
                                onClick = {
                                    selectedGender = option
                                    showGenderDialog = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(option, modifier = Modifier.fillMaxWidth())
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showGenderDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Availability Dialog
        if (showAvailabilityDialog) {
            AlertDialog(
                onDismissRequest = { showAvailabilityDialog = false },
                title = { Text("Select Availability") },
                text = {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        availabilityOptions.forEach { option ->
                            TextButton(
                                onClick = {
                                    selectedAvailability = option
                                    showAvailabilityDialog = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(option, modifier = Modifier.fillMaxWidth())
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showAvailabilityDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Availability Info Dialog
        if (showAvailabilityInfo) {
            AlertDialog(
                onDismissRequest = { showAvailabilityInfo = false },
                title = { Text("About Availability") },
                text = { Text("Select how much time you can dedicate to networking, collaboration, or business ventures.") },
                confirmButton = {
                    Button(
                        onClick = { showAvailabilityInfo = false },
                        colors = ButtonDefaults.buttonColors(containerColor = CirclYellow)
                    ) {
                        Text("OK", color = CirclBlue)
                    }
                }
            )
        }

        // Personality Type Info Dialog
        if (showPersonalityTypeInfo) {
            AlertDialog(
                onDismissRequest = { showPersonalityTypeInfo = false },
                title = { Text("About Personality Type") },
                text = { Text("Enter your MBTI personality type (e.g., INTJ, ENFP). Take the test at 16personalities.com if you don't know yours.") },
                confirmButton = {
                    Button(
                        onClick = { showPersonalityTypeInfo = false },
                        colors = ButtonDefaults.buttonColors(containerColor = CirclYellow)
                    ) {
                        Text("OK", color = CirclBlue)
                    }
                }
            )
        }

        // Date Picker (simplified - use actual DatePickerDialog in production)
        if (showDatePicker) {
            AlertDialog(
                onDismissRequest = { showDatePicker = false },
                title = { Text("Select Birthday") },
                text = { Text("Date picker would go here") },
                confirmButton = {
                    Button(
                        onClick = {
                            birthday = "01/01/2000"  // Mock date
                            showDatePicker = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CirclYellow)
                    ) {
                        Text("OK", color = CirclBlue)
                    }
                }
            )
        }

        // Alert Dialog
        if (showAlert) {
            AlertDialog(
                onDismissRequest = { showAlert = false },
                title = { Text("Validation Error") },
                text = { Text(alertMessage) },
                confirmButton = {
                    Button(
                        onClick = { showAlert = false },
                        colors = ButtonDefaults.buttonColors(containerColor = CirclYellow)
                    ) {
                        Text("OK", color = CirclBlue)
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Page5ScreenPreview() {
    Page5Screen(onNavigateToNext = {})
}

