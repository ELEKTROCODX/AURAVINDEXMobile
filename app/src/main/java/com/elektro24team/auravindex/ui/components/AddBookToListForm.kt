package com.elektro24team.auravindex.ui.components

//noinspection UsingMaterialAndMaterial3Libraries
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Button
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ButtonDefaults
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.elektro24team.auravindex.model.BookList
import com.elektro24team.auravindex.ui.theme.OrangeC
import com.elektro24team.auravindex.utils.enums.AppAction
import com.elektro24team.auravindex.utils.enums.SettingKey
import com.elektro24team.auravindex.utils.functions.isLoggedIn
import com.elektro24team.auravindex.utils.functions.mustBeLoggedInToast
import com.elektro24team.auravindex.viewmodels.BookListViewModel
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel

@Composable
fun AddBookToListForm(
    navController: NavController,
    bookLists: List<BookList>?,
    bookId: String,
    modifier: Modifier = Modifier,
    bookListViewModel: BookListViewModel,
    localSettingViewModel: LocalSettingViewModel,
    context: Context
) {
    var showCard by remember { mutableStateOf(false) }
    val localSettings = localSettingViewModel.settings.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        Button(
            onClick = {
                if(isLoggedIn(localSettings.value)) {
                    showCard = true
                } else {
                    mustBeLoggedInToast(context, AppAction.ADD_BOOK_TO_LIST, navController)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = OrangeC),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.List,
                contentDescription = "Add to book list",
                tint = Color.White,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = "Add to book list",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (showCard) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { showCard = false }
            )
            Card(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(20.dp)
                    .width(320.dp)
                    .heightIn(max = 200.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Select a List",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                        Text(
                            text = "✕",
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .clickable { showCard = false },
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (bookLists.isNullOrEmpty()) {
                        Text("You don't have any lists.")
                    } else {
                        val otherLists = bookLists.filter { it.title != "Favorites" }

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 300.dp)
                        ) {
                            bookLists.find { it.title == "Favorites" }?.let { favorites ->
                                item {
                                    ListSelectButton(
                                        list = favorites,
                                        bookId = bookId,
                                        token = localSettings.value[SettingKey.TOKEN.keySetting].toString(),
                                        context = context,
                                        bookListViewModel = bookListViewModel,
                                        showCard = { showCard = false }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }

                            items(otherLists.size) { index ->
                                val list = otherLists[index]
                                ListSelectButton(
                                    list = list,
                                    bookId = bookId,
                                    token = localSettings.value[SettingKey.TOKEN.keySetting].toString(),
                                    context = context,
                                    bookListViewModel = bookListViewModel,
                                    showCard = { showCard = false }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                        }
                    }
                }
            }


        }
    }
}

@Composable
fun ListSelectButton(
    list: BookList,
    bookId: String,
    token: String,
    context: Context,
    bookListViewModel: BookListViewModel,
    showCard: () -> Unit
) {
    val isBookInList = list.books.any { it._id == bookId }

    Button(
        onClick = {
            if (!isBookInList) {
                bookListViewModel.addBookToList(bookId, list._id, token)
                showCard()
            } else {
                Toast.makeText(context, "That book is already in the list.", Toast.LENGTH_SHORT).show()
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = OrangeC),
        shape = RoundedCornerShape(12.dp)
    ) {
        if (list.title == "Favorites") {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Favorites",
                tint = Color.White,
                modifier = Modifier.padding(end = 8.dp)
            )
        } else {
            Icon(
                imageVector = Icons.Default.LibraryAdd,
                contentDescription = "Add",
                tint = Color.White,
                modifier = Modifier.padding(end = 8.dp)
            )
        }

        Text(
            text = list.title,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
