package com.example.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

object StreamRepository {
    private val scope = CoroutineScope(Dispatchers.Default)

    // User authentication state
    private val _isLoggedIn = MutableStateFlow(true)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentUserEmail = MutableStateFlow(MockData.MASTER_ADMIN_EMAIL)
    val currentUserEmail: StateFlow<String> = _currentUserEmail.asStateFlow()

    private val _currentUserName = MutableStateFlow(MockData.MASTER_ADMIN_NAME)
    val currentUserName: StateFlow<String> = _currentUserName.asStateFlow()

    private val _currentUserRole = MutableStateFlow("SUPER_ADMIN")
    val currentUserRole: StateFlow<String> = _currentUserRole.asStateFlow()

    // Active Profile
    private val _profiles = MutableStateFlow(MockData.initialProfiles)
    val profiles: StateFlow<List<UserProfile>> = _profiles.asStateFlow()

    private val _activeProfile = MutableStateFlow(MockData.initialProfiles.first())
    val activeProfile: StateFlow<UserProfile> = _activeProfile.asStateFlow()

    // Content catalog
    private val _contents = MutableStateFlow(MockData.initialContents)
    val contents: StateFlow<List<ContentItem>> = _contents.asStateFlow()

    // Continue Watching
    private val _continueWatching = MutableStateFlow(MockData.initialContinueWatching)
    val continueWatching: StateFlow<List<ContinueWatchingItem>> = _continueWatching.asStateFlow()

    // My List (Saved Content IDs)
    private val _myList = MutableStateFlow(setOf("c_jawan", "c_sacred", "c_icc_final"))
    val myList: StateFlow<Set<String>> = _myList.asStateFlow()

    // Downloads
    private val _downloads = MutableStateFlow(MockData.initialDownloads)
    val downloads: StateFlow<List<DownloadItem>> = _downloads.asStateFlow()

    // Notifications
    private val _notifications = MutableStateFlow(MockData.initialNotifications)
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    // Subscription & Plans
    private val _subscriptionPlans = MutableStateFlow(MockData.subscriptionPlans)
    val subscriptionPlans: StateFlow<List<SubscriptionPlan>> = _subscriptionPlans.asStateFlow()

    private val _currentPlan = MutableStateFlow(MockData.subscriptionPlans[2]) // Premium default for Super Admin
    val currentPlan: StateFlow<SubscriptionPlan> = _currentPlan.asStateFlow()

    // Admin & Integrations Configuration
    private val _zapUpiConfig = MutableStateFlow(ZapUpiConfig())
    val zapUpiConfig: StateFlow<ZapUpiConfig> = _zapUpiConfig.asStateFlow()

    private val _googleOAuthConfig = MutableStateFlow(GoogleOAuthConfig())
    val googleOAuthConfig: StateFlow<GoogleOAuthConfig> = _googleOAuthConfig.asStateFlow()

    private val _smtpConfig = MutableStateFlow(SmtpConfig())
    val smtpConfig: StateFlow<SmtpConfig> = _smtpConfig.asStateFlow()

    private val _vpsStorageConfig = MutableStateFlow(VpsStorageConfig())
    val vpsStorageConfig: StateFlow<VpsStorageConfig> = _vpsStorageConfig.asStateFlow()

    private val _appConfig = MutableStateFlow(AppConfig())
    val appConfig: StateFlow<AppConfig> = _appConfig.asStateFlow()

    private val _adminUsers = MutableStateFlow(MockData.initialAdminUsers)
    val adminUsers: StateFlow<List<AdminUser>> = _adminUsers.asStateFlow()

    private val _transactions = MutableStateFlow(MockData.initialTransactions)
    val transactions: StateFlow<List<PaymentTransaction>> = _transactions.asStateFlow()

