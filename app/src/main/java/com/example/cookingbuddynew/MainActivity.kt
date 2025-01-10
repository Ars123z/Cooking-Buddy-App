package com.example.cookingbuddynew

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.cookingbuddynew.ui.theme.CookingBuddyNewTheme
import com.example.cookingbuddynew.utils.DataStoreManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CookingBuddyNewTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val dataStoreManager = DataStoreManager(LocalContext.current)
                    val userDetails by dataStoreManager.getFromDataStore().collectAsState(initial = null)
                    Text(
                        text = "Hello World!",
                        modifier = Modifier.padding(innerPadding)
                    )
                    Text(
                        text = userDetails?.full_name ?: "No User Details",
                    )
                }
            }
        }
    }
}

