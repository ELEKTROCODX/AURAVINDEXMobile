package com.elektro24team.auravindex.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.elektro24team.auravindex.utils.rememberBookCollectionViewModel
import com.elektro24team.auravindex.utils.rememberBookViewModel
import com.elektro24team.auravindex.utils.rememberLocalSettingViewModel
import com.elektro24team.auravindex.utils.rememberPlanViewModel
import com.elektro24team.auravindex.view.*
import com.elektro24team.auravindex.viewmodels.BookCollectionViewModel
import com.elektro24team.auravindex.viewmodels.BookViewModel
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel
import com.elektro24team.auravindex.viewmodels.PlanViewModel
import com.elektro24team.auravindex.viewmodels.UserViewModel


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
    const val SEARCH_RESULTS = "search_results/{query}"
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
    val planViewModel: PlanViewModel = rememberPlanViewModel()
    val bookCollectionViewModel: BookCollectionViewModel = rememberBookCollectionViewModel()
    val localSettingsViewModel: LocalSettingViewModel = rememberLocalSettingViewModel()
    val userViewModel : UserViewModel = viewModel()

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
            BooksCollectionScreen(
                navController,
                bookViewModel,
                bookCollectionName = collectionName,
                collectionId = collectionId
            )
        }
        composable(Routes.LOGIN) {
            LoginScreen(navController = navController, localSettingsViewModel = localSettingsViewModel)
        }
        composable(Routes.MAIN) {
            MainScreen(
                navController = navController,
                bookViewModel = bookViewModel,
                userViewModel = userViewModel
            )
        }
        /*composable(Routes.NOTIFICATIONS) {
            NotificationsScreen(navController = navController)
        }*/
        composable(Routes.PLANS) {
            PlanScreen(
                navController = navController,
                planViewModel = planViewModel
            )
        }
        composable(Routes.PROFILE) {
            ProfileScreen(navController = navController)
        }
        composable(Routes.SEARCH) {
            SearchScreen(
                navController = navController,
                bookViewModel = bookViewModel,
                bookCollectionViewModel = bookCollectionViewModel,
            )
        }
        composable(
            Routes.SEARCH_RESULTS,
            arguments = listOf(
                navArgument("query") { type = NavType.StringType })
        ) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            SearchResultsScreen(navController, bookViewModel, query = query)
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(
                navController = navController,
                localSettingsViewModel = localSettingsViewModel
            )
        }
        composable(Routes.WELCOME) {
            WelcomeScreen(
                navController = navController,
                bookViewModel = bookViewModel,
                planViewModel = planViewModel,
                bookCollectionViewModel = bookCollectionViewModel,
                localSettingsViewModel = localSettingsViewModel
            )
        }

    }
}
