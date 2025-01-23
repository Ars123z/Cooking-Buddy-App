package com.example.cookingbuddynew.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.cookingbuddynew.CookingBuddyApplication
import com.example.cookingbuddynew.api.Profile
import com.example.cookingbuddynew.api.ProfileUpdateRequest
import com.example.cookingbuddynew.api.ProfileUpdateResponse
import com.example.cookingbuddynew.data.SettingsRepository

class SettingsViewModel(val settingsRepository: SettingsRepository): ViewModel() {

    suspend fun updateProfile(request: ProfileUpdateRequest): ProfileUpdateResponse {
        return settingsRepository.updateProfile(request)
    }

    suspend fun getProfile(): Profile  {
        return settingsRepository.getProfile()
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CookingBuddyApplication)
                SettingsViewModel(application.container.settingsRepository)
            }
        }
    }
}