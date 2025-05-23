package com.elektro24team.auravindex.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elektro24team.auravindex.model.Book
import com.elektro24team.auravindex.utils.bookStatusIcons
import com.elektro24team.auravindex.utils.functions.TableCell
import com.elektro24team.auravindex.utils.functions.TableHeaderCell

@Composable
fun AdminBookTable(
    navController: NavController,
    books: List<Book>
) {
    var rowsPerPage by remember { mutableStateOf(9) }
    var currentPage by remember { mutableStateOf(0) }
    var totalPages = (books.size + rowsPerPage - 1) / rowsPerPage
    val currentPageBooks = books.drop(currentPage * rowsPerPage).take(rowsPerPage)
    Text(
        text = "Books",
        style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(start = 4.dp, top = 16.dp, end = 0.dp, bottom = 16.dp)
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
        ) {
            Column(
            ) {
                Row(modifier = Modifier.padding(vertical = 8.dp)) {
                    TableHeaderCell("Title", 220.dp)
                    TableHeaderCell("Classification", 120.dp)
                    TableHeaderCell("Status", 60.dp)
                    TableHeaderCell("ISBN", 140.dp)
                }
                Divider()
                currentPageBooks.forEach { book ->
                    Row(
                        modifier = Modifier
                            .clickable { navController.navigate("admin_dashboard/book/${book._id}") }
                            .padding(vertical = 6.dp)
                    ) {
                        TableCell(book.title, 220.dp)
                        TableCell(book.classification, 120.dp)
                        Spacer(modifier = Modifier.width(15.dp))
                        Icon(
                            imageVector = bookStatusIcons.find { it.book_status == book.book_status.book_status }?.icon ?: Icons.Default.CheckCircle,
                            contentDescription = book.book_status.book_status,
                            tint = MaterialTheme.colors.secondary,
                            modifier = Modifier.size(30.dp),
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        TableCell(book.isbn, 140.dp)
                    }
                    Divider()
                }
            }
        }
        // Pagination controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { if (currentPage > 0) currentPage-- },
                enabled = currentPage > 0
            ) {
                Text("<")
            }
            Text(
                text = "${currentPage + 1} of $totalPages",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterVertically)
            )
            Button(
                onClick = { if (currentPage < totalPages - 1) currentPage++ },
                enabled = currentPage < totalPages - 1
            ) {
                Text(">")
            }
        }

    }
}