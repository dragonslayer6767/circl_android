package com.fragne.circl_app.ui.circles.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.fragne.circl_app.ui.circles.dashboard.CircleData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 * Circle About Screen (CirclPopupCard)
 * Shows detailed information about a circle
 * Translated from CirclPopupCard.swift
 */
@Composable
fun CircleAboutDialog(
    circle: CircleData,
    isMember: Boolean = false,
    onDismiss: () -> Unit,
    onJoinCircle: () -> Unit = {},
    onOpenCircle: () -> Unit = {},
    onUploadPhoto: () -> Unit = {}
) {
    var showInviteCopiedToast by remember { mutableStateOf(false) }
    var isUploading by remember { mutableStateOf(false) }
    var uploadError by remember { mutableStateOf<String?>(null) }
    var uploadSuccess by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            scope.launch {
                isUploading = true
                uploadError = null
                uploadSuccess = false

                val result = uploadCircleImage(context, circle.id, it)

                isUploading = false
                if (result) {
                    uploadSuccess = true
                    // Hide success message after 3 seconds
                    kotlinx.coroutines.delay(3000)
                    uploadSuccess = false
                } else {
                    uploadError = "Failed to upload image"
                }
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Box {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Close Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = onDismiss) {
                            Icon(
                                Icons.Filled.Cancel,
                                contentDescription = "Close",
                                tint = Color.Gray,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Circle Image
                    CircleImage(
                        imageUrl = circle.profileImageUrl,
                        isModerator = circle.isModerator,
                        onUploadPhoto = { imagePickerLauncher.launch("image/*") },
                        isUploading = isUploading,
                        uploadSuccess = uploadSuccess,
                        uploadError = uploadError
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Circle Name
                    Text(
                        text = circle.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Industry
                    Text(
                        text = "Industry: ${circle.industry}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )


                    Spacer(modifier = Modifier.height(4.dp))

                    // Member Count
                    Text(
                        text = "${circle.memberCount} Members",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    Spacer(modifier = Modifier.height(16.dp))

                    // About Section
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "About This Circl",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = circle.description,
                            fontSize = 14.sp,
                            color = Color.Black,
                            lineHeight = 20.sp
                        )
                    }

                    // Access Code (for moderators)
                    if (circle.isModerator && !circle.accessCode.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Access Code",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = circle.accessCode ?: "",
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Action Buttons (if member)
                    if (isMember) {
                        // Create Invite Link Button
                        CreateInviteLinkButton(
                            circleId = circle.id,
                            onInviteCreated = { showInviteCopiedToast = true }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Open Circle Button
                        Button(
                            onClick = {
                                onDismiss()
                                onOpenCircle()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF5F5F5)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = Color(0xFF0066FF),
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Open Circl",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0066FF)
                                )
                            }
                        }
                    } else {
                        // Join Button (for non-members)
                        Button(
                            onClick = onJoinCircle,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF004AAD)
                            )
                        ) {
                            Text(
                                text = "Join Circle",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Toast for Invite Copied
                if (showInviteCopiedToast) {
                    InviteCopiedToast(
                        onDismiss = { showInviteCopiedToast = false }
                    )
                }
            }
        }
    }
}

/**
 * Circle Image with upload option for moderators
 */
@Composable
private fun CircleImage(
    imageUrl: String?,
    isModerator: Boolean,
    onUploadPhoto: () -> Unit,
    isUploading: Boolean = false,
    uploadSuccess: Boolean = false,
    uploadError: String? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Image
        Box(
            modifier = Modifier.size(100.dp),
            contentAlignment = Alignment.Center
        ) {
            if (!imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Circle Image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Placeholder
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF5F5F5)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Image,
                        contentDescription = "No Image",
                        tint = Color.Gray,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            // Upload progress overlay
            if (isUploading) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(40.dp),
                        color = Color.White,
                        strokeWidth = 4.dp
                    )
                }
            }
        }

        // Upload button for moderators
        if (isModerator) {
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(
                onClick = onUploadPhoto,
                enabled = !isUploading
            ) {
                Text(
                    text = if (isUploading) "Uploading..." else "Upload Circl Photo",
                    fontSize = 12.sp,
                    color = if (isUploading) Color.Gray else Color(0xFF0066FF)
                )
            }

            // Success/Error messages
            if (uploadSuccess) {
                Text(
                    text = "✓ Photo uploaded successfully",
                    fontSize = 11.sp,
                    color = Color(0xFF34C759),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            uploadError?.let { error ->
                Text(
                    text = "✗ $error",
                    fontSize = 11.sp,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

/**
 * Create Invite Link Button
 */
@Composable
private fun CreateInviteLinkButton(
    circleId: Int,
    onInviteCreated: () -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }

    Button(
        onClick = {
            isLoading = true
            // TODO: Call API to create invite link
            // For now, simulate success
            onInviteCreated()
            isLoading = false
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFF5F5F5)
        ),
        enabled = !isLoading
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color(0xFF0066FF)
                )
            } else {
                Icon(
                    Icons.Filled.Link,
                    contentDescription = null,
                    tint = Color(0xFF0066FF),
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Create Invite Link",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0066FF)
                )
            }
        }
    }
}

/**
 * Toast notification for invite copied
 */
@Composable
private fun InviteCopiedToast(
    onDismiss: () -> Unit
) {
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(3000)
        onDismiss()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF0066FF),
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Invite Link Copied to Your Clipboard!",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    Text(
                        text = "Paste it into your Messages to invite your network!",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

/**
 * Upload circle image to backend
 * Returns true if successful, false otherwise
 */
private suspend fun uploadCircleImage(
    context: Context,
    circleId: Int,
    imageUri: Uri
): Boolean = withContext(Dispatchers.IO) {
    try {
        // Read and compress the image
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        // Compress image to JPEG
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        val imageBytes = byteArrayOutputStream.toByteArray()

        // Create temporary file
        val tempFile = File(context.cacheDir, "circle_image_$circleId.jpg")
        FileOutputStream(tempFile).use { it.write(imageBytes) }

        // Create multipart request
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "image",
                "circle_image.jpg",
                tempFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            )
            .build()

        // Make API request
        val request = Request.Builder()
            .url("https://circl-app-server-production-37cd.up.railway.app/circles/upload_circle_image/$circleId/")
            .post(requestBody)
            .build()

        val client = OkHttpClient()
        val response = client.newCall(request).execute()

        // Clean up temp file
        tempFile.delete()

        val success = response.isSuccessful
        response.close()

        success
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}
