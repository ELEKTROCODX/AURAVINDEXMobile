package com.elektro24team.auravindex.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.elektro24team.auravindex.ui.components.BookCard
import com.elektro24team.auravindex.ui.components.BottomNavBar
import com.elektro24team.auravindex.ui.theme.*
import com.elektro24team.auravindex.viewmodels.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooksCollectionScreen(
    navController: NavController,
    bookCollectionName: String,
    collectionId: String
) {
    val bookViewModel: BookViewModel = viewModel()
    bookViewModel.fetchFilteredBooks(
        showDuplicates = false,
        showLents = true,
        filter = "book_collection",
        value = collectionId
    )
    val books = bookViewModel.filteredBooks.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Colección: $bookCollectionName",
                        color = WhiteC,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = WhiteC)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PurpleC,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
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
                    .background(WhiteC)
            ) {
                Text(
                    text = "${books.size} resultados encontrados en \"$bookCollectionName\"",
                    style = MaterialTheme.typography.titleMedium,
                    color = PurpleC,
                    modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(books) { book ->
                        BookCard(book = book, navController = navController)
                    }
                }
            }
        }
    )
}
