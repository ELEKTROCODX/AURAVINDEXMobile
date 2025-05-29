package com.elektro24team.auravindex.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Diamond
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elektro24team.auravindex.R

@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    currentRoute: String,
    onItemClick: (String) -> Unit
) {
    val outlinedIcons = listOf(
        Icons.Outlined.Home,
        Icons.Outlined.Search,
/*
        Icons.Outlined.List,
*/
        Icons.Outlined.Diamond
    )

    val filledIcons = listOf(
        Icons.Filled.Home,
        Icons.Filled.Search,
/*
        Icons.Filled.List,
*/
        Icons.Filled.Diamond
    )

    val routes = listOf("main", "search", /*"lists",*/ "plans")
    val labels = listOf("Home", "Search", /*"Lists",*/ "Plans")

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = "Fondo de la barra de navegación inferior",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        NavigationBar(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            tonalElevation = 0.dp
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
                            tint = if (isSelected) Color(0xFFFFFFFF) else Color(0xFF989898),
                            modifier = Modifier.size(34.dp)
                        )
                    },
                    label = {
                        Text(
                            text = labels[index],
                            fontSize = 12.sp,
                            color = if (isSelected) Color(0xFFFFFFFF) else Color(0xFF989898)
                        )
                    },
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent,
                        selectedIconColor = Color(0xFFFFFFFF),
                        unselectedIconColor = Color(0xFFB0B0B0),
                        selectedTextColor = Color(0xFFFFFFFF),
                        unselectedTextColor = Color(0xFFB0B0B0)
                    )
                )
            }
        }
    }
}