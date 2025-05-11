package com.elektro24team.auravindex.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.elektro24team.auravindex.utils.rememberBookViewModel
import com.elektro24team.auravindex.view.*
import com.elektro24team.auravindex.viewmodels.BookViewModel


// RUTAS
object Routes {
    const val BOOK = "book/{bookId}"
    const val COLLECTION_BOOKS = "collection_books/{collectionName}/{collectionId}"
    const val LOGIN = "login"
    const val MAIN = "main"
    const val NOTIFICATIONS = "notifications"
    const val PLANS = "plans"
    const val PRIVACY = "privacy"
    const val PROFILE = "profile"
    const val SEARCH = "search"
    const val SEARCH_RESULTS = "search_results/{filter}/{query}"
    const val SETTINGS = "settings"
    const val TEAM = "team"
    const val TERMS = "terms"
    const val WELCOME = "welcome"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(startDestination: String = Routes.WELCOME) {
    val navController = rememberNavController()
    val bookViewModel: BookViewModel = rememberBookViewModel()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(
            Routes.BOOK,
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId")
            BookScreen(
                navController = navController,
                bookId = bookId ?: "",
                bookViewModel = bookViewModel
            )
        }
        composable(
            Routes.COLLECTION_BOOKS,
            arguments = listOf(
                navArgument("collectionName"){type = NavType.StringType},
                navArgument("collectionId"){type = NavType.StringType}
            )){
                backStackEntry ->
            val collectionName = backStackEntry.arguments?.getString("collectionName") ?: ""
            val collectionId = backStackEntry.arguments?.getString("collectionId") ?: ""
            BooksCollectionScreen(navController, bookCollectionName = collectionName, collectionId = collectionId)
        }
        composable(Routes.LOGIN) {
            LoginScreen(navController = navController)
        }
        composable(Routes.MAIN) {
            MainScreen(navController = navController)
        }
        /*composable(Routes.NOTIFICATIONS) {
            NotificationsScreen(navController = navController)
        }*/
        composable(Routes.PLANS) {
            PlanScreen(navController = navController)
        }
        composable(Routes.PROFILE) {
            ProfileScreen(navController = navController)
        }
        composable(Routes.SEARCH) {
            SearchScreen(navController = navController)
        }
        composable(
            Routes.SEARCH_RESULTS,
            arguments = listOf(
                navArgument("filter") { type = NavType.StringType },
                navArgument("query") { type = NavType.StringType })
        ) { backStackEntry ->
            val filter = backStackEntry.arguments?.getString("filter") ?: ""
            val query = backStackEntry.arguments?.getString("query") ?: ""
            SearchResultsScreen(navController, filter = filter, query = query)
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(navController = navController)
        }
        composable(Routes.WELCOME) {
            WelcomeScreen(
                navController = navController,
                bookViewModel = bookViewModel
            )
        }

    }
}
