package com.example.cookingbuddynew.ui.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.cookingbuddynew.R
import com.example.cookingbuddynew.api.HistoryUpdateRequest
import com.example.cookingbuddynew.api.ResultItem
import com.example.cookingbuddynew.api.Video



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit,
    onCardClick: (Video) -> Unit,
    navigateUp: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    searchViewModel: SearchViewModel = viewModel(factory = SearchViewModel.Factory)
) {
    Scaffold(
        modifier = modifier.padding(contentPadding),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    SearchField(onSearch = onSearch)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navigateUp()
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_arrow_back_24),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        val searchUiState = searchViewModel.searchUiState
        when (searchUiState) {
            is SearchUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxWidth())
            is SearchUiState.Success -> ResultScreen(
                onCardClick = onCardClick,
                result =
                searchUiState.recipes, modifier = Modifier.fillMaxWidth().padding(innerPadding),
                updateHistory = { it ->
                    val request = HistoryUpdateRequest(video_id = it.id.videoId)
                    searchViewModel.updateHistory(request)
                }
            )
            is SearchUiState.Error -> ErrorScreen(modifier = modifier.fillMaxWidth().padding(innerPadding))
        }
    }
}
/**
 * The home screen displaying the loading message.
 */
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

/**
 * The home screen displaying error message with re-attempt button.
 */
@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
    }
}

/**
 * ResultScreen displaying number of photos retrieved.
 */
@Composable
fun ResultScreen(result: List<ResultItem>,
                 modifier: Modifier = Modifier,
                 onCardClick: (Video) -> Unit,
                 updateHistory: (ResultItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier.height(1000.dp)
            .padding(top = 100.dp),
    ) {
        items(result.size) {
            RecipeCard(
                recipe = result[it],
                onCardClick = onCardClick,
                updateHistory = updateHistory
            )
        }
    }
}

@Composable
fun RecipeCard(
    recipe: ResultItem,
    updateHistory: (ResultItem) -> Unit,
    modifier: Modifier = Modifier,
    onCardClick: (Video) -> Unit = {}
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val video = Video(
        id = recipe.id.videoId,
        title = recipe.snippet.title,
        thumbnailUrl = recipe.snippet.thumbnails.high.url
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
                model = recipe.snippet.thumbnails.high.url,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clickable {
                        onCardClick(video)
                        updateHistory(recipe)
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
                    text = recipe.snippet.title,
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
                Text(
                    text = recipe.snippet.channelTitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
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

@Composable
fun SearchField(onSearch: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
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
                if (text != "") {
                    onSearch(text)
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
                tint = Color.White
            )
        },
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            focusedContainerColor = Color.Black,
            unfocusedContainerColor = Color.Black,
            cursorColor = Color.White,
            focusedLeadingIconColor = Color.White,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        )
    )
}