    // Auth actions
    fun login(email: String, pass: String): Result<String> {
        val trimmedEmail = email.trim()
        if (trimmedEmail.equals(MockData.MASTER_ADMIN_EMAIL, ignoreCase = true)) {
            if (pass == MockData.MASTER_ADMIN_PASSWORD) {
                _currentUserEmail.value = MockData.MASTER_ADMIN_EMAIL
                _currentUserName.value = MockData.MASTER_ADMIN_NAME
                _currentUserRole.value = "SUPER_ADMIN"
                _isLoggedIn.value = true
                return Result.success("Super Admin Authenticated successfully")
            } else {
                return Result.failure(Exception("Invalid Super Admin password"))
            }
        }
        if (trimmedEmail.isNotEmpty() && pass.length >= 6) {
            _currentUserEmail.value = trimmedEmail
            _currentUserName.value = trimmedEmail.substringBefore("@").replace(".", " ").replaceFirstChar { it.uppercase() }
            _currentUserRole.value = "USER"
            _isLoggedIn.value = true
            return Result.success("Logged in successfully")
        }
        return Result.failure(Exception("Please enter a valid email and minimum 6-character password"))
    }

    fun loginWithPhoneOtp(phoneNumber: String, otp: String): Result<String> {
        if (phoneNumber.length >= 10 && otp == "8307") {
            _currentUserEmail.value = "+91 $phoneNumber"
            _currentUserName.value = "Subscriber ${phoneNumber.takeLast(4)}"
            _currentUserRole.value = "USER"
            _isLoggedIn.value = true
            return Result.success("Phone verified successfully")
        }
        return Result.failure(Exception("Invalid OTP. Use test OTP: 8307"))
    }

    fun loginWithGoogle(): Result<String> {
        // As per requirements: Google OAuth is configurable from admin
        _currentUserEmail.value = "google.user@zxh4stream.com"
        _currentUserName.value = "Google Streamer"
        _currentUserRole.value = "USER"
        _isLoggedIn.value = true
        return Result.success("Google OAuth authorized")
    }

    fun logout() {
        _isLoggedIn.value = false
        _currentUserEmail.value = ""
        _currentUserRole.value = "GUEST"
    }

    // Profile actions
    fun selectProfile(profile: UserProfile) {
        _activeProfile.value = profile
    }

    fun addProfile(name: String, isKids: Boolean, pin: String?): Boolean {
        if (name.isBlank()) return false
        val newProfile = UserProfile(
            id = "prof_${UUID.randomUUID().toString().take(6)}",
            name = name.trim(),
            avatarColorHex = if (isKids) 0xFF4CAF50 else 0xFFE50914,
            avatarIconId = if (isKids) "rocket" else "cinema",
            isKids = isKids,
            parentalPin = if (isKids) pin else null
        )
        _profiles.update { it + newProfile }
        return true
    }

    // My List & Watch History
    fun toggleMyList(contentId: String) {
        _myList.update { set ->
            if (set.contains(contentId)) set - contentId else set + contentId
        }
    }

    fun updateContinueWatching(contentId: String, currentSeconds: Long, totalSeconds: Long, sNum: Int = 1, epNum: Int = 1) {
        _continueWatching.update { list ->
            val existing = list.indexOfFirst { it.contentId == contentId }
            val item = ContinueWatchingItem(contentId, currentSeconds, totalSeconds, sNum, epNum)
            if (existing >= 0) {
                list.toMutableList().apply { set(existing, item) }
            } else {
                listOf(item) + list
            }
        }
    }

    fun removeFromContinueWatching(contentId: String) {
        _continueWatching.update { list -> list.filterNot { it.contentId == contentId } }
    }

    // Downloads
    fun startDownload(content: ContentItem, quality: String = "1080p Full HD", episodeTitle: String? = null) {
        val dlId = "dl_${UUID.randomUUID().toString().take(6)}"
        val size = when (quality) {
            "4K Ultra HD (HDR)" -> 4200
            "1080p Full HD" -> 1400
            "720p HD" -> 680
            else -> 420
        }
        val item = DownloadItem(
            id = dlId,
            contentId = content.id,
            title = content.title,
            subtitle = episodeTitle ?: "${content.releaseYear} • $quality",
            posterUrl = content.posterUrl,
            quality = quality,
            sizeMb = size,
            progressPercent = 10,
            status = DownloadStatus.DOWNLOADING
        )
        _downloads.update { listOf(item) + it }

        // Simulate asynchronous download progress
        scope.launch {
            for (p in 25..100 step 25) {
                delay(600)
                _downloads.update { list ->
                    list.map {
                        if (it.id == dlId) {
                            it.copy(
                                progressPercent = p,
                                status = if (p == 100) DownloadStatus.COMPLETED else DownloadStatus.DOWNLOADING
                            )
                        } else it
                    }
                }
            }
        }
    }

