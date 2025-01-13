package com.example.cookingbuddynew

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.cookingbuddynew.ui.theme.CookingBuddyNewTheme
import com.example.cookingbuddynew.utils.DataStoreManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        val dataStoreManager = DataStoreManager(this)
        enableEdgeToEdge()
        setContent {
            CookingBuddyNewTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Check the token and redirect accordingly
                    var isTokenValid by remember { mutableStateOf<Boolean?>(null) }

                    LaunchedEffect(Unit) {
                        delay(2000)
                        val userDetails = dataStoreManager.getFromDataStore().first()
                        isTokenValid = userDetails.access_token.isNotEmpty()
                    }

                    // Display a loading indicator while determining token validity
                    if (isTokenValid == null) {
                        Box(
                            modifier = Modifier.fillMaxSize()
                                .padding(innerPadding)
                                .background(colorResource(R.color.primary)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        // Navigate to the appropriate screen
                        if (isTokenValid == true) {
                            val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            val intent = Intent(this@SplashScreenActivity, AuthActivity::class.java)
                            startActivity(intent)
                        }
                        finish()
                    }
                }
            }
        }
    }
}
