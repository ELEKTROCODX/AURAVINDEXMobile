package com.elektro24team.auravindex.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.elektro24team.auravindex.ui.theme.MediumPadding
import com.elektro24team.auravindex.ui.theme.OrangeC
import com.elektro24team.auravindex.ui.theme.PurpleC

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePageSection(
    title: String,
    books: List<Book>,
    seeMoreAction: () -> Unit,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                textAlign = TextAlign.Start
            )

            Button(
                onClick = seeMoreAction,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = OrangeC,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(text = "More")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .heightIn(max=240.dp, min=240.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(books.size) { index ->
                Row(
                    modifier = Modifier.heightIn(min = 250.dp, max = 250.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .width(175.dp)
                            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                            .heightIn(min = 250.dp, max = 250.dp )
                            .padding(8.dp)
                            .clickable { navController.navigate("book/${books[index]._id}") },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val book = books[index]

                        // Imagen clickeable
                        ClickableImage(book.book_img, book, navController)

                        // Título
                        Text(
                            text = book.title,
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .width(120.dp),
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            ),
                            textAlign = TextAlign.Center,
                            maxLines = 2
                        )

                        Text(
                            text = book.authors.joinToString(", ") { it.name +" "+ it.last_name},
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .width(120.dp),
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Light,
                                color = Color.DarkGray
                            ),
                            textAlign = TextAlign.Center,
                            maxLines = 2
                        )
                    }

                    // Separador visual entre tarjetas
                    if (index < books.lastIndex) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(1.dp)
                                .padding(horizontal = 4.dp)
                                .background(PurpleC.copy(alpha = 0.5f))
                        )
                    }
                }

            }
        }
        Spacer(modifier = Modifier.height(MediumPadding))
        Divider()
    }
}
