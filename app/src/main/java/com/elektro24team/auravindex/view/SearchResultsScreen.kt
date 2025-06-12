package com.elektro24team.auravindex.view


import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.elektro24team.auravindex.ui.theme.PurpleC
import com.elektro24team.auravindex.viewmodels.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultsScreen(
    navController: NavController,
    bookViewModel: BookViewModel,
    query: String
) {
    LocalContext.current
    var currentQuery by remember { mutableStateOf(query) }
    var currentPage by remember { mutableIntStateOf(1) }
    val filteredBooks by bookViewModel.filteredBooks.observeAsState(emptyList())
    LaunchedEffect(Unit) {
        bookViewModel.loadBooks(showDuplicates = false, showLents = true)
    }
    val itemsPerPage = 8
    val paginatedBooks = filteredBooks
        ?.drop((currentPage - 1) * itemsPerPage)
        ?.take(itemsPerPage)
    val totalPages = (filteredBooks?.size?.plus(itemsPerPage)?.minus(1))?.div(itemsPerPage)
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = {
                    Text(
                        text = "Results",
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PurpleC
                )
            )

        },
        bottomBar = {
            BottomNavBar(
                currentRoute = navController.currentBackStackEntry?.destination?.route ?: "search",
                onItemClick = { route -> navController.navigate(route) }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFFEDE7F6), Color(0xFFD1C4E9))
                        )
                    )
            ) {
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

                if(filteredBooks?.isNullOrEmpty() == false) {
                    if(currentQuery.isNotEmpty()) {
                        Text(
                            text = "${filteredBooks?.size} results for \"$currentQuery\"",
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
                if (filteredBooks?.isNotEmpty() == true) {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(paginatedBooks?.size ?: 0) { index ->
                            BookCard(paginatedBooks?.get(index), navController)
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(
                            rememberScrollState()
                        ),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if(totalPages != null) {
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
        }
    )

}
