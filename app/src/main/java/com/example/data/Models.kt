package com.example.data

enum class ContentType {
    MOVIE,
    SERIES,
    LIVE_EVENT
}

enum class StreamQuality(val label: String, val bitrate: String) {
    AUTO("Auto (Best)", "Dynamic"),
    P480("480p SD", "1.2 Mbps"),
    P720("720p HD", "2.8 Mbps"),
    P1080("1080p Full HD", "5.5 Mbps"),
    UHD_4K("4K Ultra HD (HDR)", "16.0 Mbps")
}

data class Episode(
    val id: String,
    val episodeNumber: Int,
    val title: String,
    val description: String,
    val durationMinutes: Int,
    val thumbnail: String,
    val videoUrl: String,
    val embedUrl: String = "",
    val isFiller: Boolean = false
)

data class Season(
    val seasonNumber: Int,
    val title: String,
    val episodes: List<Episode>
)

data class ContentItem(
    val id: String,
    val title: String,
    val tagline: String = "",
    val description: String,
    val posterUrl: String,
    val backdropUrl: String,
    val trailerUrl: String = "",
    val videoUrl: String = "",
    val embedUrl: String = "",
    val durationMinutes: Int = 120,
    val releaseYear: Int = 2025,
    val maturityRating: String = "U/A 16+",
    val matchScorePercent: Int = 98,
    val genres: List<String> = listOf("Action", "Thriller"),
    val tags: List<String> = listOf("Violent", "Mind-Bending", "Gritty"),
    val cast: List<String> = listOf("Vikram Vedha", "Shah Rukh Khan", "Deepika Padukone"),
    val directors: List<String> = listOf("Christopher Nolan"),
    val audioLanguages: List<String> = listOf("Hindi [Original]", "English", "Tamil", "Telugu", "Spanish"),
    val subtitleLanguages: List<String> = listOf("English", "Hindi", "Arabic", "Off"),
    val type: ContentType = ContentType.MOVIE,
    val isFeatured: Boolean = false,
    val isTrending: Boolean = false,
    val isTop10India: Boolean = false,
    val top10Rank: Int = 0,
    val isNewRelease: Boolean = false,
    val isLiveEvent: Boolean = false,
    val liveStatus: String = "LIVE", // e.g. "LIVE NOW", "STARTS 7:30 PM"
    val liveViewersCount: String = "1.2M watching",
    val seasons: List<Season> = emptyList(),
    val isKidsSafe: Boolean = false,
    val uploadSource: String = "Embed / VPS HLS" // "Embed Link" or "Direct VPS"
)

data class ContinueWatchingItem(
    val contentId: String,
    val currentPositionSeconds: Long,
    val durationSeconds: Long,
    val seasonNumber: Int = 1,
    val episodeNumber: Int = 1
) {
    val progressFraction: Float
        get() = if (durationSeconds > 0) (currentPositionSeconds.toFloat() / durationSeconds).coerceIn(0f, 1f) else 0f
}

data class UserProfile(
    val id: String,
    val name: String,
    val avatarColorHex: Long = 0xFFE50914,
    val avatarIconId: String = "cinema",
    val avatarEmoji: String = "",
    val isKids: Boolean = false,
    val parentalPin: String? = null,
    val preferredLanguage: String = "Hindi"
)

data class SubscriptionPlan(
    val id: String,
    val name: String,
    val priceInr: Int,
    val billingCycle: String = "month",
    val resolution: String = "1080p",
    val videoQuality: String = "Great",
    val maxScreens: Int = 2,
    val canDownload: Boolean = true,
    val isPopular: Boolean = false,
    val hasAds: Boolean = false,
    val features: List<String>
)

enum class DownloadStatus {
    QUEUED,
    DOWNLOADING,
    COMPLETED,
    PAUSED
}

data class DownloadItem(
    val id: String,
    val contentId: String,
    val title: String,
    val subtitle: String,
    val posterUrl: String,
    val quality: String = "1080p Full HD",
    val sizeMb: Int = 840,
    val progressPercent: Int = 100,
    val status: DownloadStatus = DownloadStatus.COMPLETED,
    val localUri: String = "local://storage/zxh4stream/video_cache.mp4"
)

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timeAgo: String,
    val isRead: Boolean = false,
    val deepLinkContentId: String? = null,
    val bannerUrl: String? = null
)

