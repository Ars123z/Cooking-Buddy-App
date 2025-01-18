package com.example.cookingbuddynew.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.cookingbuddynew.CookingBuddyApplication
import com.example.cookingbuddynew.api.CreatePlaylistRequest
import com.example.cookingbuddynew.api.Playlist
import com.example.cookingbuddynew.api.UpdatePlaylistRequest
import com.example.cookingbuddynew.data.PlaylistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PlaylistViewModel(val playlistRepository: PlaylistRepository): ViewModel() {

    fun fetchPlaylist(): Flow<List<Playlist>> = flow {
        val playlist = playlistRepository.fetchPlaylist() // Network call
        emit(playlist)
    }.flowOn(Dispatchers.IO) // Ensure this runs on the IO dispatcher

    fun getPlaylist(id: Int): Flow<Any> = flow {
        val playlist = playlistRepository.getPlaylist(id) // Network call
        emit(playlist)
    }.flowOn(Dispatchers.IO)

    suspend fun createPlaylist(token: String, request: CreatePlaylistRequest): Playlist {
        return playlistRepository.createPlaylist(request) // Network call
    }

    suspend fun deletePlaylist(token: String, id: Int): Unit {
        return playlistRepository.deletePlaylist(id) // Network call
    }

    suspend fun updatePlaylist(id: Int, request: UpdatePlaylistRequest): Playlist {
        return playlistRepository.updatePlaylist(id, request) // Network call
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CookingBuddyApplication)
                val playlistRepository = application.container.playlistRepository
                PlaylistViewModel(playlistRepository = playlistRepository)
            }
        }
    }
}