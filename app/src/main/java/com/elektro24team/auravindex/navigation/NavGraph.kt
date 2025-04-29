package com.elektro24team.auravindex.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.elektro24team.auravindex.ui.components.ShowExternalLinkDialog
import com.elektro24team.auravindex.utils.openLink
import com.elektro24team.auravindex.view.*


// RUTAS
object Routes {
    const val MAIN = "main"
    const val SEARCH = "search"
    const val WRAPS = "wraps"
    const val PLANS = "plans"
    const val SETTINGS = "settings"
    const val PROFILE = "profile"
    const val NOTIFICATIONS = "notifications"
    const val TERMS = "terms"
    const val PRIVACY = "privacy"
    const val TEAM = "team"
    const val BOOK = "book/{bookId}"
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
        /*composable(Routes.WRAPS) {
            WrapsScreen(navController = navController)
        }*/
        composable(Routes.PLANS) {
            PlanScreen(navController = navController)
        }
        /*composable(Routes.SETTINGS) {
            SettingsScreen(navController = navController)
        }*/
        /*composable(Routes.PROFILE) {
            ProfileScreen(navController = navController)
        }*/
        /*composable(Routes.NOTIFICATIONS) {
            NotificationsScreen(navController = navController)
        }*/
        composable(
            Routes.BOOK,
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId")
            BookScreen(navController = navController, bookId = bookId ?: "")
        }

    }
}
