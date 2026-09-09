package com.example.data

object MockData {
    const val MASTER_ADMIN_EMAIL = "Robintyagi861az@gmail.com"
    const val MASTER_ADMIN_PASSWORD = "Robintyagi@83073##"
    const val MASTER_ADMIN_NAME = "Robin Tyagi (Super Admin)"

    val initialProfiles = listOf(
        UserProfile(
            id = "prof_robin",
            name = "Robin Tyagi",
            avatarColorHex = 0xFFE50914,
            avatarIconId = "crown",
            isKids = false,
            preferredLanguage = "Hindi"
        ),
        UserProfile(
            id = "prof_priya",
            name = "Priya",
            avatarColorHex = 0xFF9C27B0,
            avatarIconId = "sparkle",
            isKids = false,
            preferredLanguage = "English"
        ),
        UserProfile(
            id = "prof_chill",
            name = "Cinema Lounge",
            avatarColorHex = 0xFF007AFE,
            avatarIconId = "cinema",
            isKids = false,
            preferredLanguage = "Hindi"
        ),
        UserProfile(
            id = "prof_kids",
            name = "Kids World",
            avatarColorHex = 0xFF4CAF50,
            avatarIconId = "rocket",
            isKids = true,
            parentalPin = "1234",
            preferredLanguage = "Hindi"
        )
    )

