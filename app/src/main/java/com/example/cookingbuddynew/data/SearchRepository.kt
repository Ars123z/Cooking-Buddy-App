package com.example.cookingbuddynew.data

import com.example.cookingbuddynew.api.ApiResponse
import com.example.cookingbuddynew.network.CookingBuddyApiService

interface SearchRepository {
    suspend fun getRecipes(query: String): ApiResponse
}

class SearchRepositoryImp(
    private val searchService: CookingBuddyApiService
): SearchRepository {
    override suspend fun getRecipes(query: String): ApiResponse {
        return searchService.getRecipes(query)
    }
}
