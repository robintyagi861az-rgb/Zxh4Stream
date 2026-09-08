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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwitchAccount
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MockData
import com.example.data.StreamRepository
import com.example.data.UserProfile
import com.example.ui.components.CustomProfileAvatar
import com.example.ui.components.SuperAdminBadge
import com.example.ui.theme.BackgroundBlack
import com.example.ui.theme.BorderGray
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.NetflixRed
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ProfileScreen(
    onNavigatePlans: () -> Unit,
    onNavigateAdmin: () -> Unit,
    onSwitchProfileClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onNavigateTerms: () -> Unit = {},
    onNavigatePrivacy: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val activeProfile by StreamRepository.activeProfile.collectAsState()
    val profiles by StreamRepository.profiles.collectAsState()
    val userEmail by StreamRepository.currentUserEmail.collectAsState()
    val userRole by StreamRepository.currentUserRole.collectAsState()
    val currentPlan by StreamRepository.currentPlan.collectAsState()
    val transactions by StreamRepository.transactions.collectAsState()

    val isSuperAdmin = userRole == "SUPER_ADMIN" || userEmail.equals(MockData.MASTER_ADMIN_EMAIL, ignoreCase = true)

    var showTransactionsDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundBlack)
            .statusBarsPadding()
            .testTag("profile_screen"),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Active Profile Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomProfileAvatar(
                    profile = activeProfile,
                    size = 80.dp,
                    isSelected = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = activeProfile.name,
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = userEmail,
                    color = TextSecondary,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )

                if (isSuperAdmin) {
                    Spacer(modifier = Modifier.height(8.dp))
                    SuperAdminBadge()
                }
            }
        }

        // Quick Profiles Row (Netflix Style Switcher)
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Profiles",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Switch Profile",
                        color = NetflixRed,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { onSwitchProfileClick() }
                            .padding(4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    profiles.forEach { prof ->
                        val isSelected = prof.id == activeProfile.id
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable { StreamRepository.selectProfile(prof) }
                                .padding(4.dp)
                        ) {
                            CustomProfileAvatar(
                                profile = prof,
                                size = 52.dp,
                                isSelected = isSelected
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = prof.name,
                                color = if (isSelected) TextPrimary else TextMuted,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        // Current Subscription Plan Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceCard)
                    .border(1.dp, BorderGray, RoundedCornerShape(14.dp))
                    .clickable { onNavigatePlans() }
                    .padding(16.dp)
                    .testTag("active_plan_card")
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Current Subscription",
                                color = TextMuted,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${currentPlan.name} (₹${currentPlan.priceInr}/${currentPlan.billingCycle})",
                                color = TextPrimary,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(GreenSuccess.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "ACTIVE",
                                color = GreenSuccess,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${currentPlan.resolution} • ${currentPlan.maxScreens} Screens • ZapUPI Instant Renewal",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Change Plan",
                            color = NetflixRed,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = null,
                            tint = NetflixRed,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }

        // Master Admin Panel Quick Entry (If Super Admin)
        if (isSuperAdmin) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(NetflixRed.copy(alpha = 0.15f))
                        .border(1.dp, NetflixRed, RoundedCornerShape(12.dp))
                        .clickable { onNavigateAdmin() }
                        .padding(16.dp)
                        .testTag("enter_admin_panel_card")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Admin",
                            tint = NetflixRed,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Master Admin Dashboard",
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "Content, ZapUPI, VPS transcoding, Users & SMTP control",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = null,
                            tint = NetflixRed,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }

        // Settings list
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                Text(
                    text = "App Settings",
                    color = TextMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 6.dp)
                )

                ProfileMenuItem(
                    icon = Icons.Default.Payment,
                    title = "ZapUPI Billing & Receipts",
                    subtitle = "View transaction history and invoices",
                    onClick = { showTransactionsDialog = true }
                )

                ProfileMenuItem(
                    icon = Icons.Default.SwitchAccount,
                    title = "Profile Management",
                    subtitle = "Add, edit or set parental PIN",
                    onClick = { onSwitchProfileClick() }
                )

                ProfileMenuItem(
                    icon = Icons.Default.Settings,
                    title = "Video Quality & Playback",
                    subtitle = "Default: 4K Ultra HD (Auto Bitrate)",
                    onClick = {}
                )

                ProfileMenuItem(
                    icon = Icons.Default.Description,
                    title = "Terms and Conditions",
                    subtitle = "Usage rules, VPS streaming & subscription policies",
                    onClick = onNavigateTerms
                )

                ProfileMenuItem(
                    icon = Icons.Default.PrivacyTip,
                    title = "Privacy Policy",
                    subtitle = "Data protection, ZapUPI security & encryption",
                    onClick = onNavigatePrivacy
                )

                ProfileMenuItem(
                    icon = Icons.Default.HelpOutline,
                    title = "Help & Support",
                    subtitle = "24/7 Priority streamer care",
                    onClick = {}
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Sign Out
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(SurfaceCard)
                        .clickable {
                            StreamRepository.logout()
                            onLogoutClick()
                        }
                        .padding(16.dp)
                        .testTag("logout_btn"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Sign Out",
                        tint = NetflixRed,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Sign Out of Zxh4Stream",
                        color = NetflixRed,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Zxh4Stream v2.4.0 • Stream Beyond Limits",
                    color = TextMuted,
                    fontSize = 11.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }

    // Transactions Dialog
    if (showTransactionsDialog) {
        AlertDialog(
            onDismissRequest = { showTransactionsDialog = false },
            containerColor = SurfaceCard,
            title = {
                Text(
                    text = "ZapUPI Payment History",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    transactions.forEach { txn ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = txn.planName,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = txn.status,
                                    color = if (txn.status == "SUCCESS") GreenSuccess else NetflixRed,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                            Text(
                                text = "UTR: ${txn.utrNumber} • ${txn.timestamp}",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }
                        HorizontalDivider(color = BorderGray)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showTransactionsDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = NetflixRed)
                ) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text(text = subtitle, color = TextMuted, fontSize = 12.sp)
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(12.dp)
        )
    }
}
