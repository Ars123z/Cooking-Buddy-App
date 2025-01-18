package com.example.cookingbuddynew

import android.app.Activity
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PersonalVideo
import androidx.compose.material.icons.filled.PlaylistAddCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.twotone.AccountCircle
import androidx.compose.material.icons.twotone.History
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.PersonalVideo
import androidx.compose.material.icons.twotone.PlaylistAddCircle
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cookingbuddynew.BottomBarItem
import com.example.cookingbuddynew.api.Video
import com.example.cookingbuddynew.ui.screen.HistoryScreen
import com.example.cookingbuddynew.ui.screen.SearchViewModel
import com.example.cookingbuddynew.ui.screen.HomeScreen
import com.example.cookingbuddynew.ui.screen.PlaylistItemScreen
import com.example.cookingbuddynew.ui.screen.PlaylistsScreen
import com.example.cookingbuddynew.ui.screen.ProfileScreen
import com.example.cookingbuddynew.ui.screen.SearchScreen
import com.example.cookingbuddynew.ui.screen.VideoScreen


enum class CookingBuddyScreen(
    @StringRes val title: Int,
    val route: String,
    @DrawableRes val icon: Int,
    val arguments: List<NamedNavArgument> = emptyList(),
) {
    Home(
        title = R.string.home,
        route = "home",
        icon= R.drawable.home
    ),
    History(
        title = R.string.history,
        route = "history",
        icon = R.drawable.history
    ),
    Search(
        title = R.string.search,
        route = "search",
        arguments = listOf(navArgument("query") {
            type = NavType.StringType
        }),
        icon = R.drawable.home
    ),
    Profile(
        title = R.string.profile,
        route = "profile",
        icon = R.drawable.profile
    ),
    Playlist(
        title = R.string.playlist,
        route = "playlist",
        icon = R.drawable.playlist,
    ),
    Video(
        title = R.string.video,
        route = "video",
        icon = R.drawable.home,
    );

    companion object {
        fun fromRoute(route: String?): CookingBuddyScreen =
            when (route?.substringBefore("/")) {
                Home.route -> Home
                History.route -> History
                Search.route -> Search
                Profile.route -> Profile
                Playlist.route -> Playlist
                Video.route -> Video
                null -> Home
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
    fun createRoute(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}

@Composable
fun CookingBuddyApp(
    navController: NavHostController = rememberNavController()
) {
    //Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    //Get the name of the current screen
    val currentScreen = CookingBuddyScreen.fromRoute(
        backStackEntry?.destination?.route ?: CookingBuddyScreen.History.route
    )
    val localLifecycleOwner = LocalLifecycleOwner.current
    var navigationAwareKey: Int by remember { mutableStateOf(0) }
    val context = LocalContext.current as Activity
    Scaffold(
        bottomBar = {
                BottomBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = CookingBuddyScreen.Home.route,
            modifier = Modifier
                .fillMaxSize()
        ) {
//        Home Screen
            composable(route = CookingBuddyScreen.Home.route) {
                HomeScreen(
                    onSearch = { it ->
                        navController.navigate(CookingBuddyScreen.Search.createRoute(it))
                    },
                    onProfileClick = {
                        if (navigationAwareKey == 0) 1 else 0
                        navController.navigate(CookingBuddyScreen.Profile.route)
                    }
                )
            }
//        History Screen
            composable(route = CookingBuddyScreen.History.route) {
                HistoryScreen(
                    navigationAwareKey = 0,
                    onCardClick = { it ->
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "video",
                            value = it
                        )
                        navController.navigate(CookingBuddyScreen.Video.route)
                    }
                )
            }
//        Search Screen
            composable(
                route = CookingBuddyScreen.Search.route + "/{query}",
                arguments = CookingBuddyScreen.Search.arguments
            ) {
                val searchViewModel: SearchViewModel =
                    viewModel(factory = SearchViewModel.Factory)
                val query = it.arguments?.getString("query")
                SearchScreen(
                    query = query,
                    navigateUp = {
                        navController.navigateUp()
                    },
                    onSearch = {
                        searchViewModel.getRecipes(it)
                    },
                    onCardClick = { video ->
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "video",
                            value = video
                        )
                        navController.navigate(CookingBuddyScreen.Video.route)
                    }
                )
            }
//        Profile Screen
            composable(route = CookingBuddyScreen.Profile.route) {
                ProfileScreen(
                    seeAllHistory = {
                        navController.navigate(CookingBuddyScreen.History.route)
                    },
                    goToSignIn = {
                        val intent = Intent(context, AuthActivity::class.java)
                        context.startActivity(intent)
                        context.finish()
                    },
                    seeAllPlaylist = {
                        navController.navigate(CookingBuddyScreen.Playlist.route)
                    },
                    onPlaylistClick = { playlistId ->
                        navController.navigate(CookingBuddyScreen.Playlist.createRoute(playlistId.toString()))
                    },
                    onCardClick = { video ->
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "video",
                            value = video
                        )
                        navController.navigate(CookingBuddyScreen.Video.route)
                    }
                )
            }
//        Playlist Screen
            composable(route = CookingBuddyScreen.Playlist.route) {
                PlaylistsScreen(
                    onPlaylistClick = { playlistId ->
                        navController.navigate(CookingBuddyScreen.Playlist.createRoute(playlistId.toString()))
                    }
                )
            }
//        Playlist Item Screen
            composable(route = CookingBuddyScreen.Playlist.route + "/{playlistId}") {
                PlaylistItemScreen(
                    playlistId = it.arguments?.getString("playlistId")?.toInt() ?: 0,
                    onCardClick = { video ->
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "video",
                            value = video
                        )
                        navController.navigate(CookingBuddyScreen.Video.route)
                    }
                )
            }
//        Video Screen
            composable(route = CookingBuddyScreen.Video.route) {
                val video =
                    navController.previousBackStackEntry?.savedStateHandle?.get<Video>("video")
                VideoScreen(
                    video = video,
                    lifecycleOwner = localLifecycleOwner
                )
            }
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val items = listOf(
        CookingBuddyScreen.Home,
        CookingBuddyScreen.History,
        CookingBuddyScreen.Playlist,
        CookingBuddyScreen.Profile,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val shouldShowBottomBar = items.any {
        currentDestination?.hierarchy?.any { navDestination ->
            navDestination.route == it.route
        } == true
    }
    if (shouldShowBottomBar) {
        BottomAppBar(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            windowInsets = WindowInsets(left = 0.dp, right = 0.dp),
            containerColor = Color.Black
        ) {
            Row(
                modifier = Modifier
                    .navigationBarsPadding()
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    BottomBarItem(
                        screen = screen,
                        currentDestination = currentDestination,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun RowScope.BottomBarItem(
    screen: CookingBuddyScreen,
    currentDestination: androidx.navigation.NavDestination?,
    navController: NavHostController
) {
    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
    NavigationBarItem(
        icon = {
            Icon(
                imageVector = ImageVector.vectorResource(screen.icon),
                contentDescription = stringResource(screen.title),
                tint = if (selected) colorResource(R.color.gradient_start) else Color.White
            )
        },
        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = colorResource(R.color.gradient_start),
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            indicatorColor = Color.Transparent
        )
    )
}