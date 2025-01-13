package com.example.cookingbuddynew.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.cookingbuddynew.CookingBuddyApplication
import com.example.cookingbuddynew.api.History
import com.example.cookingbuddynew.api.HistorySearchResponse
import com.example.cookingbuddynew.data.HistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class HistoryViewModel(val historyRepository: HistoryRepository): ViewModel() {

    fun searchHistory(query: String): Flow<HistorySearchResponse> = flow {
        val history = historyRepository.searchHistory(query)
        emit(history)
    }.flowOn(Dispatchers.IO)

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CookingBuddyApplication)
                val historyRepository = application.container.historyRepository
                HistoryViewModel(historyRepository = historyRepository)
            }
        }
    }
}