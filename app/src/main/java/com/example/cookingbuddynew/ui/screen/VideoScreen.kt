package com.example.cookingbuddynew.ui.screen

//Translation imports
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cookingbuddynew.R
import com.example.cookingbuddynew.api.TranslateRequest
import com.example.cookingbuddynew.api.Video
import com.example.cookingbuddynew.utils.DataStoreManager
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.launch


@Composable
fun VideoScreen(
    video: Video?,
    lifecycleOwner: LifecycleOwner,
    modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current
    val view = LocalView.current
    val isLandscape by remember(configuration) {
        derivedStateOf { configuration.orientation == Configuration.ORIENTATION_LANDSCAPE }
    }

    LaunchedEffect(isLandscape) {
        val window = (view.context as? android.app.Activity)?.window ?: return@LaunchedEffect
        val windowInsetsController = WindowInsetsControllerCompat(window, view)
        if (isLandscape) {
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            windowInsetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            val window = (view.context as? android.app.Activity)?.window ?: return@onDispose
            val windowInsetsController = WindowInsetsControllerCompat(window, view)
            windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
        }
    }
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier.padding(top = 30.dp)
    ) {
        AndroidView(
            modifier = modifier
                .clip(RoundedCornerShape(8.dp)),
            factory = { context ->
                val youTubePlayerView = YouTubePlayerView(context)
                lifecycleOwner.lifecycle.addObserver(youTubePlayerView)
                youTubePlayerView.enableAutomaticInitialization = false
                val options = IFramePlayerOptions.Builder()
                    .controls(1)
                    .rel(1)
                    .ivLoadPolicy(1)
                    .ccLoadPolicy(0)
                    .build()
                val listener = object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.loadVideo(video?.id ?: "", 0f)
                    }
                }
                youTubePlayerView.initialize(listener, options)
                youTubePlayerView
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = video?.title ?: "",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onBackground // Title color
        )
        Spacer(modifier = Modifier.height(20.dp))
        RecipeDetail(
            videoId = video?.id ?: "",
        )
    }
}

@Composable
fun RecipeDetail(
    modifier: Modifier = Modifier
        .background(color = Color.Black)
        .clip(RoundedCornerShape(16.dp)),
    videoId: String,
    dataStore: DataStoreManager = DataStoreManager(LocalContext.current),
    videoViewModel: VideoViewModel = viewModel(factory = VideoViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val userProfile by dataStore.getFromDataStore().collectAsState(initial = null)
    val videoUiState = videoViewModel.videoUiState
    var activeTab by remember { mutableStateOf("Ingredient") }
    val language = userProfile?.language ?: "english"

    LaunchedEffect(key1 = videoId) {
        videoViewModel.getRecipeDetails(videoId)
        Log.d("RecipeDetail", "RecipeDetail: $videoId")
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    activeTab = "Ingredient"
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (activeTab == "Ingredient") colorResource(id = R.color.primary) else colorResource(
                        id = R.color.background
                    ),
                    contentColor = if (activeTab == "Ingredient") Color.Black else Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Ingredient",
                    fontStyle = MaterialTheme.typography.labelLarge.fontStyle,
                )
            }
            Button(
                onClick = {
                    activeTab = "Method"
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (activeTab == "Method") colorResource(id = R.color.primary) else colorResource(
                        id = R.color.background
                    ),
                    contentColor = if (activeTab == "Method") Color.Black else MaterialTheme.colorScheme.onBackground
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Steps",
                    fontStyle = MaterialTheme.typography.labelLarge.fontStyle,
                )
            }
            Button(
                onClick = {
                    val request = TranslateRequest(
                        id = videoId,
                        lang = language
                    )
                    Log.d("RecipeDetail", "Translate: $language")
                    coroutineScope.launch {
                        videoViewModel.translate(request)
                    }
                },
                enabled = userProfile != null,
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Black,
                    containerColor = Color.White,
                    disabledContainerColor = Color.White.copy(alpha = 0.5f),
                    disabledContentColor = Color.Black
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text="Translate",
                )
            }
        }
        if (activeTab == "Ingredient") {
            when (videoUiState) {
                is VideoUiState.Loading -> LoadingScreen()
                is VideoUiState.Success -> IngredientScreen(ingredients = videoUiState.recipeDetails.ingredient_list)
                is VideoUiState.Error -> ErrorScreen()
            }
        } else {
            when (videoUiState) {
                is VideoUiState.Loading -> LoadingScreen()
                is VideoUiState.Success -> MethodScreen(method = videoUiState.recipeDetails.method)
                is VideoUiState.Error -> ErrorScreen()
            }
        }
        Button(
            onClick = { videoViewModel.getRecipeDetails(videoId) },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.primary),
                contentColor = Color.Black
            ),
        ) {
            Text("Try Reloading")
        }
    }
}

@Composable
fun MethodScreen(
    method: List<String>,
    modifier: Modifier = Modifier
) {
    Column {
        Text(
            text = "METHOD",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White
        )

        LazyColumn(modifier = modifier.padding(16.dp)) {
            itemsIndexed(method) { index, step ->
                Row(
                    modifier = modifier,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(colorResource(id = R.color.primary)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (index + 1).toString(),
                            color = Color.Black,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                    Text(
                        text = step,
                        color = Color.White
                    )
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(colorResource(R.color.background))
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                )
            }
        }
    }
}

@Composable
fun IngredientScreen(
    ingredients: List<List<String>>,
    dataStore: DataStoreManager = DataStoreManager(LocalContext.current),
    modifier: Modifier = Modifier
) {
    Text(
        text = "INGREDIENTS",
        fontStyle = MaterialTheme.typography.headlineLarge.fontStyle,
        color = Color.White
    )
    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(ingredients) { ingredient ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = ingredient[0],
                    color = Color.White,
                )
                Text(
                    text = ingredient[1],
                    color = Color.White
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(colorResource(R.color.background))
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
            )
        }
    }
}