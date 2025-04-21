package com.elektro24team.auravindex.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.elektro24team.auravindex.view.*


// RUTAS
object Routes {
    const val MAIN = "main"
    const val SEARCH = "search"
    const val WRAPS = "wraps"
    const val PLAN = "plan"
}

@Composable
fun NavGraph(startDestination: String = Routes.MAIN) {
    val navController = rememberNavController()

    //aun me falta añadir la antigua tap to continue
    NavHost(navController = navController, startDestination = startDestination) { // aqui tambien defini que esta sea la principal
        composable(Routes.MAIN) {
            MainScreen(navController = navController)
        }
        composable(Routes.SEARCH) {
            SearchScreen(navController = navController)
        }
        composable(Routes.WRAPS) {
            WrapsScreen(navController = navController)
        }
        composable(Routes.PLAN) {
            PlanScreen(navController = navController)
        }

    }
}
