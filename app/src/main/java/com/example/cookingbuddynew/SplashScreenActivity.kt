package com.example.cookingbuddynew

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                    var showContent by remember { mutableStateOf(true) } // New state for animation

                    LaunchedEffect(Unit) {
                        delay(2000)
                        val userDetails = dataStoreManager.getFromDataStore().first()
                        isTokenValid = userDetails.access_token.isNotEmpty()
                        showContent = false // Trigger the shrinking animation
                        delay(1000) // Wait for the animation to complete
//                        Navigate to the appropriate Screen
                        if (isTokenValid == true) {
                            val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            val intent = Intent(this@SplashScreenActivity, AuthActivity::class.java)
                            startActivity(intent)
                        }
                        finish()
                    }

                    // Display a loading indicator while determining token validity
                    AnimatedVisibility(
                        visible = showContent,
                        enter = fadeIn(animationSpec = tween(durationMillis = 1000)) + expandIn(
                            expandFrom = Alignment.Center,
                            animationSpec = tween(durationMillis = 1000)
                        ),
                        exit = fadeOut(animationSpec = tween(durationMillis = 1000)) + scaleOut(
                            targetScale = 2f,
                            animationSpec = tween(durationMillis = 1000)
                        )
                    ){
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .background(colorResource(R.color.primary)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row() {
                                    Text(
                                        text = "Cook",
                                        style = TextStyle(
                                            fontFamily = FontFamily(
                                                Font(
                                                    resId = R.font.benzin_bold
                                                )
                                            ),
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 40.sp,
                                            lineHeight = 24.sp,
                                            letterSpacing = (-1).sp
                                        ),
                                        color = Color.Black
                                    )
                                    Text(
                                        text = "Verse",
                                        style = TextStyle(
                                            fontFamily = FontFamily(
                                                Font(
                                                    resId = R.font.benzin_bold
                                                )
                                            ),
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 40.sp,
                                            lineHeight = 24.sp,
                                            letterSpacing = (-1).sp
                                        ),
                                        color = Color(0xFF308415)
                                    )
                                }
                                Text(
                                    text = "Most Extensive Cookery App!",
                                    style = TextStyle(
                                        fontFamily = FontFamily(
                                            Font(resId = R.font.kulturrrno_parallone_regular),
                                        ),
                                        fontWeight = FontWeight.W600,
                                        fontSize = 16.sp,
                                        lineHeight = 24.sp,
                                        letterSpacing = 0.5.sp
                                    ),
                                    color = Color.Black,
                                    textAlign = TextAlign.Center,
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                CircularProgressIndicator(
                                    color = Color(0xFF308415),
                                    strokeWidth = 5.dp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
