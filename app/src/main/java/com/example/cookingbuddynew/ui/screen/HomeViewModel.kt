package com.example.cookingbuddynew.ui.screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.cookingbuddynew.CookingBuddyApplication
import com.example.cookingbuddynew.api.LabelApi
import com.example.cookingbuddynew.api.VideoDetails

import com.example.cookingbuddynew.data.SearchRepository
import kotlinx.coroutines.launch
import okio.IOException

private const val TAG = "HomeViewModel"

sealed interface LabelUiState {
    data class Success(val labels: List<LabelApi>): LabelUiState
    object Error: LabelUiState
    object Loading: LabelUiState
}

class HomeViewModel(val searchRepository: SearchRepository): ViewModel() {

    var labelUiState: LabelUiState by mutableStateOf(LabelUiState.Loading)
        private set

    fun fetchLabel(region: String) {
        viewModelScope.launch {
            try {
                val request = searchRepository.fetchLabels(region)
                Log.i(TAG, request.toString())
                labelUiState = LabelUiState.Success(request)
                Log.i(TAG, "Success")
            } catch (e: IOException) {
                Log.e(TAG, "Failure: ${e.message}")
                labelUiState = LabelUiState.Error
            }
        }
    }

    suspend fun videoDetails(videoId: Int): VideoDetails {
        return searchRepository.videoDetails(videoId)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CookingBuddyApplication)
                val searchRepository = application.container.searchRepository
                HomeViewModel(searchRepository = searchRepository)
            }
        }
    }
}