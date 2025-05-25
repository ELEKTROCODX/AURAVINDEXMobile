package com.elektro24team.auravindex.view

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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.elektro24team.auravindex.navigation.Routes
import com.elektro24team.auravindex.ui.components.BottomNavBar
import com.elektro24team.auravindex.ui.components.ConnectionAlert
import com.elektro24team.auravindex.ui.components.DrawerMenu
import com.elektro24team.auravindex.ui.components.ShowExternalLinkDialog
import com.elektro24team.auravindex.ui.components.TopBar
import com.elektro24team.auravindex.utils.constants.URLs.IMG_url
import com.elektro24team.auravindex.utils.enums.AppAction
import com.elektro24team.auravindex.utils.enums.SettingKey
import com.elektro24team.auravindex.utils.functions.formatUtcToLocalWithDate
import com.elektro24team.auravindex.utils.functions.hamburguerMenuNavigator
import com.elektro24team.auravindex.utils.functions.isLoggedIn
import com.elektro24team.auravindex.utils.functions.mustBeLoggedInToast
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel
import com.elektro24team.auravindex.viewmodels.UserViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    localSettingViewModel: LocalSettingViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val showTermsDialog = remember { mutableStateOf(false) }
    val showPrivacyDialog = remember { mutableStateOf(false) }
    val showTeamDialog = remember { mutableStateOf(false) }
    val user = userViewModel.user.observeAsState()
    val colors = MaterialTheme.colorScheme
    val localSettings = localSettingViewModel.settings.collectAsState()
    LaunchedEffect(Unit) {
        localSettingViewModel.loadSettings(SettingKey.TOKEN.keySetting, SettingKey.ID.keySetting)
        userViewModel.getUserById(localSettings.value.getOrDefault(SettingKey.TOKEN.keySetting, ""), localSettings.value.getOrDefault(SettingKey.ID.keySetting, ""))
    }
    if(!isLoggedIn(localSettings.value)) {
        mustBeLoggedInToast(context, AppAction.ACCESS_PROFILE_PAGE, navController)
    }
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
                    currentRoute = navController.currentBackStackEntry?.destination?.route ?: "profile",
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
                            .padding(horizontal = 16.dp)
                    ) {
                        val app = LocalContext.current.applicationContext as AuraVindexApp
                        val isConnected by app.networkLiveData.observeAsState(true)
                        ConnectionAlert(isConnected)
                        Card(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            shape = androidx.compose.material.MaterialTheme.shapes.medium
                        ) {
                            if(isLoggedIn(localSettings.value)) {
                                val imageUrl = IMG_url.trimEnd('/') + "/" + user?.value?.user_img?.trimStart('/')
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
                                        text = "User Details",
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
                                            text = "Name: ",
                                            style = TextStyle(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp,
                                                color = Color(0xFF572365)
                                            ),
                                        )
                                        Text(
                                            text = "${user.value?.name} ${user.value?.last_name}",
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
                                            text = "Email: ",
                                            style = TextStyle(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp,
                                                color = Color(0xFF572365)
                                            ),
                                        )
                                        Text(
                                            text = user.value?.email ?: "Not available",
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
                                            text = buildAnnotatedString {
                                                append("Biography: ")
                                                withStyle(
                                                    SpanStyle(
                                                        fontSize = 16.sp,
                                                        color = Color.Black,
                                                        fontWeight = FontWeight.Normal
                                                    )
                                                ) {
                                                    append(user.value?.biography ?: "Not available")
                                                }
                                            },
                                            style = TextStyle(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp,
                                                color = Color(0xFF572365),
                                                textAlign = TextAlign.Justify
                                            )
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
                                            text = "Gender: ",
                                            style = TextStyle(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp,
                                                color = Color(0xFF572365)
                                            ),
                                        )
                                        Text(
                                            text = user.value?.gender?.name ?: "Not available",
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
                                            text = "Birthdate: ",
                                            style = TextStyle(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp,
                                                color = Color(0xFF572365)
                                            ),
                                        )
                                        Text(
                                            text = formatUtcToLocalWithDate(user.value?.birthdate),
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
                                            text = "Address: ",
                                            style = TextStyle(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp,
                                                color = Color(0xFF572365)
                                            ),
                                        )
                                        Text(
                                            text = user.value?.address ?: "Not available",
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
                                            text = "Role: ",
                                            style = TextStyle(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp,
                                                color = Color(0xFF572365)
                                            ),
                                        )
                                        Text(
                                            text = user.value?.role?.name ?: "Not available",
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
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}
