package com.example.cookingbuddynew.ui.screen.auth

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun RegisterOtpScreen(
    goToSignIn: () -> Unit,
) {
    Text(text = "RegisterOtp")
    var otp by remember { mutableStateOf("") }
    OutlinedTextField(
        value = "",
        onValueChange = {otp = it},
        label = { Text("OTP") },
    )
}