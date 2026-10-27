package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.LineupPlayer
import com.example.data.model.MatchDetailData
import com.example.data.model.MatchEventItem
import com.example.data.model.MatchItem
import com.example.data.model.MatchStatComparison
import com.example.data.model.NewsArticleItem
import com.example.data.model.StandingRowItem
import com.example.data.model.TeamLineup
import com.example.ui.theme.FoulBlackBg
import com.example.ui.theme.FoulCardDark
import com.example.ui.theme.FoulCardElevated
import com.example.ui.theme.FoulDivider
import com.example.ui.theme.FoulGoldStar
import com.example.ui.theme.FoulLiveRed
import com.example.ui.theme.FoulPitchGreenDark
import com.example.ui.theme.FoulPitchGreenLight
import com.example.ui.theme.FoulPurpleBright
import com.example.ui.theme.FoulPurpleDeep
import com.example.ui.theme.FoulPurpleGlow
import com.example.ui.theme.FoulTextSecondary

private val MATCH_DETAIL_TABS = listOf(
    "الأحداث",     // 0: Events FIRST!
    "التشكيلة",    // 1: Real Pitch + Lineup + Player Photos
    "الإحصائيات",  // 2: Possession FIRST + Other Stats
    "الترتيب",     // 3: Full Standings with both teams & league colored
    "الأخبار"      // 4: Real News
)

@Composable
fun MatchDetailScreen(
    match: MatchItem,
    isLoading: Boolean,
    error: String?,
    detail: MatchDetailData?,
    onBack: () -> Unit,
    onRetry: () -> Unit
) {
    BackHandler { onBack() }

    // Default to Tab 0 ("الأحداث" - Events First!)
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FoulBlackBg)
    ) {
        // Scoreboard Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(FoulPurpleDeep, FoulCardDark, FoulBlackBg)
                    )
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Top Row: Back Arrow + League Badge + Refresh
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(FoulCardElevated)
                            .border(1.dp, FoulDivider, CircleShape)
                            .testTag("match_detail_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "رجوع",
                            tint = Color.White
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AsyncImage(
                            model = match.competitionLogoUrl,
                            contentDescription = match.competitionName,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .padding(2.dp)
                        )
                        Text(
                            text = match.competitionName,
                            style = MaterialTheme.typography.titleMedium,
                            color = FoulPurpleGlow,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(
                        onClick = onRetry,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(FoulCardElevated)
                            .border(1.dp, FoulDivider, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "تحديث تفاصيل المباراة",
                            tint = FoulPurpleGlow
                        )
                    }
                }

                // Teams & Score Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Home Team
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(62.dp)
                                .clip(CircleShape)
                                .background(FoulCardElevated)
                                .border(2.dp, FoulPurpleBright, CircleShape)
                                .padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = match.homeTeam.logoUrl,
                                contentDescription = match.homeTeam.name,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = match.homeTeam.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            maxLines = 2
                        )
                    }

                    // Score / Time Center Box
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    ) {
                        if (match.homeTeam.hasScore && match.awayTeam.hasScore) {
                            Text(
                                text = "${match.homeTeam.score}  :  ${match.awayTeam.score}",
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Text(
                                text = match.displayTime,
                                style = MaterialTheme.typography.headlineMedium,
                                color = FoulPurpleGlow,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(Modifier.height(4.dp))
                        Surface(
                            color = if (match.isLive) FoulLiveRed else FoulPurpleDeep,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = if (match.isLive && match.gameTimeDisplay.isNotEmpty()) {
                                    "مباشر • ${match.gameTimeDisplay}"
                                } else {
                                    match.statusText
                                },
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Away Team
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(62.dp)
                                .clip(CircleShape)
                                .background(FoulCardElevated)
                                .border(2.dp, Color(0xFF38BDF8), CircleShape)
                                .padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = match.awayTeam.logoUrl,
                                contentDescription = match.awayTeam.name,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = match.awayTeam.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            maxLines = 2
                        )
                    }
                }

                // Match Tabs Bar (الأحداث، التشكيلة، الإحصائيات، الترتيب، الأخبار)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(MATCH_DETAIL_TABS.size) { idx ->
                        val title = MATCH_DETAIL_TABS[idx]
                        val selected = selectedTabIndex == idx
                        Surface(
                            color = if (selected) FoulPurpleBright else FoulCardDark,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { selectedTabIndex = idx }
                                .border(
                                    1.dp,
                                    if (selected) FoulPurpleGlow else FoulDivider,
                                    RoundedCornerShape(12.dp)
                                )
                                .testTag("match_detail_tab_$idx")
                        ) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelLarge,
                                color = if (selected) Color.White else FoulTextSecondary,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp)
                            )
                        }
                    }
                }
            }
        }

        // Body Content based on Selected Tab
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CircularProgressIndicator(color = FoulPurpleBright)
                        Text(
                            text = "جاري جلب تفاصيل المباراة الحقيقية...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = FoulTextSecondary
                        )
                    }
                }
            }

            error != null && detail == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(text = error, color = Color.White)
                        Button(
                            onClick = onRetry,
                            colors = ButtonDefaults.buttonColors(containerColor = FoulPurpleBright)
                        ) {
                            Text("إعادة المحاولة")
                        }
                    }
                }
            }

            detail != null -> {
                when (selectedTabIndex) {
                    0 -> MatchEventsSection(
                        homeTeamName = match.homeTeam.name,
                        awayTeamName = match.awayTeam.name,
                        events = detail.events
                    )
                    1 -> MatchLineupsPitchSection(
                        homeLineup = detail.homeLineup,
                        awayLineup = detail.awayLineup
                    )
                    2 -> MatchStatisticsSection(
                        homeTeamName = match.homeTeam.name,
                        awayTeamName = match.awayTeam.name,
                        stats = detail.stats
                    )
                    3 -> MatchStandingsFullSection(
                        leagueName = detail.standingsLeagueName,
                        leagueLogoUrl = match.competitionLogoUrl,
                        homeTeamName = match.homeTeam.name,
                        awayTeamName = match.awayTeam.name,
                        rows = detail.standings
                    )
                    4 -> MatchNewsSection(news = detail.news)
                }
            }
        }
    }
}

