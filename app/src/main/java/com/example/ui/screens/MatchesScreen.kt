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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SportsSoccer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.data.model.LeagueMatchesGroup
import com.example.data.model.MatchDetailData
import com.example.data.model.MatchItem
import com.example.ui.theme.FoulBlackBg
import com.example.ui.theme.FoulCardDark
import com.example.ui.theme.FoulCardElevated
import com.example.ui.theme.FoulDivider
import com.example.ui.theme.FoulGoldStar
import com.example.ui.theme.FoulLiveRed
import com.example.ui.theme.FoulPurpleBright
import com.example.ui.theme.FoulPurpleDeep
import com.example.ui.theme.FoulPurpleGlow
import com.example.ui.theme.FoulTextSecondary
import com.example.ui.viewmodel.DateOption

@Composable
fun MatchesScreen(
    dateOptions: List<DateOption>,
    selectedDate: String,
    isLoading: Boolean,
    error: String?,
    leagueGroups: List<LeagueMatchesGroup>,
    selectedMatch: MatchItem?,
    isLoadingMatchDetail: Boolean,
    matchDetailError: String?,
    matchDetail: MatchDetailData?,
    onSelectDate: (String) -> Unit,
    onMatchClick: (MatchItem) -> Unit,
    onCloseMatchDetail: () -> Unit,
    onRefresh: () -> Unit
) {
    // If a match is selected, open the full-screen Match Detail Page
    if (selectedMatch != null) {
        MatchDetailScreen(
            match = selectedMatch,
            isLoading = isLoadingMatchDetail,
            error = matchDetailError,
            detail = matchDetail,
            onBack = onCloseMatchDetail,
            onRetry = { onMatchClick(selectedMatch) }
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FoulBlackBg)
    ) {
        // Top Header + Date Selector Strip
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(FoulPurpleDeep.copy(alpha = 0.65f), FoulBlackBg)
                    )
                )
                .padding(top = 12.dp, bottom = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(listOf(FoulPurpleBright, FoulPurpleDeep))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SportsSoccer,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "جدول المباريات",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "نتائج مباشرة وتفاصيل شاملة لحظة بلحظة",
                            style = MaterialTheme.typography.bodySmall,
                            color = FoulTextSecondary
                        )
                    }
                }

                IconButton(
                    onClick = onRefresh,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(FoulCardDark)
                        .border(1.dp, FoulDivider, CircleShape)
                        .testTag("matches_refresh_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "تحديث المباريات",
                        tint = FoulPurpleGlow
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Date Selector Strip
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(dateOptions, key = { it.apiDateString }) { opt ->
                    val selected = opt.apiDateString == selectedDate
                    Surface(
                        color = if (selected) FoulPurpleBright else FoulCardDark,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onSelectDate(opt.apiDateString) }
                            .border(
                                1.dp,
                                if (selected) FoulPurpleGlow else FoulDivider,
                                RoundedCornerShape(12.dp)
                            )
                            .testTag("date_chip_${opt.apiDateString}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = if (selected) Color.White else FoulPurpleGlow,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = opt.labelAr,
                                style = MaterialTheme.typography.labelLarge,
                                color = if (selected) Color.White else FoulTextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Matches List Grouped by League (UEFA CL -> La Liga -> Serie A -> Premier League -> Others)
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator(color = FoulPurpleBright)
                        Text(
                            text = "جاري جلب جدول المباريات...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = FoulTextSecondary
                        )
                    }
                }
            }

            error != null && leagueGroups.isEmpty() -> {
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
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Button(
                            onClick = onRefresh,
                            colors = ButtonDefaults.buttonColors(containerColor = FoulPurpleBright)
                        ) {
                            Text("إعادة المحاولة")
                        }
                    }
                }
            }

            leagueGroups.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "لا توجد مباريات مسجلة في هذا التاريخ",
                        style = MaterialTheme.typography.titleMedium,
                        color = FoulTextSecondary
                    )
                }
            }

            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("matches_lazy_column")
                ) {
                    items(leagueGroups, key = { it.competitionId }) { group ->
                        LeagueGroupCard(
                            group = group,
                            onMatchClick = onMatchClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LeagueGroupCard(
    group: LeagueMatchesGroup,
    onMatchClick: (MatchItem) -> Unit
) {
    val isTopPriorityLeague = group.priorityRank in 1..4
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = FoulCardDark),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isTopPriorityLeague) 1.5.dp else 1.dp,
                color = if (isTopPriorityLeague) FoulPurpleGlow.copy(alpha = 0.7f) else FoulDivider,
                shape = RoundedCornerShape(18.dp)
            )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // League Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (isTopPriorityLeague) {
                            Brush.horizontalGradient(
                                listOf(FoulPurpleDeep.copy(alpha = 0.85f), FoulCardElevated)
                            )
                        } else {
                            Brush.horizontalGradient(
                                listOf(FoulCardElevated, FoulCardDark)
                            )
                        }
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AsyncImage(
                        model = group.logoUrl,
                        contentDescription = group.competitionName,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.9f))
                            .padding(3.dp)
                    )
                    Text(
                        text = group.competitionName,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    color = FoulPurpleDeep,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "${group.matches.size} مباراة",
                        style = MaterialTheme.typography.bodySmall,
                        color = FoulPurpleGlow,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            // Matches inside this League
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                group.matches.forEach { match ->
                    MatchRowItem(
                        match = match,
                        onClick = { onMatchClick(match) }
                    )
                }
            }
        }
    }
}