    fun deleteDownload(downloadId: String) {
        _downloads.update { list -> list.filterNot { it.id == downloadId } }
    }

    // Notifications
    fun markNotificationAsRead(id: String) {
        _notifications.update { list ->
            list.map { if (it.id == id) it.copy(isRead = true) else it }
        }
    }

    fun markAllNotificationsAsRead() {
        _notifications.update { list -> list.map { it.copy(isRead = true) } }
    }

    // ZapUPI Payment simulation with instant webhook verification
    fun processZapUpiPayment(plan: SubscriptionPlan, vpaId: String, onCompleted: (Boolean, String) -> Unit) {
        scope.launch {
            val txnId = "TXN_ZAP_${System.currentTimeMillis().toString().takeLast(6)}"
            val utr = "UTR${(100000000000L..999999999999L).random()}"
            delay(1500) // Simulating network & UPI authorization

            val txn = PaymentTransaction(
                transactionId = txnId,
                userEmail = _currentUserEmail.value,
                planName = "${plan.name} (₹${plan.priceInr})",
                amountInr = plan.priceInr,
                utrNumber = utr,
                status = "SUCCESS",
                timestamp = "Just Now",
                gateway = "ZapUPI"
            )
            _transactions.update { listOf(txn) + it }
            _currentPlan.value = plan

            // Add confirmation notification
            val notif = NotificationItem(
                id = "notif_${UUID.randomUUID().toString().take(6)}",
                title = "Plan Activated: ${plan.name}",
                message = "Your ZapUPI payment of ₹${plan.priceInr} was verified via webhook (UTR: $utr). Stream beyond limits!",
                timeAgo = "Just now",
                isRead = false
            )
            _notifications.update { listOf(notif) + it }

            onCompleted(true, "Payment Confirmed via ZapUPI Webhook! Plan upgraded to ${plan.name}.")
        }
    }

    // Admin Content Management
    fun adminAddContent(item: ContentItem) {
        _contents.update { listOf(item) + it }
    }

    fun adminUpdateContent(item: ContentItem) {
        _contents.update { list -> list.map { if (it.id == item.id) item else it } }
    }

    fun adminDeleteContent(id: String) {
        _contents.update { list -> list.filterNot { it.id == id } }
    }

    // Admin Users
    fun adminToggleBanUser(email: String) {
        _adminUsers.update { list ->
            list.map {
                if (it.email == email && it.role != "SUPER_ADMIN") {
                    it.copy(isBanned = !it.isBanned)
                } else it
            }
        }
    }

    // Admin Configurations
    fun adminUpdateZapUpiConfig(config: ZapUpiConfig) {
        _zapUpiConfig.value = config
    }

    fun adminUpdateGoogleOAuthConfig(config: GoogleOAuthConfig) {
        _googleOAuthConfig.value = config
    }

    fun adminUpdateSmtpConfig(config: SmtpConfig) {
        _smtpConfig.value = config
    }

    fun adminUpdateVpsConfig(config: VpsStorageConfig) {
        _vpsStorageConfig.value = config
    }

    fun adminUpdateAppConfig(config: AppConfig) {
        _appConfig.value = config
    }

    fun adminBroadcastNotification(title: String, message: String, targetContentId: String?) {
        val notif = NotificationItem(
            id = "notif_${UUID.randomUUID().toString().take(6)}",
            title = title,
            message = message,
            timeAgo = "Just now",
            isRead = false,
            deepLinkContentId = targetContentId
        )
        _notifications.update { listOf(notif) + it }
    }

    fun adminIssueRefund(transactionId: String) {
        _transactions.update { list ->
            list.map {
                if (it.transactionId == transactionId) it.copy(status = "REFUNDED") else it
            }
        }
    }
}