/**
 * Tab 0: Events ("عرض الأحداث أول شيء بين الفريقين بشكل منفصل ليكون مرتب")
 * Displays Home team events on the Right column, Minute badge in the Center timeline,
 * and Away team events on the Left column.
 */
@Composable
private fun MatchEventsSection(
    homeTeamName: String,
    awayTeamName: String,
    events: List<MatchEventItem>
) {
    if (events.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "لا توجد أحداث مسجلة لهذه المباراة حتى الآن",
                style = MaterialTheme.typography.titleMedium,
                color = FoulTextSecondary
            )
        }
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
            .testTag("match_events_list")
    ) {
        // Column Headers for both teams separated
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FoulCardElevated),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "أحداث $homeTeamName",
                        style = MaterialTheme.typography.labelLarge,
                        color = FoulPurpleGlow,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "الدقيقة",
                        style = MaterialTheme.typography.labelMedium,
                        color = FoulTextSecondary
                    )
                    Text(
                        text = "أحداث $awayTeamName",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFF38BDF8),
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        items(events, key = { it.id }) { ev ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home Team Side (Right in RTL)
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (ev.isHome) {
                        TeamEventBubble(event = ev, isHome = true)
                    }
                }

                // Center Minute Timeline Node
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(FoulPurpleDeep)
                        .border(1.5.dp, FoulPurpleGlow, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = ev.minuteDisplay,
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Away Team Side (Left in RTL)
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    if (!ev.isHome) {
                        TeamEventBubble(event = ev, isHome = false)
                    }
                }
            }
        }
    }
}

