package com.example.cookingbuddynew.ui.screen.auth

import android.widget.Toast
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.CreateCredentialCancellationException
import androidx.credentials.exceptions.CreateCredentialException
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cookingbuddynew.AuthViewModel
import com.example.cookingbuddynew.R
import com.example.cookingbuddynew.ui.screen.auth.SignInWithGoogleButton
import com.example.cookingbuddynew.api.RegisterRequest
import com.example.cookingbuddynew.utils.DataStoreManager
import kotlinx.coroutines.launch

//@Composable
//fun RegisterScreen(
//    goToRegisterOTP: () -> Unit,
//    goToSignIn: ()-> Unit,
//    authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory),
//    modifier: Modifier = Modifier
//) {
//    val context = LocalContext.current
//    val coroutineScope = rememberCoroutineScope()
//    val credentialManager = CredentialManager.create(context)
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var passwordVisible by remember { mutableStateOf(false) }
//    var password2 by remember { mutableStateOf("") }
//    var firstName by remember { mutableStateOf("") }
//    var lastName by remember { mutableStateOf("") }
//
//    val focusManager = LocalFocusManager.current
//    val passwordFocusRequester = remember { FocusRequester() }
//    val password2FocusRequester = remember { FocusRequester() }
//    val emailFocusRequester = remember { FocusRequester() }
//    val firstNameFocusRequester = remember { FocusRequester() }
//    val lastNameFocusRequester = remember { FocusRequester() }
//    val onSignUp: (RegisterRequest) -> Unit = { request ->
//        try {
//            coroutineScope.launch {
//                credentialManager.createCredential(
//                    context = context,
//                    request = CreatePasswordRequest(
//                        id = request.email,
//                        password = request.password
//                    )
//                )
//                authViewModel.register(request)
//                Toast.makeText(context, "Account created successfully", Toast.LENGTH_SHORT).show()
//            }
//        } catch (e: CreateCredentialCancellationException) {
//            e.printStackTrace()
//        } catch(e: CreateCredentialException) {
//            e.printStackTrace()
//        }
//    }
////    Fuction Finished
//    Box(
//        modifier = modifier
//            .fillMaxSize()
//            .background(color = MaterialTheme.colorScheme.background),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            LogoDisplay()
//            Text(text = "Cooking Buddy", style = MaterialTheme.typography.headlineMedium)
//            Text(text = "Register", style = MaterialTheme.typography.titleLarge)
//            // Email Input
//            OutlinedTextField(
//                value = email,
//                onValueChange = { email = it },
//                label = { Text("Email") },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp)
//                    .focusRequester(emailFocusRequester),
//                keyboardOptions = KeyboardOptions(
//                    keyboardType = KeyboardType.Email,
//                    imeAction = ImeAction.Next
//                ),
//                keyboardActions = KeyboardActions(
//                    onNext = {
//                        passwordFocusRequester.requestFocus()
//                    }
//                )
//            )
//
//            // Password Input
//            OutlinedTextField(
//                value = password,
//                onValueChange = { password = it },
//                label = { Text("Password") },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp)
//                    .focusRequester(passwordFocusRequester),
//                singleLine = true,
//                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//                keyboardOptions = KeyboardOptions(
//                    keyboardType = KeyboardType.Password,
//                    imeAction = ImeAction.Next
//                ),
//                keyboardActions = KeyboardActions(
//                    onNext = {
//                        password2FocusRequester.requestFocus()
//                    }
//                ),
//                trailingIcon = {
//                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
//                        Icon(
//                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
//                            contentDescription = if (passwordVisible) "Hide Password" else "Show Password"
//                        )
//                    }
//                }
//            )
//
//            OutlinedTextField(
//                value = password2,
//                onValueChange = { password2 = it },
//                label = { Text("Password2") },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp)
//                    .focusRequester(emailFocusRequester),
//                keyboardOptions = KeyboardOptions(
//                    keyboardType = KeyboardType.Email,
//                    imeAction = ImeAction.Next
//                ),
//                keyboardActions = KeyboardActions(
//                    onNext = {
//                        firstNameFocusRequester.requestFocus()
//                    }
//                )
//            )
//
//            OutlinedTextField(
//                value = firstName,
//                onValueChange = { firstName = it },
//                label = { Text("First Name") },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp)
//                    .focusRequester(lastNameFocusRequester),
//                keyboardOptions = KeyboardOptions(
//                    keyboardType = KeyboardType.Email,
//                    imeAction = ImeAction.Done
//                ),
//                keyboardActions = KeyboardActions(
//                    onDone = {
//                        focusManager.clearFocus()
//                        onSignUp(RegisterRequest(email, password, firstName, lastName, password2))
//                    }
//                )
//            )
//
//            OutlinedTextField(
//                value = lastName,
//                onValueChange = { lastName = it },
//                label = { Text("Last Name") },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp)
//                    .focusRequester(lastNameFocusRequester),
//                keyboardOptions = KeyboardOptions(
//                    keyboardType = KeyboardType.Email,
//                    imeAction = ImeAction.Done
//                ),
//                keyboardActions = KeyboardActions(
//                    onDone = {
//                        focusManager.clearFocus()
//                        onSignUp(RegisterRequest(email, password, firstName, lastName, password2))
//                        goToRegisterOTP()
//                    }
//                )
//            )
//
//            // Sign In Button
//            Button(
//                onClick = {
//                    focusManager.clearFocus()
//                    onSignUp(RegisterRequest(email, password, firstName, lastName, password2))
//                },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text(text = "Register")
//            }
//
//            // Sign In with Google Button
//            SignInWithGoogleButton(
//                dataStoreManager = DataStoreManager(LocalContext.current)
//            )
//            Button(
//                onClick = goToSignIn,
//            ) {
//                Text(text = "Sign In")
//            }
//        }
//    }
//}
//
//@Composable
//fun LogoDisplay(modifier: Modifier = Modifier) {
//    val context = LocalContext.current
//    AsyncImage(
//        model = ImageRequest.Builder(context)
//            .data(R.drawable.logo) // Your PNG file in drawable
//            .build(),
//        contentDescription = "App Logo",
//        modifier = modifier.size(100.dp) // Set the size here
//    )
//}

