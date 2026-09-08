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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.ui.theme.BackgroundBlack
import com.example.ui.theme.BorderGray
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.NetflixRed
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun TermsAndPrivacyScreen(
    initialTab: Int = 0,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(initialTab) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundBlack)
            .testTag("terms_privacy_screen")
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("legal_back_btn")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Legal & Compliance",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Zxh4Stream Entertainment Platform",
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF1E1E1E))
                    .border(0.5.dp, BorderGray, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = null,
                        tint = GreenSuccess,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "v2.4 Verified",
                        color = GreenSuccess,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Tab Row (Terms vs Privacy)
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = SurfaceCard,
            contentColor = NetflixRed,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = NetflixRed,
                    height = 3.dp
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                modifier = Modifier.testTag("terms_tab"),
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Gavel,
                            contentDescription = null,
                            tint = if (selectedTab == 0) NetflixRed else TextMuted,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Terms & Conditions",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 0) TextPrimary else TextMuted,
                            fontSize = 13.sp
                        )
                    }
                }
            )

            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                modifier = Modifier.testTag("privacy_tab"),
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PrivacyTip,
                            contentDescription = null,
                            tint = if (selectedTab == 1) NetflixRed else TextMuted,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Privacy Policy",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 1) TextPrimary else TextMuted,
                            fontSize = 13.sp
                        )
                    }
                }
            )
        }

        // Content
        if (selectedTab == 0) {
            TermsOfServiceView(onBack = onBack)
        } else {
            PrivacyPolicyView(onBack = onBack)
        }
    }
}

