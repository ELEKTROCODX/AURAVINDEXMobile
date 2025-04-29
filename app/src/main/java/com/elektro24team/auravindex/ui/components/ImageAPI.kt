package com.elektro24team.auravindex.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.elektro24team.auravindex.utils.Constants.IMG_url
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun getImageAPI(url: String){
    val fullURL = IMG_url+url
    GlideImage(
        imageModel = {fullURL},
        modifier = Modifier.fillMaxWidth().height(200.dp),
        imageOptions = ImageOptions(
            contentScale = ContentScale.Fit
        ),
        loading = {
            CircularProgressIndicator()
        },
        failure = {
            Text("Erro")
        }
    )
}