package com.example.cookingbuddynew.ui.screen

import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cookingbuddynew.api.CreatePlaylistRequest
import com.example.cookingbuddynew.api.History
import com.example.cookingbuddynew.api.Playlist
import com.example.cookingbuddynew.utils.DataStoreManager
import kotlinx.coroutines.launch
import com.example.cookingbuddynew.R
import com.example.cookingbuddynew.api.Video

@Composable
fun ProfileScreen(
    seeAllHistory: () -> Unit,
    seeAllPlaylist: () -> Unit,
    onCardClick: (Video) -> Unit,
    profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.Factory),
    modifier: Modifier = Modifier
) {
    val dataStoreContext = LocalContext.current
    val dataStoreManager = DataStoreManager(dataStoreContext)
    val userDetails by dataStoreManager.getFromDataStore().collectAsState(initial = null)

    if (userDetails == null) {
        CircularProgressIndicator()
    } else {
        ProfilePage(
            userDetails!!.access_token,
            userDetails!!.picture,
            userDetails!!.full_name,
            dataStoreManager,
            profileViewModel,
            seeAllHistory,
            seeAllPlaylist,
            onCardClick = onCardClick
        )
    }
}

@Composable
fun ProfilePage(
    access_token: String?,
    picture: String?,
    name: String?,
    dataStoreManager: DataStoreManager,
    profileViewModel: ProfileViewModel,
    seeAllHistory: () -> Unit,
    seeAllPlaylist: () -> Unit,
    onCardClick: (Video) -> Unit,
    modifier: Modifier = Modifier
) {
    val name = name ?: "Loading..."
    var history by remember { mutableStateOf<List<History>?>(null) }
    var playlist by remember { mutableStateOf<List<Playlist>?>(null) }
    val coroutineScope = rememberCoroutineScope()


    if (access_token != null && access_token != "") {
        LaunchedEffect(access_token) {
            profileViewModel.fetchHistory().collect {
                history = it
                Log.i(ContentValues.TAG, history.toString())
            }
            profileViewModel.fetchPlaylist(access_token).collect {
                playlist = it
                Log.i(ContentValues.TAG, playlist.toString())
            }
        }
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End // Align content to the end (right)
        ) {
            LogOut(profileViewModel, dataStoreManager)
        }
        Spacer(modifier = Modifier.height(16.dp)) // Add some space between rows
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically // Center items vertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(data = picture)
                    .build(),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp) // Set a fixed size for the image
                    .clip(CircleShape) // Make the image circular
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = name,
                modifier = Modifier.padding(start = 8.dp) // Add some padding between the image and the text
            )
        }
        Spacer(modifier = Modifier.height(16.dp)) // Add some space between rows
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween // Distribute content evenly
        ) {
            if (history != null) {
                HistorySlider(
                    history,
                    seeAllHistory,
                    onCardClick = onCardClick
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp)) // Add some space between rows
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween // Distribute content evenly
        ) {
            if (access_token != null && access_token != "") {
                val createNewPlaylist: (request: CreatePlaylistRequest) -> Unit = {
                    Log.i(ContentValues.TAG, it.toString())
                    coroutineScope.launch {
                        profileViewModel.createPlaylist(access_token, request = it)
                        profileViewModel.fetchPlaylist(access_token).collect {
                            playlist = it
                            Log.i(ContentValues.TAG, playlist.toString())
                        }
                    }
                }
                PlaylistSlider(
                    playlist,
                    createNewPlaylist,
                    seeAllPlaylist
                )
            }
        }
    }
}

@Composable
fun HistorySlider(
    history: List<History>?,
    seeAllHistory: () -> Unit,
    onCardClick: (Video) -> Unit,
    modifier: Modifier = Modifier,

    ) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "History",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = seeAllHistory,
                modifier = Modifier.padding(end = 16.dp, bottom= 16.dp)
            ) {
                Text(text = "See All")
            }
        }
        if (history != null && history.isNotEmpty()) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(history) { historyItem ->
                    HistoryItemCard(
                        historyItem,
                        onCardClick = onCardClick)
                }
            }
        } else {
            Text(
                text = "No history yet",
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Composable
fun HistoryItemCard(history: History, onCardClick: (Video) -> Unit) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(160.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(history.thumbnail)
                    .build(),
                contentDescription = "History Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        val video = Video(
                            id = history.video_id,
                            title = history.title,
                            thumbnailUrl = history.thumbnail
                        )
                        onCardClick(video)
                    }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${history.title.substring(0, minOf(25, history.title.length))}..",
                modifier = Modifier.padding(
                    start = 8.dp,
                    bottom = 8.dp
                )
            )
        }
    }
}

@Composable
fun PlaylistSlider(
    playlist: List<Playlist>?,
    createNewPlaylist: (request: CreatePlaylistRequest) -> Unit,
    seeAllPlaylist: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showCreatePlaylistDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Playlist",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {showCreatePlaylistDialog = true},
                modifier = Modifier.padding(end = 16.dp, bottom= 16.dp)
            ) {
                Icon(painter = painterResource(id = R.drawable.baseline_add_24), contentDescription = "Add Playlist")
            }
            Button(
                onClick = seeAllPlaylist,
                modifier = Modifier.padding(end = 16.dp, bottom= 16.dp)
            ) {
                Text(text = "See All")
            }
        }
        if (playlist != null && playlist.isNotEmpty()) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(playlist) { playlistItem ->
                    PlaylistItemCard(playlistItem)
                }
            }
        } else {
            Text(
                text = "No playlist yet",
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
    if (showCreatePlaylistDialog) {
        CreatePlaylistDialog(
            onDismissRequest = { showCreatePlaylistDialog = false },
            onConfirm = { playlistName ->
                coroutineScope.launch {
                    val request: CreatePlaylistRequest = CreatePlaylistRequest(playlistName)
                    Log.i(ContentValues.TAG, request.toString())
                    createNewPlaylist(request)
                    showCreatePlaylistDialog = false
                }
            }
        )
    }
}


@Composable
fun PlaylistItemCard(playlist: Playlist) {
    Card(
        modifier = Modifier
            .width(150.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (playlist.videos.isNotEmpty()) {
                val imageUrl = playlist.videos[0].thumbnail
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .build(),
                    contentDescription = "History Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(R.drawable.empty)
                        .build(),
                    contentDescription = "Playlist Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${playlist.name.substring(0, minOf(25, playlist.name.length))}..",
                modifier = Modifier.padding(
                    start = 8.dp,
                    bottom = 8.dp
                )
            )
        }
    }
}

@Composable
fun CreatePlaylistDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit
) {
    // Implement the dialog UI here
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Create New Playlist") },
        text = {
            Column() {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Playlist Name") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { onConfirm(name) }),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(name) },
                enabled = name.isNotBlank()
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun LogOut(
    profileViewModel: ProfileViewModel,
    dataStoreManager: DataStoreManager,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val onClick: () -> Unit = {
        val credentialManager = CredentialManager.create(context)
        val request: ClearCredentialStateRequest = ClearCredentialStateRequest()
        coroutineScope.launch {
            try {
                credentialManager.clearCredentialState(request)
                dataStoreManager.clearDataStore()
                Toast.makeText(context, "you are signed out", Toast.LENGTH_SHORT).show()
            } catch (e: GetCredentialException) {
                Log.i(ContentValues.TAG, e.message.toString())
                Log.i(ContentValues.TAG, e.cause.toString())
                Log.i(ContentValues.TAG, e.stackTraceToString())
            }
        }
    }
    Button(
        onClick = onClick
    ) {
        Text(text = "Log Out")
    }
}