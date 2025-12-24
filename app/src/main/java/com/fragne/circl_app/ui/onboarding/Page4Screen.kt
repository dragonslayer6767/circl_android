package com.fragne.circl_app.ui.onboarding

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fragne.circl_app.ui.components.CloudBackground
import com.fragne.circl_app.ui.theme.CirclBlue
import com.fragne.circl_app.ui.theme.CirclYellow
import com.fragne.circl_app.ui.theme.CirclWhite

/**
 * Page4 - Profile Picture Upload Screen
 * Allows users to upload a profile picture (optional)
 */
@Composable
fun Page4Screen(
    onNavigateToNext: () -> Unit
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    CloudBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // Title Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Add Profile Picture",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = CirclYellow
                )

                Spacer(modifier = Modifier.height(8.dp))

                HorizontalDivider(
                    color = CirclWhite,
                    thickness = 2.dp
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Subtitle
            Text(
                text = "Make your profile stand out! Add a photo so others can get to know you better.",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = CirclWhite,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Profile Picture Display/Picker
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri != null) {
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = "Profile Picture",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "📷",
                            fontSize = 40.sp,
                            color = CirclBlue
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tap to add photo",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = CirclBlue
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Upload Status
            if (isUploading) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = CirclWhite
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Uploading...",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = CirclWhite
                    )
                }
            } else if (selectedImageUri != null) {
                Text(
                    text = "Photo ready to upload!",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = CirclWhite
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                // Skip Button
                OutlinedButton(
                    onClick = onNavigateToNext,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = CirclWhite
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(vertical = 15.dp)
                ) {
                    Text(
                        text = "Skip for Now",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Next Button
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
            }

            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Page4ScreenPreview() {
    Page4Screen(onNavigateToNext = {})
}

