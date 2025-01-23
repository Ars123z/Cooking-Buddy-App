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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.cookingbuddynew.ui.screen.auth.ForgetPasswordOtpScreen
import com.example.cookingbuddynew.ui.screen.auth.ForgetPasswordScreen
import com.example.cookingbuddynew.ui.screen.auth.InitialScreen
import com.example.cookingbuddynew.ui.screen.auth.RegisterOtpScreen
import com.example.cookingbuddynew.ui.screen.auth.RegisterScreen
import com.example.cookingbuddynew.ui.screen.auth.SignInScreen
import com.example.cookingbuddynew.ui.theme.CookingBuddyNewTheme
import com.example.cookingbuddynew.utils.DataStoreManager
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

const val INITIAL_SCREEN = "initial_screen"
const val REGISTER_ROUTE = "register"
const val LOGIN_ROUTE = "login"
const val FORGET_PASSWORD_ROUTE = "reset_password"
const val REGISTER_OTP_ROUTE = "register_otp"
const val FORGET_PASSWORD_OTP_ROUTE = "forget_password_otp"

@ExperimentalMaterial3Api
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
                        startDestination = INITIAL_SCREEN,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(INITIAL_SCREEN) {
                            InitialScreen(
                                goToRegister = { navController.navigate(REGISTER_ROUTE) },
                                goToSignIn = { navController.navigate(LOGIN_ROUTE) }
                            )
                        }
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












