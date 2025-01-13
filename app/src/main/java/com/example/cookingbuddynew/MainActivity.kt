package com.example.cookingbuddynew

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.cookingbuddynew.ui.theme.CookingBuddyNewTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    CookingBuddyNewTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            CookingBuddyApp() // Your main UI content
        }
    }
}

