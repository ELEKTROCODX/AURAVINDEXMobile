package com.elektrocodx.auravindex.utils.functions

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TableHeaderCell(text: String, width: Dp) {
    Text(
        text = text,
        modifier = Modifier
            .width(width)
            .padding(8.dp),
        style = TextStyle(fontWeight = FontWeight.Bold)
    )
}

@Composable
fun TableCell(text: String, width: Dp) {
    Text(
        text = text,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .width(width)
            .padding(8.dp)
    )
}