package com.example.cookingbuddynew.api

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val language: String,
    val region: String,
    val subscription: Boolean,
    val subscription_validity_date: String?,
)
@Serializable
data class ProfileUpdateRequest(
    val language: String,
    val region: String,
    )

@Serializable
data class ProfileUpdateResponse(
    val language: String,
    val region: String,
    val subscription: Boolean,
    val subscription_validity_date: String?,
)