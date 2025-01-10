package com.example.cookingbuddynew.data

import com.example.cookingbuddynew.api.RecipeDetail
import com.example.cookingbuddynew.network.CookingBuddyApiService

interface VideoRepository {
    suspend fun getRecipeDetails(id: String): RecipeDetail
}

class VideoRepositoryImpl(private val videoService: CookingBuddyApiService) : VideoRepository {
    override suspend fun getRecipeDetails(id: String): RecipeDetail {
        return videoService.getRecipeDetails(id)
    }

}