package com.elektro24team.auravindex.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elektro24team.auravindex.navigation.Routes
import androidx.compose.foundation.clickable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.elektro24team.auravindex.R
import com.elektro24team.auravindex.model.Book
import com.elektro24team.auravindex.utils.Constants.IMG_url
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage


@Composable
fun BookCard(book: Book, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navController.navigate("${Routes.BOOK}/${book._id}")
            }
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            GlideImage(
                imageModel = { IMG_url.trimEnd('/') + "/" + book.book_img.trimStart('/') },
                modifier = Modifier
                    .height(125.dp)
                    .padding(end = 4.dp),
                imageOptions = ImageOptions(contentScale = ContentScale.Fit),
                loading = { CircularProgressIndicator() },
                failure = {
                    Image(
                        painter = painterResource(id = R.mipmap.ic_launcher),
                        contentDescription = "Default img",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    )
                }
            )
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = book.title, style = MaterialTheme.typography.titleMedium)
                Text(text = book.summary, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Autor(es): ${book.authors.joinToString { it.name }}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Géneros: ${book.genres.joinToString()}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
