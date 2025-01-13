package com.example.cookingbuddynew.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PlaylistsScreen(
    modifier: Modifier = Modifier
        .padding(16.dp),
    playlistViewModel: PlaylistViewModel = viewModel(factory = PlaylistViewModel.Factory)
) {

}