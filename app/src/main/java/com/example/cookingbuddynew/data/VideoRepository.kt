package com.example.cookingbuddynew.data

import com.example.cookingbuddynew.api.RecipeDetail
import com.example.cookingbuddynew.api.TranslateRequest
import com.example.cookingbuddynew.api.TranslateResponse
import com.example.cookingbuddynew.network.CookingBuddyApiService

interface VideoRepository {
    suspend fun getRecipeDetails(id: String): RecipeDetail
    suspend fun translate(request: TranslateRequest): RecipeDetail
}

class VideoRepositoryImpl(private val videoService: CookingBuddyApiService) : VideoRepository {
    override suspend fun getRecipeDetails(id: String): RecipeDetail {
        return videoService.getRecipeDetails(id)
    }
    override suspend fun translate(request: TranslateRequest): RecipeDetail {
        return videoService.translate(request)
    }


}