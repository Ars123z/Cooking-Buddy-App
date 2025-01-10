package com.example.cookingbuddynew.data

import com.example.cookingbuddynew.api.GoogleLoginRequest
import com.example.cookingbuddynew.api.UserDetails
import com.example.cookingbuddynew.network.AuthApiService


interface AuthRepository {
    suspend fun getUserCredentials(request: GoogleLoginRequest): UserDetails
}

class AuthRepositoryImpl(
    private val authService: AuthApiService
): AuthRepository {
    override suspend fun getUserCredentials(request: GoogleLoginRequest): UserDetails {
        return authService.googleLogin(request)
    }
}
