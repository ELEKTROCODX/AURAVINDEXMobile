package com.elektro24team.auravindex.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elektro24team.auravindex.ui.components.BottomNavBar
import com.elektro24team.auravindex.ui.components.DrawerMenu
import com.elektro24team.auravindex.ui.theme.MediumPadding
import kotlinx.coroutines.launch
import androidx.navigation.NavController
import com.elektro24team.auravindex.AuraVindexApp
import androidx.navigation.compose.rememberNavController
import com.elektro24team.auravindex.navigation.Routes
import com.elektro24team.auravindex.ui.components.BookCollectionsSection
import com.elektro24team.auravindex.ui.components.ConnectionAlert
import com.elektro24team.auravindex.ui.components.ShowExternalLinkDialog
import com.elektro24team.auravindex.utils.hamburguerMenuNavigator
import com.elektro24team.auravindex.view.viewmodels.BookViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import coil.compose.AsyncImage


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController ) {
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
                    title = { Text("AURA VINDEX ") },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
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
                    currentRoute = navController.currentBackStackEntry?.destination?.route ?: "search",
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
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val app = LocalContext.current.applicationContext as AuraVindexApp
                        val isConnected by app.networkLiveData.observeAsState(true)
                        ConnectionAlert(isConnected)

                        val bookViewModel: BookViewModel = viewModel()
                        val filteredBooks by bookViewModel.filteredBooks

                        var searchText by remember { mutableStateOf("") }
                        val filterOptions = listOf("Título", "Autor", "Género")
                        var selectedFilter by remember { mutableStateOf(filterOptions[0]) }

                        TextField(
                            value = searchText,
                            onValueChange = {
                                searchText = it
                                if (searchText.isNotBlank()) {
                                    bookViewModel.fetchFilteredBooks(
                                        showDuplicates = false,
                                        showLents = true,
                                        filter = selectedFilter.lowercase(),
                                        value = searchText
                                    )
                                }
                            },
                            label = { Text("Buscar libro") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )

                        // Chips de filtro
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            filterOptions.forEach { option ->
                                FilterChip(
                                    selected = selectedFilter == option,
                                    onClick = {
                                        selectedFilter = option
                                        if (searchText.isNotBlank()) {
                                            bookViewModel.fetchFilteredBooks(
                                                showDuplicates = false,
                                                showLents = true,
                                                filter = selectedFilter.lowercase(),
                                                value = searchText
                                            )
                                        }
                                    },
                                    label = { Text(option) }
                                )
                            }
                        }

                        // Filtrado de libros según el texto de búsqueda
                        if(searchText.isNotEmpty()){
                            //se supone que es con la API
                            val filtered1 = bookViewModel.filteredBooks.value
                            //filtrado local
                            val filtered = bookViewModel.getFirst5FilteredBooks(
                                books = bookViewModel.filteredBooks.value,
                                search = searchText,
                                filter = selectedFilter
                            )


                            LazyColumn {
                                items(filtered.size) { index ->
                                    val book = filtered[index]
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                            .clickable {
                                                navController.navigate("${Routes.BOOK}/${book._id}")
                                            }
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .padding(8.dp)
                                                .fillMaxWidth()
                                        ) {
                                            AsyncImage(
                                                model = book.book_img,
                                                contentDescription = "Imagen del libro",
                                                modifier = Modifier
                                                    .size(100.dp)
                                                    .padding(end = 8.dp)
                                            )
                                            Column(modifier = Modifier.fillMaxWidth()) {
                                                Text(text = book.title, style = MaterialTheme.typography.titleMedium)
                                                Text(text = book.summary, style = MaterialTheme.typography.bodyMedium)
                                                Text(
                                                    text = "Autor(es): ${book.authors.joinToString { it.name }}",
                                                    style = MaterialTheme.typography.bodySmall
                                                )
                                                Text(
                                                    text = "Géneros: ${book.genres.joinToString()}",
                                                    style = MaterialTheme.typography.bodySmall
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                        }else{
                            BookCollectionsSection()
                        }
                        /*val filteredItems = listOf("Berry", "Banana", "Cherry", "Apple").filter {
                            it.contains(searchText, ignoreCase = true)
                        }

                        LazyColumn {
                            items(filteredItems.size) { index ->
                                Text(filteredItems[index])
                            }
                        }*/
                    }
                }
            }
        )
    }
}

//función sin parámetro navController para usar preview
@Preview(showBackground = true)
@Composable
fun previewSearchScreen(){
    val navController = rememberNavController()
    SearchScreen(navController)
}