@Composable
private fun TeamEventBubble(
    event: MatchEventItem,
    isHome: Boolean
) {
    val accentColor = when {
        event.typeName.contains("هدف") || event.typeId == 1 -> Color(0xFF22C55E)
        event.typeName.contains("حمراء") || event.typeId == 3 -> FoulLiveRed
        event.typeName.contains("صفراء") || event.typeId == 2 -> FoulGoldStar
        else -> FoulPurpleGlow
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = FoulCardDark),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isHome) FoulPurpleBright.copy(alpha = 0.5f) else Color(0xFF38BDF8).copy(alpha = 0.5f),
                RoundedCornerShape(14.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Surface(
                    color = accentColor.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = event.typeName.ifBlank { "حدث" },
                        style = MaterialTheme.typography.bodySmall,
                        color = accentColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            if (event.playerName.isNotBlank()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (event.playerPhotoUrl.isNotEmpty()) {
                        AsyncImage(
                            model = event.playerPhotoUrl,
                            contentDescription = event.playerName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(FoulCardElevated)
                        )
                    }
                    Text(
                        text = event.playerName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (event.secondaryPlayerName.isNotBlank()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = null,
                        tint = FoulTextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = event.secondaryPlayerName,
                        style = MaterialTheme.typography.bodySmall,
                        color = FoulTextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

/**
 * Tab 1: Lineups ("قسم التشكيلة بشكل ملعب مباراة حقيقي وفوقه التشكيلة وأسماء اللاعبين وصورة الاعبين
 * وتظهر الصور بشكل جلب حقيقي فعلي سريع وبشكل من قسم لكل فريق منتصف ليكون مرتب")
 */
@Composable
private fun MatchLineupsPitchSection(
    homeLineup: TeamLineup?,
    awayLineup: TeamLineup?
) {
    if (homeLineup == null && awayLineup == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "التشكيلة الرسمية غير متوفرة لهذه المباراة بعد",
                style = MaterialTheme.typography.titleMedium,
                color = FoulTextSecondary
            )
        }
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier
            .fillMaxSize()
            .testTag("match_lineups_pitch")
    ) {
        // Formation Header Banner above the Pitch ("وفوقه التشكيلة")
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FoulCardElevated),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, FoulPurpleGlow.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = homeLineup?.teamName.orEmpty(),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "الخطة: ${homeLineup?.formation ?: "-"}",
                            style = MaterialTheme.typography.labelLarge,
                            color = FoulPurpleGlow
                        )
                    }

                    Surface(
                        color = FoulPurpleDeep,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "ملعب المباراة والتشكيلة",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = awayLineup?.teamName.orEmpty(),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "الخطة: ${awayLineup?.formation ?: "-"}",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color(0xFF38BDF8)
                        )
                    }
                }
            }
        }

        // Real Football Pitch Graphic with Top Half = Home Team, Bottom Half = Away Team
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(740.dp)
                    .border(2.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
            ) {
                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    // Draw Authentic Football Stadium Pitch Stripes & Lines
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height
                        val stripeCount = 12
                        val stripeHeight = h / stripeCount
                        for (i in 0 until stripeCount) {
                            drawRect(
                                color = if (i % 2 == 0) FoulPitchGreenDark else FoulPitchGreenLight,
                                topLeft = Offset(0f, i * stripeHeight),
                                size = Size(w, stripeHeight)
                            )
                        }

                        val lineStroke = Stroke(width = 2.dp.toPx())
                        val lineColor = Color.White.copy(alpha = 0.55f)

                        // Outer boundary
                        val pad = 12.dp.toPx()
                        drawRect(
                            color = lineColor,
                            topLeft = Offset(pad, pad),
                            size = Size(w - pad * 2, h - pad * 2),
                            style = lineStroke
                        )

                        // Center halfway line (separating Home half & Away half)
                        drawLine(
                            color = lineColor,
                            start = Offset(pad, h / 2f),
                            end = Offset(w - pad, h / 2f),
                            strokeWidth = 2.dp.toPx()
                        )

                        // Center circle & spot
                        drawCircle(
                            color = lineColor,
                            radius = 48.dp.toPx(),
                            center = Offset(w / 2f, h / 2f),
                            style = lineStroke
                        )
                        drawCircle(
                            color = lineColor,
                            radius = 4.dp.toPx(),
                            center = Offset(w / 2f, h / 2f)
                        )

                        // Top Penalty Box (Home GK)
                        val boxW = w * 0.56f
                        val boxH = h * 0.14f
                        drawRect(
                            color = lineColor,
                            topLeft = Offset((w - boxW) / 2f, pad),
                            size = Size(boxW, boxH),
                            style = lineStroke
                        )
                        // Bottom Penalty Box (Away GK)
                        drawRect(
                            color = lineColor,
                            topLeft = Offset((w - boxW) / 2f, h - pad - boxH),
                            size = Size(boxW, boxH),
                            style = lineStroke
                        )
                    }

                    // Position Home Team Players in Top Half (0% .. 48% height), grouped by line
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Top Half: Home Team
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(vertical = 10.dp, horizontal = 8.dp)
                        ) {
                            TeamHalfPitchPlayers(
                                players = homeLineup?.startingPlayers.orEmpty(),
                                isTopHalf = true,
                                badgeColor = FoulPurpleBright
                            )
                        }

                        // Bottom Half: Away Team
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(vertical = 10.dp, horizontal = 8.dp)
                        ) {
                            TeamHalfPitchPlayers(
                                players = awayLineup?.startingPlayers.orEmpty(),
                                isTopHalf = false,
                                badgeColor = Color(0xFF0284C7)
                            )
                        }
                    }
                }
            }
        }

        // Substitutes Section for Both Teams
        if (!homeLineup?.substitutes.isNullOrEmpty() || !awayLineup?.substitutes.isNullOrEmpty()) {
            item {
                Text(
                    text = "مقاعد البدلاء",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = homeLineup?.teamName.orEmpty(),
                            style = MaterialTheme.typography.labelLarge,
                            color = FoulPurpleGlow
                        )
                        homeLineup?.substitutes.orEmpty().forEach { sub ->
                            SubPlayerCard(player = sub, accentColor = FoulPurpleBright)
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = awayLineup?.teamName.orEmpty(),
                            style = MaterialTheme.typography.labelLarge,
                            color = Color(0xFF38BDF8)
                        )
                        awayLineup?.substitutes.orEmpty().forEach { sub ->
                            SubPlayerCard(player = sub, accentColor = Color(0xFF0284C7))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TeamHalfPitchPlayers(
    players: List<LineupPlayer>,
    isTopHalf: Boolean,
    badgeColor: Color
) {
    if (players.isEmpty()) return

    // Group players by their tactical line (GK = 0/1, DEF, MID, FWD)
    val groupedByLine = players.groupBy { it.yardLine }
        .toSortedMap(if (isTopHalf) naturalOrder() else reverseOrder())

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        groupedByLine.values.forEach { linePlayers ->
            val orderedLine = linePlayers.sortedBy { it.fieldSide }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                orderedLine.forEach { player ->
                    PitchPlayerNode(
                        player = player,
                        badgeColor = badgeColor
                    )
                }
            }
        }
    }
}

@Composable
private fun PitchPlayerNode(
    player: LineupPlayer,
    badgeColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(68.dp)
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            // Real Player Photo Circle
            AsyncImage(
                model = player.photoUrl,
                contentDescription = player.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(FoulCardElevated)
                    .border(1.5.dp, Color.White, CircleShape)
            )

            // Jersey Number Badge
            if (player.jerseyNumber > 0) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(badgeColor)
                        .border(1.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${player.jerseyNumber}",
                        fontSize = 9.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(Modifier.height(2.dp))

        Surface(
            color = FoulBlackBg.copy(alpha = 0.78f),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(
                text = player.shortName,
                fontSize = 10.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
            )
        }
    }
}

@Composable
private fun SubPlayerCard(
    player: LineupPlayer,
    accentColor: Color
) {
    Surface(
        color = FoulCardDark,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, FoulDivider, RoundedCornerShape(12.dp))
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AsyncImage(
                model = player.photoUrl,
                contentDescription = player.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(FoulCardElevated)
                    .border(1.dp, accentColor, CircleShape)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = player.shortName,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = if (player.jerseyNumber > 0) "#${player.jerseyNumber} • ${player.positionName}" else player.positionName,
                    style = MaterialTheme.typography.bodySmall,
                    color = FoulTextSecondary,
                    maxLines = 1
                )
            }
        }
    }
}

