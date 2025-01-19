package com.example.cookingbuddynew.network

import com.example.cookingbuddynew.api.ApiResponse
import com.example.cookingbuddynew.api.CreatePlaylistRequest
import com.example.cookingbuddynew.api.GoogleLoginRequest
import com.example.cookingbuddynew.api.History
import com.example.cookingbuddynew.api.HistorySearchResponse
import com.example.cookingbuddynew.api.HistoryUpdateRequest
import com.example.cookingbuddynew.api.HistoryUpdateResponse
import com.example.cookingbuddynew.api.LabelApi
import com.example.cookingbuddynew.api.Playlist
import com.example.cookingbuddynew.api.RecipeDetail
import com.example.cookingbuddynew.api.ResultItem
import com.example.cookingbuddynew.api.UpdatePlaylistRequest
import com.example.cookingbuddynew.api.UserDetails
import com.example.cookingbuddynew.api.Video
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CookingBuddyApiService {
    @GET("api/v1/search/")
    suspend fun getRecipes(@Query("q") query: String): ApiResponse
    @GET("api/v1/video/ingredient-method/{id}/")
    suspend fun getRecipeDetails(@Path("id") id: String): RecipeDetail
    @GET("api/v1/history/")
    suspend fun fetchHistory(): List<History>
    @POST("api/v1/update-watch-history/")
    suspend fun updateHistory(@Body request: HistoryUpdateRequest): HistoryUpdateResponse
    @GET("api/v1/history/search/")
    suspend fun searchHistory(@Query("q") query: String): HistorySearchResponse
    @GET("api/v1/playlists/")
    suspend fun fetchPlaylist(): List<Playlist>
    @POST("api/v1/playlists/")
    suspend fun createPlaylist(@Body request: CreatePlaylistRequest): Playlist
    @GET("api/v1/playlists/{id}")
    suspend fun getPlaylist(@Path("id") id: Int): Playlist
    @PATCH("api/v1/playlists/{id}/")
    suspend fun updatePlaylist(@Path("id") id: Int, @Body updatePlaylistRequest: UpdatePlaylistRequest): Playlist
    @GET("api/v1/labels/")
    suspend fun fetchLabels(@Query("region") region: String): List<LabelApi>
}