package com.example.cookingbuddynew.data

import com.example.cookingbuddynew.api.GoogleLoginRequest
import com.example.cookingbuddynew.api.LoginRequest
import com.example.cookingbuddynew.api.LoginResponse
import com.example.cookingbuddynew.api.RegisterRequest
import com.example.cookingbuddynew.api.RegisterResponse
import com.example.cookingbuddynew.api.UserDetails
import com.example.cookingbuddynew.network.AuthApiService


interface AuthRepository {
    suspend fun getUserCredentials(request: GoogleLoginRequest): UserDetails
    suspend fun register(request: RegisterRequest): RegisterResponse
    suspend fun login(request: LoginRequest): LoginResponse
}

class AuthRepositoryImpl(
    private val authService: AuthApiService
): AuthRepository {
    override suspend fun getUserCredentials(request: GoogleLoginRequest): UserDetails {
        return authService.googleLogin(request)
    }
    override suspend fun register(request: RegisterRequest): RegisterResponse {
        return authService.register(request)
    }
    override suspend fun login(request: LoginRequest): LoginResponse {
        return authService.login(request)
    }
}
