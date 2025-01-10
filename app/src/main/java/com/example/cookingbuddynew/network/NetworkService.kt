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
    @POST("social/google/")
    suspend fun getUserCredentials(@Body request: GoogleLoginRequest): UserDetails
    @GET("api/v1/history/")
    suspend fun fetchHistory(@Header("Authorization") token: String?): List<History>
    @GET("api/v1/playlists/")
    suspend fun fetchPlaylist(@Header("Authorization") token: String?): List<Playlist>
    @POST("api/v1/playlists/")
    suspend fun createPlaylist(@Header("Authorization") token: String?, @Body request: CreatePlaylistRequest): Playlist
    @GET("api/v1/playlists/{id}")
    suspend fun getPlaylist(@Header("Authorization") token: String?, @Path("id") id: Int): Playlist
}