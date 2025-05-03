package com.elektro24team.auravindex.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            items(bookCollections.value.size){
                index ->
                    if(index % 2 ==0){
                        Button(
                            onClick = {}
                        ) {
                            Text(bookCollections.value[index].name)
                        }
                    }
            }
        }
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            items(bookCollections.value.size){
                index ->
                if(index % 2 != 0){
                    Button(
                        onClick = {}
                    ) {
                        Text(bookCollections.value[index].name)
                    }
                }
            }
        }
    }

}