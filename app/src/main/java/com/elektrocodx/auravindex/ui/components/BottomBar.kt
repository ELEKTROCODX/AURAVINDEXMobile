package com.elektrocodx.auravindex.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Diamond
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.elektrocodx.auravindex.R

data class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun BottomNavBar(
    currentRoute: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val navItems = listOf(
        BottomNavItem("main", "Home", Icons.Filled.Home, Icons.Outlined.Home),
        BottomNavItem("search", "Search", Icons.Filled.Search, Icons.Outlined.Search),
        BottomNavItem("lists", "Lists", Icons.AutoMirrored.Filled.List, Icons.AutoMirrored.Outlined.List),
        BottomNavItem("plans", "Plans", Icons.Filled.Diamond, Icons.Outlined.Diamond)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .zIndex(100f)
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = "Navigation background",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        )

        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            containerColor = Color.Transparent,
            tonalElevation = 0.dp
        ) {
            navItems.forEach { item ->
                val isSelected = currentRoute == item.route
                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onItemClick(item.route) },
                    icon = {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.label,
                            modifier = Modifier.size(28.dp),
                            tint = if (isSelected) Color.White else Color(0xFFB0B0B0)
                        )
                    },
                    label = {
                        Text(
                            text = item.label,
                            fontSize = 12.sp,
                            color = if (isSelected) Color.White else Color(0xFFB0B0B0)
                        )
                    },
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent,
                        selectedIconColor = Color.White,
                        unselectedIconColor = Color(0xFFB0B0B0),
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color(0xFFB0B0B0)
                    )
                )
            }
        }
    }
}
