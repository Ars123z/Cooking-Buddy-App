package com.example.cookingbuddynew

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPasswordOption
import androidx.credentials.PasswordCredential
import androidx.credentials.exceptions.CreateCredentialCancellationException
import androidx.credentials.exceptions.CreateCredentialException
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cookingbuddynew.api.GoogleLoginRequest
import com.example.cookingbuddynew.api.LoginRequest
import com.example.cookingbuddynew.api.RegisterRequest
import com.example.cookingbuddynew.api.UserDetails
import com.example.cookingbuddynew.ui.theme.CookingBuddyNewTheme
import com.example.cookingbuddynew.utils.DataStoreManager
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

const val REGISTER_ROUTE = "register"
const val LOGIN_ROUTE = "login"
const val FORGET_PASSWORD_ROUTE = "reset_password"
const val REGISTER_OTP_ROUTE = "register_otp"
const val FORGET_PASSWORD_OTP_ROUTE = "forget_password_otp"

class AuthActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CookingBuddyNewTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val dataStoreContext = LocalContext.current
                    val dataStoreManager = DataStoreManager(dataStoreContext)
                    val changeActivity = {
                        val intent = Intent(this@AuthActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    val navController = rememberNavController()
                    // Navigation Setup
                    NavHost(
                        navController = navController,
                        startDestination = REGISTER_ROUTE,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(REGISTER_ROUTE) {
                            RegisterScreen(
                                goToSignIn = { navController.navigate(LOGIN_ROUTE) },
                                goToRegisterOTP = { navController.navigate(REGISTER_OTP_ROUTE) }
                            )
                        }
                        composable(LOGIN_ROUTE) {
                            SignInScreen(
                                goToRegister = { navController.navigate(REGISTER_ROUTE) },
                                goToForgetPassword = { navController.navigate(FORGET_PASSWORD_ROUTE) },
                            )
                        }
                        composable(FORGET_PASSWORD_ROUTE) {
                            ForgetPasswordScreen(
                                goToForgetPasswordOTP = { navController.navigate(FORGET_PASSWORD_OTP_ROUTE) }
                            )
                        }
                        composable(REGISTER_OTP_ROUTE) {
                            RegisterOtpScreen(
                                goToSignIn = { navController.navigate(LOGIN_ROUTE) }
                            )
                        }
                        composable(FORGET_PASSWORD_OTP_ROUTE) {
                            ForgetPasswordOtpScreen(
                                goToSignIn = { navController.navigate(LOGIN_ROUTE) }
                            )
                        }
                    }
                }
            }
        }
    }
}

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
                Toast.makeText(context, "you are signed in as ${user?.full_name}", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            }
            Toast.makeText(context, "Account created successfully", Toast.LENGTH_SHORT).show()
        }  catch (e: CreateCredentialCancellationException) {
            e.printStackTrace()
        } catch(e: CreateCredentialException) {
            e.printStackTrace()
        }
    }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Cooking Buddy", style = MaterialTheme.typography.headlineMedium)
            Text(text = "Sign In", style = MaterialTheme.typography.titleLarge)
            // Email Input
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .focusRequester(emailFocusRequester),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        onSignIn()
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
            // Sign In Button
            Button(
                onClick = {
                    focusManager.clearFocus()
                    onSignIn()
                          },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Sign In")
            }

            // Sign In with Google Button
            SignInWithGoogleButton(
                dataStoreManager = DataStoreManager(LocalContext.current)
            )
            Button(
                onClick = goToRegister,
            ) {
                Text(text = "Register")
            }
        }
    }
}

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
        } catch(e: CreateCredentialException) {
            e.printStackTrace()
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Cooking Buddy", style = MaterialTheme.typography.headlineMedium)
            Text(text = "Register", style = MaterialTheme.typography.titleLarge)
            // Email Input
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .focusRequester(emailFocusRequester),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
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
                label = { Text("Password2") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .focusRequester(emailFocusRequester),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
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
            ) {
                Text(text = "Sign In")
            }
        }
    }

}

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

@Composable
fun SignInWithGoogleButton(
    dataStoreManager: DataStoreManager,
    authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory),
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var user: UserDetails? = null
    val onClick: () -> Unit = {
        val credentialManager = CredentialManager.create(context)

        val googleIdOptionSignIn: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("118618584336-p8h5phsqo2nsjk4t81fi6jaibu22e1tt.apps.googleusercontent.com")
            .setAutoSelectEnabled(true)
            .build()

        val googleIdOptionSignUp: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("118618584336-p8h5phsqo2nsjk4t81fi6jaibu22e1tt.apps.googleusercontent.com")
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOptionSignIn)
            .addCredentialOption(googleIdOptionSignUp)
            .build()

        coroutineScope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context,
                )
                val credential = result.credential
                Log.i(TAG, credential.toString())
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val googleIdToken = googleIdTokenCredential.idToken
                Log.i(TAG, googleIdToken.toString())
                val request: GoogleLoginRequest = GoogleLoginRequest(access_token = googleIdToken)

                authViewModel.getUserData(request).collect { userDetails ->
                    user = userDetails
                    user.access_token_expiry = System.currentTimeMillis() + 24 * 60 * 60 * 1000
                    user.refresh_token_expiry = System.currentTimeMillis() + 604800000
                    dataStoreManager.saveToDataStore(user)
                }
                Toast.makeText(context, "you are signed in as ${user?.full_name}", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            }
            catch (e: GetCredentialException) {
                Log.i(TAG, e.message.toString())
                Log.i(TAG, e.cause.toString())
                Log.i(TAG, e.stackTraceToString())
            }
        }
    }

    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground), // Use the Google logo here
                contentDescription = "Google Logo",
                modifier = Modifier.size(24.dp) // Adjust the size as needed
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Sign in with Google")
        }
    }
}

@Composable
fun ForgetPasswordScreen(
    goToForgetPasswordOTP: () -> Unit,
) {
    Text(text= "ResetPassword")
    var email by remember { mutableStateOf("") }
    OutlinedTextField(
        value = "",
        onValueChange = {email = it},
        label = { Text("Email") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                goToForgetPasswordOTP()
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun ForgetPasswordOtpScreen(
    goToSignIn: () -> Unit,
) {
    Text(text= "ResetPasswordOtp")
    var otp by remember { mutableStateOf("") }
    OutlinedTextField(
        value = "",
        onValueChange = {otp = it},
    )
}

