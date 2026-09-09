package com.example.ui.screens

import android.content.Intent
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.data.ContentItem
import com.example.data.ContentType
import com.example.data.Episode
import com.example.data.StreamRepository
import com.example.ui.components.ContentPosterCard
import com.example.ui.theme.BackgroundBlack
import com.example.ui.theme.BorderGray
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.NetflixRed
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ContentDetailScreen(
    content: ContentItem,
    onBack: () -> Unit,
    onPlay: (ContentItem, Episode?) -> Unit,
    onNavigateDetail: (ContentItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val myList by StreamRepository.myList.collectAsState()
    val allContents by StreamRepository.contents.collectAsState()
    val inMyList = myList.contains(content.id)

    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(if (content.type == ContentType.SERIES) 0 else 1) }
    var selectedSeasonIndex by remember { mutableStateOf(0) }
    var showSeasonDropdown by remember { mutableStateOf(false) }
    var showDownloadDialog by remember { mutableStateOf(false) }
    var selectedDownloadQuality by remember { mutableStateOf("1080p Full HD") }

    val similarItems = allContents
        .filter { it.id != content.id && it.genres.any { g -> content.genres.contains(g) } }
        .take(6)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundBlack)
            .testTag("content_detail_screen"),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Hero Backdrop with back button & gradient
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(content.backdropUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = content.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    loading = {
                        Box(modifier = Modifier.fillMaxSize().background(SurfaceCard))
                    }
                )

                // Top & Bottom gradient overlays
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    BackgroundBlack.copy(alpha = 0.8f),
                                    Color.Transparent,
                                    BackgroundBlack
                                )
                            )
                        )
                )

                // Back Button & Action Icons in Status Area
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.6f))
                            .testTag("detail_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Row {
                        IconButton(
                            onClick = {
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, "Watch ${content.title} on Zxh4Stream! ${content.description.take(120)}...")
                                    type = "text/plain"
                                }
                                val shareIntent = Intent.createChooser(sendIntent, "Share ${content.title}")
                                context.startActivity(shareIntent)
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.6f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // Center Play Button Overlay on backdrop
                IconButton(
                    onClick = { onPlay(content, null) },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.6f))
                        .border(1.5.dp, Color.White.copy(alpha = 0.8f), CircleShape)
                        .testTag("backdrop_play_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }

        // Title and Metadata Details
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = content.title,
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.5).sp
                )

                if (content.tagline.isNotBlank()) {
                    Text(
                        text = content.tagline,
                        color = NetflixRed,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Metadata Row: Match %, Year, Rating, Quality
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${content.matchScorePercent}% Match",
                        color = GreenSuccess,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )

                    Text(
                        text = "${content.releaseYear}",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )

                    Box(
                        modifier = Modifier
                            .border(1.dp, Color.Gray, RoundedCornerShape(3.dp))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = content.maturityRating,
                            color = TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (content.type == ContentType.SERIES) {
                        val seasonsCount = content.seasons.size.coerceAtLeast(1)
                        Text(
                            text = "$seasonsCount Season${if (seasonsCount > 1) "s" else ""}",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    } else {
                        val hours = content.durationMinutes / 60
                        val mins = content.durationMinutes % 60
                        Text(
                            text = "${hours}h ${mins}m",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .border(1.dp, Color.Gray, RoundedCornerShape(3.dp))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "4K UHD",
                            color = TextSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .border(1.dp, Color.Gray, RoundedCornerShape(3.dp))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "5.1 AUDIO",
                            color = TextSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Big Primary Play Button (White with black text, Netflix style)
                Button(
                    onClick = { onPlay(content, null) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("detail_play_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (content.isLiveEvent) "Watch Live Broadcast" else "Play",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Secondary Action: Download Button (Dark gray)
                Button(
                    onClick = { showDownloadDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF262626),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("detail_download_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Download",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Download",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Description Paragraph
                Text(
                    text = content.description,
                    color = TextPrimary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Cast & Directors
                if (content.cast.isNotEmpty()) {
                    Row {
                        Text(text = "Starring: ", color = TextMuted, fontSize = 12.sp)
                        Text(
                            text = content.cast.joinToString(", "),
                            color = TextSecondary,
                            fontSize = 12.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                if (content.directors.isNotEmpty()) {
                    Row(modifier = Modifier.padding(top = 2.dp)) {
                        Text(text = "Creator / Director: ", color = TextMuted, fontSize = 12.sp)
                        Text(
                            text = content.directors.joinToString(", "),
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }

                // Audio & Subtitles
                Row(modifier = Modifier.padding(top = 4.dp)) {
                    Text(text = "Audio: ", color = TextMuted, fontSize = 12.sp)
                    Text(
                        text = content.audioLanguages.joinToString(", "),
                        color = TextSecondary,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // My List / Rate / Share Icon Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    // My List Icon
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { StreamRepository.toggleMyList(content.id) }
                            .padding(8.dp)
                            .testTag("detail_toggle_mylist")
                    ) {
                        Icon(
                            imageVector = if (inMyList) Icons.Default.Check else Icons.Default.Add,
                            contentDescription = "My List",
                            tint = if (inMyList) NetflixRed else Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (inMyList) "In My List" else "My List",
                            color = if (inMyList) NetflixRed else TextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    // Share Icon
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { /* Share */ }
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Share", color = TextSecondary, fontSize = 11.sp)
                    }
                }
            }
        }

        // Tabs: Episodes (for series) / More Like This / Trailers
        item {
            val tabs = mutableListOf<String>()
            if (content.type == ContentType.SERIES) tabs.add("Episodes")
            tabs.add("More Like This")
            tabs.add("Trailers & More")

            TabRow(
                selectedTabIndex = selectedTab.coerceIn(0, tabs.size - 1),
                containerColor = BackgroundBlack,
                contentColor = Color.White,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab.coerceIn(0, tabs.size - 1)]),
                        color = NetflixRed,
                        height = 3.dp
                    )
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 13.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                                color = if (selectedTab == index) Color.White else TextMuted
                            )
                        }
                    )
                }
            }
        }

        // Tab Content
        if (content.type == ContentType.SERIES && selectedTab == 0) {
            // Season Selector Dropdown
            if (content.seasons.isNotEmpty()) {
                item {
                    val currentSeason = content.seasons[selectedSeasonIndex.coerceIn(0, content.seasons.size - 1)]
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(SurfaceCard)
                                .clickable { showSeasonDropdown = true }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = currentSeason.title,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        DropdownMenu(
                            expanded = showSeasonDropdown,
                            onDismissRequest = { showSeasonDropdown = false },
                            modifier = Modifier.background(SurfaceCard)
                        ) {
                            content.seasons.forEachIndexed { index, season ->
                                DropdownMenuItem(
                                    text = { Text(season.title, color = TextPrimary) },
                                    onClick = {
                                        selectedSeasonIndex = index
                                        showSeasonDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Episode List
                val activeEpisodes = content.seasons[selectedSeasonIndex.coerceIn(0, content.seasons.size - 1)].episodes
                items(activeEpisodes) { ep ->
                    EpisodeRowItem(
                        episode = ep,
                        onPlayEpisode = { onPlay(content, ep) },
                        onDownloadEpisode = {
                            StreamRepository.startDownload(content, "1080p Full HD", "${ep.title}")
                        }
                    )
                }
            }
        } else if ((content.type == ContentType.SERIES && selectedTab == 1) || (content.type != ContentType.SERIES && selectedTab == 1)) {
            // More Like This (Similar content grid)
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    Text(
                        text = "Titles you might enjoy",
                        color = TextSecondary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        similarItems.take(3).forEach { item ->
                            ContentPosterCard(
                                content = item,
                                onClick = { onNavigateDetail(item) },
                                cardWidth = 110.dp,
                                cardHeight = 160.dp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        similarItems.drop(3).take(3).forEach { item ->
                            ContentPosterCard(
                                content = item,
                                onClick = { onNavigateDetail(item) },
                                cardWidth = 110.dp,
                                cardHeight = 160.dp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        } else {
            // Trailers & Extras tab
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                    Text(
                        text = "Official Trailer & Teaser",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceCard)
                            .clickable { onPlay(content, null) }
                    ) {
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(content.backdropUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Trailer",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.35f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(NetflixRed),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play Trailer",
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }

                        Text(
                            text = "Official Cinematic Trailer (2m 45s)",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(12.dp)
                        )
                    }
                }
            }
        }
    }

    // Download Quality Selection Dialog
    if (showDownloadDialog) {
        AlertDialog(
            onDismissRequest = { showDownloadDialog = false },
            containerColor = SurfaceCard,
            title = {
                Text(
                    text = "Download Quality",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "Choose quality for offline playback on Zxh4Stream:",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val qualities = listOf(
                        "4K Ultra HD (HDR)" to "~4.2 GB",
                        "1080p Full HD" to "~1.4 GB",
                        "720p HD" to "~680 MB",
                        "480p SD (Data Saver)" to "~420 MB"
                    )

                    qualities.forEach { (q, size) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedDownloadQuality = q }
                                .padding(vertical = 6.dp)
                        ) {
                            RadioButton(
                                selected = selectedDownloadQuality == q,
                                onClick = { selectedDownloadQuality = q },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = NetflixRed,
                                    unselectedColor = Color.Gray
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(text = q, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                Text(text = size, color = TextMuted, fontSize = 12.sp)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        StreamRepository.startDownload(content, selectedDownloadQuality)
                        showDownloadDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NetflixRed)
                ) {
                    Text("Start Download")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDownloadDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
private fun EpisodeRowItem(
    episode: Episode,
    onPlayEpisode: () -> Unit,
    onDownloadEpisode: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPlayEpisode() }
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .testTag("episode_item_${episode.id}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail with play icon
            Box(
                modifier = Modifier
                    .size(width = 110.dp, height = 66.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceCard)
            ) {
                SubcomposeAsyncImage(
                    model = episode.thumbnail,
                    contentDescription = episode.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.7f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play Episode",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Episode Title & Duration
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = episode.title,
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${episode.durationMinutes}m",
                    color = TextMuted,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            // Download Icon for this episode
            IconButton(onClick = onDownloadEpisode) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Download Episode",
                    tint = TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Synopsis
        Text(
            text = episode.description,
            color = TextSecondary,
            fontSize = 12.sp,
            lineHeight = 17.sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
