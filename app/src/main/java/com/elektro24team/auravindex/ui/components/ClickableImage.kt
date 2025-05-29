package com.elektro24team.auravindex.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elektro24team.auravindex.R
import com.elektro24team.auravindex.model.Book
import com.elektro24team.auravindex.utils.constants.URLs.IMG_url
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ClickableImage(url: String, book: Book, navController: NavController) {
    val fullURL = IMG_url + url

    GlideImage(
        imageModel = { fullURL },
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp))
            .clickable { navController.navigate("book/${book._id}") },
        imageOptions = ImageOptions(
            contentScale = ContentScale.Crop
        ),
        loading = {
            CircularProgressIndicator()
        },
        failure = {
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher),
                contentDescription = "Default img",
                modifier = Modifier
                    .width(200.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp))
                    .clickable {
                        navController.navigate("book/${book._id}")
                    },
                contentScale = ContentScale.Crop
            )
        }
    )
}
