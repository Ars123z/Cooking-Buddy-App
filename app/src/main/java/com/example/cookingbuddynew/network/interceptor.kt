package com.example.cookingbuddynew.network

import android.util.Log
import com.example.cookingbuddynew.api.RefreshRequest
import com.example.cookingbuddynew.utils.DataStoreManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Response
import retrofit2.Retrofit

val TAG = "NetworkService"
class AuthInterceptor(
    private val tokenProvider: DataStoreManager,
    private val loginFlow: MutableSharedFlow<Unit> // SharedFlow to notify about redirection
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        Log.i(TAG, "${originalRequest.body}")
        Log.i(TAG, "Request URL: ${originalRequest.url}")

        // Assume credentials are never empty and only need to handle staleness
        val token = runBlocking { tokenProvider.getFromDataStore().first() }

        // Check if the request is for a public endpoint (no token required)
        val isPublicEndpoint = originalRequest.url.encodedPath.contains("/social/google/") ||
                originalRequest.url.encodedPath.contains("/accounts/token/refresh/")

        // Modify the request based on token validity
        val modifiedRequest = if (isPublicEndpoint) {
            originalRequest // Public endpoints require no authorization headers
        } else if (!isTokenExpired(token.access_token_expiry)) {
            // Access token is valid, proceed with it
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer ${token.access_token}")
                .build()
        } else if (!isTokenExpired(token.refresh_token_expiry)) {
            // Access token is expired but refresh token is valid, try refreshing the token
            val newAccessToken = runBlocking { refreshAccessToken(token.refresh_token) }
            if (newAccessToken != null) {
                originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer $newAccessToken")
                    .build()
            } else {
                // If refreshing fails, notify the app to redirect to login
                loginFlow.tryEmit(Unit)
                originalRequest
            }
        } else {
            // Both tokens are stale, notify the app to redirect to login
            loginFlow.tryEmit(Unit)
            originalRequest
        }
Log.i(TAG, "Modified Request URL: ${modifiedRequest.url}")
        // Proceed with the modified request
        val response = chain.proceed(modifiedRequest)

        if (response.code == 403) {
            // Handle 403 errors, which indicate invalid tokens
            runBlocking {
                tokenProvider.clearDataStore() // Clear stored tokens
            }
            loginFlow.tryEmit(Unit) // Notify the app to redirect to login
        }
Log.i(TAG, "Response Code: ${response.code}")
        return response
    }

    /**
     * Checks if a token is expired based on its expiry time.
     */
    private fun isTokenExpired(expiryTime: Long): Boolean {
        return System.currentTimeMillis() > expiryTime
    }

    /**
     * Attempts to refresh the access token using the refresh token.
     *
     * @param refreshToken The refresh token to use for obtaining a new access token.
     * @return The new access token if successful, or null if refreshing fails.
     */
    private suspend fun refreshAccessToken(refreshToken: String): String? {
        return try {
            // Make a network request to refresh the access token
            val refreshResponse = Retrofit.Builder()
                .baseUrl("http://192.168.1.4:8000/") // Replace with your actual base URL
                .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(AuthApiService::class.java)
                .refreshToken(RefreshRequest(refresh = refreshToken))

            // Extract tokens
            val newAccessToken = refreshResponse.access
            val newRefreshToken = refreshResponse.refresh

            // Calculate new expiry times
            val newAccessTokenExpiry = System.currentTimeMillis() + 3600 * 1000 // 1 hour
            val newRefreshTokenExpiry = System.currentTimeMillis() + 7 * 24 * 3600 * 1000 // 7 days

            // Update the tokens and expiry times in the DataStore
            tokenProvider.updateTokens(
                newAccessToken,
                newRefreshToken,
                newAccessTokenExpiry,
                newRefreshTokenExpiry
            )

            newAccessToken // Return the new access token
        } catch (e: Exception) {
            null // Return null if an error occurs during the refresh process
        }
    }
}


