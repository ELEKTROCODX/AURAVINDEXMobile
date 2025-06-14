package com.elektro24team.auravindex.ui.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elektro24team.auravindex.R
import com.elektro24team.auravindex.model.BookList
import com.elektro24team.auravindex.ui.theme.BlackC
import com.elektro24team.auravindex.ui.theme.BrownC
import com.elektro24team.auravindex.ui.theme.OrangeC
import com.elektro24team.auravindex.ui.theme.PurpleC
import com.elektro24team.auravindex.utils.constants.URLs.IMG_url
import com.elektro24team.auravindex.viewmodels.BookListViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage


@Composable
fun BookListCard(bookList: BookList?, navController: NavController, bookListViewModel: BookListViewModel, context: Context, token : String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { navController.navigate("bookList/${bookList?._id}") },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = bookList?.title.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = BlackC,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = bookList?.description.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = BrownC,
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Books: ${bookList?.books?.joinToString(",") { it.title }}",
                    style = MaterialTheme.typography.bodySmall,
                    color = OrangeC
                )
            }

            Button(
                onClick = {
                    if (bookList != null) {
                        bookListViewModel.deleteList(bookList._id,context, token)
                    }
                }
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}
