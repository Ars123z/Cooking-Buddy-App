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
data class RegisterRequest(
    val email: String,
    val password: String,
    val first_name: String,
    val last_name: String,
    val password2: String
)

@Serializable
data class RegisterData(
    val email: String,
    val first_name: String,
    val last_name: String
)
@Serializable
data class RegisterResponse(
    val data: RegisterData,
    val message: String,
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

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val email: String,
    val full_name: String,
    val access_token: String,
    val refresh_token: String
)