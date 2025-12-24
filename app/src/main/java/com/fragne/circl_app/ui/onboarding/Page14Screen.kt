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
 * Page14 - Terms & Conditions Screen
 * Shows T&C and Privacy Policy with required checkboxes
 */
@Composable
fun Page14Screen(
    onNavigateToNext: () -> Unit
) {
    var agreedToTerms by remember { mutableStateOf(false) }
    var agreedToPrivacyPolicy by remember { mutableStateOf(false) }

    CloudBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Title
            Text(
                text = "Terms and Conditions",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = CirclYellow
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Separator
            HorizontalDivider(
                color = CirclWhite,
                thickness = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // White Display Box for Legal Text
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(15.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CirclWhite
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "A. Terms & Conditions",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = CirclBlue
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Effective Date: March 30, 2025",
                        fontSize = 14.sp,
                        color = CirclBlue.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = """Welcome to Circl International Inc. ("Circl," "we," "us," or "our"). By accessing or using our platform, you agree to abide by these Terms & Conditions. If you do not agree, please do not use our platform.
                        
1. Use of the Platform
You must be at least 18 years old to use Circl.
You are responsible for all activity under your account.
Misuse of the platform, including spamming, harassment, or unauthorized access, will result in suspension or termination.

2. Intellectual Property
Circl owns all platform content, trademarks, and proprietary materials.
Users retain ownership of their content but grant Circl a license to use it for platform functionality.

3. Modification of Terms
Circl reserves the right to update these Terms & Conditions at any time. Users will be notified of significant changes.

4. Limitation of Liability
Circl is not responsible for business outcomes, financial losses, or damages resulting from platform use.
We provide the platform "as is" without warranties of any kind.

5. Governing Law
These Terms & Conditions are governed by the laws of the State of Texas, USA.

6. Termination
We reserve the right to terminate accounts violating these terms.""",
                        fontSize = 14.sp,
                        color = CirclBlue
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "B. Privacy Policy",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = CirclBlue
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Effective Date: March 30, 2025",
                        fontSize = 14.sp,
                        color = CirclBlue.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = """Circl International Inc. values your privacy. This Privacy Policy explains how we collect, use, and protect your data.

1. Information We Collect
Personal data (e.g., name, email, business details).
Usage data (e.g., interactions with the platform).

2. How We Use Your Data
To provide and improve our platform.
To personalize user experience and offer relevant business connections.
To communicate updates, promotions, and opportunities.

3. Data Sharing
We do not sell personal data.
We may share data with trusted third-party service providers.

4. Data Security
We implement industry-standard security measures to protect your data.

5. Your Rights
You can access, update, or delete your personal data at any time.

6. Cookies
We use cookies to enhance user experience. You can disable cookies in your browser settings.""",
                        fontSize = 14.sp,
                        color = CirclBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Checkboxes
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = agreedToTerms,
                    onCheckedChange = { agreedToTerms = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = CirclYellow,
                        uncheckedColor = CirclWhite
                    )
                )
                Text(
                    text = "I agree to the Terms & Conditions",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = CirclWhite
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = agreedToPrivacyPolicy,
                    onCheckedChange = { agreedToPrivacyPolicy = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = CirclYellow,
                        uncheckedColor = CirclWhite
                    )
                )
                Text(
                    text = "I agree to the Privacy Policy",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = CirclWhite
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Next Button
            Button(
                onClick = onNavigateToNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(5.dp, RoundedCornerShape(10.dp)),
                enabled = agreedToTerms && agreedToPrivacyPolicy,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CirclYellow,
                    disabledContainerColor = CirclYellow.copy(alpha = 0.5f)
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

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Page14ScreenPreview() {
    Page14Screen(onNavigateToNext = {})
}

