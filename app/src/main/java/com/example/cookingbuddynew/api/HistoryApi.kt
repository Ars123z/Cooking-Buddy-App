package com.example.cookingbuddynew.api

import kotlinx.serialization.Serializable

@Serializable
data class History(
    val id: Int,
    val video_id: String,
    val title: String,
    val description: String,
    val channel_name: String,
    val thumbnail: String,
    val method: List<String>,
    val ingredient_list: List<List<String>>
)