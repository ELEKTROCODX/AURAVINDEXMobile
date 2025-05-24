package com.elektro24team.auravindex.view


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elektro24team.auravindex.ui.components.BottomNavBar
import androidx.navigation.NavController
import com.elektro24team.auravindex.ui.components.BookCard
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.elektro24team.auravindex.viewmodels.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultsScreen(
    navController: NavController,
    bookViewModel: BookViewModel,
    query: String
) {
    val context = LocalContext.current
    var currentQuery by remember { mutableStateOf(query) }
    var currentPage by remember { mutableIntStateOf(1) }
    val filteredBooks by bookViewModel.filteredBooks.observeAsState(emptyList())
    LaunchedEffect(Unit) {
        bookViewModel.loadBooks(showDuplicates = false, showLents = true)
    }
    val itemsPerPage = 8
    val paginatedBooks = filteredBooks
        .drop((currentPage - 1) * itemsPerPage)
        .take(itemsPerPage)
    val totalPages = (filteredBooks.size + itemsPerPage - 1) / itemsPerPage


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Results") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                // Barra de búsqueda local (filtra entre los resultados previos)
                TextField(
                    value = currentQuery,
                    onValueChange = {
                        currentQuery = it
                        currentPage = 1
                        bookViewModel.searchBook(it)
                    },
                    label = { Text("Search results") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp), singleLine = true
                )

                if(filteredBooks.isNotEmpty()) {
                    if(currentQuery.isNotEmpty()) {
                        Text(
                            text = "${filteredBooks.size} results for \"$currentQuery\"",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                        )
                    } else {
                        Text(
                            text = "Displaying all books",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                        )
                    }
                } else {
                    Text(
                        text = "No results found.",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                    )
                }

                Divider(modifier = Modifier.padding(horizontal = 8.dp))

                // Resultados filtrados
                if (filteredBooks.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(paginatedBooks) { book ->
                            BookCard(book, navController)
                        }
                    }
                    // Paginación
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        //repeat(totalPages) { pageIndex ->
                        /*   Button(
                               onClick = { currentPage = pageIndex + 1 },
                               colors = ButtonDefaults.buttonColors(
                                   containerColor = if (pageIndex + 1 == currentPage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                               )
                           ) {
                               Text(text = "${pageIndex + 1}")
                           } */
                        (1..totalPages).forEach { page ->
                            Button(onClick = { currentPage = page },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (page == currentPage)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.secondaryContainer,
                                    contentColor = if (page == currentPage)
                                        MaterialTheme.colorScheme.onPrimary
                                    else
                                        MaterialTheme.colorScheme.onSecondaryContainer
                                ),
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                            ) {
                                Text(text = "$page")
                            }
                        }
                    }
                }
            }
        }
    )

}
