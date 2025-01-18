package com.example.cookingbuddynew.ui.screen.auth

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPasswordOption
import androidx.credentials.PasswordCredential
import androidx.credentials.exceptions.CreateCredentialCancellationException
import androidx.credentials.exceptions.CreateCredentialException
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cookingbuddynew.AuthViewModel
import com.example.cookingbuddynew.MainActivity
import com.example.cookingbuddynew.R
import com.example.cookingbuddynew.api.LoginRequest
import com.example.cookingbuddynew.api.UserDetails
import com.example.cookingbuddynew.utils.DataStoreManager
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    goToRegister: () -> Unit,
    goToForgetPassword: () -> Unit,
    dataStoreManager: DataStoreManager = DataStoreManager(LocalContext.current),
    modifier: Modifier = Modifier.background(MaterialTheme.colorScheme.background),
    authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory),
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val credentialManager = CredentialManager.create(context)
    val onSignIn: () -> Unit = {
        try {
            coroutineScope.launch {
                val credentialResponse = credentialManager.getCredential(
                    context = context,
                    request = GetCredentialRequest(
                        credentialOptions = listOf(GetPasswordOption())
                    )
                )
                val credential = credentialResponse.credential as? PasswordCredential
                val password = credential?.password ?: ""
                val id = credential?.id ?: ""
                val user = authViewModel.login(LoginRequest(id, password))
                Log.d("user", user.access_token)
                Log.d("user", user.refresh_token)
                Log.d("user", user.email)
                Log.d("user", user.full_name)
                val userDetails = UserDetails(
                    email = user.email,
                    full_name = user.full_name,
                    picture = "",
                    access_token = user.access_token,
                    refresh_token = user.refresh_token,
                    access_token_expiry = System.currentTimeMillis() + 24 * 60 * 60 * 1000,
                    refresh_token_expiry = System.currentTimeMillis() + 604800000
                )
                dataStoreManager.saveToDataStore(userDetails)
                Toast.makeText(
                    context,
                    "you are signed in as ${user.full_name}",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            }
            Toast.makeText(context, "Account created successfully", Toast.LENGTH_SHORT).show()
        } catch (e: GetCredentialCancellationException) {
            Toast.makeText(context, "Sign-in failed", Toast.LENGTH_SHORT).show()
        } catch (e: GetCredentialException) {
            e.printStackTrace()
        }
    }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (logoRow, card, signInHeader) = createRefs()

        // Logo and Text (Top Right)
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
                modifier = Modifier.size(40.dp), // Smaller logo size
                contentScale = ContentScale.Fit
            )
            Text(
                text = "CookVerse",
                fontSize = 20.sp, // Smaller text size
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // Sign In Header (Outside the Card)
        Text(
            text = "Sign In",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .constrainAs(signInHeader) {
                    top.linkTo(logoRow.bottom, margin = 32.dp)
                    start.linkTo(parent.start)
                }
                .padding(horizontal = 16.dp)
        )
        // Sign In Form Card
        Card(
            modifier = Modifier
                .constrainAs(card) {
                    top.linkTo(signInHeader.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Email Input
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp)
                        .focusRequester(emailFocusRequester)
                        .clip(RoundedCornerShape(8.dp)), // Rounded corners
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            passwordFocusRequester.requestFocus()
                        }
                    )
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Password Input
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(passwordFocusRequester)
                            .clip(RoundedCornerShape(8.dp)), // Rounded corners
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                // onSignIn() // Removed sign-in logic
                            }
                        ),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = if (passwordVisible) "Hide Password" else "Show Password"
                                )
                            }
                        }
                    )

                    TextButton(
                        onClick = goToForgetPassword,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(text = "Forgot Password?")
                    }
                }

                // Sign In Button
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        onSignIn()
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor = colorResource(id = R.color.gradient_start),
                        ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Sign In")
                }

                // Sign In with Google Button
                SignInWithGoogleButton(dataStoreManager = DataStoreManager(LocalContext.current)) // Removed dataStoreManager
                Button(
                    onClick = goToRegister,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor = colorResource(id = R.color.gradient_start),
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Register")
                }
            }
        }
    }
}
