package com.example.cookingbuddynew.network


import com.example.cookingbuddynew.api.GoogleLoginRequest
import com.example.cookingbuddynew.api.LoginRequest
import com.example.cookingbuddynew.api.LoginResponse
import com.example.cookingbuddynew.api.Profile
import com.example.cookingbuddynew.api.ProfileUpdateRequest
import com.example.cookingbuddynew.api.ProfileUpdateResponse
import com.example.cookingbuddynew.api.RefreshRequest
import com.example.cookingbuddynew.api.RefreshResult
import com.example.cookingbuddynew.api.RegisterRequest
import com.example.cookingbuddynew.api.RegisterResponse
import com.example.cookingbuddynew.api.UserDetails
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface AuthApiService {
    @POST("social/google/")
    suspend fun googleLogin(@Body request: GoogleLoginRequest): UserDetails

    @POST("accounts/register/")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("accounts/login/")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("accounts/verify-email/")
    suspend fun verifyEmail(@Body request: RefreshRequest): RefreshResult

    @POST("accounts/password-reset/")
    suspend fun passwordReset(@Body request: RefreshRequest): RefreshResult


    @POST("accounts/token/refresh/")
    suspend fun refreshToken(@Body request: RefreshRequest): RefreshResult

    @GET("accounts/profile/")
    suspend fun getProfile(): Profile

    @PATCH("accounts/profile/")
    suspend fun updateProfile(@Body request: ProfileUpdateRequest): ProfileUpdateResponse


}