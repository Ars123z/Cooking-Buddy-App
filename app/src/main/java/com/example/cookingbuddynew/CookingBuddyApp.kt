package com.example.cookingbuddynew

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cookingbuddynew.api.Video
import com.example.cookingbuddynew.ui.screen.HistoryScreen
import com.example.cookingbuddynew.ui.screen.SearchViewModel
import com.example.cookingbuddynew.ui.screen.HomeScreen
import com.example.cookingbuddynew.ui.screen.ProfileScreen
import com.example.cookingbuddynew.ui.screen.SearchScreen
import com.example.cookingbuddynew.ui.screen.VideoScreen


enum class CookingBuddyScreen(
    @StringRes val title: Int,
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val arguments: List<NamedNavArgument> = emptyList(),
) {
    Home(
        title = R.string.home,
        route = "home",
        icon = androidx.compose.material.icons.Icons.Filled.Home
    ),
    History(
        title = R.string.history,
        route = "history",
        icon = androidx.compose.material.icons.Icons.Filled.Home
    ),
    Search(
        title = R.string.search,
        route = "search",
        arguments = listOf(navArgument("query") {
            type = NavType.StringType
        }),
        icon = androidx.compose.material.icons.Icons.Filled.Home
    ),
    Profile(
        title = R.string.profile,
        route = "profile",
        icon = androidx.compose.material.icons.Icons.Filled.Home
    ),
    Playlist(
        title = R.string.playlist,
        route = "playlist",
        icon = androidx.compose.material.icons.Icons.Filled.Home
    ),
    Video(
        title = R.string.video,
        route = "video",
        icon = androidx.compose.material.icons.Icons.Filled.Home,
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
                seeAllPlaylist = {
                    navController.navigate(CookingBuddyScreen.Playlist.route)
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
            Button(onClick = {
                navController.navigate(CookingBuddyScreen.History.route)
            }
            ) {}
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