    val sampleEpisodesSacred = listOf(
        Episode(
            id = "ep_sg_1",
            episodeNumber = 1,
            title = "1. Ashwathama Unleashed",
            description = "Sartaj Singh receives an eerie encrypted broadcast tracing deep under Mumbai's financial fortress.",
            durationMinutes = 54,
            thumbnail = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop&q=80",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        ),
        Episode(
            id = "ep_sg_2",
            episodeNumber = 2,
            title = "2. The Serpent of Dharavi",
            description = "Gaitonde’s secret audio memoirs reveal the blueprint of the syndicate that controls the state power grid.",
            durationMinutes = 58,
            thumbnail = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=600&auto=format&fit=crop&q=80",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
        ),
        Episode(
            id = "ep_sg_3",
            episodeNumber = 3,
            title = "3. Black Gold Protocol",
            description = "A massive maritime seizure in Arabian Sea leads RAW intelligence to an offshore AI command node.",
            durationMinutes = 51,
            thumbnail = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=600&auto=format&fit=crop&q=80",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
        ),
        Episode(
            id = "ep_sg_4",
            episodeNumber = 4,
            title = "4. Zero Hour Gambit",
            description = "With 12 hours remaining before lockdown, the team must decrypt the electromagnetic detonators.",
            durationMinutes = 62,
            thumbnail = "https://images.unsplash.com/photo-1574375927938-d5a98e8ffe85?w=600&auto=format&fit=crop&q=80",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"
        )
    )

    val sampleEpisodesCyber = listOf(
        Episode(
            id = "ep_cb_1",
            episodeNumber = 1,
            title = "1. Neon Monsoon",
            description = "In 2077 Mumbai, an enhanced neural mercenary is contracted to steal a quantum memory drive.",
            durationMinutes = 48,
            thumbnail = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600&auto=format&fit=crop&q=80",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4"
        ),
        Episode(
            id = "ep_cb_2",
            episodeNumber = 2,
            title = "2. Ghost in the Marine Drive",
            description = "Submerged server farms off the coast suffer a coordinated ransomware EMP assault.",
            durationMinutes = 52,
            thumbnail = "https://images.unsplash.com/photo-1514306191717-452ec28c7814?w=600&auto=format&fit=crop&q=80",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4"
        )
    )

    val initialContents = listOf(
        ContentItem(
            id = "c_jawan",
            title = "Jawan: Director's Cut",
            tagline = "Ready or Not, Justice Has Arrived",
            description = "A high-octane action thriller outlining the emotional journey of a prison warden committed to rectifying the wrongs in Indian society, honoring a promise made years ago.",
            posterUrl = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=800&auto=format&fit=crop&q=80",
            backdropUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=1200&auto=format&fit=crop&q=80",
            trailerUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            durationMinutes = 168,
            releaseYear = 2025,
            maturityRating = "U/A 16+",
            matchScorePercent = 99,
            genres = listOf("Action", "Vengeance", "Political Thriller"),
            tags = listOf("Blockbuster", "Explosive", "Adrenaline Rush"),
            cast = listOf("Shah Rukh Khan", "Nayanthara", "Vijay Sethupathi", "Deepika Padukone"),
            directors = listOf("Atlee"),
            isFeatured = true,
            isTrending = true,
            isTop10India = true,
            top10Rank = 1,
            isNewRelease = true,
            type = ContentType.MOVIE
        ),
        ContentItem(
            id = "c_sacred",
            title = "Sacred Games: The Reckoning",
            tagline = "25 Days to Save Mumbai",
            description = "A link in their pasts leads an honest cop to a fugitive gang boss whose cryptic warning spurs a quest to save Mumbai from cataclysm.",
            posterUrl = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=800&auto=format&fit=crop&q=80",
            backdropUrl = "https://images.unsplash.com/photo-1574375927938-d5a98e8ffe85?w=1200&auto=format&fit=crop&q=80",
            trailerUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            durationMinutes = 56,
            releaseYear = 2025,
            maturityRating = "A 18+",
            matchScorePercent = 97,
            genres = listOf("Crime Noir", "Suspense Thriller", "Drama"),
            tags = listOf("Gritty", "Dark", "Mind-Bending"),
            cast = listOf("Saif Ali Khan", "Nawazuddin Siddiqui", "Radhika Apte", "Pankaj Tripathi"),
            directors = listOf("Anurag Kashyap", "Vikramaditya Motwane"),
            isFeatured = true,
            isTrending = true,
            isTop10India = true,
            top10Rank = 2,
            type = ContentType.SERIES,
            seasons = listOf(
                Season(seasonNumber = 1, title = "Season 1: Kalchakra", episodes = sampleEpisodesSacred)
            )
        ),
        ContentItem(
            id = "c_icc_final",
            title = "ICC Men's T20 Championship: Final Live",
            tagline = "India vs Australia • 4K Ultra HD Ultra-Low Latency",
            description = "Experience the historic high-stakes cricket final with multi-camera perspective, real-time hawk-eye telemetry, English, Hindi and Bhojpuri audio commentary.",
            posterUrl = "https://images.unsplash.com/photo-1540747913346-19e32dc3e97e?w=800&auto=format&fit=crop&q=80",
            backdropUrl = "https://images.unsplash.com/photo-1508098682722-e99c43a406b2?w=1200&auto=format&fit=crop&q=80",
            trailerUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            durationMinutes = 240,
            releaseYear = 2026,
            maturityRating = "U",
            matchScorePercent = 100,
            genres = listOf("Live Sports", "Cricket", "Championship"),
            tags = listOf("LIVE NOW", "Stadium Sound", "Multi-Audio"),
            cast = listOf("Rohit Sharma", "Virat Kohli", "Jasprit Bumrah", "Travis Head"),
            isFeatured = true,
            isTrending = true,
            isTop10India = true,
            top10Rank = 3,
            isLiveEvent = true,
            liveStatus = "LIVE NOW",
            liveViewersCount = "4.8M watching",
            type = ContentType.LIVE_EVENT
        ),
        ContentItem(
            id = "c_kalki",
            title = "Kalki 2898 AD: The Epilogue",
            tagline = "The End of Kali Yuga",
            description = "In a post-apocalyptic world ruled by the Complex, an immortal warrior awaits the reincarnation of the tenth avatar to alter destiny.",
            posterUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800&auto=format&fit=crop&q=80",
            backdropUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=1200&auto=format&fit=crop&q=80",
            trailerUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            durationMinutes = 180,
            releaseYear = 2025,
            maturityRating = "U/A 13+",
            matchScorePercent = 96,
            genres = listOf("Sci-Fi Myth", "Action", "Fantasy"),
            tags = listOf("Visual Spectacle", "Epic Mythology", "CGI Landmark"),
            cast = listOf("Prabhas", "Amitabh Bachchan", "Kamal Haasan", "Deepika Padukone"),
            directors = listOf("Nag Ashwin"),
            isTrending = true,
            isTop10India = true,
            top10Rank = 4,
            type = ContentType.MOVIE
        ),
        ContentItem(
            id = "c_cybermumbai",
            title = "Cyberpunk: Neo Mumbai",
            tagline = "Breathe the Smog, Rule the Grid",
            description = "A street kid trying to survive in a technology and body modification-obsessed city of the future where neural syndicates wage clandestine corporate warfare.",
            posterUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=800&auto=format&fit=crop&q=80",
            backdropUrl = "https://images.unsplash.com/photo-1514306191717-452ec28c7814?w=1200&auto=format&fit=crop&q=80",
            trailerUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4",
            durationMinutes = 45,
            releaseYear = 2026,
            maturityRating = "A 18+",
            matchScorePercent = 94,
            genres = listOf("Cyberpunk", "Anime", "Sci-Fi"),
            tags = listOf("Futuristic", "Stylized", "Synthwave"),
            cast = listOf("Rajkummar Rao", "Sobhita Dhulipala", "Jim Sarbh"),
            isTrending = true,
            isTop10India = true,
            top10Rank = 5,
            type = ContentType.SERIES,
            seasons = listOf(
                Season(seasonNumber = 1, title = "Season 1: Digital Sins", episodes = sampleEpisodesCyber)
            )
        ),
        ContentItem(
            id = "c_chhota_bheem",
            title = "Chhota Bheem: Galactic Odyssey",
            tagline = "Laddoo Power Across the Stars",
            description = "Bheem and his friends from Dholakpur embark on an interstellar quest to save the alien kingdom of Zaphyr from shadow overlords.",
            posterUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=800&auto=format&fit=crop&q=80",
            backdropUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=1200&auto=format&fit=crop&q=80",
            trailerUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4",
            durationMinutes = 88,
            releaseYear = 2025,
            maturityRating = "U",
            matchScorePercent = 95,
            genres = listOf("Kids & Family", "Animation", "Adventure"),
            tags = listOf("Fun", "Kids Friendly", "Heroic"),
            cast = listOf("Sonal Kaushal", "Rupa Bhimani"),
            isKidsSafe = true,
            isTop10India = true,
            top10Rank = 6,
            type = ContentType.MOVIE
        ),
        ContentItem(
            id = "c_mirzapur",
            title = "Mirzapur: King of Purvanchal",
            tagline = "Bhaukaal Rahega",
            description = "The battle for supremacy in the blood-soaked terrain of Purvanchal escalates as new syndicates challenge the throne of Kaleen Bhaiya.",
            posterUrl = "https://images.unsplash.com/photo-1574375927938-d5a98e8ffe85?w=800&auto=format&fit=crop&q=80",
            backdropUrl = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=1200&auto=format&fit=crop&q=80",
            trailerUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            durationMinutes = 60,
            releaseYear = 2025,
            maturityRating = "A 18+",
            matchScorePercent = 98,
            genres = listOf("Crime", "Gangland", "Action"),
            tags = listOf("Ruthless", "Intense", "Dialect"),
            cast = listOf("Pankaj Tripathi", "Ali Fazal", "Shweta Tripathi"),
            isTrending = true,
            isTop10India = true,
            top10Rank = 7,
            type = ContentType.SERIES
        ),
        ContentItem(
            id = "c_leo",
            title = "Leo: Bloody Sweet Extended Cut",
            tagline = "A Peaceful Cafe Owner with an Assassin's Ghost",
            description = "Parthiban lives a quiet family life in Himachal Pradesh until a brutal cartel mistake drags his lethal past back into the open.",
            posterUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800&auto=format&fit=crop&q=80",
            backdropUrl = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=1200&auto=format&fit=crop&q=80",
            trailerUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            durationMinutes = 164,
            releaseYear = 2024,
            maturityRating = "A 18+",
            matchScorePercent = 92,
            genres = listOf("Action", "Martial Arts", "Thriller"),
            tags = listOf("Violent", "Cinematic Universe", "High Octane"),
            cast = listOf("Thalapathy Vijay", "Sanjay Dutt", "Trisha Krishnan"),
            directors = listOf("Lokesh Kanagaraj"),
            isTop10India = true,
            top10Rank = 8,
            type = ContentType.MOVIE
        ),
        ContentItem(
            id = "c_music_fest",
            title = "Zxh4Stream Sunburn Festival Live 4K",
            tagline = "Global Electronic Dance Music Spectacle",
            description = "Live concert broadcast featuring world-class DJs, immersive spatial sound, and interactive fan reactions.",
            posterUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=800&auto=format&fit=crop&q=80",
            backdropUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=1200&auto=format&fit=crop&q=80",
            trailerUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            durationMinutes = 300,
            releaseYear = 2026,
            maturityRating = "U/A 13+",
            matchScorePercent = 93,
            genres = listOf("Concert", "EDM", "Live Events"),
            tags = listOf("Party", "Bass Heavy", "Surround Sound"),
            isLiveEvent = true,
            liveStatus = "STARTS 8:00 PM",
            liveViewersCount = "340K RSVPs",
            type = ContentType.LIVE_EVENT
        )
    )

    val initialContinueWatching = listOf(
        ContinueWatchingItem(
            contentId = "c_jawan",
            currentPositionSeconds = 3420,
            durationSeconds = 10080
        ),
        ContinueWatchingItem(
            contentId = "c_sacred",
            currentPositionSeconds = 1840,
            durationSeconds = 3240,
            seasonNumber = 1,
            episodeNumber = 2
        )
    )

    val subscriptionPlans = listOf(
        SubscriptionPlan(
            id = "plan_free",
            name = "Free (Ad-Supported)",
            priceInr = 0,
            billingCycle = "free forever",
            resolution = "480p / 720p",
            videoQuality = "Standard",
            maxScreens = 1,
            canDownload = false,
            isPopular = false,
            hasAds = true,
            features = listOf(
                "Access to selected movies & shows",
                "Standard Definition streaming",
                "Watch on 1 phone or tablet",
                "Ad-supported pre-roll and mid-roll breaks",
                "Stereo standard audio"
            )
        ),
        SubscriptionPlan(
            id = "plan_basic",
            name = "Basic",
            priceInr = 199,
            billingCycle = "month",
            resolution = "720p HD",
            videoQuality = "Good",
            maxScreens = 1,
            canDownload = true,
            isPopular = false,
            hasAds = false,
            features = listOf(
                "720p High Definition video",
                "Watch on 1 phone, tablet, or laptop",
                "Ad-free unlimited streaming",
                "Download on 1 supported device",
                "Standard stereo audio"
            )
        ),
        SubscriptionPlan(
            id = "plan_standard",
            name = "Standard",
            priceInr = 499,
            billingCycle = "month",
            resolution = "1080p Full HD",
            videoQuality = "Great",
            maxScreens = 2,
            canDownload = true,
            isPopular = true,
            hasAds = false,
            features = listOf(
                "1080p Full HD crystal streaming",
                "Watch on 2 screens simultaneously",
                "Ad-free movies, series, and Live TV",
                "Download up to 100 titles on 2 devices",
                "Spatial Audio with Dolby Digital 5.1",
                "Access to all new cinematic releases"
            )
        ),
        SubscriptionPlan(
            id = "plan_premium",
            name = "Premium Ultra",
            priceInr = 649,
            billingCycle = "month",
            resolution = "4K Ultra HD + HDR",
            videoQuality = "Best Cinema",
            maxScreens = 4,
            canDownload = true,
            isPopular = false,
            hasAds = false,
            features = listOf(
                "4K Ultra HD + Dolby Vision HDR",
                "Watch on 4 screens simultaneously",
                "Dolby Atmos 3D Immersive Sound",
                "Download on 6 devices with offline limits relaxed",
                "VIP access to 4K Live Sports & Concerts",
                "Kids profile parental controls & pin lock"
            )
        )
    )

    val initialDownloads = listOf(
        DownloadItem(
            id = "dl_1",
            contentId = "c_jawan",
            title = "Jawan: Director's Cut",
            subtitle = "Movie • 1080p Full HD",
            posterUrl = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=600&auto=format&fit=crop&q=80",
            quality = "1080p Full HD",
            sizeMb = 1420,
            progressPercent = 100,
            status = DownloadStatus.COMPLETED
        ),
        DownloadItem(
            id = "dl_2",
            contentId = "c_sacred",
            title = "Sacred Games: The Reckoning",
            subtitle = "S1:E1 • Ashwathama Unleashed",
            posterUrl = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=600&auto=format&fit=crop&q=80",
            quality = "720p HD",
            sizeMb = 580,
            progressPercent = 100,
            status = DownloadStatus.COMPLETED
        )
    )

    val initialNotifications = listOf(
        NotificationItem(
            id = "notif_1",
            title = "ICC Men's T20 Final is LIVE NOW!",
            message = "India vs Australia in 4K Ultra HD with spatial audio. Tap to stream live now on Zxh4Stream.",
            timeAgo = "10 min ago",
            isRead = false,
            deepLinkContentId = "c_icc_final"
        ),
        NotificationItem(
            id = "notif_2",
            title = "New Release Added: Cyberpunk Neo Mumbai",
            message = "Season 1 is now available for streaming and offline downloads in Dolby Atmos.",
            timeAgo = "2 hours ago",
            isRead = false,
            deepLinkContentId = "c_cybermumbai"
        ),
        NotificationItem(
            id = "notif_3",
            title = "ZapUPI Payment Gateway Active",
            message = "Instant UPI checkout is enabled with zero transaction fees on all subscription plans.",
            timeAgo = "1 day ago",
            isRead = true
        )
    )

    val initialAdminUsers = listOf(
        AdminUser(
            email = MASTER_ADMIN_EMAIL,
            name = MASTER_ADMIN_NAME,
            role = "SUPER_ADMIN",
            isBanned = false,
            joinedDate = "Jan 2024",
            subscriptionTier = "Super Admin Lifetime"
        ),
        AdminUser(
            email = "aditya.sharma@example.com",
            name = "Aditya Sharma",
            role = "CONTENT_MANAGER",
            isBanned = false,
            joinedDate = "Jul 2024",
            subscriptionTier = "Staff Access"
        ),
        AdminUser(
            email = "neha.patel@example.com",
            name = "Neha Patel",
            role = "USER",
            isBanned = false,
            joinedDate = "Aug 2025",
            subscriptionTier = "Premium Ultra"
        ),
        AdminUser(
            email = "rohit.badactor@scam.com",
            name = "Rohit B (Banned)",
            role = "USER",
            isBanned = true,
            joinedDate = "Aug 2025",
            subscriptionTier = "Suspended"
        )
    )

    val initialTransactions = listOf(
        PaymentTransaction(
            transactionId = "TXN_ZAP_99841",
            userEmail = "neha.patel@example.com",
            planName = "Premium Ultra (₹649)",
            amountInr = 649,
            utrNumber = "UTR492819381273",
            status = "SUCCESS",
            timestamp = "Today 10:45 AM"
        ),
        PaymentTransaction(
            transactionId = "TXN_ZAP_99839",
            userEmail = "arjun.das@example.com",
            planName = "Standard (₹499)",
            amountInr = 499,
            utrNumber = "UTR492819380912",
            status = "SUCCESS",
            timestamp = "Yesterday 08:20 PM"
        ),
        PaymentTransaction(
            transactionId = "TXN_ZAP_99832",
            userEmail = "kavita.m@example.com",
            planName = "Basic (₹199)",
            amountInr = 199,
            utrNumber = "UTR492819379811",
            status = "REFUNDED",
            timestamp = "06 Sep 2025"
        )
    )

    val initialAdCampaigns = listOf(
        AdCampaign(
            id = "ad_oneplus",
            title = "OnePlus 13 5G Flagship",
            brandName = "OnePlus",
            description = "Snapdragon 8 Elite, 2K 120Hz Oriental Display & Hasselblad Master Camera System.",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            imageUrl = "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=800&auto=format&fit=crop&q=80",
            targetUrl = "https://oneplus.in",
            ctaText = "Buy Now",
            format = AdFormat.PRE_ROLL,
            durationSeconds = 15,
            skipAfterSeconds = 5,
            impressionsCount = 18450,
            clicksCount = 2120,
            isActive = true,
            category = "Smartphones"
        ),
        AdCampaign(
            id = "ad_zappay",
            title = "ZapUPI Quick Checkout",
            brandName = "ZapPay",
            description = "Zero wait time, instant 2-step UPI verification with up to ₹250 flat cashback on first recharge.",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            imageUrl = "https://images.unsplash.com/photo-1559526324-4b87b5e36e44?w=800&auto=format&fit=crop&q=80",
            targetUrl = "https://zappay.in",
            ctaText = "Claim ₹250",
            format = AdFormat.MID_ROLL,
            durationSeconds = 15,
            skipAfterSeconds = 5,
            impressionsCount = 14200,
            clicksCount = 1890,
            isActive = true,
            category = "FinTech"
        ),
        AdCampaign(
            id = "ad_boat",
            title = "boAt Nirvana Ion ANC",
            brandName = "boAt Lifestyle",
            description = "120 Hours Playback with 32dB Active Noise Cancellation and HiFi DSP audio drivers.",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
            imageUrl = "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800&auto=format&fit=crop&q=80",
            targetUrl = "https://boat-lifestyle.com",
            ctaText = "Get 60% Off",
            format = AdFormat.BANNER,
            durationSeconds = 10,
            skipAfterSeconds = 3,
            impressionsCount = 28900,
            clicksCount = 3740,
            isActive = true,
            category = "Audio"
        ),
        AdCampaign(
            id = "ad_swiggy",
            title = "Swiggy One - Free Delivery",
            brandName = "Swiggy",
            description = "Unlimited free delivery on restaurants and 10-minute grocery delivery across India.",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyBlazes.mp4",
            imageUrl = "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=800&auto=format&fit=crop&q=80",
            targetUrl = "https://swiggy.com",
            ctaText = "Order Now",
            format = AdFormat.BANNER,
            durationSeconds = 12,
            skipAfterSeconds = 5,
            impressionsCount = 22100,
            clicksCount = 2950,
            isActive = true,
            category = "Food & Delivery"
        ),
        AdCampaign(
            id = "ad_rewarded_ultra",
            title = "Unlock 4K Ultra Pass",
            brandName = "Zxh4 Rewards",
            description = "Watch this sponsored showcase to instantly unlock 1080p Ultra HD streaming for your next 2 hours!",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            imageUrl = "https://images.unsplash.com/photo-1478760329108-5c3ed9d495a0?w=800&auto=format&fit=crop&q=80",
            targetUrl = "https://zxh4stream.com/rewards",
            ctaText = "Watch to Unlock",
            format = AdFormat.REWARDED,
            durationSeconds = 15,
            skipAfterSeconds = 0,
            impressionsCount = 9420,
            clicksCount = 1430,
            isActive = true,
            category = "Rewards"
        )
    )
}
