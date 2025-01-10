package com.example.cookingbuddynew

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cookingbuddynew.api.GoogleLoginRequest
import com.example.cookingbuddynew.api.UserDetails
import com.example.cookingbuddynew.ui.theme.CookingBuddyNewTheme
import com.example.cookingbuddynew.utils.DataStoreManager
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class AuthActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CookingBuddyNewTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
//                  Handle Deep Links
                    val deepLinkData = intent.data
                    val startDestination = if (
                        deepLinkData != null &&
                        deepLinkData.scheme == "https" &&
                        deepLinkData.host == "www.example.com"
                    ) {
                        val uuid = deepLinkData.getQueryParameter("uuid")
                        if (uuid != null) {
                            "reset-password/$uuid"
                        } else {
                            "login"
                        }
                    } else {
                        "login"
                    }

                    val dataStoreContext = LocalContext.current
                    val dataStoreManager = DataStoreManager(dataStoreContext)
                    val changeActivity = {
                        val intent = Intent(this@AuthActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    // Navigation Setup
                    NavHost(navController = navController, startDestination = startDestination) {
                        composable("login") { SignInScreen() }
                        composable(
                            "resetPassword/{uuid}",
                            arguments = listOf(navArgument("uuid") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val uuid = backStackEntry.arguments?.getString("uuid")
                            if (uuid != null) {
                                ResetPasswordScreen({navController.navigate("login")}, uuid)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    onSignInClick: (String, String) -> Unit = { _, _ -> },
//    onGoogleSignInClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Sign In", style = MaterialTheme.typography.headlineMedium)

            // Email Input
            BasicTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .border(1.dp, MaterialTheme.colorScheme.primary)
                    .padding(16.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                decorationBox = { innerTextField ->
                    if (email.isEmpty()) {
                        Text("Email")
                    }
                    innerTextField()
                }
            )

            // Password Input
            BasicTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .border(1.dp, MaterialTheme.colorScheme.primary)
                    .padding(16.dp),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                decorationBox = { innerTextField ->
                    if (password.isEmpty()) {
                        Text("Password")
                    }
                    innerTextField()
                }
            )

            // Toggle Password Visibility
            TextButton(onClick = { passwordVisible = !passwordVisible }) {
                Text(text = if (passwordVisible) "Hide Password" else "Show Password")
            }

            // Sign In Button
            Button(
                onClick = { onSignInClick(email, password) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Sign In")
            }

            // Sign In with Google Button
//            OutlinedButton(
//                onClick = { onGoogleSignInClick() },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text(text = "Sign In with Google")
//            }
            SignInWithGoogleButton(
                dataStoreManager = DataStoreManager(LocalContext.current)
            )
        }
    }
}

@Composable
fun RegisterScreen() {

}

@Composable
fun ResetPasswordScreen(navigateToLogin: ()-> Unit, uuid: String) {
Text(text= "ResetPassword")
    Text(text = uuid)
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
                    user.access_token_expiry = System.currentTimeMillis() + 2000
                    user.refresh_token_expiry = System.currentTimeMillis() + 604800000
                    dataStoreManager.saveToDataStore(user)
                }
                Toast.makeText(context, "you are signed in as ${user?.full_name}", Toast.LENGTH_SHORT).show()
            }
            catch (e: GetCredentialException) {
                Log.i(TAG, e.message.toString())
                Log.i(TAG, e.cause.toString())
                Log.i(TAG, e.stackTraceToString())
            }
        }
    }

    Button(
        onClick = onClick,
        modifier = modifier,
    ) {
        Text(text = "Sign in with Google")
    }
}

@Composable
fun AppContent(
    dataStoreManager: DataStoreManager,
    changeActivity: () -> Unit
) {
    var context = LocalContext.current
    var isRegistered by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    val onRegisterSuccess = { isRegistered = true }

    //lets check if user is registered in when the app start
    LaunchedEffect(key1 = Unit) {
        checkRegisterState(dataStoreManager) { it ->
            isRegistered = it
        }
    }

    if (isRegistered) {
        changeActivity()
    } else {
        RegisterPageUI(onRegisterSuccess, dataStoreManager)
    }

}

suspend fun checkRegisterState(
    dataStoreManager: DataStoreManager,
    onResult: (Boolean) -> Unit
) {
    val preferences  = dataStoreManager.getFromDataStore().first()
    val accessToken = preferences.access_token
    val isRegistered = accessToken != ""
    onResult(isRegistered)
}

@Composable
fun RegisterPageUI(onRegisterSuccess: () -> Unit, dataStoreManager: DataStoreManager) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    val mContext = LocalContext.current
    val scope = rememberCoroutineScope()

    // Create focus requesters for each TextField
    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }
    val focusRequester3 = remember { FocusRequester() }
    val focusRequester4= remember { FocusRequester() }

    // Get the current focus manager
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(16.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Main card Content for Register
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()) // Add verticalScroll modifier here
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 8.dp)
            ) {

                // Heading Jetpack Compose
                Text(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.app_name),
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp

                )

                // Register Logo
//                AsyncImage(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(128.dp)
//                        .padding(top = 8.dp),
//                    model = ImageRequest.Builder(LocalContext.current)
//                        .data(data = R.drawable.android).crossfade(enable = true).scale(Scale.FILL)
//                        .build(),
//                    contentDescription = stringResource(id = R.string.app_name)
//                )


                Text(
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                    text = "Register",
                    fontSize = 25.sp
                )

                // Name Field
                OutlinedTextField(
                    value = name, // Add a variable for name
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next // Set imeAction to Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusRequester2.requestFocus() // Move to next TextField
                        }
                    ),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .focusRequester(focusRequester1)
                )

                OutlinedTextField(
                    value = mobileNumber,
                    onValueChange = { mobileNumber = it },
                    label = { Text("Mobile Number") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next // Set imeAction to Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusRequester3.requestFocus() // Move to next TextField
                        }
                    ),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .focusRequester(focusRequester2)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next // Set imeAction to Done
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusRequester4.requestFocus() // Move to next TextField
                        }
                    ),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth().focusRequester(focusRequester3)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done // Set imeAction to Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus() // Hide the keyboard
                        }
                    ),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .focusRequester(focusRequester4)
                )
                Button(
                    onClick = {
                        if (name.isEmpty()) {
                            Toast.makeText(mContext, "Name is Empty", Toast.LENGTH_SHORT).show()
                        } else if (mobileNumber.isEmpty()) {
                            Toast.makeText(mContext, "Mobile No. is Empty", Toast.LENGTH_SHORT)
                                .show()
                        } else if (email.isEmpty()) {
                            Toast.makeText(mContext, "Email is Empty", Toast.LENGTH_SHORT).show()
                        } else if (password.isEmpty()) {
                            Toast.makeText(mContext, "Password is Empty", Toast.LENGTH_SHORT).show()
                        } else {
                            //Submit you data
                            scope.launch {
                                dataStoreManager.saveToDataStore(
                                    UserDetails(
                                        email = email,
                                        full_name = name,
                                        picture = "",
                                        access_token = "fghegegg",
                                        refresh_token = "",
                                        access_token_expiry = 0L,
                                        refresh_token_expiry = 0L
                                    )
                                )
                                onRegisterSuccess()
                            }
                        }

                    }, modifier = Modifier.padding(16.dp)
                ) {
                    Text("Submit")
                }
            }
        }
    }
}
