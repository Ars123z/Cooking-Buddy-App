package com.example.cookingbuddynew

import android.app.Application
import com.example.cookingbuddynew.data.AppContainer
import com.example.cookingbuddynew.data.DefaultAppContainer

class CookingBuddyApplication: Application()  {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }

}