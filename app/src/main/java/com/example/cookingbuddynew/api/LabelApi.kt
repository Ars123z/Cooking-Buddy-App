package com.example.cookingbuddynew.api

import kotlinx.serialization.Serializable

@Serializable
data class LabelApi(
    val id: Int,
    val name: String,
    val videos: List<Int>,
    val region: String,
    val last_updated: String
)