package com.example.cookingbuddynew.data

import com.example.cookingbuddynew.api.History
import com.example.cookingbuddynew.network.CookingBuddyApiService

interface HistoryRepository {
    suspend fun fetchHistory(): List<History>
}

class HistoryRepositoryImpl(private val historyService: CookingBuddyApiService) : HistoryRepository {
    override suspend fun fetchHistory(): List<History> {
        return historyService.fetchHistory()
    }
}