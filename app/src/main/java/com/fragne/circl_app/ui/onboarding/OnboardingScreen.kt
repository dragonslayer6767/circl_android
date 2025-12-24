package com.fragne.circl_app.ui.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Onboarding screen - initial welcome/intro screen
 * Equivalent to iOS onboarding flow
 */
@Composable
fun OnboardingScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToSignup: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Welcome to Circl",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Connect with mentors, entrepreneurs, and build your network",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onNavigateToSignup,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Get Started")
            }

            OutlinedButton(
                onClick = onNavigateToLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Log In")
            }
        }
    }
}

