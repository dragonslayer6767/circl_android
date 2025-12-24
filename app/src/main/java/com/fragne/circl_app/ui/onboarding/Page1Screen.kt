package com.fragne.circl_app.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fragne.circl_app.ui.theme.CirclBlue
import com.fragne.circl_app.ui.theme.CirclBlueDark
import com.fragne.circl_app.ui.theme.CirclYellow
import com.fragne.circl_app.ui.theme.CirclWhite

/**
 * Page1 - Login/Entry Screen
 * Main entry point where users login or click "Join Circl" to start signup
 */
@Composable
fun Page1Screen(
    onNavigateToSignup: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var forgotPasswordEmail by remember { mutableStateOf("") }
    var showInvalidCredentialsAlert by remember { mutableStateOf(false) }
    var showForgotPasswordConfirmation by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        CirclBlue,
                        CirclBlueDark,
                        CirclBlue
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            // Logo Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .background(CirclWhite, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Circl.",
                        fontSize = 55.sp,
                        fontWeight = FontWeight.Bold,
                        color = CirclBlue
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Where Ideas Go Around",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = CirclWhite,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Join Circl Button
            Button(
                onClick = onNavigateToSignup,
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CirclYellow
                ),
                shape = RoundedCornerShape(15.dp),
                contentPadding = PaddingValues(vertical = 18.dp)
            ) {
                Text(
                    text = "Join Circl",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = CirclBlue
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            // Login Card
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CirclWhite.copy(alpha = 0.15f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(30.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    // Email Field
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text("Email", color = CirclBlue.copy(alpha = 0.6f)) },
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CirclWhite.copy(alpha = 0.95f),
                            unfocusedContainerColor = CirclWhite.copy(alpha = 0.95f),
                            focusedTextColor = CirclBlue,
                            unfocusedTextColor = CirclBlue,
                            focusedBorderColor = CirclWhite.copy(alpha = 0.3f),
                            unfocusedBorderColor = CirclWhite.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(15.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true
                    )

                    // Password Field
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = { Text("Password", color = CirclBlue.copy(alpha = 0.6f)) },
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CirclWhite.copy(alpha = 0.95f),
                            unfocusedContainerColor = CirclWhite.copy(alpha = 0.95f),
                            focusedTextColor = CirclBlue,
                            unfocusedTextColor = CirclBlue,
                            focusedBorderColor = CirclWhite.copy(alpha = 0.3f),
                            unfocusedBorderColor = CirclWhite.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(15.dp),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                // Mock login for now
                                onNavigateToMain()
                            }
                        ),
                        singleLine = true
                    )

                    // Login Button
                    Button(
                        onClick = {
                            // Mock login for now
                            onNavigateToMain()
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CirclYellow
                        ),
                        shape = RoundedCornerShape(15.dp),
                        contentPadding = PaddingValues(vertical = 18.dp)
                    ) {
                        Text(
                            text = "Login",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = CirclBlue
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Forgot Password
            TextButton(
                onClick = { showForgotPasswordDialog = true }
            ) {
                Text(
                    text = "Forgot your password?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = CirclWhite.copy(alpha = 0.9f),
                    textDecoration = TextDecoration.Underline
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        // Forgot Password Dialog
        if (showForgotPasswordDialog) {
            AlertDialog(
                onDismissRequest = { showForgotPasswordDialog = false },
                title = {
                    Text(
                        "Forgot Password",
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column {
                        Text("Enter your email. We will get back to you soon with a new password.")
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = forgotPasswordEmail,
                            onValueChange = { forgotPasswordEmail = it },
                            placeholder = { Text("Email") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email
                            ),
                            singleLine = true
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showForgotPasswordDialog = false
                            showForgotPasswordConfirmation = true
                            forgotPasswordEmail = ""
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CirclYellow
                        )
                    ) {
                        Text("Submit", color = CirclBlue)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showForgotPasswordDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Forgot Password Confirmation
        if (showForgotPasswordConfirmation) {
            AlertDialog(
                onDismissRequest = { showForgotPasswordConfirmation = false },
                title = { Text("Password Reset") },
                text = { Text("We will get back to you soon with a new password.") },
                confirmButton = {
                    Button(
                        onClick = { showForgotPasswordConfirmation = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CirclYellow
                        )
                    ) {
                        Text("OK", color = CirclBlue)
                    }
                }
            )
        }

        // Invalid Credentials Alert
        if (showInvalidCredentialsAlert) {
            AlertDialog(
                onDismissRequest = { showInvalidCredentialsAlert = false },
                title = { Text("Invalid Credentials") },
                text = { Text("The email or password you entered is incorrect. Please try again.") },
                confirmButton = {
                    Button(
                        onClick = { showInvalidCredentialsAlert = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CirclYellow
                        )
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
fun Page1ScreenPreview() {
    Page1Screen(
        onNavigateToSignup = {},
        onNavigateToMain = {}
    )
}
