package com.elektro24team.auravindex.ui.components.tables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elektro24team.auravindex.model.Book
import com.elektro24team.auravindex.utils.classes.bookStatusIcons
import com.elektro24team.auravindex.utils.functions.TableCell
import com.elektro24team.auravindex.utils.functions.TableHeaderCell

@Composable
fun AdminBookTable(
    navController: NavController,
    books: List<Book>
) {
    var rowsPerPage by remember { mutableStateOf(9) }
    var currentPage by remember { mutableStateOf(0) }
    val totalPages = (books.size + rowsPerPage - 1) / rowsPerPage
    val currentPageBooks = books.drop(currentPage * rowsPerPage).take(rowsPerPage)

    Column(modifier = Modifier.fillMaxSize().padding(0.dp)) {
        Text(
            text = "Books",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
            Column {
                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                        .padding(vertical = 8.dp)
                ) {
                    TableHeaderCell("Title", 220.dp)
                    TableHeaderCell("Classification", 120.dp)
                    TableHeaderCell("Status", 80.dp)
                    TableHeaderCell("ISBN", 140.dp)
                }

                Divider()

                currentPageBooks.forEachIndexed { index, book ->
                    val backgroundColor = if (index % 2 == 0)
                        MaterialTheme.colorScheme.surface
                    else
                        MaterialTheme.colorScheme.surfaceVariant

                    Row(
                        modifier = Modifier
                            .background(backgroundColor)
                            .clickable { navController.navigate("book/${book._id}") }
                            .padding(vertical = 6.dp)
                    ) {
                        TableCell(book.title, 220.dp)
                        TableCell(book.classification, 120.dp)

                        Row(
                            modifier = Modifier.width(80.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = bookStatusIcons.find { it.book_status == book.book_status.book_status }?.icon
                                    ?: Icons.Default.CheckCircle,
                                contentDescription = book.book_status.book_status,
                                tint = Color(0xFF9C27B0),
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        TableCell(book.isbn, 140.dp)
                    }

                    Divider(thickness = 0.5.dp)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
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
