package com.example.cookingbuddynew.ui.screen

import android.R.string
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Label
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.cookingbuddynew.R
import com.example.cookingbuddynew.api.ResultItem
import com.example.cookingbuddynew.api.Video
import com.example.cookingbuddynew.ui.theme.LogoType

@Composable
fun HomeScreen(
    onSearch: () -> Unit,
    onCardClick: (Video) -> Unit,
    homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory),
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    var videos:List<Video> by remember { mutableStateOf(listOf()) }
    var videosPks:List<Int> by remember { mutableStateOf(listOf()) }

    LaunchedEffect(videosPks) {
        val fetchedVideos = mutableListOf<Video>()
        for (pk in videosPks) {
            val video = homeViewModel.videoDetails(pk)
            if (video != null) {
                val vd= Video(
                    id = video.video_id,
                    title = video.title,
                    thumbnailUrl = video.thumbnail
                )
                fetchedVideos.add(vd)
            }
        }
        videos = fetchedVideos
    }

    Scaffold() { padding ->
        LazyColumn(
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .fillMaxSize()
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Header(
                        onClick = onSearch
                    )
                }
                Spacer(modifier = Modifier.padding(4.dp))
            }
//        Horizontal List
            item {
                LabelList(
                    onLabelClick = { videosPks = it },
                    homeViewModel = homeViewModel
                )
            }

            items(videos) { video ->
                VideoCard(
                    video = video,
                    onCardClick = onCardClick,
                )
            }
        }
    }
}

@Composable
fun Header(onClick: () -> Unit = {}) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(50.dp)
        )
        Text(
            text = "CookVerse",
            style = LogoType,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = {onClick()}
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_search_24),
                contentDescription = "Search",
                modifier = Modifier.size(30.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

    }
}

@Composable fun LabelList(
    homeViewModel: HomeViewModel,
    onLabelClick: (List<Int>) -> Unit,
) {
    var selectedLabel by remember { mutableStateOf("") }
    var labelUiState = homeViewModel.labelUiState
    LaunchedEffect(Unit) {
        homeViewModel.fetchLabel("us")
    }
    when (labelUiState) {
        is LabelUiState.Loading -> {
//            Show Nothing
        }
        is LabelUiState.Success -> {
            LaunchedEffect(Unit) {
                onLabelClick(labelUiState.labels[0].videos)
                selectedLabel = labelUiState.labels[0].name
            }
            LazyRow(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                items(
                    labelUiState.labels
                ) { item ->
                    Button(
                        onClick = {
                            onLabelClick(item.videos)
                            selectedLabel = item.name },
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedLabel == item.name) {
                                Color.White
                            } else {
                                Color.DarkGray
                            },
                            contentColor = if (selectedLabel == item.name) {
                                Color.Black
                            } else {
                                Color.White
                            }
                        )
                    ) {
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
        is LabelUiState.Error -> {
//            ShowNothing
        }
    }
}


@Composable
fun VideoCard(
    video: Video,
    modifier: Modifier = Modifier,
    onCardClick: (Video) -> Unit = {}
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val video = Video(
        id = video.id,
        title = video.title,
        thumbnailUrl = video.thumbnailUrl
    )
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp), // Reduced padding
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp), // No elevation
        colors = CardDefaults.cardColors(containerColor = Color.Transparent), // Transparent background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = video.thumbnailUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clickable {
                        onCardClick(video)
                    },
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), // Added horizontal padding
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = video.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f), // Allow text to take up available space
                    color = MaterialTheme.colorScheme.onBackground // Title color
                )
                IconButton(
                    onClick = {
                        showBottomSheet = true
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "More Options",
                        tint = MaterialTheme.colorScheme.onBackground // Icon color
                    )
                }
                PlaylistBottomSheet(
                    currentVideo = video,
                    changeState = { showBottomSheet = it },
                    showBottomSheet = showBottomSheet
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp) // Added horizontal padding
            ) {
//                Text(
//                    text = recipe.snippet.channelTitle,
//                    style = MaterialTheme.typography.bodySmall,
//                    color = Color.Gray,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
//                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "•",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "1M views - 1 year ago",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}



