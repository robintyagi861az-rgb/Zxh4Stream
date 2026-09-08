package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import com.example.ui.components.FeaturedPill
import com.example.ui.components.Top10IndiaPill
import com.example.ui.components.TrendingPill
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppConfig
import com.example.data.ContentItem
import com.example.data.ContentType
import com.example.data.GoogleOAuthConfig
import com.example.data.MockData
import com.example.data.SmtpConfig
import com.example.data.StreamRepository
import com.example.data.VpsStorageConfig
import com.example.data.ZapUpiConfig
import com.example.ui.theme.BackgroundBlack
import com.example.ui.theme.BorderGray
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.NetflixRed
import com.example.ui.theme.NetflixRedDark
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import java.util.UUID

@Composable
fun AdminPanelScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSection by remember { mutableIntStateOf(0) }
    // 0: Overview, 1: Content, 2: Users, 3: Payments & ZapUPI, 4: Broadcast Notif, 5: Integrations & API Keys, 6: App Settings

    val sections = listOf(
        "Overview" to Icons.Default.Analytics,
        "Content" to Icons.Default.Movie,
        "Users" to Icons.Default.People,
        "ZapUPI Billing" to Icons.Default.Payment,
        "Broadcast" to Icons.Default.Campaign,
        "API & VPS" to Icons.Default.Dns,
        "App Settings" to Icons.Default.Settings
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundBlack)
            .statusBarsPadding()
            .testTag("admin_panel_screen")
    ) {
        // Admin Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("admin_back_btn")) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(NetflixRed),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Z", color = Color.White, fontWeight = FontWeight.Black, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Zxh4Stream Admin Console",
                        color = TextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "Super Admin: ${MockData.MASTER_ADMIN_EMAIL}",
                    color = NetflixRed,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Horizontal Category Tab Pills
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sections.indices.toList()) { index ->
                val (title, icon) = sections[index]
                val isSelected = selectedSection == index
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) NetflixRed else SurfaceCard)
                        .clickable { selectedSection = index }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("admin_tab_$index")
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isSelected) Color.White else TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = title,
                        color = if (isSelected) Color.White else TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Content Area based on selected section
        when (selectedSection) {
            0 -> AdminOverviewSection()
            1 -> AdminContentSection()
            2 -> AdminUsersSection()
            3 -> AdminPaymentsSection()
            4 -> AdminBroadcastSection()
            5 -> AdminIntegrationsSection()
            6 -> AdminAppSettingsSection()
        }
    }
}

