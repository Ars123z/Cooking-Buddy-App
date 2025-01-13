package com.example.cookingbuddynew.data

import com.example.cookingbuddynew.api.History
import com.example.cookingbuddynew.api.HistorySearchResponse
import com.example.cookingbuddynew.network.CookingBuddyApiService

interface HistoryRepository {
    suspend fun fetchHistory(): List<History>
    suspend fun searchHistory(query: String): HistorySearchResponse
}

class HistoryRepositoryImpl(private val historyService: CookingBuddyApiService) : HistoryRepository {
    override suspend fun fetchHistory(): List<History> {
        return historyService.fetchHistory()
    }

    override suspend fun searchHistory(query: String): HistorySearchResponse {
        return historyService.searchHistory(query)
    }
}