package com.fragne.circl_app.ui.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
 * Page17 - Circl Ethics Screen
 * Shows ethical guidelines and prohibited businesses
 */
@Composable
fun Page17Screen(
    onNavigateToNext: () -> Unit
) {
    CloudBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // Title and Separator
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(
                    text = "Circl Ethics",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = CirclYellow
                )

                Spacer(modifier = Modifier.height(8.dp))

                HorizontalDivider(
                    color = CirclWhite,
                    thickness = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Body Text (Scrollable)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "We will not allow businesses to be created here that produce, supply, or use: tobacco/nicotine products, unethical health, cannabis, gambling activities, casinos/online casinos, adult entertainment, predatory lending, counterfeit/knockoff products, miracle health products, whatever is deemed unethical/exploitative by Circl.",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = CirclWhite,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 25.dp)
                )

                Text(
                    text = "We have the right to remove you from our platform if any of these are met or deemed by Circl.",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = CirclWhite,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

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

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Page17ScreenPreview() {
    Page17Screen(onNavigateToNext = {})
}

