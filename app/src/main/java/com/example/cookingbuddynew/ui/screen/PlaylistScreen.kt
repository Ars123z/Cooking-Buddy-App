package com.example.cookingbuddynew.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cookingbuddynew.R
import com.example.cookingbuddynew.api.Playlist

@Composable
fun PlaylistsScreen(
    onPlaylistClick: (Int) -> Unit,
    modifier: Modifier = Modifier
        .padding(16.dp),
    playlistViewModel: PlaylistViewModel = viewModel(factory = PlaylistViewModel.Factory)
) {
    val playlists by playlistViewModel.fetchPlaylist().collectAsState(initial = emptyList())
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Playlists",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        LazyColumn(
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(playlists) { playlist ->
                PlaylistItem(
                    onPlaylistClick = onPlaylistClick,
                    playlist = playlist
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun PlaylistItem(
    onPlaylistClick: (Int) -> Unit,
    playlist: Playlist
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onPlaylistClick(playlist.id) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (playlist.videos.isNotEmpty()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(playlist.videos[0].thumbnail)
                    .crossfade(true)
                    .build(),
                contentDescription = "Playlist Cover",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(100.dp)
                    .width(150.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.empty),
                contentDescription = "Placeholder Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(100.dp)
                    .width(150.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
        Spacer(modifier = Modifier.size(16.dp))
        Column(
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = playlist.name,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        }
    }
}
