package com.example.cookingbuddynew.data

import com.example.cookingbuddynew.api.CreatePlaylistRequest
import com.example.cookingbuddynew.api.Playlist
import com.example.cookingbuddynew.network.CookingBuddyApiService

interface PlaylistRepository {
    suspend fun fetchPlaylist(): List<Playlist>
    suspend fun getPlaylist(id: Int): Playlist
    suspend fun createPlaylist(request: CreatePlaylistRequest): Playlist
}

class PlaylistRepositoryImpl(private val playlistService: CookingBuddyApiService) : PlaylistRepository {
    override suspend fun fetchPlaylist(): List<Playlist> {
        return playlistService.fetchPlaylist()
    }
    override suspend fun getPlaylist(id: Int): Playlist {
        return playlistService.getPlaylist(id)
    }
    override suspend fun createPlaylist(request: CreatePlaylistRequest): Playlist {
        return playlistService.createPlaylist(request)
    }
}