package com.elektro24team.auravindex.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elektro24team.auravindex.model.BookList
import com.elektro24team.auravindex.viewmodels.BookListViewModel

@Composable
fun BookListCardList(
    bookLists: List<BookList>,
    navController: NavController,
    bookListViewModel: BookListViewModel,
    token: String,
    userId: String
) {
    val favorites = bookLists.find { it.title == "Favorites" }
    val others = bookLists.filter { it.title != "Favorites" }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        favorites?.let {
            item {
                Text(
                    text = "Favorites",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 20.dp, top = 12.dp)
                )
                BookListCard(
                    bookList = it,
                    navController = navController,
                    bookListViewModel = bookListViewModel,
                    token = token,
                    userId = userId
                )
            }
        }

        if (others.isNotEmpty()) {
            item {
                Divider(modifier = Modifier.height(2.dp))
                Text(
                    text = "My Lists",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 20.dp, top = 20.dp)
                )

            }
            items(others) { list ->
                BookListCard(
                    bookList = list,
                    navController = navController,
                    bookListViewModel = bookListViewModel,
                    token = token,
                    userId = userId
                )
            }
        }
    }
}
