package com.elektro24team.auravindex.ui.components

import android.content.res.Resources
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.elektro24team.auravindex.R
import com.elektro24team.auravindex.model.Book
import com.elektro24team.auravindex.ui.theme.OrangeC
import com.elektro24team.auravindex.ui.theme.PurpleC
import com.elektro24team.auravindex.utils.constants.URLs.IMG_url
import com.elektro24team.auravindex.viewmodels.BookViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun AdminBookCard(
    navController: NavController,
    bookViewModel: BookViewModel,
    bookId: String
) {
    val book = bookViewModel.book.observeAsState()
    val colors = androidx.compose.material3.MaterialTheme.colorScheme
    LaunchedEffect(Unit) {
        bookViewModel.loadBook(bookId)
    }
    Card(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        val imageUrl = IMG_url.trimEnd('/') + "/" + book?.value?.book_img?.trimStart('/')
        Text(
            text = book?.value?.title ?: "Title",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                color = androidx.compose.material3.MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            GlideImage(
                imageModel = { imageUrl },
                modifier = Modifier
                    .widthIn(max=200.dp)
                    .heightIn(max=300.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shadow(8.dp, RoundedCornerShape(16.dp))
                    .align(Alignment.Center), // Alineamos la imagen al centro
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop
                ),
                loading = {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                },
                failure = {
                    Image(
                        painter = painterResource(id = R.drawable.logo_app),
                        contentDescription = "Default img",
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .shadow(8.dp, RoundedCornerShape(16.dp))
                            .align(Alignment.Center)
                    )
                }
            )
        }


        Spacer(modifier = Modifier.height(20.dp))

        Column(modifier = Modifier.fillMaxWidth()) {

            // Título de la tabla
            Text(
                text = "Book Details",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF572365)),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Divider(color = Color.LightGray, thickness = 1.dp)
            // Fila para Summary
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Summary: ")
                        withStyle(SpanStyle(fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight.Normal)) {
                            append(book.value?.summary?: "Not available")
                        }
                    },
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF572365), textAlign = TextAlign.Justify)
                )
            }
            Divider(color = Color.LightGray, thickness = 1.dp)
            // Fila para Authors
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Authors: ",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF572365)),
                )
                Text(
                    text = book.value?.authors?.joinToString(", ") { it.name + " " + it.last_name } ?: "Not available",
                    style = TextStyle(fontSize = 16.sp, color = Color.Black)
                )
            }

            Divider(color = Color.LightGray, thickness = 1.dp)

            // Fila para Editorial
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Editorial: ",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF572365)),
                )
                Text(
                    text = book.value?.editorial?.name ?: "Not available",
                    style = TextStyle(fontSize = 16.sp, color = Color.Black)
                )
            }

            Divider(color = Color.LightGray, thickness = 1.dp)

            // Fila para Collection
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Collection: ",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF572365)),
                )
                Text(
                    text = book.value?.book_collection?.name ?: "Not available",
                    style = TextStyle(fontSize = 16.sp, color = Color.Black)
                )
            }

            Divider(color = Color.LightGray, thickness = 1.dp)

            // Fila para Genres
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Genres: ",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF572365)),
                )
                Text(
                    text = book.value?.genres?.joinToString(", ") ?: "Not available",
                    style = TextStyle(fontSize = 16.sp, color = Color.Black, textAlign = TextAlign.Justify)
                )
            }

            Divider(color = Color.LightGray, thickness = 1.dp)

            // Fila para Edition
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Edition: ",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF572365)),
                )
                Text(
                    text = book.value?.edition ?: "Not available",
                    style = TextStyle(fontSize = 16.sp, color = Color.Black)
                )
            }

            Divider(color = Color.LightGray, thickness = 1.dp)

            // Fila para Language
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Language: ",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF572365)),
                )
                Text(
                    text = book.value?.language ?: "Not available",
                    style = TextStyle(fontSize = 16.sp, color = Color.Black)
                )
            }

            Divider(color = Color.LightGray, thickness = 1.dp)

            // Fila para Status
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Status: ",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF572365)),
                )
                Text(
                    text = book.value?.book_status?.book_status?.lowercase()?.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } ?: "Not available",
                    style = TextStyle(fontSize = 16.sp, color = Color.Black)
                )
            }
            Divider(color = Color.LightGray, thickness = 1.dp)
            if(book.value?.book_status?.book_status?.lowercase() == "lent") {
                /* I could try adding more info, like lent status and all that */
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Lent to: ",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF572365)),
                    )
                    Text(
                        text = "Some User",
                        style = TextStyle(fontSize = 16.sp, color = Color.Black)
                    )
                }
                Divider(color = Color.LightGray, thickness = 1.dp)


            }
            // Fila para Classification
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Classification: ",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF572365)),
                )
                Text(
                    text = book.value?.classification ?: "Not available",
                    style = TextStyle(fontSize = 16.sp, color = Color.Black)
                )
            }

            Divider(color = Color.LightGray, thickness = 1.dp)

            // Fila para ISBN
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "ISBN: ",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF572365)),
                )
                Text(
                    text = book.value?.isbn ?: "Not available",
                    style = TextStyle(fontSize = 16.sp, color = Color.Black)
                )
            }

            Divider(color = Color.LightGray, thickness = 1.dp)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp), // Espaciado entre botones
            verticalAlignment = Alignment.CenterVertically // Alineación vertical
        ) {
            // Botón "Editar"
            Button(
                onClick = {  /* Acción para "Editar" */ },
                modifier = Modifier
                    .height(48.dp)
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(backgroundColor = PurpleC),
                shape = RoundedCornerShape(12.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit",
                    tint = Color.White,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Edit",
                    color = Color.White,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                )
            }

            Button(
                onClick = { /* Acción para "Cancel"*/  },
                modifier = Modifier
                    .height(48.dp)
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(backgroundColor = colors.error),
                shape = RoundedCornerShape(12.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = Color.White,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Delete",
                    color = Color.White,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                )
            }
        }
    }
}

