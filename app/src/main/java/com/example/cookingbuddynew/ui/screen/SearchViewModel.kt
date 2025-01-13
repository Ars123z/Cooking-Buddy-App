package com.example.cookingbuddynew.ui.screen

import android.util.Log
import android.widget.Toast
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
import com.example.cookingbuddynew.api.HistoryUpdateRequest
import com.example.cookingbuddynew.api.HistoryUpdateResponse
import com.example.cookingbuddynew.api.ResultItem
import com.example.cookingbuddynew.data.SearchRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

private const val TAG = "SearchViewModel"


sealed interface SearchUiState {
    data class Success(val recipes: List<ResultItem>): SearchUiState
    object Error: SearchUiState
    object Loading: SearchUiState
}


class SearchViewModel(private val searchRepository: SearchRepository): ViewModel() {
    var searchUiState: SearchUiState by mutableStateOf(SearchUiState.Loading)
        private set

    fun updateHistory(request: HistoryUpdateRequest) {
        viewModelScope.launch {
            try {
                searchRepository.updateHistory(request)
            } catch(e: IOException) {
                Log.e(TAG, "Failure: ${e.message}")
            }
            catch(e: HttpException) {
                Log.e(TAG, "Failure: ${e.message}")
            }
        }
    }
    fun getRecipes(query: String) {
        viewModelScope.launch {
            searchUiState = SearchUiState.Loading
            searchUiState = try {
                val request = searchRepository.getRecipes(query)
                val result = request.results
                Log.i(TAG, "Success")
                SearchUiState.Success(result)
            } catch(e: IOException) {
                Log.e(TAG, "Failure: ${e.message}")
                SearchUiState.Error
            }
            catch(e: HttpException) {
                Log.e(TAG, "Failure: ${e.message}")
                SearchUiState.Error
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