package com.elektro24team.auravindex.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Diamond
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    currentRoute: String, // Ruta actual para
    onItemClick: (String) -> Unit // se pasan las rutas
) {
    // Lista de íconos para el menú inferior
    //con ayuda de ChatGPT para conseguir los adecuados
    val items = listOf(
        Icons.Outlined.Home,
        Icons.Outlined.Search,
        /*Icons.Outlined.Book,*/
        Icons.Outlined.Diamond
    )

    // Rutas correspondientes
    val routes = listOf("main", "search", /*"wraps", */"plans")

    // Etiquetas bajo de los íconos
    val labels = listOf("home", "search", /*"wraps", */"plans")

    // Componente de barra del menu
    NavigationBar(modifier = modifier) {
        // Por cada ítem de la barra  . . . .
        items.forEachIndexed { index, icon ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = labels[index],
                        modifier = Modifier.size(30.dp) // Tamaño del ícono (ver en otros modelos)
                    )
                },
                label = { Text(labels[index]) }, // Texto debajo del ícono
                selected = currentRoute == routes[index], // Marca si está activo
                onClick = { onItemClick(routes[index]) }
            )
        }
    }
}
