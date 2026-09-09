package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BrightnessHigh
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.data.AdCampaign
import com.example.data.AdFormat
import com.example.data.ContentItem
import com.example.data.ContentType
import com.example.data.Episode
import com.example.data.StreamQuality
import com.example.data.StreamRepository
import com.example.ui.components.RewardedAdUnlockDialog
import com.example.ui.components.VideoAdOverlay
import com.example.ui.theme.BackgroundBlack
import com.example.ui.theme.NetflixRed
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay

@Composable
fun VideoPlayerScreen(
    content: ContentItem,
    initialEpisode: Episode? = null,
    onClose: () -> Unit,
    onUpgradeToVip: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Advertisement System State
    val adConfig by StreamRepository.adConfig.collectAsState()
    val adCampaigns by StreamRepository.adCampaigns.collectAsState()
    val currentPlan by StreamRepository.currentPlan.collectAsState()
    val temporaryAdFreeUntil by StreamRepository.temporaryAdFreeUnlockUntil.collectAsState()

    val isUserFreeTier = currentPlan.hasAds || currentPlan.id == "plan_free"
    val isTemporaryUnlocked = temporaryAdFreeUntil > System.currentTimeMillis()
    val shouldPlayAds = adConfig.adsEnabled && isUserFreeTier && !isTemporaryUnlocked

    // Pre-roll ad state
    val preRollAd = remember(adCampaigns) {
        adCampaigns.firstOrNull { it.isActive && it.format == AdFormat.PRE_ROLL }
            ?: adCampaigns.firstOrNull { it.isActive }
    }
    var isPreRollActive by remember { mutableStateOf(shouldPlayAds && adConfig.enablePreRoll && preRollAd != null) }
    var adSecondsElapsed by remember { mutableIntStateOf(0) }
    var preRollLogged by remember { mutableStateOf(false) }

    // Mid-roll ad state
    val midRollAd = remember(adCampaigns) {
        adCampaigns.firstOrNull { it.isActive && it.format == AdFormat.MID_ROLL }
            ?: adCampaigns.firstOrNull { it.isActive }
    }
    var isMidRollActive by remember { mutableStateOf(false) }
    var midRollTriggered by remember { mutableStateOf(false) }

    // Rewarded Ad state
    val rewardedAd = remember(adCampaigns) {
        adCampaigns.firstOrNull { it.isActive && it.format == AdFormat.REWARDED }
            ?: adCampaigns.firstOrNull { it.isActive }
    }
    var showRewardedAdDialog by remember { mutableStateOf(false) }
    var isPlayingRewardedAd by remember { mutableStateOf(false) }

    var isPlaying by remember { mutableStateOf(!isPreRollActive) }
    var currentPositionSeconds by remember { mutableLongStateOf(25L) }
    val totalDurationSeconds = remember {
        (initialEpisode?.durationMinutes ?: content.durationMinutes) * 60L
    }
    var showControls by remember { mutableStateOf(true) }
    var isLocked by remember { mutableStateOf(false) }
    var showSeekFeedback by remember { mutableStateOf<String?>(null) }

    // VIP ad-free banner chip for paid users (auto-dismisses in 3.5s)
    var showVipAdFreeChip by remember { mutableStateOf(!isUserFreeTier || isTemporaryUnlocked) }
    LaunchedEffect(showVipAdFreeChip) {
        if (showVipAdFreeChip) {
            delay(3500)
            showVipAdFreeChip = false
        }
    }

    // Pre-roll ticker
    LaunchedEffect(isPreRollActive, adSecondsElapsed) {
        if (isPreRollActive && preRollAd != null) {
            if (!preRollLogged) {
                StreamRepository.recordAdImpression(preRollAd.id)
                preRollLogged = true
            }
            delay(1000)
            if (adSecondsElapsed < preRollAd.durationSeconds) {
                adSecondsElapsed += 1
            } else {
                isPreRollActive = false
            }
        }
    }

    // Mid-roll ticker
    LaunchedEffect(isMidRollActive, adSecondsElapsed) {
        if (isMidRollActive && midRollAd != null) {
            delay(1000)
            if (adSecondsElapsed < midRollAd.durationSeconds) {
                adSecondsElapsed += 1
            } else {
                isMidRollActive = false
            }
        }
    }

    // Rewarded ad ticker
    LaunchedEffect(isPlayingRewardedAd, adSecondsElapsed) {
        if (isPlayingRewardedAd && rewardedAd != null) {
            delay(1000)
            if (adSecondsElapsed < rewardedAd.durationSeconds) {
                adSecondsElapsed += 1
            } else {
                isPlayingRewardedAd = false
                StreamRepository.unlockTemporaryReward(120)
                showSeekFeedback = "1080p Ultra HD Unlocked!"
            }
        }
    }

    // Trigger mid-roll at 600s
    LaunchedEffect(currentPositionSeconds) {
        if (shouldPlayAds && adConfig.enableMidRoll && !midRollTriggered && currentPositionSeconds >= 600L && midRollAd != null) {
            midRollTriggered = true
            adSecondsElapsed = 0
            isMidRollActive = true
            StreamRepository.recordAdImpression(midRollAd.id)
        }
    }

    // Gestures HUD
    var brightnessLevel by remember { mutableFloatStateOf(0.75f) }
    var volumeLevel by remember { mutableFloatStateOf(0.80f) }
    var showGestureHud by remember { mutableStateOf<String?>(null) }

    // Dialogs
    var showQualityDialog by remember { mutableStateOf(false) }
    var showAudioSubtitleDialog by remember { mutableStateOf(false) }
    var showSpeedDialog by remember { mutableStateOf(false) }

    var selectedQuality by remember { mutableStateOf(StreamQuality.UHD_4K) }
    var selectedAudio by remember { mutableStateOf(content.audioLanguages.firstOrNull() ?: "Hindi [Original]") }
    var selectedSubtitle by remember { mutableStateOf("English [CC]") }
    var selectedSpeed by remember { mutableFloatStateOf(1.0f) }

    // Auto-hide controls timer
    LaunchedEffect(showControls, isPlaying, isLocked) {
        if (showControls && isPlaying && !isLocked) {
            delay(4000)
            showControls = false
        }
    }

    // Playback progress ticker
    LaunchedEffect(isPlaying, isPreRollActive, isMidRollActive, isPlayingRewardedAd) {
        while (isPlaying && !isPreRollActive && !isMidRollActive && !isPlayingRewardedAd) {
            delay(1000)
            if (currentPositionSeconds < totalDurationSeconds) {
                currentPositionSeconds += 1
                StreamRepository.updateContinueWatching(
                    content.id,
                    currentPositionSeconds,
                    totalDurationSeconds,
                    sNum = 1,
                    epNum = initialEpisode?.episodeNumber ?: 1
                )
            } else {
                isPlaying = false
            }
        }
    }

    // Clear gesture HUD after delay
    LaunchedEffect(showGestureHud) {
        if (showGestureHud != null) {
            delay(1200)
            showGestureHud = null
        }
    }

    // Clear seek feedback after delay
    LaunchedEffect(showSeekFeedback) {
        if (showSeekFeedback != null) {
            delay(800)
            showSeekFeedback = null
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("video_player_container")
            // Tap & double tap detection for seeking and control toggle
            .pointerInput(isLocked) {
                detectTapGestures(
                    onDoubleTap = { offset ->
                        if (!isLocked) {
                            val screenWidth = size.width
                            if (offset.x < screenWidth / 2) {
                                // Double tap left: seek -10s
                                currentPositionSeconds = (currentPositionSeconds - 10).coerceAtLeast(0)
                                showSeekFeedback = "-10s"
                            } else {
                                // Double tap right: seek +10s
                                currentPositionSeconds = (currentPositionSeconds + 10).coerceAtMost(totalDurationSeconds)
                                showSeekFeedback = "+10s"
                            }
                        }
                    },
                    onTap = {
                        showControls = !showControls
                    }
                )
            }
            // Vertical Drag for brightness (left half) and volume (right half)
            .pointerInput(isLocked) {
                detectDragGestures { change, dragAmount ->
                    if (!isLocked) {
                        val screenWidth = size.width
                        val isLeftSide = change.position.x < screenWidth / 2
                        val delta = -dragAmount.y / 600f
                        if (isLeftSide) {
                            brightnessLevel = (brightnessLevel + delta).coerceIn(0.1f, 1.0f)
                            showGestureHud = "Brightness: ${(brightnessLevel * 100).toInt()}%"
                        } else {
                            volumeLevel = (volumeLevel + delta).coerceIn(0.0f, 1.0f)
                            showGestureHud = "Volume: ${(volumeLevel * 100).toInt()}%"
                        }
                    }
                }
            }
    ) {
        // Simulated video frame (High definition backdrop with subtle motion)
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(content.backdropUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Playback Frame",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Dark dimming overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = if (showControls) 0.55f else 0.15f))
        )

        // Gesture HUD Overlay (Brightness or Volume indicator)
        if (showGestureHud != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.8f))
                    .border(1.dp, NetflixRed.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (showGestureHud!!.startsWith("Brightness")) Icons.Default.BrightnessHigh else Icons.Default.VolumeUp,
                        contentDescription = null,
                        tint = NetflixRed,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = showGestureHud!!,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Seek Ripple Feedback Indicator
        if (showSeekFeedback != null) {
            Box(
                modifier = Modifier
                    .align(if (showSeekFeedback == "-10s") Alignment.CenterStart else Alignment.CenterEnd)
                    .padding(horizontal = 48.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = if (showSeekFeedback == "-10s") Icons.Default.Replay10 else Icons.Default.Forward10,
                        contentDescription = null,
                        tint = NetflixRed,
                        modifier = Modifier.size(36.dp)
                    )
                    Text(
                        text = showSeekFeedback!!,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Skip Intro Button (Appears between 5s and 45s)
        if (currentPositionSeconds in 5L..45L && !isLocked) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 24.dp, bottom = 100.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.Black.copy(alpha = 0.8f))
                    .border(1.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                    .clickable { currentPositionSeconds = 50L }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .testTag("skip_intro_btn")
            ) {
                Text(
                    text = "Skip Intro ▶",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // CONTROLS OVERLAY
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Black.copy(alpha = 0.85f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.9f)
                            )
                        )
                    )
            ) {
                // Top App Bar Controls
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.testTag("player_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Exit Player",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = content.title,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        if (initialEpisode != null) {
                            Text(
                                text = "S1:E${initialEpisode.episodeNumber} • ${initialEpisode.title}",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        } else if (content.isLiveEvent) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(NetflixRed)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "4K Ultra HD LIVE • ${content.liveViewersCount}",
                                    color = NetflixRed,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    // Lock Button
                    IconButton(onClick = { isLocked = !isLocked }) {
                        Icon(
                            imageVector = if (isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                            contentDescription = "Lock",
                            tint = if (isLocked) NetflixRed else Color.White
                        )
                    }

                    // Quality Selector Button
                    IconButton(onClick = { showQualityDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.HighQuality,
                            contentDescription = "Quality",
                            tint = Color.White
                        )
                    }

                    // Audio & Subtitles Button
                    IconButton(onClick = { showAudioSubtitleDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Subtitles,
                            contentDescription = "Audio & Subtitles",
                            tint = Color.White
                        )
                    }

                    // Playback Speed Button
                    IconButton(onClick = { showSpeedDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = "Speed",
                            tint = Color.White
                        )
                    }
                }

                // Center Play / Seek Controls
                if (!isLocked) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(bottom = 24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(40.dp)
                    ) {
                        // Replay 10s
                        IconButton(
                            onClick = {
                                currentPositionSeconds = (currentPositionSeconds - 10).coerceAtLeast(0)
                            },
                            modifier = Modifier.size(54.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Replay10,
                                contentDescription = "Seek -10s",
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        // Play/Pause Big Center Button
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.6f))
                                .border(2.dp, Color.White, CircleShape)
                                .clickable { isPlaying = !isPlaying }
                                .testTag("player_play_pause_btn"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                tint = Color.White,
                                modifier = Modifier.size(44.dp)
                            )
                        }

                        // Forward 10s
                        IconButton(
                            onClick = {
                                currentPositionSeconds = (currentPositionSeconds + 10).coerceAtMost(totalDurationSeconds)
                            },
                            modifier = Modifier.size(54.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Forward10,
                                contentDescription = "Seek +10s",
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                }

                // Bottom Scrubber Bar & Quick Action Controls
                if (!isLocked) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .navigationBarsPadding()
                            .padding(horizontal = 20.dp, vertical = 12.dp)
                    ) {
                        // Time Position / Total Duration labels
                        val currentMins = currentPositionSeconds / 60
                        val currentSecs = currentPositionSeconds % 60
                        val totalMins = totalDurationSeconds / 60
                        val totalSecs = totalDurationSeconds % 60

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = String.format("%02d:%02d", currentMins, currentSecs),
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = String.format("%02d:%02d", totalMins, totalSecs),
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        }

                        // Scrubber Slider
                        Slider(
                            value = currentPositionSeconds.toFloat(),
                            onValueChange = { currentPositionSeconds = it.toLong() },
                            valueRange = 0f..totalDurationSeconds.toFloat(),
                            colors = SliderDefaults.colors(
                                thumbColor = NetflixRed,
                                activeTrackColor = NetflixRed,
                                inactiveTrackColor = Color.Gray.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(24.dp)
                                .testTag("player_progress_slider")
                        )

                        // Bottom Actions Row: Next Episode / Audio / Quality
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${selectedQuality.label} • ${selectedSpeed}x",
                                color = TextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )

                            if (content.type == ContentType.SERIES) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFF242424))
                                        .clickable {
                                            currentPositionSeconds = 0L
                                            showSeekFeedback = "Next Episode"
                                        }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                        .testTag("next_episode_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.SkipNext,
                                        contentDescription = "Next Episode",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Next Episode",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // VIP Ad-Free Chip Notification (Shown on playback start for paid subscribers)
        if (showVipAdFreeChip && !isPreRollActive && !isMidRollActive && !isPlayingRewardedAd) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(top = 16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Black.copy(alpha = 0.75f))
                    .border(1.dp, Color(0xFFFFD700).copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isTemporaryUnlocked) "1080p Ad-Free Pass Active" else "VIP Ad-Free Streaming Active",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Pre-Roll Video Ad Overlay
        if (isPreRollActive && preRollAd != null) {
            VideoAdOverlay(
                ad = preRollAd,
                secondsElapsed = adSecondsElapsed,
                totalDurationSeconds = preRollAd.durationSeconds,
                skipAfterSeconds = preRollAd.skipAfterSeconds,
                onSkipAd = {
                    isPreRollActive = false
                    isPlaying = true
                },
                onAdClick = {
                    StreamRepository.recordAdClick(preRollAd.id)
                },
                onUpgradeToVip = {
                    onClose()
                    onUpgradeToVip()
                }
            )
        }

        // Mid-Roll Video Ad Overlay
        if (isMidRollActive && midRollAd != null) {
            VideoAdOverlay(
                ad = midRollAd,
                secondsElapsed = adSecondsElapsed,
                totalDurationSeconds = midRollAd.durationSeconds,
                skipAfterSeconds = midRollAd.skipAfterSeconds,
                onSkipAd = {
                    isMidRollActive = false
                    isPlaying = true
                },
                onAdClick = {
                    StreamRepository.recordAdClick(midRollAd.id)
                },
                onUpgradeToVip = {
                    onClose()
                    onUpgradeToVip()
                }
            )
        }

        // Rewarded Video Ad Overlay
        if (isPlayingRewardedAd && rewardedAd != null) {
            VideoAdOverlay(
                ad = rewardedAd,
                secondsElapsed = adSecondsElapsed,
                totalDurationSeconds = rewardedAd.durationSeconds,
                skipAfterSeconds = 0,
                onSkipAd = {},
                onAdClick = {
                    StreamRepository.recordAdClick(rewardedAd.id)
                },
                onUpgradeToVip = {
                    onClose()
                    onUpgradeToVip()
                }
            )
        }
    }

    // REWARDED AD UNLOCK DIALOG
    if (showRewardedAdDialog) {
        RewardedAdUnlockDialog(
            onWatchAd = {
                showRewardedAdDialog = false
                isPlaying = false
                isPreRollActive = false
                isMidRollActive = false
                adSecondsElapsed = 0
                isPlayingRewardedAd = true
                rewardedAd?.let { StreamRepository.recordAdImpression(it.id) }
            },
            onDismiss = { showRewardedAdDialog = false },
            onUpgradeVip = {
                showRewardedAdDialog = false
                onClose()
                onUpgradeToVip()
            }
        )
    }

    // QUALITY SELECTOR DIALOG
    if (showQualityDialog) {
        AlertDialog(
            onDismissRequest = { showQualityDialog = false },
            containerColor = SurfaceCard,
            title = {
                Text(
                    text = "Streaming Quality",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    StreamQuality.values().forEach { q ->
                        val isLockedBehindAd = shouldPlayAds && (q == StreamQuality.P1080 || q == StreamQuality.UHD_4K)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (isLockedBehindAd) {
                                        showQualityDialog = false
                                        showRewardedAdDialog = true
                                    } else {
                                        selectedQuality = q
                                        showQualityDialog = false
                                    }
                                }
                                .padding(vertical = 8.dp)
                        ) {
                            RadioButton(
                                selected = selectedQuality == q,
                                onClick = {
                                    if (isLockedBehindAd) {
                                        showQualityDialog = false
                                        showRewardedAdDialog = true
                                    } else {
                                        selectedQuality = q
                                        showQualityDialog = false
                                    }
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = NetflixRed)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = q.label, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                    if (isLockedBehindAd) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(text = "Watch Ad", color = Color(0xFFFFD700), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                Text(text = q.bitrate, color = TextMuted, fontSize = 12.sp)
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showQualityDialog = false }) {
                    Text("Close", color = TextSecondary)
                }
            }
        )
    }

    // AUDIO & SUBTITLES DIALOG
    if (showAudioSubtitleDialog) {
        AlertDialog(
            onDismissRequest = { showAudioSubtitleDialog = false },
            containerColor = SurfaceCard,
            title = {
                Text(
                    text = "Audio & Subtitles",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "Audio Track",
                        color = NetflixRed,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    content.audioLanguages.forEach { audio ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedAudio = audio }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = selectedAudio == audio,
                                onClick = { selectedAudio = audio },
                                colors = RadioButtonDefaults.colors(selectedColor = NetflixRed)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = audio, color = TextPrimary, fontSize = 13.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Subtitles",
                        color = NetflixRed,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    content.subtitleLanguages.forEach { sub ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedSubtitle = sub }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = selectedSubtitle == sub,
                                onClick = { selectedSubtitle = sub },
                                colors = RadioButtonDefaults.colors(selectedColor = NetflixRed)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = sub, color = TextPrimary, fontSize = 13.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showAudioSubtitleDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = NetflixRed)
                ) {
                    Text("Apply")
                }
            }
        )
    }

    // PLAYBACK SPEED DIALOG
    if (showSpeedDialog) {
        AlertDialog(
            onDismissRequest = { showSpeedDialog = false },
            containerColor = SurfaceCard,
            title = {
                Text(
                    text = "Playback Speed",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                val speeds = listOf(0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 2.0f)
                Column {
                    speeds.forEach { spd ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedSpeed = spd
                                    showSpeedDialog = false
                                }
                                .padding(vertical = 6.dp)
                        ) {
                            RadioButton(
                                selected = selectedSpeed == spd,
                                onClick = {
                                    selectedSpeed = spd
                                    showSpeedDialog = false
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = NetflixRed)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (spd == 1.0f) "Normal (1.0x)" else "${spd}x",
                                color = TextPrimary,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            },
            confirmButton = {}
        )
    }
}