@Composable
private fun MatchRowItem(
    match: MatchItem,
    onClick: () -> Unit
) {
    Surface(
        color = FoulCardElevated.copy(alpha = 0.7f),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .border(1.dp, FoulDivider.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
            .testTag("match_card_${match.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Home Team (Right in RTL)
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AsyncImage(
                    model = match.homeTeam.logoUrl,
                    contentDescription = match.homeTeam.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(32.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = match.homeTeam.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (match.homeTeam.redCards > 0) {
                        Text(
                            text = "بطاقة حمراء (${match.homeTeam.redCards})",
                            style = MaterialTheme.typography.bodySmall,
                            color = FoulLiveRed
                        )
                    }
                }
            }

            // Center Score / Kickoff Time / Live Minute
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                if (match.homeTeam.hasScore && match.awayTeam.hasScore) {
                    Surface(
                        color = if (match.isLive) FoulLiveRed.copy(alpha = 0.2f) else FoulPurpleDeep,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.border(
                            1.dp,
                            if (match.isLive) FoulLiveRed else FoulPurpleGlow.copy(alpha = 0.5f),
                            RoundedCornerShape(10.dp)
                        )
                    ) {
                        Text(
                            text = "${match.homeTeam.score}  -  ${match.awayTeam.score}",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text = if (match.isLive && match.gameTimeDisplay.isNotEmpty()) {
                            "مباشر ${match.gameTimeDisplay}"
                        } else {
                            match.statusText
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = if (match.isLive) FoulLiveRed else FoulGoldStar,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Surface(
                        color = FoulPurpleDeep.copy(alpha = 0.65f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = match.displayTime.ifBlank { match.statusText },
                            style = MaterialTheme.typography.labelLarge,
                            color = FoulPurpleGlow,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }

            // Away Team (Left in RTL)
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = match.awayTeam.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.End
                    )
                    if (match.awayTeam.redCards > 0) {
                        Text(
                            text = "بطاقة حمراء (${match.awayTeam.redCards})",
                            style = MaterialTheme.typography.bodySmall,
                            color = FoulLiveRed
                        )
                    }
                }
                Spacer(Modifier.width(8.dp))
                AsyncImage(
                    model = match.awayTeam.logoUrl,
                    contentDescription = match.awayTeam.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}