data class AdminUser(
    val email: String,
    val name: String,
    val role: String, // "SUPER_ADMIN", "CONTENT_MANAGER", "SUPPORT"
    val isBanned: Boolean = false,
    val joinedDate: String = "Sep 2025",
    val subscriptionTier: String = "Premium (Active)"
)

data class ZapUpiConfig(
    val merchantId: String = "ZAP_ZXH4_MERCHANT_LIVE",
    val apiKey: String = "zap_live_sec_99381837199201a9",
    val webhookUrl: String = "https://api.zxh4stream.com/v1/payments/zapupi/webhook",
    val merchantVpa: String = "zxh4stream@icici",
    val isTestMode: Boolean = false,
    val isEnabled: Boolean = false // Default OFF as requested
)

data class GoogleOAuthConfig(
    val clientId: String = "891823791823-zxh4stream.apps.googleusercontent.com",
    val clientSecret: String = "GOCSPX-zxh4_stream_secret_key_prod",
    val isEnabled: Boolean = false // Default OFF as requested
)

data class SmtpConfig(
    val host: String = "smtp.sendgrid.net",
    val port: Int = 587,
    val username: String = "apikey",
    val password: String = "SG.zxh4_smtp_token_example",
    val fromEmail: String = "noreply@zxh4stream.com",
    val isEnabled: Boolean = false // Default OFF as requested
)

data class VpsStorageConfig(
    val serverHost: String = "vps-stream-storage.zxh4stream.net",
    val storageBucket: String = "hls-vod-transcoded",
    val cdnUrl: String = "https://cdn.zxh4stream.com/vod",
    val ffmpegPresets: String = "480p, 720p, 1080p HLS auto",
    val isConnected: Boolean = true
)

data class AppConfig(
    val appName: String = "Zxh4Stream",
    val tagline: String = "Stream Beyond Limits",
    val maintenanceMode: Boolean = false,
    val forceUpdateMinVersion: String = "1.0.0",
    val allowCellularDownloads: Boolean = false,
    val maxKidsAge: Int = 12
)

data class PaymentTransaction(
    val transactionId: String,
    val userEmail: String,
    val planName: String,
    val amountInr: Int,
    val utrNumber: String,
    val status: String, // "SUCCESS", "PENDING", "REFUNDED"
    val timestamp: String,
    val gateway: String = "ZapUPI"
)

enum class AdFormat(val label: String) {
    PRE_ROLL("Pre-Roll Video"),
    MID_ROLL("Mid-Roll Video"),
    BANNER("Feed Banner"),
    REWARDED("Rewarded Video")
}

data class AdCampaign(
    val id: String,
    val title: String,
    val brandName: String,
    val description: String,
    val videoUrl: String = "",
    val imageUrl: String = "",
    val targetUrl: String = "https://zxh4stream.com/promo",
    val ctaText: String = "Learn More",
    val format: AdFormat = AdFormat.PRE_ROLL,
    val durationSeconds: Int = 15,
    val skipAfterSeconds: Int = 5,
    val impressionsCount: Int = 1420,
    val clicksCount: Int = 186,
    val isActive: Boolean = true,
    val category: String = "Electronics"
) {
    val ctrPercent: Float
        get() = if (impressionsCount > 0) (clicksCount.toFloat() / impressionsCount * 100f) else 0f
}

data class AdConfig(
    val adsEnabled: Boolean = true,
    val enablePreRoll: Boolean = true,
    val enableMidRoll: Boolean = true,
    val enableRewardedAds: Boolean = true,
    val enableBannerAds: Boolean = true,
    val midRollIntervalMinutes: Int = 10,
    val skipDelaySeconds: Int = 5,
    val adNetworkProvider: String = "Zxh4 Direct Ad Engine",
    val allowAdFreeTrial: Boolean = true,
    val estimatedRevenueInr: Int = 24850
)

