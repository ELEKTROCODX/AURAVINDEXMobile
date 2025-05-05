package com.elektro24team.auravindex.view


import android.annotation.SuppressLint
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.DefaultTab.PhotosTab.value
import androidx.compose.foundation.Image
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
import com.elektro24team.auravindex.ui.components.BookCard
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.elektro24team.auravindex.R
import com.elektro24team.auravindex.utils.Constants.IMG_url
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultsScreen(
    navController: NavController,
    filter: String,
    query: String,
    bookViewModel: BookViewModel = viewModel()
) {
    val context = LocalContext.current
    val allBooks = bookViewModel.filteredBooks.value
    var currentQuery by remember { mutableStateOf(query) }
    var currentPage by remember { mutableStateOf(1) }
    val booksPerPage = 5
    val filteredBooks = remember(currentQuery, filter, allBooks) {
        bookViewModel.filterBooksLocally(allBooks, filter, currentQuery)
    }
    val pagedBooks = filteredBooks.chunked(booksPerPage)
    val totalPages = pagedBooks.size
    val currentBooks = pagedBooks.getOrNull(currentPage - 1) ?: emptyList()

    LaunchedEffect(key1 = filter, key2 = query) {
        bookViewModel.applyLocalFilter(filter, query)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resultados de búsqueda") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->

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
                },
                label = { Text("Buscar dentro de resultados") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), singleLine = true
            )

            // Recomendaciones según el filtro
            Text(
                text = "Recomendaciones relacionadas a \"$query\"",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )

            val recommendations = bookViewModel.getRecommendations(filter, currentQuery)

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                items(recommendations) { book ->
                    BookCard(book, navController)
                }
            }

            Divider(modifier = Modifier.padding(horizontal = 8.dp))

            // Resultados filtrados
            if (filteredBooks.isEmpty()) {
                Text("No se encontraron resultados.")
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(filteredBooks) { book ->
                        BookCard(book, navController)
                    }
                }
            }


            // Paginación
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(totalPages) { pageIndex ->
                    Button(
                        onClick = { currentPage = pageIndex + 1 },
                        modifier = Modifier.padding(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (pageIndex + 1 == currentPage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text(text = "${pageIndex + 1}")
                    }
                }
            }
        }
    }
}
