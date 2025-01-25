package com.example.cookingbuddynew.api

import kotlinx.serialization.Serializable

@Serializable
data class RecipeDetail(
    val ingredient_list: List<List<String>>,
    val method: List<String>
)

@Serializable
data class TranslateRequest(
    val id: String,
    val lang: String
)

@Serializable
data class TranslateResponse(
    val ingredient_list: List<List<String>>,
    val method: List<String>
)