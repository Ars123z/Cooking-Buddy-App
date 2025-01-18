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

@Serializable
sealed class UpdatePlaylistRequest {
    @Serializable
    data class AddVideoRequest(val name: String,
                               val id: Int,
                               val add_video_ids: Array<String>
    ) : UpdatePlaylistRequest()

    @Serializable
    data class RemoveVideoRequest(
        val name: String,
        val id: Int,
        val remove_video_ids: Array<String>
    ) : UpdatePlaylistRequest()
}