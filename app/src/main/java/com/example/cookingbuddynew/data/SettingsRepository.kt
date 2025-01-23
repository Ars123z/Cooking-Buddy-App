package com.example.cookingbuddynew.data

import com.example.cookingbuddynew.api.Profile
import com.example.cookingbuddynew.api.ProfileUpdateRequest
import com.example.cookingbuddynew.api.ProfileUpdateResponse
import com.example.cookingbuddynew.network.AuthApiService
import com.example.cookingbuddynew.network.CookingBuddyApiService

interface SettingsRepository {
    suspend fun getProfile(): Profile
    suspend fun updateProfile(request: ProfileUpdateRequest): ProfileUpdateResponse
}

class SettingsRepositoryImp(val settingsService: AuthApiService): SettingsRepository {
    override suspend fun getProfile(): Profile {
        return settingsService.getProfile()
    }

    override suspend fun updateProfile(request: ProfileUpdateRequest): ProfileUpdateResponse {
        return settingsService.updateProfile(request)
    }
}