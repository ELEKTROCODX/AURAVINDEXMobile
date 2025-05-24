package com.elektro24team.auravindex.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
fun ClickableImage(url: String, book: Book, navController: NavController){
    val fullURL = IMG_url+url
    GlideImage(
        imageModel = {fullURL},
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min=150.dp, max = 150.dp)
            .clip(RoundedCornerShape(25.dp))
            .clickable{ navController.navigate("book/${book._id}") },
        imageOptions = ImageOptions(
            contentScale = ContentScale.Fit
        ),
        loading = {
            CircularProgressIndicator()
        },
        failure = {
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher),
                contentDescription = "Default img",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .clickable {
                        navController.navigate("book/${book._id}")
                    }
            )
        }
    )
}