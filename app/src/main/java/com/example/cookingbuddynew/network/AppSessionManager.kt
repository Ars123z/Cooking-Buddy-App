package com.example.cookingbuddynew.network

import kotlinx.coroutines.flow.MutableSharedFlow

object AppSessionManager {
    val loginFlow = MutableSharedFlow<Unit>()
}