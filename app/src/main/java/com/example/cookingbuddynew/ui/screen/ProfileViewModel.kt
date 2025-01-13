package com.example.cookingbuddynew.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.cookingbuddynew.CookingBuddyApplication
import com.example.cookingbuddynew.api.CreatePlaylistRequest
import com.example.cookingbuddynew.api.History
import com.example.cookingbuddynew.api.Playlist
import com.example.cookingbuddynew.data.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class ProfileViewModel(private val profileRepository: ProfileRepository): ViewModel() {

    fun fetchHistory(): Flow<List<History>> = flow {
        try {
            val history = profileRepository.fetchHistory() // Network call
            emit(history)
        } catch (e: Exception) {
            // Handle the exception here
            Log.e("HistoryRepository", "Error fetching history", e)
            // You can emit an empty list, an error state, or rethrow the exception
            emit(emptyList()) // Example: Emit an empty list on error
            // Or: throw e // Rethrow the exception if you want to handle it upstream
        }
    }.flowOn(Dispatchers.IO)


    fun fetchPlaylist(id: String): Flow<List<Playlist>> = flow {
        val playlist = profileRepository.fetchPlaylist() // Network call
        emit(playlist)
    }.flowOn(Dispatchers.IO) // Ensure this runs on the IO dispatcher

    fun getPlaylist(token: String, id: Int): Flow<Any> = flow {
        val playlist = profileRepository.getPlaylist(id) // Network call
        emit(playlist)
    }.flowOn(Dispatchers.IO)

    suspend fun createPlaylist(token: String, request: CreatePlaylistRequest): Playlist {
        return profileRepository.createPlaylist(request) // Network call
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CookingBuddyApplication)
                val profileRepository = application.container.profileRepository
                ProfileViewModel(profileRepository = profileRepository)
            }
        }
    }
}