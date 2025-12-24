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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import com.fragne.circl_app.tutorial.TutorialManager
import com.fragne.circl_app.tutorial.models.OnboardingData
import com.fragne.circl_app.ui.components.CloudBackground
import com.fragne.circl_app.ui.theme.CirclBlue
import com.fragne.circl_app.ui.theme.CirclYellow
import com.fragne.circl_app.ui.theme.CirclWhite

/**
 * Page3 - User Information Form
 * Collects basic user info, usage interest, and industry category
 */
@Composable
fun Page3Screen(
    onNavigateToNext: () -> Unit
) {
    val context = LocalContext.current
    val tutorialManager = remember { TutorialManager.getInstance(context) }

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedUsageInterest by remember { mutableStateOf<String?>(null) }
    var selectedIndustryInterest by remember { mutableStateOf<String?>(null) }
    var showUsageInterestDialog by remember { mutableStateOf(false) }
    var showIndustryDialog by remember { mutableStateOf(false) }
    var showAlert by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }

    val usageInterestOptions = listOf(
        "Sell a Skill", "Make Investments", "Share Knowledge", "Be Part of the Community",
        "Find Investors", "Find Mentors", "Find Co-Founder/s", "Network with Entrepreneurs",
        "Scale Your Business", "Start Your Business", "Student"
    )

    val industryCategories = mapOf(
        "Technology & Digital" to listOf(
            "Artificial Intelligence", "Blockchain & Web3", "Cloud Computing", "Cybersecurity",
            "Data Science & Analytics", "Internet of Things (IoT)", "Metaverse Technologies",
            "Robotics & Automation", "Software Development", "Virtual/Augmented Reality"
        ),
        "Business & Finance" to listOf(
            "Accounting & Financial Services", "Consulting & Professional Services",
            "E-Commerce & Marketplaces", "FinTech (Financial Technology)", "Franchising",
            "Investment & Venture Capital", "Real Estate & Property Tech", "Startups & Entrepreneurship"
        ),
        "Consumer Goods & Services" to listOf(
            "Beauty & Personal Care", "Consumer Electronics", "Fashion & Apparel", "Food & Beverage",
            "Home Goods & Furniture", "Luxury Goods", "Retail & Merchandising"
        ),
        "Creative & Media" to listOf(
            "Advertising & Marketing", "Architecture & Design", "Arts & Culture", "Entertainment & Media",
            "Gaming & Esports", "Music & Audio Production", "Publishing & Journalism"
        ),
        "Education & Knowledge" to listOf(
            "Corporate Training", "EdTech (Education Technology)", "Higher Education", "K-12 Education",
            "Online Learning Platforms", "Professional Development"
        ),
        "Energy & Environment" to listOf(
            "Clean Energy", "Environmental Services", "Green Technology", "Recycling & Waste Management",
            "Sustainability Solutions"
        ),
        "Food & Agriculture" to listOf(
            "AgTech (Agriculture Technology)", "Beverage Production", "Food Production",
            "Food Service & Hospitality", "Organic Farming", "Restaurant & Dining"
        ),
        "Health & Wellness" to listOf(
            "Biotechnology", "Fitness & Sports", "HealthTech", "Medical Devices", "Mental Health Services",
            "Pharmaceuticals", "Wellness & Self-Care"
        ),
        "Industrial & Manufacturing" to listOf(
            "3D Printing", "Advanced Manufacturing", "Aerospace & Defense", "Automotive",
            "Chemical Production", "Construction", "Industrial Equipment"
        ),
        "Services" to listOf(
            "Childcare & Education", "Cleaning Services", "Event Planning", "Home Services",
            "Legal Services", "Logistics & Delivery", "Staffing & Recruiting"
        ),
        "Social Impact" to listOf(
            "Community Development", "Nonprofit Organizations", "Social Enterprises",
            "Urban Development", "Women-Led Businesses"
        )
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

            HorizontalDivider(
                color = CirclWhite,
                thickness = 2.dp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Personal Information Section
            Text(
                text = "Personal Information",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = CirclWhite,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(15.dp))

            // First Name
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                placeholder = { Text("First Name", color = CirclBlue.copy(alpha = 0.6f)) },
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

            // Last Name
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                placeholder = { Text("Last Name", color = CirclBlue.copy(alpha = 0.6f)) },
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

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email", color = CirclBlue.copy(alpha = 0.6f)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CirclWhite,
                    unfocusedContainerColor = CirclWhite,
                    focusedTextColor = CirclBlue,
                    unfocusedTextColor = CirclBlue
                ),
                shape = RoundedCornerShape(10.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Phone Number
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                placeholder = { Text("Phone Number", color = CirclBlue.copy(alpha = 0.6f)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CirclWhite,
                    unfocusedContainerColor = CirclWhite,
                    focusedTextColor = CirclBlue,
                    unfocusedTextColor = CirclBlue
                ),
                shape = RoundedCornerShape(10.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Password", color = CirclBlue.copy(alpha = 0.6f)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CirclWhite,
                    unfocusedContainerColor = CirclWhite,
                    focusedTextColor = CirclBlue,
                    unfocusedTextColor = CirclBlue
                ),
                shape = RoundedCornerShape(10.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Confirm Password
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = { Text("Confirm Password", color = CirclBlue.copy(alpha = 0.6f)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CirclWhite,
                    unfocusedContainerColor = CirclWhite,
                    focusedTextColor = CirclBlue,
                    unfocusedTextColor = CirclBlue
                ),
                shape = RoundedCornerShape(10.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Usage Interest
            Text(
                text = "What brings you here?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = CirclWhite,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { showUsageInterestDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CirclWhite
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = selectedUsageInterest ?: "Select Usage Interest",
                    color = CirclBlue,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Industry Interest
            Text(
                text = "Which industry interests you?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = CirclWhite,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { showIndustryDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CirclWhite
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = selectedIndustryInterest ?: "Select Industry",
                    color = CirclBlue,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Next Button
            Button(
                onClick = {
                    // Validate form
                    when {
                        firstName.isBlank() || lastName.isBlank() || email.isBlank() ||
                        phoneNumber.isBlank() || password.isBlank() || confirmPassword.isBlank() -> {
                            alertMessage = "Please fill in all fields"
                            showAlert = true
                        }
                        password != confirmPassword -> {
                            alertMessage = "Passwords do not match"
                            showAlert = true
                        }
                        selectedUsageInterest == null -> {
                            alertMessage = "Please select your usage interest"
                            showAlert = true
                        }
                        selectedIndustryInterest == null -> {
                            alertMessage = "Please select your industry interest"
                            showAlert = true
                        }
                        else -> {
                            // Create onboarding data
                            val onboardingData = OnboardingData(
                                usageInterests = selectedUsageInterest ?: "",
                                industryInterests = selectedIndustryInterest ?: "",
                                location = "", // Add location if captured in future
                                userGoals = null
                            )

                            // Detect and set user type for tutorial
                            tutorialManager.detectAndSetUserType(onboardingData)

                            // Mark that onboarding was just completed to trigger tutorial
                            val sharedPreferences = context.getSharedPreferences(
                                "tutorial_preferences",
                                android.content.Context.MODE_PRIVATE
                            )
                            sharedPreferences.edit()
                                .putBoolean("just_completed_onboarding", true)
                                .putBoolean("onboarding_completed", true)
                                .apply()

                            android.util.Log.d("Page3Screen", "✅ Onboarding completed - Tutorial will trigger after login")

                            // Navigate to next page
                            onNavigateToNext()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(5.dp, RoundedCornerShape(10.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CirclYellow
                ),
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

        // Usage Interest Dialog
        if (showUsageInterestDialog) {
            AlertDialog(
                onDismissRequest = { showUsageInterestDialog = false },
                title = { Text("What brings you here?") },
                text = {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        usageInterestOptions.forEach { option ->
                            TextButton(
                                onClick = {
                                    selectedUsageInterest = option
                                    showUsageInterestDialog = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(option, modifier = Modifier.fillMaxWidth())
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showUsageInterestDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Industry Dialog
        if (showIndustryDialog) {
            AlertDialog(
                onDismissRequest = { showIndustryDialog = false },
                title = { Text("Select Industry") },
                text = {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        industryCategories.forEach { (category, industries) ->
                            Text(
                                text = category,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                            industries.forEach { industry ->
                                TextButton(
                                    onClick = {
                                        selectedIndustryInterest = industry
                                        showIndustryDialog = false
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(industry, modifier = Modifier.fillMaxWidth())
                                }
                            }
                            HorizontalDivider()
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showIndustryDialog = false }) {
                        Text("Cancel")
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
fun Page3ScreenPreview() {
    Page3Screen(onNavigateToNext = {})
}

