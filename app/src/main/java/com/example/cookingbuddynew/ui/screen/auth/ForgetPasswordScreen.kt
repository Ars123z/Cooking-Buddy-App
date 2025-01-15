package com.example.cookingbuddynew.ui.screen.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.cookingbuddynew.R

//@Composable
//fun ForgetPasswordScreen(
//    goToForgetPasswordOTP: () -> Unit,
//) {
//    Text(text= "ResetPassword")
//    var email by remember { mutableStateOf("") }
//    OutlinedTextField(
//        value = "",
//        onValueChange = {email = it},
//        label = { Text("Email") },
//        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
//        keyboardActions = KeyboardActions(
//            onDone = {
//                goToForgetPasswordOTP()
//            }
//        ),
//        modifier = Modifier
//            .fillMaxWidth()
//    )
//}

@Composable
fun ForgetPasswordScreen(
    goToForgetPasswordOTP: () -> Unit,
    modifier: Modifier = Modifier.fillMaxSize()
) {
    var email by remember { mutableStateOf("") }
    ConstraintLayout(modifier = modifier.background(MaterialTheme.colorScheme.background)) {
        val (logoRow, header, emailField, sendOtpButton) = createRefs()
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

        // "Reset Password" Header
        Text(
            text = "Reset Password",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .constrainAs(header) {
                    top.linkTo(logoRow.bottom, margin = 32.dp)
                    start.linkTo(parent.start)
                }
                .padding(horizontal = 16.dp)
        )
        // Email Input Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    goToForgetPasswordOTP()
                }
            ),
            modifier = Modifier
                .constrainAs(emailField) {
                    top.linkTo(header.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        // "Send OTP" Button
        Button(
            onClick = goToForgetPasswordOTP,
            modifier = Modifier
                .constrainAs(sendOtpButton) {
                    top.linkTo(emailField.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(text = "Send OTP")
        }
    }
}
