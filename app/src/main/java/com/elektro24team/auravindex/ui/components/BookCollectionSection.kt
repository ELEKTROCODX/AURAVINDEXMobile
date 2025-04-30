package com.elektro24team.auravindex.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elektro24team.auravindex.view.viewmodels.BookCollectionViewModel

@Composable
fun BookCollectionsSection(
    viewModel: BookCollectionViewModel = viewModel()
) {
    val bookCollections = viewModel.posts
    /*
    * Create two lazy columns with half max width
    * First lazy column: Show even book collections
    * Second lazy column: Show odd book collections
    * */
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(bookCollections.value.size) { index ->
            Button(
                modifier = Modifier
                    .padding(4.dp),
                onClick = {/* Open search by book collection */}
            ) {
                Text(
                    text = bookCollections.value[index].name
                )
            }
        }
        /*items(bookCollections.value.size) { index ->
            if((index % 2) == 0) {
                Button(
                    onClick = { *//*TODO*//* }
                ) {
                    Text(text = bookCollections.value[index].name)
                }
                Button(
                    onClick = { *//*TODO*//* }
                ) {
                    Text(text = bookCollections.value[index+1].name)
                }
        }*/
    }
}