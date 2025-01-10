package com.example.cookingbuddynew.data

import android.content.Context
import com.example.cookingbuddynew.network.AppSessionManager
import com.example.cookingbuddynew.network.AuthApiService
import com.example.cookingbuddynew.network.AuthInterceptor
import com.example.cookingbuddynew.network.CookingBuddyApiService
import com.example.cookingbuddynew.utils.DataStoreManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface AppContainer {
    val authRepository: AuthRepository
}

val loginFlow = AppSessionManager.loginFlow

class DefaultAppContainer(context: Context) : AppContainer {
    private val BASE_URL = "http://192.168.1.4:8000/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(DataStoreManager(context), loginFlow)) // Add your custom interceptor here
        .build()

    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(Json
            .asConverterFactory("application/json".toMediaType())
        )
        .baseUrl(BASE_URL)
        .build()

    private val authRetrofitService: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }
    private val cookingBuddyRetrofitService: CookingBuddyApiService by lazy {
        retrofit.create(CookingBuddyApiService::class.java)
    }

    override val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(authRetrofitService)
    }
}
