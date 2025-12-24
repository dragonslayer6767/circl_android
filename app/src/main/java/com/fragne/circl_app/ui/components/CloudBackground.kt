package com.fragne.circl_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.fragne.circl_app.ui.theme.CirclBlue
import com.fragne.circl_app.ui.theme.CirclBlueDark
import com.fragne.circl_app.ui.theme.CirclWhite

/**
 * Reusable cloud background matching iOS design
 */
@Composable
fun CloudBackground(
    content: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

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
        // Top Left Cloud
        Box(
            modifier = Modifier
                .size(120.dp)
                .offset(x = -screenWidth / 2 + 60.dp, y = -screenHeight / 2 + 60.dp)
                .background(CirclWhite, CircleShape)
        )
        Box(
            modifier = Modifier
                .size(100.dp)
                .offset(x = -screenWidth / 2 + 30.dp, y = -screenHeight / 2 + 40.dp)
                .background(CirclWhite, CircleShape)
        )
        Box(
            modifier = Modifier
                .size(100.dp)
                .offset(x = -screenWidth / 2 + 110.dp, y = -screenHeight / 2 + 30.dp)
                .background(CirclWhite, CircleShape)
        )
        Box(
            modifier = Modifier
                .size(100.dp)
                .offset(x = -screenWidth / 2 + 170.dp, y = -screenHeight / 2 + 30.dp)
                .background(CirclWhite, CircleShape)
        )
        Box(
            modifier = Modifier
                .size(100.dp)
                .offset(x = -screenWidth / 2 + 210.dp, y = -screenHeight / 2 + 60.dp)
                .background(CirclWhite, CircleShape)
        )
        Box(
            modifier = Modifier
                .size(80.dp)
                .offset(x = -screenWidth / 2 + 90.dp, y = -screenHeight / 2 + 50.dp)
                .background(CirclWhite, CircleShape)
        )
        Box(
            modifier = Modifier
                .size(90.dp)
                .offset(x = -screenWidth / 2 + 50.dp, y = -screenHeight / 2 + 30.dp)
                .background(CirclWhite, CircleShape)
        )
        Box(
            modifier = Modifier
                .size(110.dp)
                .offset(x = -screenWidth / 2 + 150.dp, y = -screenHeight / 2 + 80.dp)
                .background(CirclWhite, CircleShape)
        )

        // Bottom Right Cloud
        Box(
            modifier = Modifier
                .size(120.dp)
                .offset(x = screenWidth / 2 - 60.dp, y = screenHeight / 2 - 60.dp)
                .background(CirclWhite, CircleShape)
        )
        Box(
            modifier = Modifier
                .size(100.dp)
                .offset(x = screenWidth / 2 - 30.dp, y = screenHeight / 2 - 40.dp)
                .background(CirclWhite, CircleShape)
        )
        Box(
            modifier = Modifier
                .size(80.dp)
                .offset(x = screenWidth / 2 - 90.dp, y = screenHeight / 2 - 90.dp)
                .background(CirclWhite, CircleShape)
        )
        Box(
            modifier = Modifier
                .size(90.dp)
                .offset(x = screenWidth / 2 - 50.dp, y = screenHeight / 2 - 30.dp)
                .background(CirclWhite, CircleShape)
        )
        Box(
            modifier = Modifier
                .size(90.dp)
                .offset(x = screenWidth / 2 - 20.dp, y = screenHeight / 2 - 110.dp)
                .background(CirclWhite, CircleShape)
        )
        Box(
            modifier = Modifier
                .size(110.dp)
                .offset(x = screenWidth / 2 - 145.dp, y = screenHeight / 2 - 50.dp)
                .background(CirclWhite, CircleShape)
        )

        // Content on top
        content()
    }
}

