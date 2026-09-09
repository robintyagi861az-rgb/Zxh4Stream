package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AdFormat
import com.example.data.ContentItem
import com.example.data.ContentType
import com.example.data.StreamRepository
import com.example.ui.components.ContentPosterCard
import com.example.ui.components.FeaturedHeroBanner
import com.example.ui.components.SponsoredFeedBanner
import com.example.ui.theme.BackgroundBlack
import com.example.ui.theme.BlueLive
import com.example.ui.theme.GoldStar
import com.example.ui.theme.NetflixRed
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun HomeScreen(
    onContentClick: (ContentItem) -> Unit,
    onPlayContent: (ContentItem) -> Unit,
    onNavigateCategory: (String) -> Unit,
    onGoAdFree: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val contents by StreamRepository.contents.collectAsState()
    val continueWatching by StreamRepository.continueWatching.collectAsState()
    val myList by StreamRepository.myList.collectAsState()
    val activeProfile by StreamRepository.activeProfile.collectAsState()

    // Advertisement System Integration
    val adConfig by StreamRepository.adConfig.collectAsState()
    val adCampaigns by StreamRepository.adCampaigns.collectAsState()
    val currentPlan by StreamRepository.currentPlan.collectAsState()
    val temporaryAdFreeUntil by StreamRepository.temporaryAdFreeUnlockUntil.collectAsState()
    var bannerDismissed by remember { mutableStateOf(false) }

    val isUserSubjectToAds = (currentPlan.hasAds || currentPlan.id == "plan_free") && temporaryAdFreeUntil < System.currentTimeMillis()
    val bannerAd = remember(adCampaigns) {
        adCampaigns.firstOrNull { it.isActive && it.format == AdFormat.BANNER }
            ?: adCampaigns.firstOrNull { it.isActive }
    }

    LaunchedEffect(bannerAd?.id) {
        bannerAd?.let {
            if (isUserSubjectToAds && adConfig.adsEnabled && adConfig.enableBannerAds && !bannerDismissed) {
                StreamRepository.recordAdImpression(it.id)
            }
        }
    }

    // Filter kids content if active profile is Kids
    val displayContents = if (activeProfile.isKids) {
        contents.filter { it.isKidsSafe }
    } else {
        contents
    }

    val featuredList = displayContents.filter { it.isFeatured }
    val trendingList = displayContents.filter { it.isTrending }
    val top10List = displayContents.filter { it.isTop10India }.sortedBy { it.top10Rank }
    val liveEventsList = displayContents.filter { it.isLiveEvent }
    val newReleasesList = displayContents.filter { it.isNewRelease || it.releaseYear >= 2025 }
    val recommendedList = displayContents.filter { it.matchScorePercent >= 95 }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundBlack)
            .testTag("home_screen_feed"),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Top Large Auto-Sliding Featured Hero Banner
        item {
            FeaturedHeroBanner(
                featuredList = if (featuredList.isNotEmpty()) featuredList else displayContents.take(3),
                isSavedInMyList = { id -> myList.contains(id) },
                onPlayClick = { onPlayContent(it) },
                onToggleMyList = { StreamRepository.toggleMyList(it.id) },
                onInfoClick = { onContentClick(it) }
            )
        }

        // Quick Category Filter Pills
        item {
            val categories = listOf("All", "Movies", "Web Series", "Live TV", "Action", "Crime", "Hindi", "Top 10")
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (cat == "All") NetflixRed else Color(0xFF242424))
                            .clickable { onNavigateCategory(cat) }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                            .testTag("category_pill_$cat")
                    ) {
                        Text(
                            text = cat,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = if (cat == "All") FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Row 1: Continue Watching (if user has watch history)
        if (continueWatching.isNotEmpty()) {
            item {
                SectionHeader(title = "Continue Watching for ${activeProfile.name}", onSeeAll = null)
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(continueWatching) { cw ->
                        val item = contents.find { it.id == cw.contentId }
                        if (item != null) {
                            ContentPosterCard(
                                content = item,
                                onClick = { onPlayContent(item) },
                                continueWatchingFraction = cw.progressFraction,
                                cardWidth = 135.dp,
                                cardHeight = 195.dp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // Row 2: Live TV & Sporting Events
        if (liveEventsList.isNotEmpty() && !activeProfile.isKids) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LiveTv,
                        contentDescription = "Live",
                        tint = NetflixRed,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Live Sports & Premieres",
                        color = TextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(liveEventsList) { item ->
                        ContentPosterCard(
                            content = item,
                            onClick = { onContentClick(item) },
                            cardWidth = 140.dp,
                            cardHeight = 200.dp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // Row 3: Trending Now
        item {
            SectionHeader(title = "Trending Now", onSeeAll = { onNavigateCategory("Trending") })
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(trendingList) { item ->
                    ContentPosterCard(
                        content = item,
                        onClick = { onContentClick(item) },
                        cardWidth = 125.dp,
                        cardHeight = 180.dp
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Native Sponsored Feed Banner Ad
        if (adConfig.adsEnabled && adConfig.enableBannerAds && isUserSubjectToAds && bannerAd != null && !bannerDismissed) {
            item {
                SponsoredFeedBanner(
                    ad = bannerAd,
                    onAdClick = {
                        StreamRepository.recordAdClick(bannerAd.id)
                    },
                    onDismiss = {
                        bannerDismissed = true
                    },
                    onGoAdFree = onGoAdFree
                )
                Spacer(modifier = Modifier.height(14.dp))
            }
        }

        // Row 4: Top 10 in India Today (Numbered 1-10)
        if (top10List.isNotEmpty()) {
            item {
                SectionHeader(title = "Top 10 in India Today", onSeeAll = null)
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(top10List) { index, item ->
                        ContentPosterCard(
                            content = item,
                            onClick = { onContentClick(item) },
                            showRank = index + 1,
                            cardWidth = 125.dp,
                            cardHeight = 180.dp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // Row 5: AI-Based Recommendations ("Recommended for You")
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "AI Match",
                    tint = GoldStar,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Recommended for You (AI Match)",
                    color = TextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(recommendedList) { item ->
                    ContentPosterCard(
                        content = item,
                        onClick = { onContentClick(item) },
                        cardWidth = 125.dp,
                        cardHeight = 180.dp
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Row 6: New Releases
        item {
            SectionHeader(title = "New Releases on Zxh4Stream", onSeeAll = { onNavigateCategory("New") })
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(newReleasesList) { item ->
                    ContentPosterCard(
                        content = item,
                        onClick = { onContentClick(item) },
                        cardWidth = 125.dp,
                        cardHeight = 180.dp
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Row 7: Action & Thrillers
        val actionList = displayContents.filter { it.genres.any { g -> g.contains("Action", true) || g.contains("Thriller", true) } }
        if (actionList.isNotEmpty()) {
            item {
                SectionHeader(title = "Action & High-Octane Thrillers", onSeeAll = { onNavigateCategory("Action") })
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(actionList) { item ->
                        ContentPosterCard(
                            content = item,
                            onClick = { onContentClick(item) },
                            cardWidth = 125.dp,
                            cardHeight = 180.dp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    onSeeAll: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = TextPrimary,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.2).sp
        )

        if (onSeeAll != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable { onSeeAll() }
                    .padding(4.dp)
            ) {
                Text(
                    text = "See All",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "See All",
                    tint = TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
