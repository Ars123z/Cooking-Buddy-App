package com.example.cookingbuddynew.data

import com.example.cookingbuddynew.api.CreatePlaylistRequest
import com.example.cookingbuddynew.api.History
import com.example.cookingbuddynew.api.HistoryUpdateResponse
import com.example.cookingbuddynew.api.Playlist
import com.example.cookingbuddynew.api.ResultItem
import com.example.cookingbuddynew.network.CookingBuddyApiService

interface ProfileRepository {
    suspend fun fetchHistory(): List<History>
    suspend fun fetchPlaylist(): List<Playlist>
    suspend fun getPlaylist(id: Int): Playlist
    suspend fun createPlaylist(request: CreatePlaylistRequest): Playlist
}

class ProfileRepositoryImpl(private val profileService: CookingBuddyApiService) : ProfileRepository {
    override suspend fun fetchHistory(): List<History> {
        return profileService.fetchHistory()
    }
    override suspend fun fetchPlaylist(): List<Playlist> {
        return profileService.fetchPlaylist()
    }
    override suspend fun getPlaylist(id: Int): Playlist {
        return profileService.getPlaylist(id)
    }
    override suspend fun createPlaylist(request: CreatePlaylistRequest): Playlist {
        return profileService.createPlaylist(request)
    }
}