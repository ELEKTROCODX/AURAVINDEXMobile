package com.elektro24team.auravindex.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.elektro24team.auravindex.ui.components.BottomNavBar
import com.elektro24team.auravindex.ui.components.DrawerMenu
import androidx.navigation.NavController
import com.elektro24team.auravindex.ui.components.dialogs.ShowExternalLinkDialog
import com.elektro24team.auravindex.utils.functions.hamburguerMenuNavigator
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.elektro24team.auravindex.AuraVindexApp
import com.elektro24team.auravindex.R
import com.elektro24team.auravindex.ui.components.alerts.NotLoggedInAlert
import com.elektro24team.auravindex.ui.components.alerts.ConnectionAlert
import com.elektro24team.auravindex.ui.components.TopBar
import com.elektro24team.auravindex.ui.theme.BlackC
import com.elektro24team.auravindex.ui.theme.BrownC
import com.elektro24team.auravindex.ui.theme.OrangeC
import com.elektro24team.auravindex.ui.theme.PurpleC
import com.elektro24team.auravindex.ui.theme.WhiteC
import com.elektro24team.auravindex.utils.constants.URLs.IMG_url
import com.elektro24team.auravindex.utils.enums.SettingKey
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveTokenExpiration
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveInsufficientPermissions
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveSuccess
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveError
import com.elektro24team.auravindex.utils.functions.isLoggedIn
import com.elektro24team.auravindex.viewmodels.BookListViewModel
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel
import com.elektro24team.auravindex.viewmodels.NotificationViewModel
import com.elektro24team.auravindex.viewmodels.UserViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyListScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    localSettingViewModel: LocalSettingViewModel,
    bookListViewModel: BookListViewModel,
    notificationViewModel: NotificationViewModel,
    listId: String
) {
    val colors = MaterialTheme.colorScheme
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    rememberCoroutineScope()
    val context = LocalContext.current
    val showTermsDialog = remember { mutableStateOf(false) }
    val showPrivacyDialog = remember { mutableStateOf(false) }
    val showTeamDialog = remember { mutableStateOf(false) }
    val settings = localSettingViewModel.settings.collectAsState()
    val myList by bookListViewModel.myBookList.collectAsState()

    LaunchedEffect(Unit) {
        if(isLoggedIn(settings.value)){
            bookListViewModel.getBookList(settings.value[SettingKey.TOKEN.keySetting].toString(),listId)
        }
    }
    ObserveTokenExpiration(bookListViewModel, navController, localSettingViewModel)
    ObserveInsufficientPermissions(bookListViewModel, navController)
    ObserveError(bookListViewModel)
    ObserveSuccess(bookListViewModel)
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
            content = { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
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
                        NotLoggedInAlert(settings.value)
                        androidx.compose.material3.Text(
                            text = myList?.title.toString(),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(vertical = 8.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                        if (myList?.books?.isNotEmpty() == true) {
                            val booksInList = myList?.books ?: emptyList()
                            LazyColumn {
                                items(booksInList) { book ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp, vertical = 5.dp)
                                            .clickable {
                                                navController.navigate("book/${book._id}")
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
                                                imageModel = { IMG_url.trimEnd('/') + "/" + book.book_img.trimStart('/') },
                                                modifier = Modifier
                                                    .size(width = 100.dp, height = 185.dp)
                                                    .clip(RoundedCornerShape(10.dp)),
                                                imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                                                loading = {
                                                    Box(
                                                        modifier = Modifier.size(90.dp),
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
                                                    text = book.title,
                                                    style = MaterialTheme.typography.titleMedium,
                                                    color = OrangeC,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    text = book.authors.joinToString { "${it.name} ${it.last_name}" },
                                                    style = MaterialTheme.typography.labelMedium,
                                                    color = BrownC,
                                                    maxLines = 1
                                                )
                                                Spacer(modifier = Modifier.height(5.dp))
                                                Row {
                                                    book.genres.forEach { genre ->
                                                        Card(
                                                            shape = RoundedCornerShape(10.dp),
                                                            colors = CardDefaults.cardColors(containerColor = Color(0xFFEDE7F6)),
                                                            elevation = CardDefaults.cardElevation(4.dp)
                                                        ) {
                                                            Text(
                                                                text = genre,
                                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                                color = Color(0xFF5E35B1),
                                                                fontSize = 9.sp,
                                                                textAlign = TextAlign.Center,
                                                            )
                                                        }
                                                    }
                                                }
                                                Spacer(modifier = Modifier.height(5.dp))
                                                Text(
                                                    text = book.summary,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = BlackC,
                                                    maxLines = 3,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Button(
                                                    onClick = {
                                                        bookListViewModel.removeBookFromList(settings.value[SettingKey.TOKEN.keySetting].toString(),listId,book._id)
                                                    },
                                                    modifier = Modifier
                                                        .height(36.dp),
                                                    colors = androidx.compose.material.ButtonDefaults.buttonColors(
                                                        backgroundColor = colors.error
                                                    ),
                                                    shape = RoundedCornerShape(12.dp),
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Delete,
                                                        contentDescription = "Remove from list",
                                                        tint = Color.White,
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                    Text(
                                                        text = "Remove from list",
                                                        color = Color.White,
                                                        style = TextStyle(
                                                            fontWeight = FontWeight.Bold,
                                                            fontSize = 12.sp,
                                                            color = Color(0xFF572365)
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            Text("No books yet")
                        }
                    }
                }
            }
        )
    }
}
