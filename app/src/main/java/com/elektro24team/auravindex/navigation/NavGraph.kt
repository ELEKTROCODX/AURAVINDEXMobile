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
    const val SETTINGS = "settings"
    const val PROFILE = "profile"
    const val NOTIFICATIONS = "notifications"
    const val TERMS = "terms"
    const val PRIVACY = "privacy"
    const val TEAM = "team"
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
        /*composable(Routes.SETTINGS) {
            SettingsScreen(navController = navController)
        }*/
        /*composable(Routes.PROFILE) {
            ProfileScreen(navController = navController)
        }*/
        /*composable(Routes.NOTIFICATIONS) {
            NotificationsScreen(navController = navController)
        }*/
        /*composable(Routes.TERMS) {
            TermsScreen(navController = navController)
        }*/
        /*composable(Routes.PRIVACY) {
            PrivacyScreen(navController = navController)
        }*/
        /*composable(Routes.TEAM) {
            TeamScreen(navController = navController)
        }*/

    }
}
