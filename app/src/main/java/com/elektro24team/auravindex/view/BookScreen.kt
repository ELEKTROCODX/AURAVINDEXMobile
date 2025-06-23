package com.elektro24team.auravindex.view


import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material.icons.filled.SettingsBackupRestore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.elektro24team.auravindex.AuraVindexApp
import com.elektro24team.auravindex.R
import com.elektro24team.auravindex.model.BookList
import com.elektro24team.auravindex.ui.components.BottomNavBar
import com.elektro24team.auravindex.ui.components.DrawerMenu
import com.elektro24team.auravindex.ui.components.ConnectionAlert
import com.elektro24team.auravindex.ui.components.RequestLoanDialog
import com.elektro24team.auravindex.ui.components.ShowExternalLinkDialog
import com.elektro24team.auravindex.ui.components.TopBar
import com.elektro24team.auravindex.ui.components.UserBookLists
import com.elektro24team.auravindex.ui.theme.MediumPadding
import com.elektro24team.auravindex.ui.theme.PurpleC
import com.elektro24team.auravindex.utils.constants.URLs.IMG_url
import com.elektro24team.auravindex.utils.enums.AppAction
import com.elektro24team.auravindex.utils.enums.SettingKey
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveError
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveSuccess
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveTokenExpiration
import com.elektro24team.auravindex.utils.functions.formatUtcToLocalWithDate
import com.elektro24team.auravindex.utils.functions.hamburguerMenuNavigator
import com.elektro24team.auravindex.utils.functions.isAdmin
import com.elektro24team.auravindex.utils.functions.isLoggedIn
import com.elektro24team.auravindex.utils.functions.mustBeLoggedInToast
import com.elektro24team.auravindex.utils.functions.mustBeSubscribedToast
import com.elektro24team.auravindex.viewmodels.ActivePlanViewModel
import com.elektro24team.auravindex.viewmodels.BookListViewModel
import com.elektro24team.auravindex.viewmodels.BookViewModel
import com.elektro24team.auravindex.viewmodels.LoanStatusViewModel
import com.elektro24team.auravindex.viewmodels.LoanViewModel
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel
import com.elektro24team.auravindex.viewmodels.NotificationViewModel
import com.elektro24team.auravindex.viewmodels.UserViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@RequiresApi(Build.VERSION_CODES.O)
@RequiresPermission(android.Manifest.permission.POST_NOTIFICATIONS)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BookScreen(
    navController: NavController,
    bookId: String,
    bookViewModel: BookViewModel,
    activePlanViewModel: ActivePlanViewModel,
    loanViewModel: LoanViewModel,
    loanStatusViewModel: LoanStatusViewModel,
    localSettingViewModel: LocalSettingViewModel,
    userViewModel: UserViewModel,
    notificationViewModel: NotificationViewModel,
    bookListViewModel: BookListViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme
    val showTermsDialog = remember { mutableStateOf(false) }
    val showPrivacyDialog = remember { mutableStateOf(false) }
    val showTeamDialog = remember { mutableStateOf(false) }
    val showRequestLoanDialog = remember { mutableStateOf(false) }
    val book = bookViewModel.book.collectAsState()
    val loanStatus = loanStatusViewModel.loanStatus.observeAsState()
    val activePlan = activePlanViewModel.activePlan.observeAsState()
    val settings = localSettingViewModel.settings.collectAsState()
    val userLoans = loanViewModel.userLoans.collectAsState()
    val userLists by bookListViewModel.bookLists.collectAsState()
    val bookLoans = loanViewModel.bookLoans.collectAsState()
    val createdLoan = loanViewModel.createdLoan.collectAsState()
    LaunchedEffect(bookId) {
        loanViewModel.clearViewModelData()
        if(isLoggedIn(settings.value)) {
            bookListViewModel.loadUserLists(settings.value[SettingKey.TOKEN.keySetting].toString(), settings.value[SettingKey.ID.keySetting].toString())
            bookViewModel.fetchBookWithAuth(settings.value[SettingKey.TOKEN.keySetting].toString(), bookId)
            loanViewModel.loadBookLoans(settings.value.getOrDefault(SettingKey.TOKEN.keySetting, ""), bookId)
        } else {
            bookViewModel.loadBook(bookId)
        }
        loanStatusViewModel.loadLoanStatusByName("PENDING")
        if(isLoggedIn(settings.value) && !settings.value[SettingKey.ACTIVE_PLAN_ID.keySetting].isNullOrEmpty()) {
            activePlanViewModel.loadActivePlanById(settings.value[SettingKey.TOKEN.keySetting].toString(), settings.value[SettingKey.ACTIVE_PLAN_ID.keySetting].toString())
        } else if(isLoggedIn(settings.value) && settings.value[SettingKey.ACTIVE_PLAN_ID.keySetting].isNullOrEmpty()) {
            activePlanViewModel.loadActivePlanByUserId(
                settings.value[SettingKey.TOKEN.keySetting].toString(),
                settings.value[SettingKey.ID.keySetting].toString()
            )
        }
        if(isLoggedIn(settings.value)) {
            loanViewModel.loadUserLoans(
                settings.value[SettingKey.TOKEN.keySetting].toString(),
                settings.value[SettingKey.ID.keySetting].toString()
            )
        }
    }
    LaunchedEffect(createdLoan.value) {
        if(createdLoan.value) {
            loanViewModel.loadUserLoans(
                settings.value[SettingKey.TOKEN.keySetting].toString(),
                settings.value[SettingKey.ID.keySetting].toString()
            )
            bookViewModel.loadBook(bookId)
        }
    }
    ObserveTokenExpiration(bookViewModel, navController, localSettingViewModel)
    ObserveTokenExpiration(activePlanViewModel, navController, localSettingViewModel)
    ObserveTokenExpiration(loanViewModel, navController, localSettingViewModel)
    ObserveTokenExpiration(loanStatusViewModel, navController, localSettingViewModel)
    ObserveTokenExpiration(bookListViewModel, navController, localSettingViewModel)
    ObserveError(bookViewModel)
    ObserveError(loanStatusViewModel)
    ObserveError(activePlanViewModel)
    ObserveError(bookListViewModel)
    ObserveError(loanViewModel)
    ObserveSuccess(loanViewModel)
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
                TopBar(navController = navController, drawerState = drawerState)
            },
            bottomBar = {
                BottomNavBar(
                    currentRoute = navController.currentBackStackEntry?.destination?.route ?: "book",
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
                                colors = listOf(Color(0xFFB185BD), Color(0xFFE2D9E5))
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(MediumPadding)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.Start
                    ) {
                        val app = LocalContext.current.applicationContext as AuraVindexApp
                        val isConnected by app.networkLiveData.observeAsState(true)
                        ConnectionAlert(isConnected)

                        if (showRequestLoanDialog.value) {
                            RequestLoanDialog(
                                navController = navController,
                                showRequestLoanDialog = showRequestLoanDialog,
                                loanViewModel = loanViewModel,
                                bookViewModel = bookViewModel,
                                notificationViewModel = notificationViewModel,
                                loanStatus = loanStatus.value!!,
                                token = settings.value.getOrDefault(SettingKey.TOKEN.keySetting, ""),
                                book = book.value!!,
                                plan = activePlanViewModel.activePlan.value?.plan!!,
                                userId = settings.value.getOrDefault(SettingKey.ID.keySetting, ""),
                            )
                        }
                        val imageUrl = IMG_url.trimEnd('/') + "/" + book.value?.book_img?.trimStart('/')

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp, horizontal = 8.dp)
                                ) {
                                    Text(
                                        text = book.value?.title ?: "Title",
                                        modifier = Modifier.align(Alignment.Center),
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 24.sp,
                                            textAlign = TextAlign.Center,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp, horizontal = 8.dp)
                                ) {
                                    GlideImage(
                                        imageModel = { imageUrl },
                                        modifier = Modifier
                                            .widthIn(max = 200.dp)
                                            .heightIn(max = 300.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                            .shadow(8.dp, RoundedCornerShape(16.dp))
                                            .align(Alignment.Center),
                                        imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                                        loading = {
                                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                                        },
                                        failure = {
                                            Image(
                                                painter = painterResource(id = R.drawable.logo_app),
                                                contentDescription = "Default img",
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(16.dp))
                                                    .shadow(8.dp, RoundedCornerShape(16.dp))
                                                    .align(Alignment.Center)
                                            )
                                        }
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Book Details",
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = Color(0xFF572365),
                                        textAlign = TextAlign.Center
                                    ),
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )
                                Divider(color = Color.LightGray, thickness = 1.dp)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = buildAnnotatedString {
                                            append("Summary: ")
                                            withStyle(SpanStyle(fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight.Normal)) {
                                                append(book.value?.summary ?: "Not available")
                                            }
                                        },
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = Color(0xFF572365)
                                        ),
                                        modifier = Modifier.padding(bottom = 12.dp))
                                }
                                Divider(color = Color.LightGray, thickness = 1.dp)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Authors: ",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = Color(0xFF572365)
                                        ),
                                        modifier = Modifier.padding(bottom = 12.dp))
                                    Text(
                                        text = book.value?.authors?.joinToString(", ") { it.name + " " + it.last_name } ?: "Not available",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = Color(0xFF572365)
                                        ),
                                        modifier = Modifier.padding(bottom = 12.dp))
                                }
                                Divider(color = Color.LightGray, thickness = 1.dp)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Editorial: ",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = Color(0xFF572365)
                                        ),
                                        modifier = Modifier.padding(bottom = 12.dp))
                                    Text(
                                        text = book.value?.editorial?.name ?: "Not available",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = Color(0xFF572365)
                                        ),
                                        modifier = Modifier.padding(bottom = 12.dp))
                                }
                                Divider(color = Color.LightGray, thickness = 1.dp)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Collection: ",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = Color(0xFF572365)
                                        ),
                                        modifier = Modifier.padding(bottom = 12.dp))
                                    Text(
                                        text = book.value?.book_collection?.name ?: "Not available",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = Color(0xFF572365)
                                        ),
                                        modifier = Modifier.padding(bottom = 12.dp))
                                }
                                Divider(color = Color.LightGray, thickness = 1.dp)
                                Text(
                                    text = "Genres: ",
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = Color(0xFF572365)
                                    ),
                                    modifier = Modifier.padding(top = 12.dp, bottom = 1.dp)
                                )
                                FlowRow(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    book.value?.genres?.forEach { genre ->
                                        Card(
                                            shape = RoundedCornerShape(10.dp),
                                            colors = CardDefaults.cardColors(containerColor = Color(0xFFEDE7F6)),
                                            elevation = CardDefaults.cardElevation(4.dp),
                                            modifier = Modifier.padding(end = 6.dp)
                                        ) {
                                            Text(
                                                text = genre,
                                                modifier = Modifier
                                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                                color = Color(0xFF5E35B1),
                                                fontSize = 12.sp,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                                Divider(color = Color.LightGray, thickness = 1.dp)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Edition: ",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = Color(0xFF572365)
                                        ),
                                        modifier = Modifier.padding(bottom = 12.dp))
                                    Text(
                                        text = book.value?.edition ?: "Not available",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = Color(0xFF572365)
                                        ),
                                        modifier = Modifier.padding(bottom = 12.dp))
                                }
                                Divider(color = Color.LightGray, thickness = 1.dp)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Language: ",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = Color(0xFF572365)
                                        ),
                                    )
                                    Text(
                                        text = book.value?.language ?: "Not available",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = Color(0xFF572365)
                                        ),
                                        modifier = Modifier.padding(bottom = 12.dp))
                                }
                                Divider(color = Color.LightGray, thickness = 1.dp)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Location: ",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = Color(0xFF572365)
                                        ),
                                    )
                                    Text(
                                        text = book.value?.location ?: "Not available",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = Color(0xFF572365)
                                        ),
                                        modifier = Modifier.padding(bottom = 12.dp))
                                }
                                Divider(color = Color.LightGray, thickness = 1.dp)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Status: ",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = Color(0xFF572365)
                                        ),
                                        modifier = Modifier.padding(bottom = 12.dp))
                                    Text(
                                        text = book.value?.book_status?.book_status ?: "Not available",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = Color(0xFF572365)
                                        ),
                                        modifier = Modifier.padding(bottom = 12.dp))
                                }
                                Divider(color = Color.LightGray, thickness = 1.dp)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Classification: ",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = Color(0xFF572365)
                                        ),
                                        modifier = Modifier.padding(bottom = 12.dp))
                                    Text(
                                        text = book.value?.classification ?: "Not available",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = Color(0xFF572365)
                                        ),
                                        modifier = Modifier.padding(bottom = 12.dp))
                                }
                                Divider(color = Color.LightGray, thickness = 1.dp)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "ISBN: ",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = Color(0xFF572365)
                                        ),
                                        modifier = Modifier.padding(bottom = 12.dp))
                                    Text(
                                        text = book.value?.isbn ?: "Not available",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = Color(0xFF572365)
                                        ),
                                        modifier = Modifier.padding(bottom = 12.dp))
                                }
                                Divider(color = Color.LightGray, thickness = 1.dp)
                                book.value?.let { UserBookLists(bookLists = userLists, bookId = it._id, bookListViewModel = bookListViewModel, context = context, token = settings.value[SettingKey.TOKEN.keySetting].toString()) }
                                if (isLoggedIn(settings.value) && book.value?.book_status?.book_status == "AVAILABLE") {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(
                                        onClick = {
                                            if (!isLoggedIn(settings.value)) {
                                                mustBeLoggedInToast(context, AppAction.LOAN_BOOK, navController)
                                            } else if (activePlanViewModel.activePlan.value == null) {
                                                mustBeSubscribedToast(context, AppAction.LOAN_BOOK, navController)
                                            } else if (book.value?.book_status?.book_status != "AVAILABLE") {
                                                Toast.makeText(context, "The book is not available.", Toast.LENGTH_SHORT).show()
                                            } else showRequestLoanDialog.value = true
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp),
                                        colors = ButtonDefaults.buttonColors(backgroundColor = PurpleC),
                                        shape = RoundedCornerShape(12.dp),
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.LibraryAdd,
                                            contentDescription = "Loan",
                                            tint = Color.White,
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                        Text(
                                            text = "Loan",
                                            color = Color.White,
                                            style = TextStyle(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 18.sp,
                                                color = Color(0xFF572365)
                                            )
                                        )
                                    }
                                }
                            }
                        }
                        if(isLoggedIn(settings.value) && (book.value?.book_status?.book_status?.lowercase() == "lent")) {
                            bookLoans.value?.forEach { loan ->
                                if ((loan?.book?._id == bookId) && (loan?.loan_status?.loan_status?.lowercase() != "finished") && (loan?.user?._id == settings.value.getOrDefault(SettingKey.ID.keySetting, ""))) {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 16.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color.White
                                        )
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text(
                                                "Loan Details",
                                                style = TextStyle(
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 18.sp,
                                                    color = Color(0xFF572365),
                                                    textAlign = TextAlign.Center
                                                ),
                                                modifier = Modifier.padding(bottom = 12.dp)
                                            )
                                            Divider(color = Color.LightGray, thickness = 1.dp)

                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 4.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = "Loan status: ",
                                                    style = TextStyle(
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 18.sp,
                                                        color = Color(0xFF572365)
                                                    ),
                                                    modifier = Modifier.padding(bottom = 12.dp))
                                                Text(
                                                    text = loan.loan_status.loan_status,
                                                    style = TextStyle(
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 18.sp,
                                                        color = Color(0xFF572365)
                                                    ),
                                                    modifier = Modifier.padding(bottom = 12.dp))
                                            }
                                            Divider(color = Color.LightGray, thickness = 1.dp)
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 4.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = "Return date: ",
                                                    style = TextStyle(
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 18.sp,
                                                        color = Color(0xFF572365)
                                                    ),
                                                    modifier = Modifier.padding(bottom = 12.dp))
                                                Text(
                                                    text = formatUtcToLocalWithDate(loan.return_date),
                                                    style = TextStyle(
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 18.sp,
                                                        color = Color(0xFF572365)
                                                    ),
                                                    modifier = Modifier.padding(bottom = 12.dp))
                                            }
                                            Divider(color = Color.LightGray, thickness = 1.dp)
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 4.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = "Renewals left: ",
                                                    style = TextStyle(
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 18.sp,
                                                        color = Color(0xFF572365)
                                                    ),
                                                    modifier = Modifier.padding(bottom = 12.dp))
                                                Text(
                                                    text = "${activePlanViewModel.activePlan.value?.plan?.max_renewals_per_loan?.toInt()?.minus(loan.renewals.toInt())}/${activePlanViewModel.activePlan.value?.plan?.max_renewals_per_loan}",
                                                    style = TextStyle(
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 18.sp,
                                                        color = Color(0xFF572365)
                                                    ),
                                                    modifier = Modifier.padding(bottom = 12.dp))
                                            }
                                            Divider(color = Color.LightGray, thickness = 1.dp)
                                            if (loan.renewals.toInt() < activePlanViewModel.activePlan.value?.plan?.max_renewals_per_loan?.toInt()!!) {
                                                Spacer(modifier = Modifier.height(16.dp))
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Button(
                                                        onClick = {
                                                            if (!isLoggedIn(settings.value)) {
                                                                mustBeLoggedInToast(
                                                                    context,
                                                                    AppAction.LOAN_BOOK,
                                                                    navController
                                                                )
                                                            } else if (activePlan.value == null) {
                                                                mustBeSubscribedToast(
                                                                    context,
                                                                    AppAction.LOAN_BOOK,
                                                                    navController
                                                                )
                                                            } else if (loan.loan_status.loan_status != "ACTIVE" && loan.loan_status.loan_status != "RENEWED") {
                                                                Toast.makeText(
                                                                    context,
                                                                    "This loan is currently not active.",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            } else {
                                                                loanViewModel.renewLoan(
                                                                    settings.value[SettingKey.TOKEN.keySetting].toString(),
                                                                    loan,
                                                                    notificationViewModel
                                                                )
                                                                navController.navigate("book/$bookId")
                                                            }
                                                        },
                                                        modifier = Modifier
                                                            .height(48.dp)
                                                            .weight(1f),
                                                        colors = ButtonDefaults.buttonColors(
                                                            backgroundColor = PurpleC
                                                        ),
                                                        shape = RoundedCornerShape(12.dp),
                                                    ) {
                                                        androidx.compose.material.Icon(
                                                            imageVector = Icons.Filled.Autorenew,
                                                            contentDescription = "Renew",
                                                            tint = Color.White,
                                                            modifier = Modifier.padding(end = 8.dp)
                                                        )
                                                        Text(
                                                            text = "Renew",
                                                            color = Color.White,
                                                            style = TextStyle(
                                                                fontWeight = FontWeight.Bold,
                                                                fontSize = 16.sp
                                                            )
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                if((isLoggedIn(settings.value)) && (loan.loan_status.loan_status.lowercase() != "finished") && (isAdmin(settings.value))) {
                                    if (book.value?.book_status?.book_status?.lowercase() == "lent") {
                                        bookLoans.value?.forEach { bookLoan ->
                                            if (bookLoan.book._id == bookId && bookLoan.loan_status.loan_status.lowercase() != "finished") {
                                                Card(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(top = 16.dp),
                                                    shape = RoundedCornerShape(12.dp),
                                                    colors = CardDefaults.cardColors(containerColor = Color.White)
                                                ) {
                                                    Column {
                                                        Column(modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(horizontal = 16.dp)
                                                        ) {
                                                            Text(
                                                                text = "Loan Details (ADMIN VIEW)",
                                                                style = TextStyle(
                                                                    fontWeight = FontWeight.Bold,
                                                                    fontSize = 18.sp,
                                                                    color = Color(0xFF572365)
                                                                ),
                                                                modifier = Modifier.padding(vertical = 12.dp)
                                                            )
                                                            Divider(color = Color.LightGray, thickness = 1.dp)
                                                            Row(
                                                                modifier = Modifier
                                                                    .fillMaxWidth()
                                                                    .padding(vertical = 4.dp)
                                                                    .clickable { navController.navigate("admin_dashboard/user/${loan.user._id}") },
                                                                horizontalArrangement = Arrangement.SpaceBetween
                                                            ) {
                                                                Text(
                                                                    text = "Loaned to:",
                                                                    style = TextStyle(
                                                                        fontWeight = FontWeight.Bold,
                                                                        fontSize = 16.sp,
                                                                        color = Color(0xFF572365)
                                                                    )
                                                                )
                                                                Text(
                                                                    text = "${loan.user.name} ${loan.user.last_name}",
                                                                    style = TextStyle(
                                                                        fontWeight = FontWeight.Bold,
                                                                        fontSize = 18.sp,
                                                                        color = Color(0xFF572365)
                                                                    ),
                                                                    modifier = Modifier.padding(bottom = 12.dp)
                                                                )
                                                            }
                                                            Divider(color = Color.LightGray, thickness = 1.dp)
                                                            Row(
                                                                modifier = Modifier
                                                                    .fillMaxWidth()
                                                                    .padding(vertical = 4.dp),
                                                                horizontalArrangement = Arrangement.SpaceBetween
                                                            ) {
                                                                Text(
                                                                    text = "Start Date:",
                                                                    style = TextStyle(
                                                                        fontWeight = FontWeight.Bold,
                                                                        fontSize = 16.sp,
                                                                        color = Color(0xFF572365)
                                                                    )
                                                                )
                                                                Text(
                                                                    text = formatUtcToLocalWithDate(bookLoan.createdAt),
                                                                    style = TextStyle(
                                                                        fontWeight = FontWeight.Bold,
                                                                        fontSize = 18.sp,
                                                                        color = Color(0xFF572365)
                                                                    ),
                                                                    modifier = Modifier.padding(bottom = 12.dp)
                                                                )
                                                            }
                                                            Divider(color = Color.LightGray, thickness = 1.dp)
                                                            Row(
                                                                modifier = Modifier
                                                                    .fillMaxWidth()
                                                                    .padding(vertical = 4.dp),
                                                                horizontalArrangement = Arrangement.SpaceBetween
                                                            ) {
                                                                Text(
                                                                    text = "End Date:",
                                                                    style = TextStyle(
                                                                        fontWeight = FontWeight.Bold,
                                                                        fontSize = 16.sp,
                                                                        color = Color(0xFF572365)
                                                                    )
                                                                )
                                                                Text(
                                                                    text = formatUtcToLocalWithDate(bookLoan.return_date),
                                                                    style = TextStyle(
                                                                        fontWeight = FontWeight.Bold,
                                                                        fontSize = 18.sp,
                                                                        color = Color(0xFF572365)
                                                                    ),
                                                                    modifier = Modifier.padding(bottom = 12.dp)
                                                                )
                                                            }
                                                            Divider(color = Color.LightGray, thickness = 1.dp)
                                                            Row(
                                                                modifier = Modifier
                                                                    .fillMaxWidth()
                                                                    .padding(vertical = 4.dp),
                                                                horizontalArrangement = Arrangement.SpaceBetween
                                                            ) {
                                                                Text(
                                                                    text = "Status:",
                                                                    style = TextStyle(
                                                                        fontWeight = FontWeight.Bold,
                                                                        fontSize = 16.sp,
                                                                        color = Color(0xFF572365)
                                                                    )
                                                                )
                                                                Text(
                                                                    text = bookLoan.loan_status.loan_status,
                                                                    style = TextStyle(
                                                                        fontWeight = FontWeight.Bold,
                                                                        fontSize = 18.sp,
                                                                        color = Color(0xFF572365)
                                                                    ),
                                                                    modifier = Modifier.padding(bottom = 12.dp)
                                                                )
                                                            }
                                                            Divider(color = Color.LightGray, thickness = 1.dp)
                                                        }
                                                        Divider(color = Color.LightGray, thickness = 1.dp)
                                                        Row(
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .padding(16.dp),
                                                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                                                            verticalAlignment = Alignment.CenterVertically
                                                        ) {
                                                            if(bookLoan.loan_status.loan_status == "PENDING") {
                                                                Button(
                                                                    onClick = {
                                                                        loanViewModel.approveLoan(settings.value.getOrDefault(SettingKey.TOKEN.keySetting, ""), bookLoan, notificationViewModel)
                                                                        navController.navigate("book/${bookId}")
                                                                    },
                                                                    modifier = Modifier
                                                                        .height(48.dp)
                                                                        .weight(1f),
                                                                    colors = ButtonDefaults.buttonColors(backgroundColor = PurpleC),
                                                                    shape = RoundedCornerShape(12.dp),
                                                                ) {
                                                                    androidx.compose.material.Icon(
                                                                        imageVector = Icons.Filled.Check,
                                                                        contentDescription = "Approve",
                                                                        tint = Color.White,
                                                                        modifier = Modifier.padding(end = 8.dp)
                                                                    )
                                                                    Text(
                                                                        text = "Approve",
                                                                        color = Color.White,
                                                                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                                                    )
                                                                }
                                                            }
                                                            Button(
                                                                onClick = {
                                                                    loanViewModel.finishLoan(
                                                                        settings.value.getOrDefault(SettingKey.TOKEN.keySetting, ""),
                                                                        bookLoan,
                                                                        notificationViewModel
                                                                    )
                                                                    navController.navigate("book/${bookId}")
                                                                },
                                                                modifier = Modifier
                                                                    .height(48.dp)
                                                                    .weight(1f),
                                                                colors = ButtonDefaults.buttonColors(backgroundColor = colors.error),
                                                                shape = RoundedCornerShape(12.dp),
                                                            ) {
                                                                androidx.compose.material.Icon(
                                                                    imageVector = Icons.Filled.SettingsBackupRestore,
                                                                    contentDescription = "Finish",
                                                                    tint = Color.White,
                                                                    modifier = Modifier.padding(end = 8.dp)
                                                                )
                                                                Text(
                                                                    text = "Finish",
                                                                    color = Color.White,
                                                                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}
