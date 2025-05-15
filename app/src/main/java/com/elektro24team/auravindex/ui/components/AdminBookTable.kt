package com.elektro24team.auravindex.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elektro24team.auravindex.model.Book

@Composable
fun AdminBookTable(
    navController: NavController,
    books: List<Book>
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Books",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        // Table Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Title", modifier = Modifier.weight(3f), style = TextStyle(fontWeight = FontWeight.Bold))
            Text("Classification", modifier = Modifier.weight(2f), style = TextStyle(fontWeight = FontWeight.Bold))
            Text("Status", modifier = Modifier.weight(2f), style = TextStyle(fontWeight = FontWeight.Bold))
        }

        Divider()

        // Table Rows
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxHeight()
        ) {
            items(books.size) { index ->
                val book = books[index]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { navController.navigate("admin_dashboard/book/${book._id}") },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(book.title, modifier = Modifier.weight(3f))
                    Text(book.classification, modifier = Modifier.weight(2f))
                    Text(book.book_status.book_status, modifier = Modifier.weight(2f))
                }
                Divider()
            }
        }
    }
}