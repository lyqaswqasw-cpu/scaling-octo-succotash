package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.data.model.LibraryFilterType
import com.example.data.model.LibraryMediaItem
import com.example.data.model.MediaDetailInfo
import com.example.data.model.SeriesEpisode
import com.example.ui.theme.FoulBlackBg
import com.example.ui.theme.FoulCardDark
import com.example.ui.theme.FoulCardElevated
import com.example.ui.theme.FoulDivider
import com.example.ui.theme.FoulGoldStar
import com.example.ui.theme.FoulPurpleBright
import com.example.ui.theme.FoulPurpleDeep
import com.example.ui.theme.FoulPurpleGlow
import com.example.ui.theme.FoulTextSecondary

@Composable
fun LibraryScreen(
    isLoading: Boolean,
    error: String?,
    mainFilter: LibraryFilterType,
    items: List<LibraryMediaItem>,
    hasMore: Boolean,
    isSearchOpen: Boolean,
    searchQuery: String,
    searchFilter: LibraryFilterType,
    searchResults: List<LibraryMediaItem>,
    isSearching: Boolean,
    selectedMediaItem: LibraryMediaItem?,
    isLoadingDetail: Boolean,
    detailError: String?,
    mediaDetail: MediaDetailInfo?,
    selectedSeasonNumber: Int,
    onSelectMainFilter: (LibraryFilterType) -> Unit,
    onLoadMore: () -> Unit,
    onOpenSearch: () -> Unit,
    onCloseSearch: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSearchFilterChange: (LibraryFilterType) -> Unit,
    onItemClick: (LibraryMediaItem) -> Unit,
    onCloseDetail: () -> Unit,
    onSelectSeason: (Int) -> Unit,
    onPlayMovie: (LibraryMediaItem) -> Unit,
    onPlayEpisode: (LibraryMediaItem, SeriesEpisode) -> Unit,
    onRetry: () -> Unit
) {
    // 1. If a Movie or Series Detail page is open, show full detail view
    if (selectedMediaItem != null) {
        LibraryMediaDetailScreen(
            item = selectedMediaItem,
            isLoading = isLoadingDetail,
            error = detailError,
            detail = mediaDetail,
            selectedSeasonNumber = selectedSeasonNumber,
            onSelectSeason = onSelectSeason,
            onPlayMovie = { onPlayMovie(selectedMediaItem) },
            onPlayEpisode = { ep -> onPlayEpisode(selectedMediaItem, ep) },
            onBack = onCloseDetail,
            onRetry = { onItemClick(selectedMediaItem) }
        )
        return
    }

    // 2. If the dedicated Search Page is open, show Search screen with Back Arrow & Filter tabs
    if (isSearchOpen) {
        LibraryDedicatedSearchScreen(
            query = searchQuery,
            selectedFilter = searchFilter,
            results = searchResults,
            isSearching = isSearching,
            onQueryChange = onSearchQueryChange,
            onFilterChange = onSearchFilterChange,
            onItemClick = onItemClick,
            onBack = onCloseSearch
        )
        return
    }

    // 3. Main Library Screen: Sticky Header with Fixed Search Icon + 3x3 Grid (60-80 items per batch) + Load More
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FoulBlackBg)
    ) {
        // Fixed Top Header with Sticky Search Icon
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(FoulPurpleDeep.copy(alpha = 0.65f), FoulBlackBg)
                    )
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
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
                                imageVector = Icons.Default.VideoLibrary,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "المكتبة الضخمة",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "أحدث الأفلام والمسلسلات بجودة فائقة",
                                style = MaterialTheme.typography.bodySmall,
                                color = FoulTextSecondary
                            )
                        }
                    }

                    // Fixed Search Icon Button
                    Surface(
                        color = FoulPurpleBright,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable(onClick = onOpenSearch)
                            .border(1.dp, FoulPurpleGlow, RoundedCornerShape(14.dp))
                            .testTag("library_open_search_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "بحث في المكتبة",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "بحث سريع",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White
                            )
                        }
                    }
                }

                // Filter Strip (الكل / الأفلام / المسلسلات)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    LibraryFilterType.entries.forEach { filterType ->
                        val selected = mainFilter == filterType
                        Surface(
                            color = if (selected) FoulPurpleBright else FoulCardDark,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onSelectMainFilter(filterType) }
                                .border(
                                    1.dp,
                                    if (selected) FoulPurpleGlow else FoulDivider,
                                    RoundedCornerShape(12.dp)
                                )
                                .testTag("library_main_filter_${filterType.name}")
                        ) {
                            Text(
                                text = filterType.labelAr,
                                style = MaterialTheme.typography.labelLarge,
                                color = if (selected) Color.White else FoulTextSecondary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                        }
                    }
                }
            }
        }

        // Body: 3x3 Grid (3 columns) with 72 items per batch + "عرض المزيد" button
        when {
            isLoading && items.isEmpty() -> {
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
                            text = "جاري جلب الأفلام والمسلسلات بسرعة فائقة...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = FoulTextSecondary
                        )
                    }
                }
            }

            error != null && items.isEmpty() -> {
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
                            onClick = onRetry,
                            colors = ButtonDefaults.buttonColors(containerColor = FoulPurpleBright)
                        ) {
                            Text("إعادة المحاولة")
                        }
                    }
                }
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3), // 3x3 Grid Layout
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("library_grid_3x3")
                ) {
                    items(
                        items = items,
                        key = { "${if (it.isSeries) "S" else "M"}_${it.id}" }
                    ) { mediaItem ->
                        LibraryPosterCard(
                            item = mediaItem,
                            onClick = { onItemClick(mediaItem) }
                        )
                    }

                    // "عرض المزيد" (Load More) Button at bottom of grid
                    if (hasMore) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Button(
                                onClick = onLoadMore,
                                colors = ButtonDefaults.buttonColors(containerColor = FoulPurpleDeep),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp)
                                    .height(52.dp)
                                    .border(1.dp, FoulPurpleGlow, RoundedCornerShape(16.dp))
                                    .testTag("library_load_more_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    tint = FoulPurpleGlow
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "عرض المزيد",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LibraryDedicatedSearchScreen(
    query: String,
    selectedFilter: LibraryFilterType,
    results: List<LibraryMediaItem>,
    isSearching: Boolean,
    onQueryChange: (String) -> Unit,
    onFilterChange: (LibraryFilterType) -> Unit,
    onItemClick: (LibraryMediaItem) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FoulBlackBg)
    ) {
        // Search Header with Back Arrow + Search Input + 3 Filter Tabs (الكل أولاً، الأفلام، المسلسلات)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(FoulPurpleDeep.copy(alpha = 0.7f), FoulBlackBg)
                    )
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(FoulCardDark)
                        .border(1.dp, FoulDivider, CircleShape)
                        .testTag("search_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "رجوع",
                        tint = Color.White
                    )
                }

                OutlinedTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    placeholder = {
                        Text(
                            text = "ابحث عن فيلم أو مسلسل...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = FoulTextSecondary
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = FoulPurpleGlow
                        )
                    },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = { onQueryChange("") }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "مسح البحث",
                                    tint = FoulTextSecondary
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = FoulCardDark,
                        unfocusedContainerColor = FoulCardDark,
                        focusedBorderColor = FoulPurpleBright,
                        unfocusedBorderColor = FoulDivider,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = FoulPurpleGlow
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("library_search_input")
                )
            }

            // Filter Selection Row: الكل (First), الأفلام, المسلسلات
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                LibraryFilterType.entries.forEach { filterType ->
                    val selected = selectedFilter == filterType
                    Surface(
                        color = if (selected) FoulPurpleBright else FoulCardDark,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onFilterChange(filterType) }
                            .border(
                                1.dp,
                                if (selected) FoulPurpleGlow else FoulDivider,
                                RoundedCornerShape(12.dp)
                            )
                            .testTag("search_filter_tab_${filterType.name}")
                    ) {
                        Text(
                            text = filterType.labelAr,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (selected) Color.White else FoulTextSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }
                }
            }
        }

        if (isSearching) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = FoulPurpleBright)
            }
        } else if (results.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "لا توجد نتائج مطابقة لبحثك",
                    style = MaterialTheme.typography.titleMedium,
                    color = FoulTextSecondary
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("search_results_grid")
            ) {
                items(
                    items = results,
                    key = { "search_${if (it.isSeries) "S" else "M"}_${it.id}" }
                ) { item ->
                    LibraryPosterCard(
                        item = item,
                        onClick = { onItemClick(item) }
                    )
                }
            }
        }
    }
}

