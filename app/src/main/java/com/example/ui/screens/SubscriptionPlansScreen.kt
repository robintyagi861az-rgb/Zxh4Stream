package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StreamRepository
import com.example.data.SubscriptionPlan
import com.example.ui.theme.BackgroundBlack
import com.example.ui.theme.BorderGray
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.NetflixRed
import com.example.ui.theme.NetflixRedDark
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SubscriptionPlansScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val plans by StreamRepository.subscriptionPlans.collectAsState()
    val currentPlan by StreamRepository.currentPlan.collectAsState()
    val zapConfig by StreamRepository.zapUpiConfig.collectAsState()

    var selectedPlan by remember { mutableStateOf(plans.find { it.isPopular } ?: plans.first()) }
    var showZapUpiDialog by remember { mutableStateOf(false) }
    var paymentInProgress by remember { mutableStateOf(false) }
    var paymentStatusMessage by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundBlack)
            .statusBarsPadding()
            .testTag("subscription_plans_screen"),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Choose the plan that's right for you",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Watch all you want. Ad-free. Cancel anytime.",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Plan Cards
        items(plans) { plan ->
            val isSelected = selectedPlan.id == plan.id
            val isCurrent = currentPlan.id == plan.id

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceCard)
                    .border(
                        width = if (plan.isPopular || isSelected) 2.dp else 1.dp,
                        color = if (isSelected) NetflixRed else if (plan.isPopular) NetflixRed.copy(alpha = 0.6f) else BorderGray,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { selectedPlan = plan }
                    .testTag("plan_card_${plan.id}")
            ) {
                // Popular Badge
                if (plan.isPopular) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .clip(RoundedCornerShape(bottomStart = 12.dp, topEnd = 16.dp))
                            .background(NetflixRed)
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "MOST POPULAR",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = plan.name,
                                color = TextPrimary,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "${plan.resolution} • ${plan.videoQuality} Quality",
                                color = NetflixRed,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Price
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "₹${plan.priceInr}",
                                color = TextPrimary,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "/ ${plan.billingCycle}",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Features list
                    plan.features.forEach { feat ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = GreenSuccess,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = feat,
                                color = TextSecondary,
                                fontSize = 13.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (isCurrent) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(GreenSuccess.copy(alpha = 0.15f))
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = GreenSuccess,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Current Active Plan",
                                color = GreenSuccess,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                        }
                    }
                }
            }
        }

        // Pay with ZapUPI Button at Bottom
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { showZapUpiDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NetflixRed,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("pay_zapupi_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "ZapUPI",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Pay ₹${selectedPlan.priceInr} with ZapUPI",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Secured",
                        tint = TextMuted,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "End-to-end encrypted 256-bit UPI webhook verification",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }

    // ZapUPI Checkout Dialog
    if (showZapUpiDialog) {
        AlertDialog(
            onDismissRequest = {
                if (!paymentInProgress) showZapUpiDialog = false
            },
            containerColor = SurfaceCard,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(NetflixRed),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "ZapUPI",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ZapUPI Instant Checkout",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Plan: ${selectedPlan.name} • ₹${selectedPlan.priceInr}",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "Merchant VPA: ${zapConfig.merchantVpa}",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // QR Code visual box
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .border(2.dp, NetflixRed, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.QrCode,
                                contentDescription = "Scan UPI",
                                tint = Color.Black,
                                modifier = Modifier.size(90.dp)
                            )
                            Text(
                                text = "Scan in Any UPI App",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (paymentInProgress) {
                        CircularProgressIndicator(
                            color = NetflixRed,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Awaiting Webhook Confirmation...",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    } else if (paymentStatusMessage != null) {
                        Text(
                            text = paymentStatusMessage!!,
                            color = GreenSuccess,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    } else {
                        Text(
                            text = "Open GPay / PhonePe / Paytm / BHIM to complete authorization.",
                            color = TextMuted,
                            fontSize = 12.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            },
            confirmButton = {
                if (!paymentInProgress && paymentStatusMessage == null) {
                    Button(
                        onClick = {
                            paymentInProgress = true
                            StreamRepository.processZapUpiPayment(
                                plan = selectedPlan,
                                vpaId = zapConfig.merchantVpa
                            ) { success, msg ->
                                paymentInProgress = false
                                paymentStatusMessage = msg
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NetflixRed),
                        modifier = Modifier.testTag("confirm_zapupi_webhook_btn")
                    ) {
                        Text("Simulate UPI Payment Success")
                    }
                } else if (paymentStatusMessage != null) {
                    Button(
                        onClick = {
                            showZapUpiDialog = false
                            paymentStatusMessage = null
                            onBack()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenSuccess)
                    ) {
                        Text("Done")
                    }
                }
            },
            dismissButton = {
                if (!paymentInProgress && paymentStatusMessage == null) {
                    TextButton(onClick = { showZapUpiDialog = false }) {
                        Text("Cancel", color = TextSecondary)
                    }
                }
            }
        )
    }
}
