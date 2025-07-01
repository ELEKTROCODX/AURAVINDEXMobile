package com.elektro24team.auravindex.view


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.elektro24team.auravindex.R
import com.elektro24team.auravindex.ui.components.BottomNavBar
import com.elektro24team.auravindex.ui.components.cards.BookCard
import com.elektro24team.auravindex.viewmodels.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultsScreen(
    navController: NavController,
    bookViewModel: BookViewModel,
    query: String
) {
    var currentQuery by remember { mutableStateOf(query) }
    var currentPage by remember { mutableIntStateOf(0) }
    val filteredBooks by bookViewModel.filteredBooks.collectAsState()
    LaunchedEffect(Unit) {
        bookViewModel.loadBooks(showDuplicates = false, showLents = true)
        bookViewModel.searchBook(query)
    }
    val itemsPerPage by remember { mutableIntStateOf(8) }
    val paginatedBooks = filteredBooks?.drop(currentPage * itemsPerPage)?.take(itemsPerPage)
    val totalPages = (filteredBooks?.size?.plus(itemsPerPage)?.minus(1))?.div(itemsPerPage)
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp, max = 100.dp)
                    .zIndex(100f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bg),
                    contentDescription = "Background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                TopAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding(),
                    title = {
                        Text(
                            text = "Results",
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                )
            }
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
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = { if (currentPage > 0) currentPage-- },
                                    enabled = currentPage > 0
                                ) {
                                    Text("Prev")
                                }

                                Text(
                                    text = "Page ${currentPage + 1} of $totalPages",
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )

                                Button(
                                    onClick = { if (currentPage < totalPages - 1) currentPage++ },
                                    enabled = currentPage < totalPages - 1
                                ) {
                                    Text("Next")
                                }
                            }
                        }
                    }
                }
            }
        }
    )

}
