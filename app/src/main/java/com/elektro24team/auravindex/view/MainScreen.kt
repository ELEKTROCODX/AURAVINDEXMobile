package com.elektro24team.auravindex.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val showTermsDialog = remember { mutableStateOf(false) }
    val showPrivacyDialog = remember { mutableStateOf(false) }
    val showTeamDialog = remember { mutableStateOf(false) }
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
                            title = "Recommendations",
                            books = listOf(
                                Book(
                                    id = "1",
                                    title = "Libro 1",
                                    isbn = "1234567890",
                                    classification = "Fiction",
                                    summary = "A summary of the book",
                                    editorial = "Penguin Books",
                                    language = "English",
                                    edition = "1st",
                                    sample = "1",
                                    location = "Library A",
                                    book_status = "Available",
                                    genres = listOf("Fiction", "Fantasy"),
                                    book_collection = "Collection X",
                                    authors = listOf("Author 1", "Author 2"),
                                    book_img = "https://upload.wikimedia.org/wikipedia/commons/4/47/PNG_transparency_demonstration_1.png\n"
                                ),
                                Book(
                                    id = "2",
                                    title = "Libro 2",
                                    isbn = "1234567890",
                                    classification = "Fiction",
                                    summary = "A summary of the book",
                                    editorial = "Penguin Books",
                                    language = "English",
                                    edition = "1",
                                    sample = "1",
                                    location = "Library A",
                                    book_status = "Available",
                                    genres = listOf("Fiction", "Fantasy"),
                                    book_collection = "Collection X",
                                    authors = listOf("Author 1", "Author 2"),
                                    book_img = "https://example.com/book1.jpg"
                                ),
                                Book(
                                    id = "3",
                                    title = "Libro 3",
                                    isbn = "1234567890",
                                    classification = "Fiction",
                                    summary = "A summary of the book",
                                    editorial = "Penguin Books",
                                    language = "English",
                                    edition = "1",
                                    sample = "1",
                                    location = "Library A",
                                    book_status = "Available",
                                    genres = listOf("Fiction", "Fantasy"),
                                    book_collection = "Collection X",
                                    authors = listOf("Author 1", "Author 2"),
                                    book_img = "https://api.auravindex.me/images/books/810.55.1.png"
                                ),
                                Book(
                                    id = "4",
                                    title = "Libro 4",
                                    isbn = "1234567890",
                                    classification = "Fiction",
                                    summary = "A summary of the book",
                                    editorial = "Penguin Books",
                                    language = "English",
                                    edition = "1",
                                    sample = "1",
                                    location = "Library A",
                                    book_status = "Available",
                                    genres = listOf("Fiction", "Fantasy"),
                                    book_collection = "Collection X",
                                    authors = listOf("Author 1", "Author 2"),
                                    book_img = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/64/Logo_UCA_2015.jpg/1200px-Logo_UCA_2015.jpg"
                                ),
                                Book(
                                    id = "5",
                                    title = "Libro 5",
                                    isbn = "1234567890",
                                    classification = "Fiction",
                                    summary = "A summary of the book",
                                    editorial = "Penguin Books",
                                    language = "English",
                                    edition = "1",
                                    sample = "1",
                                    location = "Library A",
                                    book_status = "Available",
                                    genres = listOf("Fiction", "Fantasy"),
                                    book_collection = "Collection X",
                                    authors = listOf("Author 1", "Author 2"),
                                    book_img = "https://auravindex.me/imgs/6.png"
                                )
                            ),
                            seeMoreAction = {

                            },
                            navController = navController
                        )
                    }
                }
            }
        )
    }
}
