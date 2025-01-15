package com.example.cookingbuddynew.ui.screen.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.cookingbuddynew.R

//@Composable
//fun InitialScreen(
//    goToRegister: () -> Unit,
//    goToSignIn: () -> Unit,
//    modifier: Modifier = Modifier.fillMaxSize()
//) {
//    Box(
//        modifier = modifier.background(color = MaterialTheme.colorScheme.background),
//        contentAlignment = Alignment.Center // Center content in the Box
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth() // Fill the width of the Box
//                .padding(16.dp), // Add some padding
//            verticalArrangement = Arrangement.Center, // Center vertically
//            horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
//        ) {
//            Text(
//                text = "CookVerse",
//                style = MaterialTheme.typography.headlineMedium,
//                modifier = Modifier.padding(bottom = 32.dp) // Add some space below the text
//            )
//            OutlinedButton( // Outlined button for Register
//                onClick = goToRegister,
//                modifier = Modifier
//                    .fillMaxWidth() // Full width button
//                    .padding(bottom = 8.dp) // Add some space below the button
//            ) {
//                Text(text = "Register")
//            }
//            Button( // Filled button for Sign In
//                onClick = goToSignIn,
//                modifier = Modifier.fillMaxWidth() // Full width button
//            ) {
//                Text(text = "Sign In")
//            }
//        }
//    }
//}

@Composable
fun InitialScreen(
    goToRegister: () -> Unit,
    goToSignIn: () -> Unit,
    modifier: Modifier = Modifier.fillMaxSize()
) {
    ConstraintLayout(
        modifier = modifier.background(color = MaterialTheme.colorScheme.background)
    ) {
        val (logoRow, buttonsColumn) = createRefs()
        // Logo and Name (Top Left)
        Row(
            modifier = Modifier
                .constrainAs(logoRow) {
                    top.linkTo(parent.top, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(40.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = "CookVerse",
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        // Buttons (Centered)
        Column(
            modifier = Modifier
                .constrainAs(buttonsColumn) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedButton(
                onClick = goToRegister,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text(text = "Register")
            }
            Button(
                onClick = goToSignIn,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Sign In")
            }
        }
    }
}