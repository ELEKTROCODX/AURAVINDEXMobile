package com.elektro24team.auravindex.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.elektro24team.auravindex.ui.components.BottomNavBar
import com.elektro24team.auravindex.ui.components.DrawerMenu
import com.elektro24team.auravindex.ui.theme.MediumPadding
import kotlinx.coroutines.launch
import androidx.navigation.NavController
import com.elektro24team.auravindex.model.Book
import com.elektro24team.auravindex.navigation.Routes
import com.elektro24team.auravindex.ui.components.HomePageSection
import com.elektro24team.auravindex.ui.components.ShowExternalLinkDialog
import com.elektro24team.auravindex.utils.hamburguerMenuNavigator
import com.elektro24team.auravindex.utils.openLink
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elektro24team.auravindex.ui.components.ImageAPI
import com.elektro24team.auravindex.view.viewmodels.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: BookViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val showTermsDialog = remember { mutableStateOf(false) }
    val showPrivacyDialog = remember { mutableStateOf(false) }
    val showTeamDialog = remember { mutableStateOf(false) }
    val books = viewModel.posts
    ModalNavigationDrawer(
        drawerContent = {
            DrawerMenu(onItemSelected = { route ->
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
                TopAppBar(
                    title = { Text("AURA VINDEX") },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch { drawerState.open() } // Abre el drawer
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Menú"
                            )
                        }
                    }
                )
            },
            bottomBar = {
                BottomNavBar(
                    currentRoute = navController.currentBackStackEntry?.destination?.route ?: "main",
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
                            .padding(MediumPadding),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        /*
                        * Recommendations
                        * */
                        HomePageSection(
                            "Recommendations",
                            books,
                            seeMoreAction = { navController.navigate(Routes.SEARCH) },
                            navController
                        )
                    }
                }
            }
        )
    }
}
