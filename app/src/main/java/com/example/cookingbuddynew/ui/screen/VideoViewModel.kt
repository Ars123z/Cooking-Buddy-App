package com.example.cookingbuddynew.ui.screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.cookingbuddynew.CookingBuddyApplication
import com.example.cookingbuddynew.api.RecipeDetail
import com.example.cookingbuddynew.api.TranslateRequest
import com.example.cookingbuddynew.api.TranslateResponse
import com.example.cookingbuddynew.data.VideoRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

private const val TAG = "VideoViewModel"

sealed interface VideoUiState {
    data class Success(val recipeDetails: RecipeDetail): VideoUiState
    object Error: VideoUiState
    object Loading: VideoUiState
}

class VideoViewModel(private val videoRepository: VideoRepository): ViewModel() {
    var videoUiState: VideoUiState by mutableStateOf(VideoUiState.Loading)
        private set


    fun getRecipeDetails(id: String) {
        viewModelScope.launch {
            videoUiState = VideoUiState.Loading
            videoUiState = try {
                val request = videoRepository.getRecipeDetails(id)
                val result = request
                Log.i(TAG, "Success")
                Log.d(TAG, "Success: $result")
                VideoUiState.Success(result)
            } catch (e: IOException) {
                Log.e(TAG, "Failure: ${e.message}")
                VideoUiState.Error
            } catch (e: HttpException) {
                Log.e(TAG, "Failure: ${e.message}")
                VideoUiState.Error
            } catch (e: RuntimeException) {
                Log.e(TAG, "Failure: ${e.message}")
                VideoUiState.Error
            }
        }
    }

    fun translate(request: TranslateRequest) {
        viewModelScope.launch {
            videoUiState = VideoUiState.Loading
            videoUiState = try {
                val response = videoRepository.translate(request)
                Log.i(TAG, "Success")
                Log.d(TAG, "Success: $response")
                VideoUiState.Success(response)
            } catch (e: IOException) {
                Log.e(TAG, "Failure: ${e.message}")
                VideoUiState.Error
            } catch (e: HttpException) {
                Log.e(TAG, "Failure: ${e.message}")
                VideoUiState.Error
            } catch (e: RuntimeException) {
                Log.e(TAG, "Failure: ${e.message}")
                VideoUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CookingBuddyApplication)
                val videoRepository = application.container.videoRepository
                VideoViewModel(videoRepository = videoRepository)
            }
        }
    }
}