package com.elektrocodx.auravindex.view

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.elektrocodx.auravindex.AuraVindexApp
import com.elektrocodx.auravindex.R
import com.elektrocodx.auravindex.ui.components.BookCollectionsSection
import com.elektrocodx.auravindex.ui.components.BottomNavBar
import com.elektrocodx.auravindex.ui.components.DrawerMenu
import com.elektrocodx.auravindex.ui.components.TopBar
import com.elektrocodx.auravindex.ui.components.alerts.ConnectionAlert
import com.elektrocodx.auravindex.ui.components.dialogs.ShowExternalLinkDialog
import com.elektrocodx.auravindex.ui.theme.BlackC
import com.elektrocodx.auravindex.ui.theme.BrownC
import com.elektrocodx.auravindex.ui.theme.OrangeC
import com.elektrocodx.auravindex.ui.theme.PurpleC
import com.elektrocodx.auravindex.ui.theme.WhiteC
import com.elektrocodx.auravindex.utils.constants.URLs.IMG_url
import com.elektrocodx.auravindex.utils.functions.hamburguerMenuNavigator
import com.elektrocodx.auravindex.viewmodels.BookCollectionViewModel
import com.elektrocodx.auravindex.viewmodels.BookViewModel
import com.elektrocodx.auravindex.viewmodels.LocalSettingViewModel
import com.elektrocodx.auravindex.viewmodels.NotificationViewModel
import com.elektrocodx.auravindex.viewmodels.UserViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    bookViewModel: BookViewModel,
    bookCollectionViewModel: BookCollectionViewModel,
    userViewModel: UserViewModel,
    notificationViewModel: NotificationViewModel,
    localSettingViewModel: LocalSettingViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    rememberCoroutineScope()
    val context = LocalContext.current
    val showTermsDialog = remember { mutableStateOf(false) }
    val showPrivacyDialog = remember { mutableStateOf(false) }
    val showTeamDialog = remember { mutableStateOf(false) }
    val filteredBooks by bookViewModel.filteredBooks.collectAsState()
    LaunchedEffect(Unit) {
        bookViewModel.loadBooks(showDuplicates = false, showLents = true)
    }

    ModalNavigationDrawer(
        drawerContent = {
            DrawerMenu(
                navController = navController,
                currentRoute = navController.currentBackStackEntry?.destination?.route,
                userViewModel = userViewModel,
                notificationViewModel = notificationViewModel,
                localSettingViewModel = localSettingViewModel,
                onItemSelected = { route ->
                    hamburguerMenuNavigator(
                        route,
                        navController,
                        showTermsDialog,
                        showPrivacyDialog,
                        showTeamDialog
                    )
                }
            )

        },
        drawerState = drawerState
    ) {
        ShowExternalLinkDialog(showTermsDialog, context, "https://auravindex.me/tos/")
        ShowExternalLinkDialog(showPrivacyDialog, context, "https://auravindex.me/privacy/")
        ShowExternalLinkDialog(showTeamDialog, context, "https://auravindex.me/about/")
        Scaffold(
            topBar = {
                TopBar(
                    navController = navController,
                    drawerState = drawerState,
                    localSettingViewModel = localSettingViewModel,
                    notificationViewModel = notificationViewModel
                )
            },
            bottomBar = {
                BottomNavBar(
                    currentRoute = navController.currentBackStackEntry?.destination?.route ?: "search",
                    onItemClick = { route -> navController.navigate(route) }
                )
            },
            content = { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color(0xFFEDE7F6), Color(0xFFD1C4E9))
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val app = LocalContext.current.applicationContext as AuraVindexApp
                        val isConnected by app.networkLiveData.observeAsState(true)
                        ConnectionAlert(isConnected)
                        var bookQuery by remember { mutableStateOf("") }
                        OutlinedTextField(
                            value = bookQuery,
                            onValueChange = {
                                bookQuery = it
                                bookViewModel.searchBook(it)
                                            },
                            label = { Text("Search a book...") },
                            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    if (bookQuery.isNotBlank()) {
                                        navController.navigate("search_results/${bookQuery}")
                                    }
                                }
                            )
                        )
                        if (bookQuery.isNotEmpty()) {
                            LazyColumn {
                                items(filteredBooks?.size ?: 0) { index ->
                                    val book = filteredBooks?.get(index)
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp, vertical = 5.dp)
                                            .clickable {
                                                navController.navigate("book/${book?._id}")
                                            },
                                        colors = CardDefaults.cardColors(containerColor = WhiteC),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(8.dp)
                                        ) {
                                            GlideImage(
                                                imageModel = { IMG_url.trimEnd('/') + "/" + book?.book_img?.trimStart('/') },
                                                modifier = Modifier
                                                    .size(width=100.dp, height = 150.dp)
                                                    .clip(RoundedCornerShape(10.dp))
                                                ,
                                                imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                                                loading = {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(90.dp),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        CircularProgressIndicator(color = PurpleC, strokeWidth = 2.dp)
                                                    }
                                                },
                                                failure = {
                                                    Image(
                                                        painter = painterResource(id = R.mipmap.ic_launcher),
                                                        contentDescription = "Default img",
                                                        modifier = Modifier
                                                            .size(90.dp)
                                                            .clip(RoundedCornerShape(10.dp))
                                                    )
                                                }
                                            )
                                            Column(
                                                modifier = Modifier
                                                    .padding(start = 12.dp, end = 8.dp)
                                                    .fillMaxWidth(),
                                                verticalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Text(
                                                    text = book?.title ?: "",
                                                    style = MaterialTheme.typography.titleMedium,
                                                    color = OrangeC,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    text = book?.authors?.joinToString { "${it.name} ${it.last_name}" } ?: "",
                                                    style = MaterialTheme.typography.labelMedium,
                                                    color = BrownC,
                                                    maxLines = 1
                                                )
                                                Spacer(modifier = Modifier.height(5.dp))
                                                Row{
                                                    book?.genres?.forEach { genre ->
                                                        Card(
                                                            shape = RoundedCornerShape(10.dp),
                                                            colors = CardDefaults.cardColors(containerColor = Color(0xFFEDE7F6)),
                                                            elevation = CardDefaults.cardElevation(4.dp)
                                                        ) {
                                                            Text(
                                                                text = genre,
                                                                modifier = Modifier
                                                                    .padding(
                                                                        horizontal = 8.dp,
                                                                        vertical = 4.dp
                                                                    ),
                                                                color = Color(0xFF5E35B1),
                                                                fontSize = 9.sp,
                                                                textAlign = TextAlign.Center,
                                                            )
                                                        }
                                                    }
                                                }
                                                Spacer(modifier = Modifier.height(5.dp))
                                                Text(
                                                    text = book?.summary ?: "",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = BlackC,
                                                    maxLines = 3,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        } else{
                            BookCollectionsSection(navController, bookCollectionViewModel)
                        }
                    }
                }
            }
        )
    }
}