@Composable
private fun LibraryPosterCard(
    item: LibraryMediaItem,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = FoulCardDark),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .border(1.dp, FoulDivider, RoundedCornerShape(14.dp))
            .testTag("library_item_${item.id}")
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.68f)
                    .background(FoulCardElevated)
            ) {
                AsyncImage(
                    model = item.posterUrl,
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Type Badge (فيلم / مسلسل)
                Surface(
                    color = if (item.isSeries) FoulPurpleBright.copy(alpha = 0.92f) else FoulPurpleDeep.copy(alpha = 0.92f),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(6.dp)
                ) {
                    Text(
                        text = if (item.isSeries) "مسلسل" else "فيلم",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                // Rating Badge
                if (item.rating.isNotEmpty()) {
                    Surface(
                        color = FoulBlackBg.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = FoulGoldStar,
                                modifier = Modifier.size(11.dp)
                            )
                            Text(
                                text = item.rating,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (item.releaseYear.isNotEmpty()) {
                    Text(
                        text = item.releaseYear,
                        style = MaterialTheme.typography.bodySmall,
                        color = FoulTextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun LibraryMediaDetailScreen(
    item: LibraryMediaItem,
    isLoading: Boolean,
    error: String?,
    detail: MediaDetailInfo?,
    selectedSeasonNumber: Int,
    onSelectSeason: (Int) -> Unit,
    onPlayMovie: () -> Unit,
    onPlayEpisode: (SeriesEpisode) -> Unit,
    onBack: () -> Unit,
    onRetry: () -> Unit
) {
    BackHandler { onBack() }

    val seasons = detail?.seasons.orEmpty()
    val currentEpisodes = detail?.episodesBySeason?.get(selectedSeasonNumber).orEmpty()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FoulBlackBg),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Hero Backdrop + Poster + Metadata
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(290.dp)
            ) {
                AsyncImage(
                    model = detail?.backdropUrl?.ifBlank { item.posterUrl } ?: item.posterUrl,
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    FoulBlackBg.copy(alpha = 0.35f),
                                    FoulBlackBg.copy(alpha = 0.85f),
                                    FoulBlackBg
                                )
                            )
                        )
                )

                // Back Button
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .padding(16.dp)
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(FoulCardDark.copy(alpha = 0.85f))
                        .border(1.dp, FoulDivider, CircleShape)
                        .align(Alignment.TopStart)
                        .testTag("media_detail_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "رجوع",
                        tint = Color.White
                    )
                }

                // Poster + Title Row at Bottom of Hero
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .width(105.dp)
                            .height(152.dp)
                            .border(1.dp, FoulPurpleGlow.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
                    ) {
                        AsyncImage(
                            model = item.posterUrl,
                            contentDescription = item.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = 6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            color = FoulPurpleDeep,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = if (item.isSeries) Icons.Default.Tv else Icons.Default.Movie,
                                    contentDescription = null,
                                    tint = FoulPurpleGlow,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = if (item.isSeries) "مسلسل" else "فيلم",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White
                                )
                            }
                        }

                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            val ratingVal = detail?.rating?.ifBlank { item.rating } ?: item.rating
                            if (ratingVal.isNotEmpty()) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = FoulGoldStar,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = ratingVal,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = Color.White
                                    )
                                }
                            }
                            val releaseVal = detail?.releaseDate?.ifBlank { item.releaseYear } ?: item.releaseYear
                            if (releaseVal.isNotEmpty()) {
                                Text(
                                    text = releaseVal,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = FoulTextSecondary
                                )
                            }
                            if (!detail?.duration.isNullOrBlank()) {
                                Text(
                                    text = "${detail?.duration} دقيقة",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = FoulPurpleGlow
                                )
                            }
                        }
                    }
                }
            }
        }

        // Play Button for Movie
        if (!item.isSeries) {
            item {
                Button(
                    onClick = onPlayMovie,
                    colors = ButtonDefaults.buttonColors(containerColor = FoulPurpleBright),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                        .height(54.dp)
                        .testTag("play_movie_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "تشغيل الفيلم الآن",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Real Description / Plot Section (حسب حساب Xtreme الفعلي)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FoulCardDark),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .border(1.dp, FoulDivider, RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "قصة العمل والوصف",
                        style = MaterialTheme.typography.titleMedium,
                        color = FoulPurpleGlow,
                        fontWeight = FontWeight.Bold
                    )

                    if (isLoading) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            CircularProgressIndicator(
                                color = FoulPurpleBright,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "جاري جلب الوصف والتفاصيل الحقيقية...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = FoulTextSecondary
                            )
                        }
                    } else {
                        Text(
                            text = detail?.description
                                ?: item.plotPreview.ifBlank { "لا يتوفر وصف إضافي لهذا العمل." },
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )

                        if (!detail?.genre.isNullOrBlank()) {
                            Text(
                                text = "التصنيف: ${detail?.genre}",
                                style = MaterialTheme.typography.bodySmall,
                                color = FoulPurpleGlow
                            )
                        }
                        if (!detail?.cast.isNullOrBlank()) {
                            Text(
                                text = "البطولة: ${detail?.cast}",
                                style = MaterialTheme.typography.bodySmall,
                                color = FoulTextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Error Retry if series/movie info failed
        if (error != null && !isLoading) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = error, color = Color.White)
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = onRetry,
                        colors = ButtonDefaults.buttonColors(containerColor = FoulPurpleBright)
                    ) {
                        Text("إعادة المحاولة")
                    }
                }
            }
        }

        // Series Seasons & Episodes Section
        if (item.isSeries && !isLoading) {
            if (seasons.isNotEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    ) {
                        Text(
                            text = "المواسم (${seasons.size})",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(Modifier.height(10.dp))
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(seasons, key = { it.seasonNumber }) { season ->
                                val isSelected = season.seasonNumber == selectedSeasonNumber
                                Surface(
                                    color = if (isSelected) FoulPurpleBright else FoulCardDark,
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(14.dp))
                                        .clickable { onSelectSeason(season.seasonNumber) }
                                        .border(
                                            1.dp,
                                            if (isSelected) FoulPurpleGlow else FoulDivider,
                                            RoundedCornerShape(14.dp)
                                        )
                                        .testTag("season_chip_${season.seasonNumber}")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = "الموسم ${season.seasonNumber}",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Surface(
                                            color = if (isSelected) FoulPurpleDeep else FoulCardElevated,
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                text = "${season.episodeCount} حلقة",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = FoulPurpleGlow,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "الحلقات (${currentEpisodes.size})",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            items(currentEpisodes, key = { it.id }) { ep ->
                EpisodeRowCard(
                    episode = ep,
                    onClick = { onPlayEpisode(ep) }
                )
            }
        }
    }
}

@Composable
private fun EpisodeRowCard(
    episode: SeriesEpisode,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = FoulCardDark),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .border(1.dp, FoulDivider, RoundedCornerShape(16.dp))
            .testTag("episode_card_${episode.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Episode Number Badge
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(listOf(FoulPurpleBright, FoulPurpleDeep))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "حلقة",
                        style = MaterialTheme.typography.bodySmall,
                        color = FoulPurpleGlow
                    )
                    Text(
                        text = "${episode.episodeNum}",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "الحلقة ${episode.episodeNum} - ${episode.title}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (episode.plot.isNotBlank()) {
                    Text(
                        text = episode.plot,
                        style = MaterialTheme.typography.bodySmall,
                        color = FoulTextSecondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (episode.duration.isNotBlank()) {
                    Text(
                        text = "المدة: ${episode.duration}",
                        style = MaterialTheme.typography.bodySmall,
                        color = FoulPurpleGlow
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(FoulPurpleBright),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "تشغيل الحلقة",
                    tint = Color.White
                )
            }
        }
    }
}
