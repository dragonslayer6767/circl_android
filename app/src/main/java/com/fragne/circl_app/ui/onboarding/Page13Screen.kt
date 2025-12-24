package com.fragne.circl_app.ui.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fragne.circl_app.ui.components.CloudBackground
import com.fragne.circl_app.ui.theme.CirclBlue
import com.fragne.circl_app.ui.theme.CirclYellow
import com.fragne.circl_app.ui.theme.CirclWhite

/**
 * Page13 - Notifications Permission Screen
 * Asks user to enable push notifications
 */
@Composable
fun Page13Screen(
    onNavigateToNext: () -> Unit
) {
    var notificationsEnabled by remember { mutableStateOf(false) }

    CloudBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = "Notifications",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = CirclYellow
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Separator Line
                HorizontalDivider(
                    color = CirclWhite,
                    thickness = 2.dp
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Body Text
                Text(
                    text = "Turn on your notifications so we can give you new information, exclusive deals, and more!",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = CirclWhite,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(80.dp))

                // Toggle Section
                Text(
                    text = "Mobile App Notifications",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = CirclWhite
                )

                Spacer(modifier = Modifier.height(15.dp))

                // Custom styled toggle
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = CirclYellow,
                        checkedTrackColor = CirclYellow.copy(alpha = 0.5f),
                        uncheckedThumbColor = CirclWhite,
                        uncheckedTrackColor = CirclWhite.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier.scale(1.5f)
                )
            }

            // Next Button
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onNavigateToNext,
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
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Page13ScreenPreview() {
    Page13Screen(onNavigateToNext = {})
}


