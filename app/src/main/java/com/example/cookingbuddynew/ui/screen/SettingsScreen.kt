package com.example.cookingbuddynew.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cookingbuddynew.api.ProfileUpdateRequest
import com.example.cookingbuddynew.data.LanguageData
import com.example.cookingbuddynew.data.RegionData
import com.example.cookingbuddynew.utils.DataStoreManager
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel(factory = SettingsViewModel.Factory),
    dataStore: DataStoreManager = DataStoreManager(LocalContext.current),
    goBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val profile by dataStore.getFromDataStore().collectAsState(initial = null)
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showRegionDialog by remember { mutableStateOf(false) }
//    var showThemeDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { goBack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {

            SettingsItem(
                icon = Icons.Default.Language,
                text = "Language",
                value = profile?.language ?: "Select Language",
                onClick = { showLanguageDialog = true }
            )

            SettingsItem(
                icon = Icons.Default.Place,
                text = "Region",
                value = profile?.region ?: "Select Region",
                onClick = { showRegionDialog = true }
            )

//        SettingsItem(
//            icon = Icons.Default.Palette,
//            text = "Theme: ${profile?.theme ?: "Select Theme"}",
//            onClick = { showThemeDialog = true }
//        )

//        SettingsItem(
//            icon = Icons.Default.Subscriptions,
//            text = "Subscribe",
//            onClick = { /* TODO: Implement subscribe action */ }
//        )
        }

        if (showLanguageDialog) {
            LanguageDialog(
                onDismissRequest = { showLanguageDialog = false },
                selectedLanguage = profile!!.language,
                onSelectLanguage = {
                    val request: ProfileUpdateRequest = ProfileUpdateRequest(
                        language = it,
                        region = profile!!.region,
                    )
                    coroutineScope.launch {
                        val response = viewModel.updateProfile(request)
                        dataStore.updateProfile(
                            newLanguage = response.language,
                            newRegion = response.region,
                            newSubscription = response.subscription,
                            newSubscriptionValidityDate = response.subscription_validity_date ?: ""
                        )
                    }
                }
            )
        }

        if (showRegionDialog) {
            RegionDialog(
                onDismissRequest = { showRegionDialog = false },
                selectedRegion = profile!!.region,
                onSelectRegion = {
                    val request: ProfileUpdateRequest = ProfileUpdateRequest(
                        language = profile!!.language,
                        region = it,
                    )
                    coroutineScope.launch {
                        val response = viewModel.updateProfile(request)
                        dataStore.updateProfile(
                            newLanguage = response.language,
                            newRegion = response.region,
                            newSubscription = response.subscription,
                            newSubscriptionValidityDate = response.subscription_validity_date ?: ""
                        )
                    }
                }
            )
        }

//    if (showThemeDialog) {
//        ThemeDialog(
//            onDismissRequest = { showThemeDialog = false },
//            onSelectTheme = {
//                val request: ProfileUpdateRequest = ProfileUpdateRequest(
//                    language = it,
//                    region = profile!!.region,
//                )
//                coroutineScope.launch {
//                    val response = viewModel.updateProfile(request)
//                    dataStore.updateProfile(
//                        newLanguage = response.language,
//                        newRegion =  response.region,
//                        newSubscription = response.subscription,
//                        newSubscriptionValidityDate = response.subscription_validity_date?: ""
//                    )
//                }
//            }
//        )
//    }
    }
}

@Composable
fun SettingsItem(icon: ImageVector, text: String, value: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp).padding(end = 16.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp)
            )
        }
        Spacer(modifier = Modifier.height(-(4).dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp, color = Color.Gray),
            modifier = Modifier.padding(start = 40.dp)
        )
    }
}


@ExperimentalMaterial3Api
@Composable
fun LanguageDialog(
    onDismissRequest: () -> Unit,
    onSelectLanguage: (String) -> Unit,
    selectedLanguage: String,
) {
    val languages = LanguageData.languages
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .width(300.dp)
                .height(700.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "App Language",
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.height(550.dp)
                ) {
                    items(languages) { language ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectLanguage(language[1])
                                    onDismissRequest()
                                }
                                .padding(vertical = 12.dp)
                                .padding(12.dp)
                        ) {
                            RadioButton(
                                selected = selectedLanguage == language[1],
                                onClick = null // Handled by Row click
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = language[0],
                                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp),
                    modifier = Modifier
                        .clickable(onClick = onDismissRequest)
                        .padding(vertical = 12.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun RegionDialog(
    onDismissRequest: () -> Unit,
    onSelectRegion: (String) -> Unit,
    selectedRegion: String,
) {
    val regions = RegionData.regions
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .width(300.dp)
                .height(700.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Region",
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.height(550.dp)
                ) {
                    items(regions) { region ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectRegion(region[1])
                                    onDismissRequest()
                                }
                                .padding(vertical = 12.dp)
                                .padding(12.dp)
                        ) {
                            RadioButton(
                                selected = selectedRegion == region[1],
                                onClick = null // Handled by Row click
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = region[0],
                                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp),
                    modifier = Modifier
                        .clickable(onClick = onDismissRequest)
                        .padding(vertical = 12.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@ExperimentalMaterial3Api
@Composable
fun ThemeDialog(onDismissRequest: () -> Unit, onSelectTheme: (String) -> Unit) {
    val themes = listOf("Light", "Dark", "System Default") // Add more themes as needed

    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
    ) {
        LazyColumn {
            item {
                Text(
                    text = "Select Theme",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            items(themes) { theme ->
                Text(
                    text = theme,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSelectTheme(theme)
                            onDismissRequest()
                        }
                        .padding(vertical = 8.dp)
                )
            }
        }
    }
}
