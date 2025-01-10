package com.example.cookingbuddynew.api

import kotlinx.serialization.Serializable

@Serializable
data class UserDetails(
    val email: String,
    val full_name: String,
    val picture: String,
    var access_token: String,
    var refresh_token: String,
    var access_token_expiry: Long = 0L,
    var refresh_token_expiry: Long = 0L
)

@Serializable
data class GoogleLoginRequest(
    val access_token: String
)

@Serializable
data class RefreshRequest(
    val refresh: String
)

@Serializable
data class RefreshResult(
    val access: String,
    val refresh: String
)