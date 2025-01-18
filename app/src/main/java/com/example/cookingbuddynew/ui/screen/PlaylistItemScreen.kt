package com.example.cookingbuddynew.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cookingbuddynew.api.Item
import com.example.cookingbuddynew.api.Playlist
import com.example.cookingbuddynew.api.UpdatePlaylistRequest
import com.example.cookingbuddynew.api.Video
import kotlinx.coroutines.launch

@Composable
fun PlaylistItemScreen(
    onCardClick: (Video) -> Unit,
    playlistId: Int,
    playlistViewModel: PlaylistViewModel = viewModel(factory = PlaylistViewModel.Factory)
) {
    Scaffold(
        contentWindowInsets = WindowInsets.statusBars,
    ) { innerPadding ->
        val playlist =
            playlistViewModel.getPlaylist(playlistId).collectAsState(initial = null).value
        when (playlist) {
            null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is Playlist -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    item {
                        PlaylistHeader(playlist = playlist)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    items(playlist.videos.size) { index ->
                        VideoCard(
                            playlist = playlist,
                            onCardClick = onCardClick,
                            item = playlist.videos[index]
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlaylistHeader(playlist: Playlist) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp) // Adjust the height as needed
    ) {
        // Background Image
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(
                    playlist.videos.firstOrNull()?.thumbnail ?: ""
                ) // Use first video's thumbnail or empty string
                .crossfade(true)
                .build(),
            contentDescription = "Playlist Cover",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Gradient Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .height(150.dp)
                .width(200.dp)
                .clip(RoundedCornerShape(16.dp)) // Optional: Clip to a shape
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(
                        playlist.videos.firstOrNull()?.thumbnail ?: ""
                    ) // Use first video's thumbnail or empty string
                    .build(),
                contentDescription = "Playlist Cover",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                text = playlist.name,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = "total ${playlist.videos.size} videos", // Replace with actual video count}", // Replace with actual username if available
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun VideoCard(
    playlist: Playlist,
    onCardClick: (Video) -> Unit,
    item: Item
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val video = Video(
                    id = item.video_id,
                    title = item.title,
                    thumbnailUrl = item.thumbnail,
                )
                onCardClick(video)
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item.thumbnail)
                .crossfade(true)
                .build(),
            contentDescription = "Video Thumbnail",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(120.dp)
                .height(80.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = truncateTextToTenWords(item.title),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(onClick = { showBottomSheet = true }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "More Options",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            PlaylistItemBottomSheet(
                currentPlaylist = playlist,
                currentVideo = Video(
                    id = item.video_id,
                    title = item.title,
                    thumbnailUrl = item.thumbnail,
                ),
                changeState = { showBottomSheet = it },
                showBottomSheet = showBottomSheet
            )
        }
    }
}

fun truncateTextToTenWords(text: String): String {
    val words = text.split(" ")
    return if (words.size <= 10) {
        text
    } else {
        words.take(10).joinToString(" ") + "..."
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistBottomSheet(
    currentVideo: Video,
    changeState: (value: Boolean) -> Unit,
    showBottomSheet: Boolean,
    playlistViewModel: PlaylistViewModel = viewModel(factory = PlaylistViewModel.Factory),
) {
    val coroutineScope = rememberCoroutineScope()
    val playlists by playlistViewModel.fetchPlaylist().collectAsState(initial = emptyList<Playlist>())
    when (playlists) {
        emptyList<Playlist>() -> {
            val bottomSheetState = rememberModalBottomSheetState()
            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        changeState(false)
                    },
                    sheetState = bottomSheetState
                ) {
                    Text(text = "Loading...")
                }
            }
        }
        else -> {
            val bottomSheetState = rememberModalBottomSheetState()
            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        changeState(false)
                    },
                    sheetState = bottomSheetState
                ) {
                    // Bottom sheet content
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Add to Playlist")
                        IconButton(onClick = {
                            coroutineScope.launch { bottomSheetState.hide() }
                                .invokeOnCompletion {
                                    if (!bottomSheetState.isVisible) {
                                        changeState(false)
                                    }
                                }
                        }) {
                            Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
                        }
                    }
                    LazyColumn() {
                        items(playlists) { playlist ->
                            var isChecked by remember {
                                mutableStateOf(playlist.videos.any { it.video_id == currentVideo.id })
                            }
                            LaunchedEffect(playlist.videos) {
                                isChecked = playlist.videos.any { it.video_id == currentVideo.id }
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        isChecked = !isChecked
                                        if (isChecked) {
                                            val request: UpdatePlaylistRequest =
                                                UpdatePlaylistRequest.AddVideoRequest(
                                                    name = playlist.name,
                                                    id = playlist.id,
                                                    add_video_ids = arrayOf(currentVideo.id)
                                                )
                                            coroutineScope.launch {
                                                playlistViewModel.updatePlaylist(
                                                    playlist.id,
                                                    request
                                                )
                                            }
                                        } else {
                                            val request: UpdatePlaylistRequest =
                                                UpdatePlaylistRequest.RemoveVideoRequest(
                                                    name = playlist.name,
                                                    id = playlist.id,
                                                    remove_video_ids = arrayOf(currentVideo.id)
                                                )
                                            coroutineScope.launch {
                                                playlistViewModel.updatePlaylist(
                                                    playlist.id,
                                                    request
                                                )
                                            }
                                        }
                                    }
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = {}
                                )
                                Text(
                                    text = playlist.name,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }
                    }
                    Button(
                        onClick = {changeState(false)}
                    ) {
                       Text(text = "Done")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistItemBottomSheet(
    currentVideo: Video,
    currentPlaylist: Playlist,
    changeState: (value: Boolean) -> Unit,
    showBottomSheet: Boolean,
    playlistViewModel: PlaylistViewModel = viewModel(factory = PlaylistViewModel.Factory),
) {
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                changeState(false)
            },
            sheetState = bottomSheetState,
            dragHandle = {
                // You can customize the drag handle here if needed
            }
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Video Options",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    onClick = {
                        val request: UpdatePlaylistRequest =
                            UpdatePlaylistRequest.RemoveVideoRequest(
                                name = currentPlaylist.name,
                                id = currentPlaylist.id,
                                remove_video_ids = arrayOf(currentVideo.id)
                            )
                        coroutineScope.launch {
                            playlistViewModel.updatePlaylist(
                                currentPlaylist.id,
                                request
                            )
                            bottomSheetState.hide()
                        }.invokeOnCompletion {
                            if (!bottomSheetState.isVisible) {
                                changeState(false)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Remove Video from the Playlist")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            bottomSheetState.hide()
                        }.invokeOnCompletion {
                            if (!bottomSheetState.isVisible) {
                                changeState(false)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Done")
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}