package com.fragne.circl_app.ui.circles.home

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.fragne.circl_app.ui.circles.dashboard.CircleData
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale

/**
 * Circle Dues Screen
 * Translated from PageDues.swift
 * Shows dues information with different UI for moderators vs members
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CircleDuesScreen(
    circle: CircleData,
    userId: Int,
    onNavigateBack: () -> Unit = {}
) {
    var duesAmount by remember { mutableStateOf<Int?>(null) }
    var isModerator by remember { mutableStateOf(false) }
    var hasStripeAccount by remember { mutableStateOf(false) }
    var newDuesAmount by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val primaryBlue = Color(0xFF004AAD)

    // Fetch dues information on launch
    LaunchedEffect(circle.id) {
        fetchDuesInfo(circle.id, userId) { dues, moderator, stripeAccount ->
            duesAmount = dues
            isModerator = moderator
            hasStripeAccount = stripeAccount
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Circle Dues",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = circle.name,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryBlue
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            primaryBlue.copy(alpha = 0.1f),
                            primaryBlue.copy(alpha = 0.05f),
                            Color.White
                        )
                    )
                )
        ) {
            if (isLoading) {
                LoadingState()
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

                    if (duesAmount != null) {
                        // Current dues display
                        CurrentDuesCard(
                            duesAmount = duesAmount!!,
                            primaryBlue = primaryBlue
                        )

                        if (isModerator) {
                            // Moderator actions card
                            ModeratorActionsCard(
                                newDuesAmount = newDuesAmount,
                                onDuesAmountChange = { newDuesAmount = it },
                                hasStripeAccount = hasStripeAccount,
                                primaryBlue = primaryBlue,
                                onUpdateDues = {
                                    scope.launch {
                                        updateDues(
                                            circleId = circle.id,
                                            userId = userId,
                                            newAmount = newDuesAmount
                                        ) { success ->
                                            if (success) {
                                                // Refresh data
                                                fetchDuesInfo(circle.id, userId) { dues, moderator, stripeAccount ->
                                                    duesAmount = dues
                                                    isModerator = moderator
                                                    hasStripeAccount = stripeAccount
                                                    newDuesAmount = ""
                                                }
                                            }
                                        }
                                    }
                                },
                                onSetupStripe = {
                                    scope.launch {
                                        createStripeAccount(context, circle.id, userId)
                                    }
                                }
                            )
                        } else {
                            // Member payment card
                            MemberPaymentCard(
                                onPayNow = {
                                    scope.launch {
                                        startCheckout(context, circle.id, userId)
                                    }
                                }
                            )
                        }
                    } else {
                        // No dues set
                        NoDuesSetCard(
                            isModerator = isModerator,
                            hasStripeAccount = hasStripeAccount,
                            newDuesAmount = newDuesAmount,
                            onDuesAmountChange = { newDuesAmount = it },
                            primaryBlue = primaryBlue,
                            onSetDues = {
                                scope.launch {
                                    updateDues(
                                        circleId = circle.id,
                                        userId = userId,
                                        newAmount = newDuesAmount
                                    ) { success ->
                                        if (success) {
                                            // Refresh data
                                            fetchDuesInfo(circle.id, userId) { dues, moderator, stripeAccount ->
                                                duesAmount = dues
                                                isModerator = moderator
                                                hasStripeAccount = stripeAccount
                                                newDuesAmount = ""
                                            }
                                        }
                                    }
                                }
                            },
                            onSetupStripe = {
                                scope.launch {
                                    createStripeAccount(context, circle.id, userId)
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

/**
 * Loading State
 */
@Composable
private fun LoadingState() {
    val primaryBlue = Color(0xFF004AAD)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(60.dp),
                color = primaryBlue,
                strokeWidth = 4.dp
            )
            Text(
                text = "Loading dues information...",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = primaryBlue
            )
        }
    }
}

/**
 * Current Dues Display Card
 */
