package com.example.cookingbuddynew.ui.screen.auth

import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cookingbuddynew.AuthViewModel
import com.example.cookingbuddynew.MainActivity
import com.example.cookingbuddynew.R
import com.example.cookingbuddynew.api.GoogleLoginRequest
import com.example.cookingbuddynew.api.UserDetails
import com.example.cookingbuddynew.utils.DataStoreManager
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch

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
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color.Black,
            containerColor = colorResource(id = R.color.gradient_start),
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.google_logo), // Use the Google logo here
                contentDescription = "Google Logo",
                modifier = Modifier.size(24.dp) // Adjust the size as needed
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Sign in with Google")
        }
    }
}