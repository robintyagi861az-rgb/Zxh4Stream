package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.testTag
import com.example.ui.components.CustomProfileAvatar
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StreamRepository
import com.example.data.UserProfile
import com.example.ui.theme.BackgroundBlack
import com.example.ui.theme.BorderGray
import com.example.ui.theme.NetflixRed
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ProfileSelectorScreen(
    onProfileChosen: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val profiles by StreamRepository.profiles.collectAsState()
    val activeProfile by StreamRepository.activeProfile.collectAsState()

    var showPinDialogForProfile by remember { mutableStateOf<UserProfile?>(null) }
    var enteredPin by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }

    var showAddProfileDialog by remember { mutableStateOf(false) }
    var newProfileName by remember { mutableStateOf("") }
    var isNewProfileKids by remember { mutableStateOf(false) }
    var newProfilePin by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundBlack)
            .statusBarsPadding()
            .testTag("profile_selector_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Back Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Who's Watching?",
            color = TextPrimary,
            fontSize = 26.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = (-0.5).sp
        )
        Text(
            text = "Select your profile to continue streaming",
            color = TextSecondary,
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Profiles 2x2 or 2x3 Grid
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            profiles.chunked(2).forEach { rowProfiles ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    rowProfiles.forEach { prof ->
                        val isCurrent = prof.id == activeProfile.id
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable {
                                    if (prof.parentalPin != null) {
                                        showPinDialogForProfile = prof
                                        enteredPin = ""
                                        pinError = false
                                    } else {
                                        StreamRepository.selectProfile(prof)
                                        onProfileChosen()
                                    }
                                }
                                .testTag("profile_avatar_${prof.id}")
                        ) {
                            CustomProfileAvatar(
                                profile = prof,
                                size = 90.dp,
                                isSelected = isCurrent
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = prof.name,
                                color = if (isCurrent) TextPrimary else TextSecondary,
                                fontSize = 14.sp,
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium
                            )

                            if (prof.isKids) {
                                Text(
                                    text = "Kids",
                                    color = NetflixRed,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Add Profile Button
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { showAddProfileDialog = true }
                    .padding(top = 8.dp)
                    .testTag("add_profile_btn")
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceCard)
                        .border(1.dp, BorderGray, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Profile",
                        tint = Color.White,
                        modifier = Modifier.size(34.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Add Profile",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    // Parental PIN Dialog
    if (showPinDialogForProfile != null) {
        val target = showPinDialogForProfile!!
        AlertDialog(
            onDismissRequest = { showPinDialogForProfile = null },
            containerColor = SurfaceCard,
            title = {
                Text(
                    text = "Enter Profile PIN",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "This profile is protected with parental controls (Default PIN: 1234)",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = enteredPin,
                        onValueChange = { enteredPin = it.take(4) },
                        placeholder = { Text("4-digit PIN", color = TextMuted) },
                        isError = pinError,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NetflixRed,
                            unfocusedBorderColor = BorderGray,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (pinError) {
                        Text(
                            text = "Incorrect PIN. Try 1234",
                            color = NetflixRed,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (enteredPin == target.parentalPin || enteredPin == "1234") {
                            StreamRepository.selectProfile(target)
                            showPinDialogForProfile = null
                            onProfileChosen()
                        } else {
                            pinError = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NetflixRed)
                ) {
                    Text("Unlock")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPinDialogForProfile = null }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }

    // Add Profile Dialog
    if (showAddProfileDialog) {
        AlertDialog(
            onDismissRequest = { showAddProfileDialog = false },
            containerColor = SurfaceCard,
            title = {
                Text(
                    text = "Add New Profile",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = newProfileName,
                        onValueChange = { newProfileName = it },
                        label = { Text("Profile Name", color = TextSecondary) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NetflixRed,
                            unfocusedBorderColor = BorderGray,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Kids Profile", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            Text(text = "Show titles for age 12 & under", color = TextMuted, fontSize = 11.sp)
                        }
                        Switch(
                            checked = isNewProfileKids,
                            onCheckedChange = { isNewProfileKids = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = NetflixRed)
                        )
                    }

                    if (isNewProfileKids) {
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = newProfilePin,
                            onValueChange = { newProfilePin = it.take(4) },
                            label = { Text("Parental PIN (optional)", color = TextSecondary) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NetflixRed,
                                unfocusedBorderColor = BorderGray,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newProfileName.isNotBlank()) {
                            StreamRepository.addProfile(
                                name = newProfileName,
                                isKids = isNewProfileKids,
                                pin = if (newProfilePin.isNotBlank()) newProfilePin else null
                            )
                            showAddProfileDialog = false
                            newProfileName = ""
                            isNewProfileKids = false
                            newProfilePin = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NetflixRed)
                ) {
                    Text("Create Profile")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddProfileDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }
}
