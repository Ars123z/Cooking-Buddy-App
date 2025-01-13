package com.example.cookingbuddynew.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cookingbuddynew.api.History
import com.example.cookingbuddynew.utils.DataStoreManager
import com.example.cookingbuddynew.R
import com.example.cookingbuddynew.api.Video
import kotlinx.coroutines.launch

@Composable
fun HistoryScreen(
    navigationAwareKey: Int,
    onCardClick: (Video) -> Unit,
    modifier: Modifier = Modifier.padding(horizontal = 16.dp),
    historyViewModel: HistoryViewModel = viewModel(factory = HistoryViewModel.Factory),
    profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.Factory)
) {
    var history by remember { mutableStateOf(emptyList<History>()) }
    var text by remember { mutableStateOf("") }
    val dataStoreContext = LocalContext.current
    val dataStoreManager = DataStoreManager(dataStoreContext)
    val coroutineScope = rememberCoroutineScope()

    val userDetails by dataStoreManager.getFromDataStore().collectAsState(initial = null)
    val accessToken = userDetails?.access_token ?: ""

    if (text == "") {
        LaunchedEffect(text) {
            coroutineScope.launch {
                profileViewModel.fetchHistory(
                ).collect { fetchedHistory ->
                    history = fetchedHistory
                }
            }
        }
    }

    if (accessToken.isNotEmpty()) {
        LaunchedEffect(navigationAwareKey) {
            profileViewModel.fetchHistory(
            ).collect { fetchedHistory ->
                history = fetchedHistory
            }
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize() // Take up the full available space
            .windowInsetsPadding(WindowInsets.statusBars) // Add padding for the status bar
            .navigationBarsPadding() // Add padding for the navigation bar
    ) {
        Text(
            text = "History",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp) // Add padding below the title
        )

        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = androidx.compose.ui.text.input.ImeAction.Search,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    coroutineScope.launch {
                        historyViewModel.searchHistory(text).collect { fetchedHistory ->
                            history = fetchedHistory.results
                        }
                    }
                }
            ),
            placeholder = {
                Text(
                    text = "Search for recipes...",
                    color = Color.Gray
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_search_24),
                    contentDescription = null, // Add content description if needed
                    modifier = Modifier.size(24.dp),
                    tint = Color.Gray // Change the color to Gray
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp), // Change the shape to 8.dp
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                cursorColor = Color.Black,
                focusedLeadingIconColor = Color.Gray, // Change the color to Gray
                focusedBorderColor = Color.Gray, // Change the color to Gray
                unfocusedBorderColor = Color.Gray // Change the color to Gray
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
//            items(history) { item ->
//                HistoryItem(history = item)
//            }
            items(history) {
                HistoryItem(history = it, onCardClick = onCardClick)
            }
        }
    }
}

@Composable
fun HistoryItem(history: History, onCardClick: (Video) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp) // Add vertical padding to each item
            .clickable {
                val video = Video(
                    id = history.video_id,
                    title = history.title,
                    thumbnailUrl = history.thumbnail,
                )
                onCardClick(video)
            },
        verticalAlignment = Alignment.CenterVertically // Center items vertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(data = history.thumbnail)
                .build(),
            contentDescription = history.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp) // Set a fixed size for the image
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = history.title,
            modifier = Modifier.weight(1f) // Make the text take up remaining space
        )
    }
}
