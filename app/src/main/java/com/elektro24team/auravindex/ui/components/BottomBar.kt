package com.elektro24team.auravindex.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Diamond
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elektro24team.auravindex.ui.theme.PurpleC

@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    currentRoute: String,
    onItemClick: (String) -> Unit
) {
    val outlinedIcons = listOf(
        Icons.Outlined.Home,
        Icons.Outlined.Search,
        Icons.Outlined.Diamond
    )

    val filledIcons = listOf(
        Icons.Filled.Home,
        Icons.Filled.Search,
       Icons.Filled.Diamond
    )

    val routes = listOf("main", "search", "plans")
    val labels = listOf("Home", "Search", "Plans")

    NavigationBar(
        modifier = modifier,
        containerColor = Color.White.copy(alpha = 0.85f) // Blanco semitransparente
    ) {
        routes.forEachIndexed { index, route ->
            val isSelected = currentRoute == route

            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemClick(route) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) filledIcons[index] else outlinedIcons[index],
                        contentDescription = labels[index],
                        modifier = Modifier.size(35.dp),
                        tint = if (isSelected) PurpleC else Color.Gray
                    )
                },
                        label = {
                    Text(
                        text = labels[index],
                        fontSize = 12.sp,
                        color = if (isSelected) PurpleC else Color.Gray
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
