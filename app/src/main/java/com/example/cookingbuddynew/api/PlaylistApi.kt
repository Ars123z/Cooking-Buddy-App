package com.example.cookingbuddynew.api


import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id: Int,
    val video_id: String,
    val title: String,
    val description: String,
    val channel_name: String,
    val thumbnail: String,
    val ingredient_list: List<List<String>>,
    val method: List<String>,
)

@Serializable
data class Playlist(
    val id: Int,
    val name: String,
    val videos: List<Item>,
    val user: Int
)

@Serializable
data class CreatePlaylistRequest(
    var name: String,
)