@Composable
private fun TermsOfServiceView(onBack: () -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("terms_content_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            HeaderCard(
                title = "Terms and Conditions of Use",
                lastUpdated = "September 2026",
                summary = "Please read these terms carefully before accessing or using Zxh4Stream services, media streaming players, VPS CDN nodes, or ZapUPI payment processing.",
                icon = Icons.Default.Description
            )
        }

        item {
            LegalSectionCard(
                sectionNumber = "1",
                title = "Acceptance of Agreement",
                content = "By creating an account, selecting a profile, or accessing any media content on Zxh4Stream, you agree to be bound by these Terms and Conditions and our Privacy Policy. If you do not agree to all terms, you must discontinue using the service immediately."
            )
        }

        item {
            LegalSectionCard(
                sectionNumber = "2",
                title = "User Accounts & Multi-Profile Governance",
                content = "Each registered user may configure up to 5 user profiles. You are solely responsible for maintaining the confidentiality of your login credentials and parental control PINs. Profiles configured in Kids Mode restrict playback to family-safe and rated titles. Account credentials may not be shared outside your immediate household."
            )
        }

        item {
            LegalSectionCard(
                sectionNumber = "3",
                title = "Subscriptions, Billing & ZapUPI Payments",
                content = "Access to high-definition (1080p) and 4K Ultra HD streams requires an active subscription tier (Mobile, Basic, Standard, or Premium Ultra). Payments processed through the ZapUPI Gateway are confirmed in real-time via merchant webhooks. All fees are in Indian Rupees (INR) and inclusive of applicable taxes. Auto-renewal may be cancelled at any time prior to the next billing cycle from the Subscription Manager."
            )
        }

        item {
            LegalSectionCard(
                sectionNumber = "4",
                title = "Intellectual Property & VPS CDN Streaming",
                content = "All video, audio, graphics, and metadata streamed through our VPS transcoding nodes are protected by copyright and intellectual property laws. You are granted a non-exclusive, non-transferable, revocable license for personal, non-commercial streaming. Screen recording, ripping, decompiling, or re-broadcasting Zxh4Stream streams is strictly prohibited and subject to immediate account termination and legal action."
            )
        }

        item {
            LegalSectionCard(
                sectionNumber = "5",
                title = "Offline Downloads & Storage Limits",
                content = "Subscribers with supported tiers may temporarily store encrypted video files for offline viewing within the application. Downloaded files remain playable exclusively within the authorized Zxh4Stream mobile app and expire automatically upon subscription termination or after 30 days of offline storage."
            )
        }

        item {
            LegalSectionCard(
                sectionNumber = "6",
                title = "Platform Availability & VPS Maintenance",
                content = "We strive to maintain 99.9% uptime across all global CDN delivery networks. Periodically, scheduled VPS maintenance or node transcoding updates may occur. In the event of emergency maintenance, our banner notification system alerts active streamers."
            )
        }

        item {
            LegalSectionCard(
                sectionNumber = "7",
                title = "Governing Law & Dispute Resolution",
                content = "These Terms shall be governed by and construed in accordance with the laws of India. Any legal dispute or claim arising under these terms shall be subject to the exclusive jurisdiction of the courts located in New Delhi, India."
            )
        }

        item {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = NetflixRed),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("agree_terms_btn")
            ) {
                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "I Understand & Agree to Terms", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun PrivacyPolicyView(onBack: () -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("privacy_content_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            HeaderCard(
                title = "Privacy and Data Protection Policy",
                lastUpdated = "September 2026",
                summary = "Your privacy and media streaming security are paramount. This policy outlines how Zxh4Stream collects, encrypts, and handles your personal and telemetry information.",
                icon = Icons.Default.Policy
            )
        }

        item {
            LegalSectionCard(
                sectionNumber = "1",
                title = "Information We Collect",
                content = "We collect information necessary to deliver high-performance streaming:\n• Account Information: Email address, encrypted authentication hashes, and profile names.\n• Streaming Preferences: Watch history, playback timestamp positions, 'My List' selections, and parental controls.\n• Technical Telemetry: Device model, screen resolution, operating system version, and CDN buffer health logs."
            )
        }

        item {
            LegalSectionCard(
                sectionNumber = "2",
                title = "ZapUPI Payment Security",
                content = "When initiating subscription upgrades, payment transactions are routed securely via the ZapUPI Gateway. We do not store banking passwords, UPI MPINs, or debit card credentials on our servers. We store only cryptographic transaction tokens, webhook references, and UTR numbers for invoice reconciliation."
            )
        }

        item {
            LegalSectionCard(
                sectionNumber = "3",
                title = "How Information Is Utilized",
                content = "Your information is used strictly to:\n• Stream multi-bitrate HLS and 4K video feeds with minimal latency.\n• Generate personalized AI recommendations and 'Continue Watching' queues.\n• Deliver crucial service announcements and transaction receipts.\n• Prevent fraudulent multi-IP credential leaks or unauthorized re-broadcasting."
            )
        }

        item {
            LegalSectionCard(
                sectionNumber = "4",
                title = "Data Encryption & VPS Cloud Storage",
                content = "All communication between your device, our master API, and VPS streaming nodes is encrypted using industry-standard TLS 1.3 encryption. Passwords utilize salted cryptographic hashing. Video chunks are served over signed CDN tokens."
            )
        }

        item {
            LegalSectionCard(
                sectionNumber = "5",
                title = "Children's Privacy Protection",
                content = "Zxh4Stream offers dedicated Kids Profiles with strict content filtering. Profiles tagged as Kids do not track personalized ad telemetry, nor do they permit access to social or unrated streaming content. Parental PIN protections require authentication for switching."
            )
        }

        item {
            LegalSectionCard(
                sectionNumber = "6",
                title = "Your Privacy Rights & Data Deletion",
                content = "You have the absolute right to inspect your stored data, request export of your watch history, or request permanent deletion of your account and all associated profile telemetry. Contact our Data Protection Officer at privacy@zxh4stream.com for immediate fulfillment."
            )
        }

        item {
            LegalSectionCard(
                sectionNumber = "7",
                title = "Grievance Redressal & Contact",
                content = "In accordance with Information Technology Rules, the contact details for our Grievance Officer are:\nLegal & Compliance Officer\nZxh4Stream Media Network\nEmail: legal@zxh4stream.com\nAddress: Sector 62, Electronic City, Noida, Uttar Pradesh, India"
            )
        }

        item {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = NetflixRed),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("agree_privacy_btn")
            ) {
                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Acknowledge Privacy Policy", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun HeaderCard(
    title: String,
    lastUpdated: String,
    summary: String,
    icon: ImageVector
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(NetflixRed.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = NetflixRed,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Effective: $lastUpdated",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = BorderGray)
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = summary,
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun LegalSectionCard(
    sectionNumber: String,
    title: String,
    content: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF262626)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = sectionNumber,
                        color = NetflixRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = content,
                color = TextSecondary,
                fontSize = 12.5.sp,
                lineHeight = 18.sp
            )
        }
    }
}
