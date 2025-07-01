package com.elektrocodx.auravindex.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Button
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ButtonDefaults
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Divider
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.elektrocodx.auravindex.R
import com.elektrocodx.auravindex.model.Book
import com.elektrocodx.auravindex.ui.theme.MediumPadding
import com.elektrocodx.auravindex.ui.theme.OrangeC
import com.elektrocodx.auravindex.ui.theme.PurpleC
import com.elektrocodx.auravindex.ui.theme.WhiteC

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HomePageSection(
    title: String,
    books: List<Book>?,
    seeMoreAction: () -> Unit,
    navController: NavController
) {
    Column(
        modifier = Modifier
    ) {

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(start=5.dp, end=5.dp, top=15.dp)
            ,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = title,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PurpleC
                ),
                textAlign = TextAlign.Center
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
                Text(text = "+")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            books?.let { list ->
                itemsIndexed(list) { index, book ->
                    Card(
                        modifier = Modifier
                            .heightIn(max = 365.dp, min = 365.dp)
                            .widthIn(max = 275.dp, min = 275.dp)
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .clickable {
                                navController.navigate("book/${book._id}")
                            },
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {

                            Image(
                                painter = painterResource(id = R.drawable.bg),
                                contentDescription = "Background",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.White.copy(alpha = 0.1f))
                            )

                            // Contenido
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .heightIn(min = 200.dp, max = 200.dp)
                                        .widthIn(min = 150.dp, max = 150.dp)
                                ) {
                                    ClickableImage(book.book_img, book, navController)
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = book.title,
                                    color = WhiteC,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Column(
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = book.authors.joinToString(", ") { "${it.name} ${it.last_name}" },
                                        color = Color.LightGray,
                                        fontSize = 15.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                    )
                                    Spacer(modifier = Modifier.height(5.dp))

                                    FlowRow(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalArrangement = Arrangement.spacedBy(6.dp),
                                    ) {
                                        book.genres.forEach { genre ->
                                            Card(
                                                shape = RoundedCornerShape(10.dp),
                                                colors = CardDefaults.cardColors(containerColor = Color(0xFFEDE7F6)),
                                                elevation = CardDefaults.cardElevation(4.dp)
                                            ) {
                                                Text(
                                                    text = genre,
                                                    modifier = Modifier
                                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                                    color = Color(0xFF5E35B1),
                                                    fontSize = 9.sp,
                                                    textAlign = TextAlign.Center,
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(4.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }

        Spacer(modifier = Modifier.height(MediumPadding))
        Divider()
    }
}
