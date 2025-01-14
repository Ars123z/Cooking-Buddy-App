package com.example.cookingbuddynew

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.cookingbuddynew.api.GoogleLoginRequest
import com.example.cookingbuddynew.api.LoginRequest
import com.example.cookingbuddynew.api.LoginResponse
import com.example.cookingbuddynew.api.RegisterRequest
import com.example.cookingbuddynew.api.RegisterResponse
import com.example.cookingbuddynew.api.UserDetails
import com.example.cookingbuddynew.data.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun getUserData(request: GoogleLoginRequest): Flow<UserDetails> = flow {
        val user = authRepository.getUserCredentials(request) // Network call
        emit(user)
    }.flowOn(Dispatchers.IO)

    suspend fun register(request: RegisterRequest): RegisterResponse {
        return authRepository.register(request)
    }
    suspend fun login(request: LoginRequest): LoginResponse {
        return authRepository.login(request)
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CookingBuddyApplication)
                val authRepository = application.container.authRepository
                AuthViewModel(authRepository)
            }
        }
    }
}