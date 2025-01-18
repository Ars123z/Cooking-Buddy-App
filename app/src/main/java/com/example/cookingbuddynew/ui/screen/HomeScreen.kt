package com.example.cookingbuddynew.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cookingbuddynew.R
import com.example.cookingbuddynew.data.ExploreData
import com.example.cookingbuddynew.data.dataList
import com.example.cookingbuddynew.ui.theme.lightRed
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onSearch: (String) -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var text by remember { mutableStateOf("") }
    var selectedTabIndex by remember { mutableStateOf(0) }
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = modifier
                .wrapContentSize()
                .background(Color(0xFFFFE500)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically, // Vertically align items
                modifier = Modifier
                    .fillMaxWidth() // Fill the width of the screen
                    .padding(
                        top = 25.dp,
                        bottom = 0.dp,
                        start = 16.dp,
                        end = 0.dp
                    )
            ) {
                Text(
                    text = stringResource(R.string.screen_title),
                    style = TextStyle(
                        fontFamily = FontFamily(
                            Font(resId = R.font.habanera_rounded_extrabold),
                        ),
                        fontWeight = FontWeight.W600,
                        fontSize = 20.sp,
                        lineHeight = 24.sp,
                        letterSpacing = (-1).sp
                    ), // Use a heading style for the a
                    color = Color.Black

                )
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = onProfileClick,
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape),
                        tint = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp)) // Add spacing between Row and
            OutlinedTextField(
                value = text,
                onValueChange = {
                    text = it
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = androidx.compose.ui.text.input.ImeAction.Search,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearch(text)
                    }
                ),
                placeholder = {
                    Text(
                        text = "Search for recipes...",
                        color = Color.Gray
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_search_24),
                        contentDescription = null, // Add content description if needed
                        modifier = Modifier.size(24.dp),
                        tint = Color.Black
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = Color.Black,
                    focusedLeadingIconColor = Color.Black,
                    focusedBorderColor = Color.Black,
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        Spacer(
            modifier = Modifier.height(25.dp)
        )
        Row(
            modifier = Modifier.padding(horizontal = 40.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            TabButtons(
                selectedTabIndex = selectedTabIndex,
                changeTabIndex = {selectedTabIndex = it}
            )
        }
        TabContent(selectedTabIndex)
        Spacer(
            modifier = Modifier.height(20.dp)
        )
        Explore(
            onCardClick = onSearch,
        )
    }
}

@Composable
fun TabButtons(
    selectedTabIndex: Int,
    changeTabIndex: (Int) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically, // Vertically align items
        modifier = Modifier
            .wrapContentSize() // Fill the width of the screen
            .padding(
                vertical = 0.dp,
                horizontal = 16.dp
            )
            .background(
                color = Color.Black,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        TextButton(
            onClick = {changeTabIndex(0)},
            colors = ButtonDefaults.textButtonColors(
                containerColor = if (selectedTabIndex == 0) lightRed else Color.Black,
                contentColor = if (selectedTabIndex == 0) Color.Yellow else Color.White
            ),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = "Recommended",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 6.dp)
            )
        }
        TextButton(
            onClick = { changeTabIndex(1) },
            colors = ButtonDefaults.textButtonColors(
                containerColor = if (selectedTabIndex == 1) lightRed else Color.Black,
                contentColor = if (selectedTabIndex == 1) Color.Yellow else Color.White
            ),
            shape = RoundedCornerShape(16.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_bookmark_24),
                contentDescription = null, // Add content description if needed
                modifier = Modifier.size(24.dp),
            )
            Text(
                text = "Collections",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 6.dp)
            )
        }
    }
}


@Composable
fun TabContent(selectedTabIndex: Int) {
    when (selectedTabIndex) {
        0 -> RecommendedTab()
        else -> CollectionsTab()
    }
}

@Composable
fun RecommendedTab() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3), // Set the number of columns
        verticalArrangement = Arrangement.spacedBy(8.dp), // Add vertical spacing
        horizontalArrangement = Arrangement.spacedBy(8.dp), // Add horizontal spacing
        modifier = Modifier.height(350.dp)
    ) {
        items(5) { index ->
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy((-5).dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.rectangle),
                    contentDescription = null,
                    modifier = Modifier.size(150.dp)
                )
                Text(
                    text = "Cheese Burger",
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}

@Composable
fun CollectionsTab() {
    LazyRow() {
        item {
            for (i in 1..10) {
                Image(
                    painter = painterResource(id = R.drawable.rectangle),
                    contentDescription = null, // Add content description if needed
                    modifier = Modifier.size(200.dp),
                )
            }
        }
    }
}

@Composable
fun Explore(
    onCardClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text="EXPLORE",
        color = MaterialTheme.colorScheme.onBackground,
        fontStyle = MaterialTheme.typography.headlineLarge.fontStyle,
        textAlign = TextAlign.Center,
        fontSize = 40.sp,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
    Spacer(modifier = Modifier.height(30.dp))
    Column(
        verticalArrangement = Arrangement.spacedBy(50.dp),
        modifier = Modifier.padding(horizontal = 16.dp)

    ) {
        ExploreCard(
            onCardClick = onCardClick,
            data = dataList[0]
        )
        ExploreCard(
            onCardClick = onCardClick,
            data = dataList[1]
        )
        ExploreCard(
            onCardClick = onCardClick,
            data = dataList[2]
        )
        ExploreCard(
            onCardClick = onCardClick,
            data = dataList[3]
        )
        ExploreCard(
            onCardClick = onCardClick,
            data = dataList[4]
        )
    }
}

@Composable
fun ExploreCard(
    onCardClick: (String) -> Unit,
    data: ExploreData,
    modifier: Modifier = Modifier,
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.card_background)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(data.image)
                        .build(),
                    contentDescription = "icon",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(225.dp)
                        .clickable {
                            onCardClick(data.title)
                        }
                )
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Bookmark,
                        contentDescription = "BookMarked", // Important for accessibility
                        tint = Color.Red
                    )
                }
            }
            Row() {
                Text(
                    text = data.title.uppercase(),
                    color = Color.White,
                    fontStyle = MaterialTheme.typography.headlineLarge.fontStyle,
                    fontSize = 25.sp,
                    letterSpacing = (2.sp),
                    modifier = Modifier.padding(16.dp)
                )
                Spacer(Modifier.weight(1f))
                IconButton(
                    onClick = {
                        showBottomSheet = true
                    },
                    modifier = Modifier.padding(vertical = 5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "More Options", // Important for accessibility
                        tint = Color.White
                    )
                }
//                PlaylistBottomSheet(
//                    currentVideo = data,
//                    changeState = { showBottomSheet = it },
//                    showBottomSheet = showBottomSheet
//                )
            }
        }
    }
}

