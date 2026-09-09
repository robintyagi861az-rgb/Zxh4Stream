package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ContentItem
import com.example.data.Episode
import com.example.data.MockData
import com.example.data.StreamRepository
import com.example.ui.components.NavigationTab
import com.example.ui.components.ZxhBottomNav
import com.example.ui.components.ZxhTopBar
import com.example.ui.screens.AdminPanelScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.ContentDetailScreen
import com.example.ui.screens.DownloadsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MyListScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ProfileSelectorScreen
import com.example.ui.screens.TermsAndPrivacyScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.SubscriptionPlansScreen
import com.example.ui.screens.VideoPlayerScreen
import com.example.ui.theme.BackgroundBlack
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NetflixRed

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Zxh4StreamApp()
            }
        }
    }
}

@Composable
fun Zxh4StreamApp() {
    val isLoggedIn by StreamRepository.isLoggedIn.collectAsState()
    val activeProfile by StreamRepository.activeProfile.collectAsState()
    val userEmail by StreamRepository.currentUserEmail.collectAsState()
    val userRole by StreamRepository.currentUserRole.collectAsState()
    val notifications by StreamRepository.notifications.collectAsState()
    val appConfig by StreamRepository.appConfig.collectAsState()

    val unreadNotifsCount = notifications.count { !it.isRead }
    val isSuperAdmin = userRole == "SUPER_ADMIN" || userEmail.equals(MockData.MASTER_ADMIN_EMAIL, ignoreCase = true)

    // Navigation and screen states
    var currentTab by remember { mutableStateOf(NavigationTab.HOME) }
    var selectedDetailContent by remember { mutableStateOf<ContentItem?>(null) }
    var activePlayingContent by remember { mutableStateOf<ContentItem?>(null) }
    var activePlayingEpisode by remember { mutableStateOf<Episode?>(null) }

    var showProfileSelector by remember { mutableStateOf(false) }
    var showNotifications by remember { mutableStateOf(false) }
    var showSubscriptionPlans by remember { mutableStateOf(false) }
    var showAdminPanel by remember { mutableStateOf(false) }
    var showTermsAndPrivacy by remember { mutableStateOf(false) }
    var legalInitialTab by remember { mutableIntStateOf(0) }

    // Intercept back button for nested views
    BackHandler(
        enabled = activePlayingContent != null ||
                selectedDetailContent != null ||
                showTermsAndPrivacy ||
                showAdminPanel ||
                showSubscriptionPlans ||
                showNotifications ||
                showProfileSelector ||
                currentTab != NavigationTab.HOME
    ) {
        when {
            activePlayingContent != null -> {
                activePlayingContent = null
                activePlayingEpisode = null
            }
            selectedDetailContent != null -> selectedDetailContent = null
            showTermsAndPrivacy -> showTermsAndPrivacy = false
            showAdminPanel -> showAdminPanel = false
            showSubscriptionPlans -> showSubscriptionPlans = false
            showNotifications -> showNotifications = false
            showProfileSelector -> showProfileSelector = false
            currentTab != NavigationTab.HOME -> currentTab = NavigationTab.HOME
        }
    }

    if (!isLoggedIn) {
        AuthScreen(
            onAuthSuccess = {
                // Return to Home tab
                currentTab = NavigationTab.HOME
            }
        )
        return
    }

    // 1. Full Screen Video Player
    if (activePlayingContent != null) {
        VideoPlayerScreen(
            content = activePlayingContent!!,
            initialEpisode = activePlayingEpisode,
            onClose = {
                activePlayingContent = null
                activePlayingEpisode = null
            },
            onUpgradeToVip = {
                activePlayingContent = null
                activePlayingEpisode = null
                showSubscriptionPlans = true
            }
        )
        return
    }

    // 2. Master Admin Panel Screen
    if (showAdminPanel) {
        AdminPanelScreen(
            onBack = { showAdminPanel = false }
        )
        return
    }

    // 3. Subscription Plans (ZapUPI)
    if (showSubscriptionPlans) {
        SubscriptionPlansScreen(
            onBack = { showSubscriptionPlans = false }
        )
        return
    }

    // 4. Notifications Screen
    if (showNotifications) {
        NotificationsScreen(
            onBack = { showNotifications = false },
            onOpenContent = { item ->
                showNotifications = false
                selectedDetailContent = item
            }
        )
        return
    }

    // 5. Profile Selector ("Who's Watching?")
    if (showProfileSelector) {
        ProfileSelectorScreen(
            onProfileChosen = { showProfileSelector = false },
            onBack = { showProfileSelector = false }
        )
        return
    }

    // 5b. Terms & Privacy Screen
    if (showTermsAndPrivacy) {
        TermsAndPrivacyScreen(
            initialTab = legalInitialTab,
            onBack = { showTermsAndPrivacy = false }
        )
        return
    }

    // 6. Content Detail Screen
    if (selectedDetailContent != null) {
        ContentDetailScreen(
            content = selectedDetailContent!!,
            onBack = { selectedDetailContent = null },
            onPlay = { content, episode ->
                activePlayingContent = content
                activePlayingEpisode = episode
            },
            onNavigateDetail = { newContent ->
                selectedDetailContent = newContent
            }
        )
        return
    }

    // 7. Main Dashboard with Scaffold + ZxhTopBar + ZxhBottomNav
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BackgroundBlack,
        topBar = {
            Column {
                if (appConfig.maintenanceMode) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(NetflixRed)
                            .padding(vertical = 5.dp, horizontal = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "MAINTENANCE MODE ACTIVE: Upgrades in progress",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                ZxhTopBar(
                    activeProfile = activeProfile,
                    unreadNotificationsCount = unreadNotifsCount,
                    isSuperAdmin = isSuperAdmin,
                    onSearchClick = { currentTab = NavigationTab.SEARCH },
                    onNotificationsClick = { showNotifications = true },
                    onProfileClick = { showProfileSelector = true },
                    onAdminPanelClick = { showAdminPanel = true }
                )
            }
        },
        bottomBar = {
            ZxhBottomNav(
                currentTab = currentTab,
                onTabSelected = { tab ->
                    currentTab = tab
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(
                targetState = currentTab,
                animationSpec = tween(300),
                label = "tab_crossfade"
            ) { tab ->
                when (tab) {
                    NavigationTab.HOME -> {
                        HomeScreen(
                            onContentClick = { selectedDetailContent = it },
                            onPlayContent = { activePlayingContent = it },
                            onNavigateCategory = { cat ->
                                currentTab = NavigationTab.SEARCH
                            },
                            onGoAdFree = { showSubscriptionPlans = true }
                        )
                    }
                    NavigationTab.SEARCH -> {
                        SearchScreen(
                            onContentClick = { selectedDetailContent = it }
                        )
                    }
                    NavigationTab.MY_LIST -> {
                        MyListScreen(
                            onContentClick = { selectedDetailContent = it },
                            onPlayContent = { activePlayingContent = it },
                            onBrowseClick = { currentTab = NavigationTab.HOME }
                        )
                    }
                    NavigationTab.DOWNLOADS -> {
                        DownloadsScreen(
                            onPlayContent = { activePlayingContent = it },
                            onExploreClick = { currentTab = NavigationTab.HOME }
                        )
                    }
                    NavigationTab.PROFILE -> {
                        ProfileScreen(
                            onNavigatePlans = { showSubscriptionPlans = true },
                            onNavigateAdmin = { showAdminPanel = true },
                            onSwitchProfileClick = { showProfileSelector = true },
                            onNavigateTerms = {
                                legalInitialTab = 0
                                showTermsAndPrivacy = true
                            },
                            onNavigatePrivacy = {
                                legalInitialTab = 1
                                showTermsAndPrivacy = true
                            },
                            onLogoutClick = {
                                // Logout handled by StreamRepository
                            }
                        )
                    }
                }
            }
        }
    }
}
