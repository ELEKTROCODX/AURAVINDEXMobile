package com.elektro24team.auravindex.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elektro24team.auravindex.ui.theme.PurpleC
import com.elektro24team.auravindex.viewmodels.BookCollectionViewModel

@Composable
fun BookCollectionsSection(
    navController: NavController,
    bookCollectionViewModel: BookCollectionViewModel
) {
    val bookCollections by bookCollectionViewModel.bookCollections.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        bookCollectionViewModel.loadBookCollections()
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(bookCollections.size) { index ->
                var name = bookCollections[index].name
                var id = bookCollections[index]._id
                CollectionCard(
                    name = name,
                    id = id,
                    onClick = {
                        navController.navigate("collection_books/${name}/${id}")
                    }
                )
            }
        }
    }
}

@Composable
fun CollectionCard(name: String, id: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = PurpleC),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Icon(
                imageVector = Icons.Default.CollectionsBookmark,
                contentDescription = "Collection Icon",
                modifier = Modifier
                    .size(60.dp)
                    .alpha(0.15f), // Transparente
                tint = Color.White
            )
        }
    }
}
