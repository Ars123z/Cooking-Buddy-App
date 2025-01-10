package com.example.cookingbuddynew.api

import kotlinx.serialization.Serializable

@Serializable
data class RecipeDetail(
    val ingredient_list: List<List<String>>,
    val method: List<String>
)