/**
 * Tab 2: Statistics ("قسم الإحصائيات وجلب الاستواذ أول شيء وبقية الاحصائيات")
 */
@Composable
private fun MatchStatisticsSection(
    homeTeamName: String,
    awayTeamName: String,
    stats: List<MatchStatComparison>
) {
    if (stats.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "لا تتوفر إحصائيات مسجلة لهذه المباراة حالياً",
                style = MaterialTheme.typography.titleMedium,
                color = FoulTextSecondary
            )
        }
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .testTag("match_stats_list")
    ) {
        items(stats, key = { it.statId }) { stat ->
            if (stat.isPossession) {
                // Hero Possession Card FIRST!
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = FoulCardElevated),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.5.dp, FoulPurpleGlow, RoundedCornerShape(18.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "نسبة الاستحواذ على الكرة",
                            style = MaterialTheme.typography.titleMedium,
                            color = FoulPurpleGlow,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = homeTeamName,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = FoulTextSecondary
                                )
                                Text(
                                    text = stat.homeValue,
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = FoulPurpleGlow,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = awayTeamName,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = FoulTextSecondary
                                )
                                Text(
                                    text = stat.awayValue,
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = Color(0xFF38BDF8),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Dual Possession Bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(14.dp)
                                .clip(RoundedCornerShape(8.dp))
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(stat.homeRatio.coerceAtLeast(0.05f))
                                    .fillMaxHeight()
                                    .background(FoulPurpleBright)
                            )
                            Spacer(Modifier.width(3.dp))
                            Box(
                                modifier = Modifier
                                    .weight(stat.awayRatio.coerceAtLeast(0.05f))
                                    .fillMaxHeight()
                                    .background(Color(0xFF0284C7))
                            )
                        }
                    }
                }
            } else {
                // Standard Stat Row
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = FoulCardDark),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, FoulDivider, RoundedCornerShape(14.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stat.homeValue,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stat.name,
                                style = MaterialTheme.typography.bodyMedium,
                                color = FoulTextSecondary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = stat.awayValue,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(stat.homeRatio.coerceAtLeast(0.05f))
                                    .fillMaxHeight()
                                    .background(FoulPurpleBright)
                            )
                            Spacer(Modifier.width(2.dp))
                            Box(
                                modifier = Modifier
                                    .weight(stat.awayRatio.coerceAtLeast(0.05f))
                                    .fillMaxHeight()
                                    .background(Color(0xFF0284C7))
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Tab 3: Standings ("قسم الترتيب حسب المباراة الداخل عليها وبشكل لون للفريقين إلي داخل عليهم والدوري
 * وأيضا جعل تصنيف بشكل صفحة كاملة وأنيقة جدا")
 */
@Composable
private fun MatchStandingsFullSection(
    leagueName: String,
    leagueLogoUrl: String,
    homeTeamName: String,
    awayTeamName: String,
    rows: List<StandingRowItem>
) {
    if (rows.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "جدول الترتيب غير متاح لهذه البطولة حالياً",
                style = MaterialTheme.typography.titleMedium,
                color = FoulTextSecondary
            )
        }
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .testTag("match_standings_table")
    ) {
        // League Banner & Color Key for Both Match Teams
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = FoulCardElevated),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, FoulPurpleGlow.copy(alpha = 0.7f), RoundedCornerShape(18.dp))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        AsyncImage(
                            model = leagueLogoUrl,
                            contentDescription = leagueName,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .padding(4.dp)
                        )
                        Column {
                            Text(
                                text = "جدول ترتيب $leagueName",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "تصنيف كامل مع تمييز لوني لفريقي المباراة",
                                style = MaterialTheme.typography.bodySmall,
                                color = FoulPurpleGlow
                            )
                        }
                    }

                    // Legend showing the distinct colors for Home & Away teams
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(FoulPurpleBright)
                            )
                            Text(
                                text = homeTeamName,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0284C7))
                            )
                            Text(
                                text = awayTeamName,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // Table Column Headers
        item {
            Surface(
                color = FoulPurpleDeep.copy(alpha = 0.75f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("#", Modifier.width(28.dp), color = FoulPurpleGlow, fontWeight = FontWeight.Bold)
                    Text("الفريق", Modifier.weight(1f), color = FoulPurpleGlow, fontWeight = FontWeight.Bold)
                    Text("لعب", Modifier.width(34.dp), color = FoulPurpleGlow, textAlign = TextAlign.Center)
                    Text("ف", Modifier.width(28.dp), color = FoulPurpleGlow, textAlign = TextAlign.Center)
                    Text("ت", Modifier.width(28.dp), color = FoulPurpleGlow, textAlign = TextAlign.Center)
                    Text("خ", Modifier.width(28.dp), color = FoulPurpleGlow, textAlign = TextAlign.Center)
                    Text("+/-", Modifier.width(38.dp), color = FoulPurpleGlow, textAlign = TextAlign.Center)
                    Text("نقاط", Modifier.width(42.dp), color = Color.White, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                }
            }
        }

        // Standings Rows with Distinct Highlighting for Home & Away Match Teams
        items(rows, key = { it.competitorId }) { row ->
            val bgColor = when {
                row.isHomeMatchTeam -> FoulPurpleDeep.copy(alpha = 0.9f)
                row.isAwayMatchTeam -> Color(0xFF0C4A6E).copy(alpha = 0.9f)
                else -> FoulCardDark
            }
            val borderColor = when {
                row.isHomeMatchTeam -> FoulPurpleGlow
                row.isAwayMatchTeam -> Color(0xFF38BDF8)
                else -> FoulDivider
            }

            Surface(
                color = bgColor,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = if (row.isHomeMatchTeam || row.isAwayMatchTeam) 1.5.dp else 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${row.position}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (row.position <= 4) FoulGoldStar else Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.width(28.dp)
                    )

                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AsyncImage(
                            model = row.logoUrl,
                            contentDescription = row.teamName,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = row.teamName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            fontWeight = if (row.isHomeMatchTeam || row.isAwayMatchTeam) FontWeight.Bold else FontWeight.Normal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Text("${row.played}", Modifier.width(34.dp), color = FoulTextSecondary, textAlign = TextAlign.Center)
                    Text("${row.won}", Modifier.width(28.dp), color = FoulTextSecondary, textAlign = TextAlign.Center)
                    Text("${row.drawn}", Modifier.width(28.dp), color = FoulTextSecondary, textAlign = TextAlign.Center)
                    Text("${row.lost}", Modifier.width(28.dp), color = FoulTextSecondary, textAlign = TextAlign.Center)
                    Text(
                        text = if (row.goalDiff > 0) "+${row.goalDiff}" else "${row.goalDiff}",
                        modifier = Modifier.width(38.dp),
                        color = FoulTextSecondary,
                        textAlign = TextAlign.Center
                    )
                    Surface(
                        color = if (row.isHomeMatchTeam) FoulPurpleBright else if (row.isAwayMatchTeam) Color(0xFF0284C7) else FoulCardElevated,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.width(42.dp)
                    ) {
                        Text(
                            text = "${row.points}",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Tab 4: News ("وقسم الأخبار أيضا بشكل جلب الحقيقي")
 */
@Composable
private fun MatchNewsSection(news: List<NewsArticleItem>) {
    val context = LocalContext.current

    if (news.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "لا توجد أخبار متاحة حالياً",
                style = MaterialTheme.typography.titleMedium,
                color = FoulTextSecondary
            )
        }
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .testTag("match_news_list")
    ) {
        items(news, key = { it.id }) { item ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FoulCardDark),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        if (item.url.isNotBlank()) {
                            try {
                                context.startActivity(
                                    Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
                                )
                            } catch (_: Exception) {
                            }
                        }
                    }
                    .border(1.dp, FoulDivider, RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (item.imageUrl.isNotBlank()) {
                        AsyncImage(
                            model = item.imageUrl,
                            contentDescription = item.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(86.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(FoulCardElevated)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(86.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(FoulPurpleDeep),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Newspaper,
                                contentDescription = null,
                                tint = FoulPurpleGlow,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = item.formattedDate,
                                style = MaterialTheme.typography.bodySmall,
                                color = FoulPurpleGlow
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                contentDescription = "قراءة الخبر",
                                tint = FoulTextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
