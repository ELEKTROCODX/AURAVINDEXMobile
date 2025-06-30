package com.elektro24team.auravindex.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.elektro24team.auravindex.R
import com.elektro24team.auravindex.ui.components.cards.BookCard
import com.elektro24team.auravindex.ui.components.BottomNavBar
import com.elektro24team.auravindex.ui.theme.*
import com.elektro24team.auravindex.viewmodels.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooksCollectionScreen(
    navController: NavController,
    bookViewModel: BookViewModel,
    bookCollectionName: String,
    collectionId: String,
) {
    val books by bookViewModel.filteredBooks.collectAsState()

    LaunchedEffect(collectionId) {
        bookViewModel.loadBooksAndFilter(showDuplicates = false, showLents = true, filterField = "book_collection", filterValue = collectionId)
    }
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
                            "Collection: $bookCollectionName",
                            color = WhiteC,
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = WhiteC)
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
                    .background(WhiteC)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFFEDE7F6), Color(0xFFD1C4E9))
                        )
                    )
            ) {
                Text(
                    text = "${books?.size} results for \"$bookCollectionName\"",
                    style = MaterialTheme.typography.titleMedium,
                    color = PurpleC,
                    modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(books?.size ?: 0) { index ->
                        BookCard(book = books?.get(index), navController = navController)
                    }
                }
            }
        }
    )
}
