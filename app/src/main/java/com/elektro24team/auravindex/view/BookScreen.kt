package com.elektro24team.auravindex.view

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.LibraryAdd
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import com.elektro24team.auravindex.model.Book
import com.elektro24team.auravindex.ui.components.BottomNavBar
import com.elektro24team.auravindex.ui.components.DrawerMenu
import com.elektro24team.auravindex.ui.components.ConnectionAlert
import com.elektro24team.auravindex.ui.components.RequestLoanDialog
import com.elektro24team.auravindex.ui.components.ShowExternalLinkDialog
import com.elektro24team.auravindex.ui.components.TopBar
import com.elektro24team.auravindex.ui.theme.MediumPadding
import com.elektro24team.auravindex.ui.theme.OrangeC
import com.elektro24team.auravindex.ui.theme.PurpleC
import com.elektro24team.auravindex.utils.constants.URLs.IMG_url
import com.elektro24team.auravindex.utils.enums.AppAction
import com.elektro24team.auravindex.utils.enums.SettingKey
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveError
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveSuccess
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveTokenExpiration
import com.elektro24team.auravindex.utils.functions.formatUtcToLocalWithDate
import com.elektro24team.auravindex.utils.functions.hamburguerMenuNavigator
import com.elektro24team.auravindex.utils.functions.isLoggedIn
import com.elektro24team.auravindex.utils.functions.mustBeLoggedInToast
import com.elektro24team.auravindex.utils.functions.mustBeSubscribedToast
import com.elektro24team.auravindex.viewmodels.ActivePlanViewModel
import com.elektro24team.auravindex.viewmodels.BookViewModel
import com.elektro24team.auravindex.viewmodels.LoanStatusViewModel
import com.elektro24team.auravindex.viewmodels.LoanViewModel
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookScreen(
    navController: NavController,
    bookId: String,
    bookViewModel: BookViewModel,
    activePlanViewModel: ActivePlanViewModel,
    loanViewModel: LoanViewModel,
    loanStatusViewModel: LoanStatusViewModel,
    localSettingViewModel: LocalSettingViewModel,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val context = LocalContext.current
    val showTermsDialog = remember { mutableStateOf(false) }
    val showPrivacyDialog = remember { mutableStateOf(false) }
    val showTeamDialog = remember { mutableStateOf(false) }
    val showRequestLoanDialog = remember { mutableStateOf(false) }
    val book = bookViewModel.book.observeAsState()
    val loanStatus = loanStatusViewModel.loanStatus.observeAsState()
    val activePlan = activePlanViewModel.activePlan.observeAsState()
    val settings = localSettingViewModel.settings.collectAsState()
    val userLoans = loanViewModel.userLoans.observeAsState()
    LaunchedEffect(bookId) {
        if(isLoggedIn(settings.value)) {
            bookViewModel.fetchBookWithAuth(settings.value[SettingKey.TOKEN.keySetting].toString(), bookId)
        } else {
            bookViewModel.loadBook(bookId)
        }
        loanStatusViewModel.loadLoanStatusByName("PENDING")
        if(isLoggedIn(settings.value) && !settings.value[SettingKey.ACTIVE_PLAN_ID.keySetting].isNullOrEmpty()) {
            activePlanViewModel.loadActivePlanById(settings.value[SettingKey.TOKEN.keySetting].toString(), settings.value[SettingKey.ACTIVE_PLAN_ID.keySetting].toString())
        } else if(settings.value[SettingKey.ACTIVE_PLAN_ID.keySetting].isNullOrEmpty()) {
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
    ObserveTokenExpiration(bookViewModel, navController, localSettingViewModel)
    ObserveTokenExpiration(activePlanViewModel, navController, localSettingViewModel)
    ObserveTokenExpiration(loanViewModel, navController, localSettingViewModel)
    ObserveTokenExpiration(loanStatusViewModel, navController, localSettingViewModel)
    ObserveError(bookViewModel)
    ObserveError(loanStatusViewModel)
    ObserveError(activePlanViewModel)
    ObserveError(loanViewModel)
    ObserveSuccess(loanViewModel)
    ModalNavigationDrawer(
        drawerContent = {
            DrawerMenu(
                navController = navController,
                currentRoute = navController.currentBackStackEntry?.destination?.route,
                onItemSelected = { route ->
                hamburguerMenuNavigator(
                    route,
                    navController,
                    showTermsDialog,
                    showPrivacyDialog,
                    showTeamDialog
                )
            })
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
            content = { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
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
                        if(showRequestLoanDialog.value) {
                            RequestLoanDialog(
                                showRequestLoanDialog = showRequestLoanDialog,
                                loanViewModel = loanViewModel,
                                bookViewModel = bookViewModel,
                                loanStatus = loanStatus.value!!,
                                token = settings.value.getOrDefault(SettingKey.TOKEN.keySetting, ""),
                                book = book.value!!,
                                plan = activePlanViewModel.activePlan.value?.plan!!,
                                userId = settings.value.getOrDefault(SettingKey.ID.keySetting, ""),
                            )
                        }
                        val imageUrl = IMG_url.trimEnd('/') + "/" + book.value?.book_img?.trimStart('/')
                        Text(
                            text = book.value?.title ?: "Title",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 25.sp,
                                color = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.padding(top = 16.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            GlideImage(
                                imageModel = { imageUrl },
                                modifier = Modifier
                                    .widthIn(max=200.dp)
                                    .heightIn(max=300.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .shadow(8.dp, RoundedCornerShape(16.dp))
                                    .align(Alignment.Center),
                                imageOptions = ImageOptions(
                                    contentScale = ContentScale.Crop
                                ),
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
                        Spacer(modifier = Modifier.height(20.dp))
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                "Book Details",
                                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF572365)),
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
                                            append(book.value?.summary?: "Not available")
                                        }
                                    },
                                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF572365), textAlign = TextAlign.Justify)
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
                                    text = "Authors: ",
                                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF572365)),
                                )
                                Text(
                                    text = book.value?.authors?.joinToString(", ") { it.name + " " + it.last_name } ?: "Not available",
                                    style = TextStyle(fontSize = 16.sp, color = Color.Black)
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
                                    text = "Editorial: ",
                                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF572365)),
                                )
                                Text(
                                    text = book.value?.editorial?.name ?: "Not available",
                                    style = TextStyle(fontSize = 16.sp, color = Color.Black)
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
                                    text = "Collection: ",
                                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF572365)),
                                )
                                Text(
                                    text = book.value?.book_collection?.name ?: "Not available",
                                    style = TextStyle(fontSize = 16.sp, color = Color.Black)
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
                                    text = "Genres: ",
                                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF572365)),
                                )
                                Text(
                                    text = book.value?.genres?.joinToString(", ") ?: "Not available",
                                    style = TextStyle(fontSize = 16.sp, color = Color.Black, textAlign = TextAlign.Justify)
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
                                    text = "Edition: ",
                                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF572365)),
                                )
                                Text(
                                    text = book.value?.edition ?: "Not available",
                                    style = TextStyle(fontSize = 16.sp, color = Color.Black)
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
                                    text = "Language: ",
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = Color(0xFF572365)
                                    ),
                                )
                                Text(
                                    text = book.value?.language ?: "Not available",
                                    style = TextStyle(fontSize = 16.sp, color = Color.Black)
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
                                    text = "Location: ",
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = Color(0xFF572365)
                                    ),
                                )
                                Text(
                                    text = book.value?.location ?: "Not available",
                                    style = TextStyle(fontSize = 16.sp, color = Color.Black)
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
                                    text = "Status: ",
                                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF572365)),
                                )
                                Text(
                                    /*text = book.value?.book_status?.book_status?.lowercase()?.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } ?: "Not available",*/
                                    text = book.value?.book_status?.book_status ?: "Not available",
                                    style = TextStyle(fontSize = 16.sp, color = Color.Black)
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
                                    text = "Classification: ",
                                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF572365)),
                                )
                                Text(
                                    text = book.value?.classification ?: "Not available",
                                    style = TextStyle(fontSize = 16.sp, color = Color.Black)
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
                                    text = "ISBN: ",
                                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF572365)),
                                )
                                Text(
                                    text = book.value?.isbn ?: "Not available",
                                    style = TextStyle(fontSize = 16.sp, color = Color.Black)
                                )
                            }
                            Divider(color = Color.LightGray, thickness = 1.dp)
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if(isLoggedIn(settings.value) && book.value?.book_status?.book_status == "AVAILABLE") {
                                Button(
                                    onClick = {
                                        if(!isLoggedIn(settings.value)) {
                                            mustBeLoggedInToast(context, AppAction.LOAN_BOOK, navController)
                                        } else if(activePlanViewModel.activePlan.value == null) {
                                            mustBeSubscribedToast(context, AppAction.LOAN_BOOK, navController)
                                        } else if(book.value?.book_status?.book_status != "AVAILABLE") {
                                            Toast.makeText(context, "The book is not available.", Toast.LENGTH_SHORT).show()
                                        } else showRequestLoanDialog.value = true
                                    },
                                    modifier = Modifier
                                        .height(48.dp)
                                        .weight(1f),
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
                                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    )
                                }
                            }
                        }
                        if(isLoggedIn(settings.value) && (book.value?.book_status?.book_status?.lowercase() == "lent")) {
                            userLoans.value?.forEach { loan ->
                                if(loan?.book?._id == bookId && loan?.loan_status?.loan_status?.lowercase() != "finished") {
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            "Loan Details",
                                            style = TextStyle(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 18.sp,
                                                color = Color(0xFF572365)
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
                                                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF572365)),
                                            )
                                            Text(
                                                text = loan.loan_status.loan_status ?: "Not available",
                                                style = TextStyle(fontSize = 16.sp, color = Color.Black)
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
                                                text = "Return date: ",
                                                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF572365)),
                                            )
                                            Text(
                                                text = formatUtcToLocalWithDate(loan.return_date),
                                                style = TextStyle(fontSize = 16.sp, color = Color.Black)
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
                                                text = "Renewals left: ",
                                                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF572365)),
                                            )
                                            Text(
                                                text = "${activePlanViewModel.activePlan.value?.plan?.max_renewals_per_loan?.toInt()
                                                    ?.minus(loan.renewals.toInt())}/${activePlanViewModel.activePlan.value?.plan?.max_renewals_per_loan}",
                                                style = TextStyle(fontSize = 16.sp, color = Color.Black)
                                            )
                                        }
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            if(loan.renewals.toInt() < activePlanViewModel.activePlan.value?.plan?.max_renewals_per_loan?.toInt()!!) {
                                                Button(
                                                    onClick = {
                                                        if(!isLoggedIn(settings.value)) {
                                                            mustBeLoggedInToast(context, AppAction.LOAN_BOOK, navController)
                                                        } else if(activePlan.value == null) {
                                                            mustBeSubscribedToast(context, AppAction.LOAN_BOOK, navController)
                                                        } else if(loan.loan_status.loan_status != "ACTIVE") {
                                                            Toast.makeText(context, "This loan is currently not active.", Toast.LENGTH_SHORT).show()
                                                        } else {
                                                            loanViewModel.renewLoan(
                                                                settings.value[SettingKey.TOKEN.keySetting].toString(),
                                                                loan._id
                                                            )
                                                        }
                                                    },
                                                    modifier = Modifier
                                                        .height(48.dp)
                                                        .weight(1f),
                                                    colors = ButtonDefaults.buttonColors(backgroundColor = PurpleC),
                                                    shape = RoundedCornerShape(12.dp),
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Filled.Autorenew,
                                                        contentDescription = "Renew",
                                                        tint = Color.White,
                                                        modifier = Modifier.padding(end = 8.dp)
                                                    )
                                                    Text(
                                                        text = "Renew",
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
        )
    }
}
