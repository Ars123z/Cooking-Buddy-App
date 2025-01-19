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

import com.example.cookingbuddynew.data.SearchRepository
import kotlinx.coroutines.launch
import okio.IOException

private const val TAG = "HomeViewModel"

sealed interface HomeUiState {
    data class Success(val recipes: List<LabelApi>): HomeUiState
    object Error: HomeUiState
    object Loading: HomeUiState
}

class HomeViewModel(val searchRepository: SearchRepository): ViewModel() {

    var homeUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    fun fetchLabel(region: String) {
        viewModelScope.launch {
            try {
                val request = searchRepository.fetchLabels(region)
                homeUiState = HomeUiState.Success(request)
                Log.i(TAG, "Success")
            } catch (e: IOException) {
                Log.e(TAG, "Failure: ${e.message}")
                homeUiState = HomeUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CookingBuddyApplication)
                val searchRepository = application.container.searchRepository
                SearchViewModel(searchRepository = searchRepository)
            }
        }
    }
}