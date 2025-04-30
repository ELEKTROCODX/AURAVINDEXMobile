package com.elektro24team.auravindex.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.elektro24team.auravindex.model.Book

@Composable
fun HomePageSection(title: String, books: MutableState<List<Book>>, seeMoreAction: () -> Unit, navController: NavController) {
    Column(
        modifier = Modifier.padding(8.dp).fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Start,
            )
            Button(
                onClick = seeMoreAction,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = "See more"
                )
            }
        }
        LazyRow(
            modifier = Modifier
                .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
        ) {
            items(books.value.size) { index ->
                Column(
                    modifier = Modifier
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    /*AsyncImage(
                        model = books[index].book_img,
                        contentDescription = books[index].title,
                        modifier = Modifier
                            .size(150.dp)
                            .padding(16.dp)
                            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                            .clickable{ navController.navigate("/book/${books[index].id}") },
                        error = painterResource(id = R.drawable.logo_app),
                        placeholder = painterResource(id = R.drawable.logo_app),
                    )*/
                    ClickableImage(books.value[index].book_img, books.value[index], navController)
                    Text(
                        text = books.value[index].title,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 8.dp)
                            .clickable{ navController.navigate("book/${books.value[index]._id}") },
                        style = TextStyle(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
