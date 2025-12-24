package com.fragne.circl_app.ui.circles.calendar

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

/**
 * Media Picker Component
 * Translated from MediaPicker.swift
 *
 * Uses Android's Photo Picker to select images or videos
 */
@Composable
fun MediaPicker(
    onImageSelected: (Uri?) -> Unit = {},
    onVideoSelected: (Uri?) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    // Photo Picker launcher for images and videos
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            // Determine if it's an image or video based on MIME type
            // For simplicity, we'll treat all as images for now
            // TODO: Check MIME type and call appropriate callback
            onImageSelected(uri)
        } else {
            onDismiss()
        }
    }

    // Launch picker immediately when this composable is displayed
    LaunchedEffect(Unit) {
        photoPickerLauncher.launch(
            PickVisualMediaRequest(
                // Allow both images and videos
                ActivityResultContracts.PickVisualMedia.ImageAndVideo
            )
        )
    }
}

/**
 * Image-only Picker
 */
@Composable
fun ImagePicker(
    onImageSelected: (Uri?) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            onImageSelected(uri)
        } else {
            onDismiss()
        }
    }

    LaunchedEffect(Unit) {
        photoPickerLauncher.launch(
            PickVisualMediaRequest(
                ActivityResultContracts.PickVisualMedia.ImageOnly
            )
        )
    }
}

/**
 * Video-only Picker
 */
@Composable
fun VideoPicker(
    onVideoSelected: (Uri?) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            onVideoSelected(uri)
        } else {
            onDismiss()
        }
    }

    LaunchedEffect(Unit) {
        photoPickerLauncher.launch(
            PickVisualMediaRequest(
                ActivityResultContracts.PickVisualMedia.VideoOnly
            )
        )
    }
}