@Composable
fun RegisterScreen(
    goToRegisterOTP: () -> Unit,
    goToSignIn: ()-> Unit,
    authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val credentialManager = CredentialManager.create(context)
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var password2 by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    val passwordFocusRequester = remember { FocusRequester() }
    val password2FocusRequester = remember { FocusRequester() }
    val emailFocusRequester = remember { FocusRequester() }
    val firstNameFocusRequester = remember { FocusRequester() }
    val lastNameFocusRequester = remember { FocusRequester() }
    val onSignUp: (RegisterRequest) -> Unit = { request ->
        try {
            coroutineScope.launch {
                credentialManager.createCredential(
                    context = context,
                    request = CreatePasswordRequest(
                        id = request.email,
                        password = request.password
                    )
                )
                authViewModel.register(request)
                Toast.makeText(context, "Account created successfully", Toast.LENGTH_SHORT).show()
            }
        } catch (e: CreateCredentialCancellationException) {
            e.printStackTrace()
        } catch (e: CreateCredentialException) {
            e.printStackTrace()
        }
    }
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
                    .padding(8.dp)
                    .focusRequester(emailFocusRequester),
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
            // Password Input
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .focusRequester(passwordFocusRequester),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        password2FocusRequester.requestFocus()
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
                OutlinedTextField(
                value = password2,
                onValueChange = { password2 = it },
                label = { Text("Confirm Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .focusRequester(emailFocusRequester),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        firstNameFocusRequester.requestFocus()
                    }
                )
            )

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .focusRequester(lastNameFocusRequester),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        onSignUp(RegisterRequest(email, password, firstName, lastName, password2))
                    }
                )
            )
                OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .focusRequester(lastNameFocusRequester),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        onSignUp(RegisterRequest(email, password, firstName, lastName, password2))
                        goToRegisterOTP()
                    }
                )
            )
                // Sign In Button
            Button(
                onClick = {
                    focusManager.clearFocus()
                    onSignUp(RegisterRequest(email, password, firstName, lastName, password2))
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Black,
                    containerColor = colorResource(id = R.color.gradient_start),
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Register")
            }

            // Sign In with Google Button
            SignInWithGoogleButton(
                dataStoreManager = DataStoreManager(LocalContext.current)
            )
            Button(
                onClick = goToSignIn,
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Black,
                    containerColor = colorResource(id = R.color.gradient_start),
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Sign In")
            }
            }
        }
    }
}
