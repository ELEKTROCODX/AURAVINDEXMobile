package com.elektro24team.auravindex.ui.components

import android.net.Uri
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.elektro24team.auravindex.view.viewmodels.BookCollectionViewModel

@Composable
fun BookCollectionsSection(
    navController: NavController,
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
                    var collectionName = bookCollections.value[index].name
                    var collectionId = bookCollections.value[index]._id
                    if(index % 2 ==0){
                        Button(
                            onClick = {
                                navController.navigate("collection_books/${collectionName}/${collectionId}")
                            }
                        ) {
                            Text(collectionName)
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
                var collectionName = bookCollections.value[index].name
                var collectionId = bookCollections.value[index]._id
                if(index % 2 != 0){
                    Button(
                        onClick = {
                            navController.navigate("collection_books/${collectionName}/${collectionId}")
                        }
                    ) {
                        Text(bookCollections.value[index].name)
                    }
                }
            }
        }
    }

}