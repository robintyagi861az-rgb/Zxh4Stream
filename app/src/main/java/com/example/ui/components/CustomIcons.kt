package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Games
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserProfile
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.NetflixRed

/**
 * Returns the appropriate custom vector icon for a profile avatar based on avatarIconId
 */
fun getProfileIconVector(iconId: String): ImageVector {
    return when (iconId.lowercase()) {
        "crown", "admin", "king" -> Icons.Default.WorkspacePremium
        "lotus", "flower", "sparkle" -> Icons.Default.Star
        "cinema", "movie", "film" -> Icons.Default.Movie
        "rocket", "kids", "space" -> Icons.Default.RocketLaunch
        "gamepad", "gamer" -> Icons.Default.Games
        "headphones", "music" -> Icons.Default.Headphones
        "popcorn", "theater" -> Icons.Default.LocalActivity
        "shield", "hero" -> Icons.Default.Shield
        else -> Icons.Default.Movie
    }
}

/**
 * Custom-crafted Profile Avatar view with zero emojis.
 * Features gradient background, custom vector icon, subtle border, and optional parental PIN lock.
 */
@Composable
fun CustomProfileAvatar(
    profile: UserProfile,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    isSelected: Boolean = false,
    showBorder: Boolean = true
) {
    val baseColor = Color(profile.avatarColorHex)
    val cornerRadius = (size.value * 0.22f).dp
    val iconSize = (size.value * 0.52f).dp

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        baseColor.copy(alpha = 0.95f),
                        baseColor.copy(alpha = 0.65f),
                        Color(0xFF141414)
                    )
                )
            )
            .then(
                if (showBorder) {
                    Modifier.border(
                        width = if (isSelected) 2.5.dp else 1.dp,
                        color = if (isSelected) NetflixRed else Color.White.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(cornerRadius)
                    )
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        // Inner custom vector icon
        val iconVector = getProfileIconVector(profile.avatarIconId)
        Icon(
            imageVector = iconVector,
            contentDescription = profile.name,
            tint = Color.White,
            modifier = Modifier.size(iconSize)
        )

        // Parental PIN indicator lock
        if (profile.parentalPin != null) {
            val pinBadgeSize = (size.value * 0.28f).coerceAtLeast(14f).dp
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(3.dp)
                    .size(pinBadgeSize)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "PIN Protected",
                    tint = NetflixRed,
                    modifier = Modifier.size(pinBadgeSize * 0.65f)
                )
            }
        }
    }
}

/**
 * Custom pulsing Live Broadcasting badge replacing emoji dots
 */
@Composable
fun CustomLiveBadge(
    modifier: Modifier = Modifier,
    viewersCount: String? = null
) {
    val infiniteTransition = rememberInfiniteTransition(label = "live_pulse")
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "live_alpha"
    )

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(NetflixRed.copy(alpha = 0.18f))
            .border(0.75.dp, NetflixRed.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
            .padding(horizontal = 7.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(modifier = Modifier.size(7.dp)) {
            drawCircle(
                color = NetflixRed.copy(alpha = alphaAnim),
                radius = size.minDimension / 2
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = if (viewersCount != null) "LIVE • $viewersCount" else "LIVE",
            color = NetflixRed,
            fontSize = 10.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 0.6.sp
        )
    }
}

/**
 * Custom Super Admin badge replacing crown emoji
 */
@Composable
fun SuperAdminBadge(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(NetflixRed, Color(0xFFB00610))
                )
            )
            .border(0.75.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Security,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(12.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "SUPER ADMIN",
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 0.5.sp
        )
    }
}

/**
 * Custom ZapUPI Payment Badge with Bolt vector icon
 */
@Composable
fun ZapUpiBadge(
    modifier: Modifier = Modifier,
    isLive: Boolean = true
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF1E1E1E))
            .border(0.75.dp, if (isLive) GreenSuccess.copy(alpha = 0.5f) else Color(0xFFFF9933).copy(alpha = 0.5f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Bolt,
            contentDescription = "ZapUPI",
            tint = if (isLive) GreenSuccess else Color(0xFFFF9933),
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = if (isLive) "ZapUPI LIVE" else "ZapUPI TEST",
            color = if (isLive) GreenSuccess else Color(0xFFFF9933),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Custom Trending Pill with Whatshot vector icon
 */
@Composable
fun TrendingPill(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(NetflixRed.copy(alpha = 0.15f))
            .border(0.5.dp, NetflixRed.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Whatshot,
            contentDescription = null,
            tint = NetflixRed,
            modifier = Modifier.size(11.dp)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = "TRENDING",
            color = NetflixRed,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Custom Featured Pill with Star vector icon
 */
@Composable
fun FeaturedPill(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(GreenSuccess.copy(alpha = 0.15f))
            .border(0.5.dp, GreenSuccess.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = GreenSuccess,
            modifier = Modifier.size(11.dp)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = "FEATURED",
            color = GreenSuccess,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Custom Top 10 India Badge replacing 🇮🇳 emoji
 */
@Composable
fun Top10IndiaPill(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0xFFFF9933).copy(alpha = 0.15f))
            .border(0.5.dp, Color(0xFFFF9933).copy(alpha = 0.5f), RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(modifier = Modifier.size(9.dp)) {
            // Stylized tricolor dot
            drawCircle(color = Color(0xFFFF9933), radius = size.minDimension / 2)
            drawCircle(color = Color.White, radius = size.minDimension / 3)
            drawCircle(color = Color(0xFF138808), radius = size.minDimension / 5)
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "TOP 10 IN",
            color = Color(0xFFFF9933),
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
