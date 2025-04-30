package com.elektro24team.auravindex.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.elektro24team.auravindex.model.Book
import com.elektro24team.auravindex.ui.theme.SmallPadding


//lista de obejtos tipo libro
@Composable
fun BookList(books: List<Book>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(SmallPadding)
    ) {
        items(books.size) { index ->
            val book = books[index]
            Card(
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.width(120.dp)) {
                    Spacer(modifier = Modifier.height(SmallPadding))
                    Text(text = book.title)
                    Text(text = book.authors.size.toString())
                }
            }
        }
    }
}
