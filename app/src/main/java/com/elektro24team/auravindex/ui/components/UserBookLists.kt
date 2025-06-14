package com.elektro24team.auravindex.ui.components

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.elektro24team.auravindex.model.BookList
import androidx.compose.ui.Alignment
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.elektro24team.auravindex.viewmodels.BookListViewModel

@Composable
fun UserBookLists(bookLists: List<BookList>?, bookId: String, modifier: Modifier = Modifier,bookListViewModel: BookListViewModel, token: String, context: Context){
    var showCard by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Button(onClick = { showCard = true }) {
            Text("Add to List")
        }

        if (showCard) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black.copy(alpha = 0.5f))
                    .clickable { showCard = false }
            )

            Card(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
                    .width(300.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Select a List", style = MaterialTheme.typography.h6)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (bookLists.isNullOrEmpty()) {
                        Text("No lists available")
                    } else {
                        bookLists.forEach { list ->
                            Button(
                                onClick = {
                                    bookListViewModel.add(bookId,list._id,token,context)
                                    showCard = false
                                },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            ) {
                                Text(list.title)
                            }
                        }
                    }
                }
            }
        }
    }
}