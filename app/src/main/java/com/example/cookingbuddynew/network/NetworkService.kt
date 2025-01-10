package com.example.cookingbuddynew.network

import com.example.cookingbuddynew.api.ApiResponse
import com.example.cookingbuddynew.api.CreatePlaylistRequest
import com.example.cookingbuddynew.api.GoogleLoginRequest
import com.example.cookingbuddynew.api.History
import com.example.cookingbuddynew.api.Playlist
import com.example.cookingbuddynew.api.RecipeDetail
import com.example.cookingbuddynew.api.UserDetails
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CookingBuddyApiService {
    @GET("api/v1/search/")
    suspend fun getRecipes(@Query("q") query: String): ApiResponse
    @GET("api/v1/video/ingredient-method/{id}")
    suspend fun getRecipeDetails(@Path("id") id: String): RecipeDetail
    @GET("api/v1/history/")
    suspend fun fetchHistory(): List<History>
    @GET("api/v1/playlists/")
    suspend fun fetchPlaylist(): List<Playlist>
    @POST("api/v1/playlists/")
    suspend fun createPlaylist(@Body request: CreatePlaylistRequest): Playlist
    @GET("api/v1/playlists/{id}")
    suspend fun getPlaylist(@Path("id") id: Int): Playlist
}