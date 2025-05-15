package com.elektro24team.auravindex.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.elektro24team.auravindex.ui.components.BottomNavBar
import com.elektro24team.auravindex.ui.components.DrawerMenu
import androidx.navigation.NavController
import com.elektro24team.auravindex.AuraVindexApp
import com.elektro24team.auravindex.ui.components.BookCollectionsSection
import com.elektro24team.auravindex.ui.components.ConnectionAlert
import com.elektro24team.auravindex.ui.components.ShowExternalLinkDialog
import com.elektro24team.auravindex.utils.hamburguerMenuNavigator
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import com.elektro24team.auravindex.R
import com.elektro24team.auravindex.ui.components.TopBar
import com.elektro24team.auravindex.ui.theme.BlackC
import com.elektro24team.auravindex.ui.theme.BrownC
import com.elektro24team.auravindex.ui.theme.OrangeC
import com.elektro24team.auravindex.ui.theme.PurpleC
import com.elektro24team.auravindex.ui.theme.WhiteC
import com.elektro24team.auravindex.utils.constants.URLs.IMG_url
import com.elektro24team.auravindex.viewmodels.BookCollectionViewModel
import com.elektro24team.auravindex.viewmodels.BookViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    bookViewModel: BookViewModel,
    bookCollectionViewModel: BookCollectionViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val showTermsDialog = remember { mutableStateOf(false) }
    val showPrivacyDialog = remember { mutableStateOf(false) }
    val showTeamDialog = remember { mutableStateOf(false) }
    val filteredBooks by bookViewModel.filteredBooks.observeAsState(emptyList())
    LaunchedEffect(Unit) {
        bookViewModel.loadBooks(showDuplicates = false, showLents = true)
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
                    currentRoute = navController.currentBackStackEntry?.destination?.route ?: "search",
                    onItemClick = { route -> navController.navigate(route) }
                )
            },
            content = { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp)
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

                        var bookQuery by remember { mutableStateOf("") }

                        // Barra de búsqueda
                        OutlinedTextField(
                            value = bookQuery,
                            onValueChange = {
                                bookQuery = it
                                bookViewModel.searchBook(it)
                                            },
                            label = { Text("Search a book...") },
                            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    if (bookQuery.isNotBlank()) {
                                        navController.navigate("search_results/${bookQuery}")
                                    }
                                }
                            )
                        )

                        // Filtrado de libros según el texto de búsqueda
                        if(bookQuery.isNotEmpty()){

                            //LIBROS BUSCADOS POR EL FILTRO DE 3 Y SEARCHBAR
                            LazyColumn {
                                items(filteredBooks.size) { index ->
                                    val book = filteredBooks[index]
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                            .clickable {
                                                navController.navigate("book/${book._id}")
                                            },
                                        colors = CardDefaults.cardColors(containerColor = WhiteC),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                        shape = RoundedCornerShape(16.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .padding(12.dp)
                                                .fillMaxWidth()
                                        ) {
                                            GlideImage(
                                                imageModel = { IMG_url.trimEnd('/') + "/" + book.book_img.trimStart('/') },
                                                modifier = Modifier
                                                    .height(130.dp)
                                                    .width(95.dp)
                                                    .clip(RoundedCornerShape(12.dp)),
                                                imageOptions = ImageOptions(
                                                    contentScale = ContentScale.Crop
                                                ),
                                                loading = {
                                                    CircularProgressIndicator(color = PurpleC, strokeWidth = 2.dp)
                                                },
                                                failure = {
                                                    Image(
                                                        painter = painterResource(id = R.mipmap.ic_launcher),
                                                        contentDescription = "Default img",
                                                        modifier = Modifier
                                                            .height(130.dp)
                                                            .width(95.dp)
                                                            .clip(RoundedCornerShape(12.dp))
                                                    )
                                                }
                                            )

                                            Spacer(modifier = Modifier.width(12.dp))

                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .align(Alignment.CenterVertically)
                                            ) {
                                                Text(
                                                    text = book.title,
                                                    style = MaterialTheme.typography.titleMedium,
                                                    color = PurpleC,
                                                    maxLines = 2
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = book.summary,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = BlackC,
                                                    maxLines = 3
                                                )
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Text(
                                                    text = "Authors: ${book.authors.joinToString { it.name + " " + it.last_name }}",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = BrownC
                                                )
                                                Text(
                                                    text = "Genres: ${book.genres.joinToString()}",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = OrangeC
                                                )
                                            }
                                        }
                                    }
                                }
                            }


                        }else{
                            BookCollectionsSection(navController, bookCollectionViewModel)
                        }
                    }
                }
            }
        )
    }
}