// 1. OVERVIEW SECTION
@Composable
private fun AdminOverviewSection() {
    val contents by StreamRepository.contents.collectAsState()
    val users by StreamRepository.adminUsers.collectAsState()
    val transactions by StreamRepository.transactions.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Platform Analytics & Health",
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // KPI Stat Cards Grid (2x2)
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AdminStatCard(
                    title = "Total Subscribers",
                    value = "14,280",
                    change = "+12% this week",
                    modifier = Modifier.weight(1f)
                )
                AdminStatCard(
                    title = "Monthly Revenue",
                    value = "₹28,45,000",
                    change = "+18% via ZapUPI",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AdminStatCard(
                    title = "Active Streams",
                    value = "3,412 Live",
                    change = "Peak: 9,800",
                    modifier = Modifier.weight(1f)
                )
                AdminStatCard(
                    title = "VPS CDN Uptime",
                    value = "99.98%",
                    change = "Latency: 28ms",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Transcoding Queue & Storage status
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceCard)
                    .border(1.dp, BorderGray, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "VPS Storage (NVMe 4TB)",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "1.82 TB / 4.0 TB (45%)",
                            color = GreenSuccess,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { 0.45f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = NetflixRed,
                        trackColor = Color(0xFF2B2B2B)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "FFmpeg Adaptive Bitrate Engine: Active (HLS Multi-Quality: 480p, 720p, 1080p, 4K UHD)",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Recent ZapUPI Transactions
        item {
            Text(
                text = "Recent ZapUPI Transactions",
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(transactions.take(4)) { txn ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceCard)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${txn.userEmail} • ${txn.planName}",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "UTR: ${txn.utrNumber} • ${txn.timestamp}",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (txn.status == "SUCCESS") GreenSuccess.copy(alpha = 0.2f) else NetflixRed.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = txn.status,
                        color = if (txn.status == "SUCCESS") GreenSuccess else NetflixRed,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminStatCard(
    title: String,
    value: String,
    change: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceCard)
            .border(1.dp, BorderGray, RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Column {
            Text(text = title, color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = change, color = GreenSuccess, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// 2. CONTENT MANAGEMENT SECTION
@Composable
private fun AdminContentSection() {
    val contents by StreamRepository.contents.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    // Form fields for adding new content
    var newTitle by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }
    var newGenres by remember { mutableStateOf("Action, Thriller") }
    var newType by remember { mutableStateOf(ContentType.MOVIE) }
    var uploadMode by remember { mutableStateOf("VPS") } // "VPS" or "EMBED"
    var videoUrl by remember { mutableStateOf("https://vps.zxh4stream.com/hls/master.m3u8") }
    var isTranscodingSimulated by remember { mutableStateOf(false) }
    var transcodeProgress by remember { mutableFloatStateOf(0f) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Catalog Content (${contents.size})",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Button(
                    onClick = { showAddDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = NetflixRed),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("admin_add_content_btn")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Content", fontSize = 13.sp)
                }
            }
        }

        items(contents) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(SurfaceCard)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = item.title,
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(NetflixRed.copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = item.type.name,
                                color = NetflixRed,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Text(
                        text = "${item.releaseYear} • ${item.genres.take(2).joinToString(", ")} • ${item.maturityRating}",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (item.isFeatured) FeaturedPill()
                        if (item.isTrending) TrendingPill()
                        if (item.isTop10India) Top10IndiaPill()
                    }
                }

                // Action Buttons: Toggle Featured, Delete
                IconButton(
                    onClick = {
                        StreamRepository.adminUpdateContent(item.copy(isFeatured = !item.isFeatured))
                    }
                ) {
                    Icon(
                        imageVector = if (item.isFeatured) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = "Toggle Featured",
                        tint = if (item.isFeatured) GreenSuccess else TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = { StreamRepository.adminDeleteContent(item.id) },
                    modifier = Modifier.testTag("admin_delete_content_${item.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Content",
                        tint = NetflixRed,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }

    // Add Content Dialog with VPS Storage Upload / Transcode or Embed Link
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { if (!isTranscodingSimulated) showAddDialog = false },
            containerColor = SurfaceCard,
            title = {
                Text(
                    text = "Add Movie / Web Series / Live Stream",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text("Title", color = TextSecondary) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NetflixRed,
                            unfocusedBorderColor = BorderGray,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = newDescription,
                        onValueChange = { newDescription = it },
                        label = { Text("Synopsis / Description", color = TextSecondary) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NetflixRed,
                            unfocusedBorderColor = BorderGray,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Content Source Type:", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = uploadMode == "VPS",
                            onClick = { uploadMode = "VPS" },
                            colors = RadioButtonDefaults.colors(selectedColor = NetflixRed)
                        )
                        Text("VPS Direct Upload (FFmpeg HLS)", color = TextPrimary, fontSize = 12.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = uploadMode == "EMBED",
                            onClick = { uploadMode = "EMBED" },
                            colors = RadioButtonDefaults.colors(selectedColor = NetflixRed)
                        )
                        Text("External Embed Link / CDN M3U8", color = TextPrimary, fontSize = 12.sp)
                    }

                    if (uploadMode == "EMBED") {
                        OutlinedTextField(
                            value = videoUrl,
                            onValueChange = { videoUrl = it },
                            label = { Text("Stream URL / M3U8", color = TextSecondary) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NetflixRed,
                                unfocusedBorderColor = BorderGray,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        // VPS Upload Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(BackgroundBlack)
                                .border(1.dp, BorderGray, RoundedCornerShape(8.dp))
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.CloudUpload, contentDescription = null, tint = NetflixRed, modifier = Modifier.size(32.dp))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Select MP4/MKV video to upload to VPS", color = TextSecondary, fontSize = 11.sp)
                                Text("Auto FFmpeg transcoding into 480p, 720p, 1080p HLS segments", color = TextMuted, fontSize = 10.sp)
                            }
                        }
                    }

                    if (isTranscodingSimulated) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Text("Transcoding via FFmpeg on VPS... ${(transcodeProgress * 100).toInt()}%", color = GreenSuccess, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        LinearProgressIndicator(
                            progress = { transcodeProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = NetflixRed
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newTitle.isNotBlank()) {
                            isTranscodingSimulated = true
                            transcodeProgress = 0.2f
                            // Simulate upload & transcoding
                            val newItem = ContentItem(
                                id = "c_${UUID.randomUUID().toString().take(6)}",
                                title = newTitle.trim(),
                                tagline = "Stream Beyond Limits",
                                description = if (newDescription.isNotBlank()) newDescription else "Exciting new premiere on Zxh4Stream.",
                                posterUrl = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=800",
                                backdropUrl = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=1200",
                                genres = newGenres.split(",").map { it.trim() },
                                releaseYear = 2026,
                                maturityRating = "U/A 16+",
                                durationMinutes = 135,
                                matchScorePercent = 97,
                                type = newType,
                                videoUrl = videoUrl,
                                isFeatured = true,
                                isNewRelease = true
                            )
                            StreamRepository.adminAddContent(newItem)
                            showAddDialog = false
                            isTranscodingSimulated = false
                            newTitle = ""
                            newDescription = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NetflixRed)
                ) {
                    Text("Publish to App")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }
}

// 3. USER MANAGEMENT SECTION
@Composable
private fun AdminUsersSection() {
    val users by StreamRepository.adminUsers.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val filtered = users.filter {
        it.email.contains(searchQuery, ignoreCase = true) || it.name.contains(searchQuery, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search users by email / name", color = TextMuted) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NetflixRed,
                    unfocusedBorderColor = BorderGray,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        items(filtered) { u ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(SurfaceCard)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = u.name,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (u.role == "SUPER_ADMIN") NetflixRed else Color(0xFF333333))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = u.role,
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Text(
                        text = u.email,
                        color = TextSecondary,
                        fontSize = 12.sp
                    )

                    Text(
                        text = "Plan: ${u.subscriptionTier} • Joined ${u.joinedDate}",
                        color = TextMuted,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                // Ban Toggle (Master Admin cannot be banned)
                if (u.role != "SUPER_ADMIN") {
                    Button(
                        onClick = { StreamRepository.adminToggleBanUser(u.email) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (u.isBanned) GreenSuccess else NetflixRed
                        ),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text(
                            text = if (u.isBanned) "Unban" else "Ban",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Text(
                        text = "Master Protected",
                        color = GreenSuccess,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// 4. PAYMENTS & ZAPUPI SECTION
@Composable
private fun AdminPaymentsSection() {
    val txns by StreamRepository.transactions.collectAsState()
    val plans by StreamRepository.subscriptionPlans.collectAsState()
    val zapConfig by StreamRepository.zapUpiConfig.collectAsState()

    var couponCode by remember { mutableStateOf("ZXH50") }
    var couponDiscount by remember { mutableStateOf("50%") }
    var couponCreatedMsg by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // ZapUPI Active Status Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceCard)
                    .border(1.dp, BorderGray, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Bolt, contentDescription = null, tint = NetflixRed, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ZapUPI Payment Gateway",
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(GreenSuccess.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = if (!zapConfig.isTestMode) "LIVE" else "TEST MODE",
                                color = GreenSuccess,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Merchant VPA: ${zapConfig.merchantVpa}", color = TextSecondary, fontSize = 12.sp)
                    Text(text = "Instant Webhook Verification: ENABLED", color = GreenSuccess, fontSize = 11.sp)
                }
            }
        }

        // Coupon Generator
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceCard)
                    .border(1.dp, BorderGray, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Create Promotional Coupon",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = couponCode,
                            onValueChange = { couponCode = it.uppercase() },
                            label = { Text("Code", color = TextSecondary) },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NetflixRed, unfocusedBorderColor = BorderGray, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                        )
                        OutlinedTextField(
                            value = couponDiscount,
                            onValueChange = { couponDiscount = it },
                            label = { Text("Discount", color = TextSecondary) },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NetflixRed, unfocusedBorderColor = BorderGray, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = { couponCreatedMsg = "Coupon $couponCode ($couponDiscount off) activated!" },
                        colors = ButtonDefaults.buttonColors(containerColor = NetflixRed),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Activate Coupon")
                    }
                    if (couponCreatedMsg != null) {
                        Text(text = couponCreatedMsg!!, color = GreenSuccess, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }
        }

        // Transactions Log Table
        item {
            Text(
                text = "ZapUPI Audit Log (${txns.size})",
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }

        items(txns) { t ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceCard)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "${t.transactionId} • ₹${t.amountInr}", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text(text = "${t.userEmail} • ${t.planName}", color = TextSecondary, fontSize = 12.sp)
                    Text(text = "UTR: ${t.utrNumber} • ${t.timestamp}", color = TextMuted, fontSize = 11.sp)
                }

                if (t.status == "SUCCESS") {
                    TextButton(
                        onClick = { StreamRepository.adminIssueRefund(t.transactionId) }
                    ) {
                        Text("Refund", color = NetflixRed, fontSize = 12.sp)
                    }
                } else {
                    Text(text = t.status, color = TextMuted, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// 5. NOTIFICATION CENTER (BROADCAST)
@Composable
private fun AdminBroadcastSection() {
    var titleInput by remember { mutableStateOf("Big Premiere Alert: Jawan 2 Extended Cut!") }
    var messageInput by remember { mutableStateOf("Watch now in pristine 4K Ultra HD with Dolby Atmos only on Zxh4Stream.") }
    var targetContentId by remember { mutableStateOf("c_jawan") }
    var sentMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Broadcast Push Notification to All Users",
            color = TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Send instant notification to all active mobile & web subscribers with optional deep link.",
            color = TextSecondary,
            fontSize = 12.sp
        )

        OutlinedTextField(
            value = titleInput,
            onValueChange = { titleInput = it },
            label = { Text("Notification Title", color = TextSecondary) },
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NetflixRed, unfocusedBorderColor = BorderGray, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = messageInput,
            onValueChange = { messageInput = it },
            label = { Text("Notification Body Message", color = TextSecondary) },
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NetflixRed, unfocusedBorderColor = BorderGray, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                StreamRepository.adminBroadcastNotification(
                    title = titleInput.trim(),
                    message = messageInput.trim(),
                    targetContentId = targetContentId
                )
                sentMessage = "Notification broadcasted successfully to 14,280 subscribers!"
            },
            colors = ButtonDefaults.buttonColors(containerColor = NetflixRed),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("admin_send_broadcast_btn")
        ) {
            Icon(Icons.Default.Campaign, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Send Push Broadcast Now", fontWeight = FontWeight.Bold)
        }

        if (sentMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(GreenSuccess.copy(alpha = 0.2f))
                    .border(1.dp, GreenSuccess, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Text(text = sentMessage!!, color = GreenSuccess, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}

// 6. API & INTEGRATIONS CONFIGURATION SECTION (NO HARDCODING)
@Composable
private fun AdminIntegrationsSection() {
    val zapConfig by StreamRepository.zapUpiConfig.collectAsState()
    val googleConfig by StreamRepository.googleOAuthConfig.collectAsState()
    val smtpConfig by StreamRepository.smtpConfig.collectAsState()
    val vpsConfig by StreamRepository.vpsStorageConfig.collectAsState()

    var zapApiKey by remember { mutableStateOf(zapConfig.apiKey) }
    var zapMerchantVpa by remember { mutableStateOf(zapConfig.merchantVpa) }
    var zapLiveMode by remember { mutableStateOf(!zapConfig.isTestMode) }

    var googleClientId by remember { mutableStateOf(googleConfig.clientId) }
    var googleSecret by remember { mutableStateOf(googleConfig.clientSecret) }
    var googleEnabled by remember { mutableStateOf(googleConfig.isEnabled) }

    var smtpHost by remember { mutableStateOf(smtpConfig.host) }
    var smtpPort by remember { mutableStateOf(smtpConfig.port.toString()) }
    var smtpUser by remember { mutableStateOf(smtpConfig.username) }
    var smtpPass by remember { mutableStateOf(smtpConfig.password) }
    var smtpEnabled by remember { mutableStateOf(smtpConfig.isEnabled) }

    var vpsServerHost by remember { mutableStateOf(vpsConfig.serverHost) }
    var vpsCdnUrl by remember { mutableStateOf(vpsConfig.cdnUrl) }
    var vpsPresets by remember { mutableStateOf(vpsConfig.ffmpegPresets) }

    var saveConfirmation by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "API Keys, Credentials & VPS Storage",
                color = TextPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Everything is dynamically managed from this admin console without modifying source code.",
                color = TextSecondary,
                fontSize = 12.sp
            )
        }

        // ZapUPI Configuration Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceCard)
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Bolt, contentDescription = null, tint = NetflixRed, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "ZapUPI Payment Gateway", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Switch(
                            checked = zapLiveMode,
                            onCheckedChange = { zapLiveMode = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = NetflixRed)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = zapMerchantVpa,
                        onValueChange = { zapMerchantVpa = it },
                        label = { Text("Merchant VPA", color = TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NetflixRed, unfocusedBorderColor = BorderGray, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = zapApiKey,
                        onValueChange = { zapApiKey = it },
                        label = { Text("ZapUPI API Key", color = TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NetflixRed, unfocusedBorderColor = BorderGray, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                    )
                }
            }
        }

        // Google OAuth Configuration Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceCard)
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Key, contentDescription = null, tint = NetflixRed, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Google OAuth Client", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Switch(
                            checked = googleEnabled,
                            onCheckedChange = { googleEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = NetflixRed)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = googleClientId,
                        onValueChange = { googleClientId = it },
                        label = { Text("Client ID", color = TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NetflixRed, unfocusedBorderColor = BorderGray, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                    )
                }
            }
        }

        // SMTP Email Configuration Card (Default is OFF)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceCard)
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = NetflixRed, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "SMTP Mail Server", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Text(text = if (smtpEnabled) "Enabled" else "Disabled by default", color = if (smtpEnabled) GreenSuccess else TextMuted, fontSize = 11.sp)
                        }
                        Switch(
                            checked = smtpEnabled,
                            onCheckedChange = { smtpEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = NetflixRed)
                        )
                    }

                    if (smtpEnabled) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = smtpHost,
                                onValueChange = { smtpHost = it },
                                label = { Text("Host", color = TextSecondary) },
                                modifier = Modifier.weight(2f),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NetflixRed, unfocusedBorderColor = BorderGray, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                            )
                            OutlinedTextField(
                                value = smtpPort,
                                onValueChange = { smtpPort = it },
                                label = { Text("Port", color = TextSecondary) },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NetflixRed, unfocusedBorderColor = BorderGray, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                            )
                        }
                    }
                }
            }
        }

        // VPS Streaming Server Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceCard)
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Dns, contentDescription = null, tint = NetflixRed, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "VPS Transcoding & CDN Node", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = vpsServerHost,
                        onValueChange = { vpsServerHost = it },
                        label = { Text("Server Host / IP", color = TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NetflixRed, unfocusedBorderColor = BorderGray, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = vpsCdnUrl,
                        onValueChange = { vpsCdnUrl = it },
                        label = { Text("CDN Streaming URL", color = TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NetflixRed, unfocusedBorderColor = BorderGray, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                    )
                }
            }
        }

        // Save Button
        item {
            Button(
                onClick = {
                    StreamRepository.adminUpdateZapUpiConfig(zapConfig.copy(apiKey = zapApiKey, merchantVpa = zapMerchantVpa, isTestMode = !zapLiveMode))
                    StreamRepository.adminUpdateGoogleOAuthConfig(googleConfig.copy(clientId = googleClientId, clientSecret = googleSecret, isEnabled = googleEnabled))
                    StreamRepository.adminUpdateSmtpConfig(smtpConfig.copy(host = smtpHost, port = smtpPort.toIntOrNull() ?: 587, username = smtpUser, password = smtpPass, isEnabled = smtpEnabled))
                    StreamRepository.adminUpdateVpsConfig(vpsConfig.copy(serverHost = vpsServerHost, cdnUrl = vpsCdnUrl, ffmpegPresets = vpsPresets))
                    saveConfirmation = "All integration keys and VPS settings updated!"
                },
                colors = ButtonDefaults.buttonColors(containerColor = NetflixRed),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("admin_save_integrations_btn")
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save All Settings", fontWeight = FontWeight.Bold)
            }

            if (saveConfirmation != null) {
                Text(
                    text = saveConfirmation!!,
                    color = GreenSuccess,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

// 7. APP SETTINGS SECTION
@Composable
private fun AdminAppSettingsSection() {
    val appConfig by StreamRepository.appConfig.collectAsState()
    var maintenanceMode by remember { mutableStateOf(appConfig.maintenanceMode) }
    var tagline by remember { mutableStateOf(appConfig.tagline) }
    var minVersion by remember { mutableStateOf(appConfig.forceUpdateMinVersion) }
    var savedMsg by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Global App Settings",
            color = TextPrimary,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(SurfaceCard)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Maintenance Mode", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(text = "Temporarily pause user streams with custom message", color = TextMuted, fontSize = 11.sp)
                }
                Switch(
                    checked = maintenanceMode,
                    onCheckedChange = { maintenanceMode = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = NetflixRed)
                )
            }
        }

        OutlinedTextField(
            value = tagline,
            onValueChange = { tagline = it },
            label = { Text("App Tagline", color = TextSecondary) },
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NetflixRed, unfocusedBorderColor = BorderGray, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = minVersion,
            onValueChange = { minVersion = it },
            label = { Text("Minimum App Version Required", color = TextSecondary) },
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NetflixRed, unfocusedBorderColor = BorderGray, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary),
            modifier = Modifier.fillMaxWidth()
        )

        // Super Admin Account Info (Read-only security block)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(NetflixRed.copy(alpha = 0.15f))
                .border(1.dp, NetflixRed, RoundedCornerShape(10.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = NetflixRed, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Super Admin Master Credentials", color = NetflixRed, fontWeight = FontWeight.Black, fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Email: ${MockData.MASTER_ADMIN_EMAIL}", color = TextPrimary, fontSize = 12.sp)
                Text(text = "Role: Super Admin (Permanent / Protected against deletion)", color = TextSecondary, fontSize = 11.sp)
            }
        }

        Button(
            onClick = {
                StreamRepository.adminUpdateAppConfig(
                    appConfig.copy(
                        maintenanceMode = maintenanceMode,
                        tagline = tagline,
                        forceUpdateMinVersion = minVersion
                    )
                )
                savedMsg = "Global app configuration updated!"
            },
            colors = ButtonDefaults.buttonColors(containerColor = NetflixRed),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Save Global Config", fontWeight = FontWeight.Bold)
        }

        if (savedMsg != null) {
            Text(text = savedMsg!!, color = GreenSuccess, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}