@Composable
private fun CurrentDuesCard(
    duesAmount: Int,
    primaryBlue: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Current Dues",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )

            Text(
                text = "$${String.format(Locale.US, "%.2f", duesAmount / 100.0)}",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = primaryBlue,
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            HorizontalDivider(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                color = primaryBlue.copy(alpha = 0.1f)
            )
        }
    }
}

/**
 * Moderator Actions Card
 */
@Composable
private fun ModeratorActionsCard(
    newDuesAmount: String,
    onDuesAmountChange: (String) -> Unit,
    hasStripeAccount: Boolean,
    primaryBlue: Color,
    onUpdateDues: () -> Unit,
    onSetupStripe: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.Shield,
                    contentDescription = null,
                    tint = Color(0xFFFFA500),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Moderator Actions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = primaryBlue
                )
            }

            // Dues amount input
            OutlinedTextField(
                value = newDuesAmount,
                onValueChange = onDuesAmountChange,
                label = { Text("Update dues amount") },
                leadingIcon = {
                    Icon(
                        Icons.Filled.AttachMoney,
                        contentDescription = null,
                        tint = primaryBlue
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryBlue,
                    focusedLabelColor = primaryBlue,
                    cursorColor = primaryBlue
                )
            )

            // Update button
            Button(
                onClick = onUpdateDues,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                contentPadding = PaddingValues(vertical = 16.dp),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Icon(
                    Icons.Filled.Upload,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Update Dues",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Stripe setup button
            TextButton(
                onClick = onSetupStripe,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(
                    if (hasStripeAccount) Icons.Filled.CreditCard else Icons.Filled.AddCircle,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (hasStripeAccount) "Edit Stripe Info" else "Set Up Stripe",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * Member Payment Card
 */
@Composable
private fun MemberPaymentCard(
    onPayNow: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.CreditCard,
                    contentDescription = null,
                    tint = Color.Green,
                    modifier = Modifier.size(28.dp)
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Payment Required",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    Text(
                        text = "Complete your circle dues payment",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            // Pay now button
            Button(
                onClick = onPayNow,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                contentPadding = PaddingValues(vertical = 16.dp),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Pay Now",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/**
 * No Dues Set Card
 */
@Composable
private fun NoDuesSetCard(
    isModerator: Boolean,
    hasStripeAccount: Boolean,
    newDuesAmount: String,
    onDuesAmountChange: (String) -> Unit,
    primaryBlue: Color,
    onSetDues: () -> Unit,
    onSetupStripe: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Warning icon and message
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    Icons.Filled.Warning,
                    contentDescription = null,
                    tint = Color(0xFFFFA500),
                    modifier = Modifier.size(48.dp)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Dues Not Set",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "Circle dues haven't been configured yet",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }

            if (isModerator) {
                if (hasStripeAccount) {
                    // Moderator can set dues
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = newDuesAmount,
                            onValueChange = onDuesAmountChange,
                            label = { Text("Enter dues amount") },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.AttachMoney,
                                    contentDescription = null,
                                    tint = primaryBlue
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryBlue,
                                focusedLabelColor = primaryBlue
                            )
                        )

                        Button(
                            onClick = onSetDues,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                            contentPadding = PaddingValues(vertical = 16.dp),
                            elevation = ButtonDefaults.buttonElevation(4.dp)
                        ) {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Set Dues Amount",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        TextButton(
                            onClick = onSetupStripe,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Icon(
                                if (hasStripeAccount) Icons.Filled.CreditCard else Icons.Filled.AddCircle,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (hasStripeAccount) "Edit Stripe Info" else "Set Up Stripe",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                } else {
                    // Moderator needs to setup Stripe first
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            Icons.Filled.AccountBalance,
                            contentDescription = null,
                            tint = primaryBlue,
                            modifier = Modifier.size(32.dp)
                        )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Setup Required",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                            Text(
                                text = "Connect Stripe to start collecting dues",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }

                        Button(
                            onClick = onSetupStripe,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                            contentPadding = PaddingValues(vertical = 16.dp),
                            elevation = ButtonDefaults.buttonElevation(4.dp)
                        ) {
                            Icon(
                                Icons.Filled.Link,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Set Up Stripe",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            } else {
                // Regular member waiting for setup
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        Icons.Filled.Schedule,
                        contentDescription = null,
                        tint = Color(0xFFFFA500),
                        modifier = Modifier.size(32.dp)
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Waiting for Setup",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                        Text(
                            text = "Moderators will configure dues soon",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

// MARK: - Backend API Calls

/**
 * Fetch dues information
 */
private fun fetchDuesInfo(
    circleId: Int,
    userId: Int,
    callback: (duesAmount: Int?, isModerator: Boolean, hasStripeAccount: Boolean) -> Unit
) {
    Thread {
        try {
            val url = URL("https://circl-app-server-production-37cd.up.railway.app/circles/get_circle_details/?circle_id=$circleId&user_id=$userId")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 10000
            connection.readTimeout = 10000

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().readText()
                val json = JSONObject(response)

                val duesAmount = if (json.has("dues_amount") && !json.isNull("dues_amount")) {
                    json.getInt("dues_amount")
                } else {
                    null
                }
                val isModerator = json.optBoolean("is_moderator", false)
                val hasStripeAccount = json.optBoolean("has_stripe_account", false)

                callback(duesAmount, isModerator, hasStripeAccount)
            } else {
                callback(null, false, false)
            }
            connection.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
            callback(null, false, false)
        }
    }.start()
}

/**
 * Update dues amount
 */
private fun updateDues(
    circleId: Int,
    userId: Int,
    newAmount: String,
    callback: (Boolean) -> Unit
) {
    Thread {
        try {
            val amountInCents = ((newAmount.toDoubleOrNull() ?: 0.0) * 100).toInt()

            val url = URL("https://circl-app-server-production-37cd.up.railway.app/circles/set_circle_dues/")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.doOutput = true
            connection.setRequestProperty("Content-Type", "application/json")
            connection.connectTimeout = 10000
            connection.readTimeout = 10000

            val payload = JSONObject().apply {
                put("user_id", userId)
                put("circle_id", circleId)
                put("price_cents", amountInCents)
            }

            connection.outputStream.write(payload.toString().toByteArray())

            val responseCode = connection.responseCode
            callback(responseCode == HttpURLConnection.HTTP_OK)
            connection.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
            callback(false)
        }
    }.start()
}

/**
 * Create Stripe account
 */
private fun createStripeAccount(
    context: Context,
    circleId: Int,
    userId: Int
) {
    Thread {
        try {
            val url = URL("https://circl-app-server-production-37cd.up.railway.app/circles/create_stripe_account/")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.doOutput = true
            connection.setRequestProperty("Content-Type", "application/json")
            connection.connectTimeout = 10000
            connection.readTimeout = 10000

            val payload = JSONObject().apply {
                put("user_id", userId)
                put("circle_id", circleId)
            }

            connection.outputStream.write(payload.toString().toByteArray())

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().readText()
                val json = JSONObject(response)

                if (json.has("url")) {
                    val stripeUrl = json.getString("url")
                    // Open URL in browser
                    val intent = Intent(Intent.ACTION_VIEW, stripeUrl.toUri())
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
            }
            connection.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.start()
}

/**
 * Start checkout session
 */
private fun startCheckout(
    context: Context,
    circleId: Int,
    userId: Int
) {
    Thread {
        try {
            val url = URL("https://circl-app-server-production-37cd.up.railway.app/circles/create_checkout_session/")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.doOutput = true
            connection.setRequestProperty("Content-Type", "application/json")
            connection.connectTimeout = 10000
            connection.readTimeout = 10000

            val payload = JSONObject().apply {
                put("circle_id", circleId)
                put("user_id", userId)
            }

            connection.outputStream.write(payload.toString().toByteArray())

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().readText()
                val json = JSONObject(response)

                if (json.has("checkout_url")) {
                    val checkoutUrl = json.getString("checkout_url")
                    // Open URL in browser
                    val intent = Intent(Intent.ACTION_VIEW, checkoutUrl.toUri())
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
            }
            connection.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.start()
}

