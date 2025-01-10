package com.example.cookingbuddynew.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.cookingbuddynew.api.UserDetails
import kotlinx.coroutines.flow.map

const val USER_DATASTORE = "user_data"

val Context.preferenceDataStore : DataStore<Preferences> by preferencesDataStore(name = USER_DATASTORE)

class DataStoreManager(val context: Context) {

    companion object{

        val EMAIL = stringPreferencesKey("EMAIL")
        val FULL_NAME = stringPreferencesKey("FULL_NAME")
        val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
        val REFRESH_TOKEN = stringPreferencesKey("REFRESH_TOKEN")
        val PICTURE = stringPreferencesKey("PICTURE")
        val ACCESS_TOKEN_EXPIRY = longPreferencesKey("ACCESS_TOKEN_EXPIRY")
        val REFRESH_TOKEN_EXPIRY = longPreferencesKey("REFRESH_TOKEN_EXPIRY")

    }

    suspend fun saveToDataStore(userDetails: UserDetails){
        context.preferenceDataStore.edit {
            it[EMAIL] = userDetails.email
            it[FULL_NAME] = userDetails.full_name
            it[PICTURE] = userDetails.picture
            it[ACCESS_TOKEN] = userDetails.access_token
            it[REFRESH_TOKEN] = userDetails.refresh_token
            it[ACCESS_TOKEN_EXPIRY] = userDetails.access_token_expiry
            it[REFRESH_TOKEN_EXPIRY] = userDetails.refresh_token_expiry
        }
    }

    fun getFromDataStore() = context.preferenceDataStore.data.map {
        UserDetails(
            email = it[EMAIL]?:"",
            access_token = it[ACCESS_TOKEN]?:"",
            refresh_token = it[REFRESH_TOKEN]?:"",
            full_name = it[FULL_NAME]?:"",
            picture = it[PICTURE]?:"",
            access_token_expiry = it[ACCESS_TOKEN_EXPIRY]?: 0L,
            refresh_token_expiry = it[REFRESH_TOKEN_EXPIRY]?:0L

        )
    }

    suspend fun updateTokens(
        newAccessToken: String,
        newRefreshToken: String,
        newAccessTokenExpiry: Long,
        newRefreshTokenExpiry: Long
    ) {
        context.preferenceDataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = newAccessToken
            preferences[REFRESH_TOKEN] = newRefreshToken
            preferences[ACCESS_TOKEN_EXPIRY] = newAccessTokenExpiry
            preferences[REFRESH_TOKEN_EXPIRY] = newRefreshTokenExpiry
        }
    }

    suspend fun clearDataStore() = context.preferenceDataStore.edit {
        it.clear()
    }
}