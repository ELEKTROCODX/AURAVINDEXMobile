package com.elektro24team.auravindex.navigation

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.elektro24team.auravindex.utils.functions.rememberAuditLogViewModel
import com.elektro24team.auravindex.utils.functions.rememberAuthViewModel
import com.elektro24team.auravindex.utils.functions.rememberBookCollectionViewModel
import com.elektro24team.auravindex.utils.functions.rememberBookViewModel
import com.elektro24team.auravindex.utils.functions.rememberGenderViewModel
import com.elektro24team.auravindex.utils.functions.rememberLocalSettingViewModel
import com.elektro24team.auravindex.utils.functions.rememberPlanViewModel
import com.elektro24team.auravindex.utils.functions.rememberRecentBookViewModel
import com.elektro24team.auravindex.utils.functions.rememberUserViewModel
import com.elektro24team.auravindex.view.*
import com.elektro24team.auravindex.viewmodels.AuditLogViewModel
import com.elektro24team.auravindex.viewmodels.AuthViewModel
import com.elektro24team.auravindex.viewmodels.BookCollectionViewModel
import com.elektro24team.auravindex.viewmodels.BookViewModel
import com.elektro24team.auravindex.viewmodels.GenderViewModel
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel
import com.elektro24team.auravindex.viewmodels.PlanViewModel
import com.elektro24team.auravindex.viewmodels.RecentBookViewModel
import com.elektro24team.auravindex.viewmodels.UserViewModel


// RUTAS
object Routes {
    const val ADMIN_DASHBOARD = "admin_dashboard"
    const val ADMIN_DASHBOARD_OBJECTS = "admin_dashboard/{objectName}"
    const val ADMIN_DASHBOARD_OBJECT = "admin_dashboard/{objectName}/{objectId}"
    const val BOOK = "book/{bookId}"
    const val COLLECTION_BOOKS = "collection_books/{collectionName}/{collectionId}"
    const val LISTS = "lists"
    const val LOGIN = "login"
    const val SIGNUP ="signup"
    const val LOGOUT = "logout"
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

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(startDestination: String = Routes.WELCOME) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = rememberAuthViewModel()
    val bookViewModel: BookViewModel = rememberBookViewModel()
    val planViewModel: PlanViewModel = rememberPlanViewModel()
    val bookCollectionViewModel: BookCollectionViewModel = rememberBookCollectionViewModel()
    val localSettingViewModel: LocalSettingViewModel = rememberLocalSettingViewModel()
    val userViewModel : UserViewModel = rememberUserViewModel()
    val auditLogViewModel : AuditLogViewModel = rememberAuditLogViewModel()
    val recentBookViewModel: RecentBookViewModel = rememberRecentBookViewModel()
    val genderViewModel : GenderViewModel = rememberGenderViewModel()
    val localSettings by localSettingViewModel.settings.collectAsState()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.ADMIN_DASHBOARD) {
            AdminDashBoardScreen(
                navController = navController,
                bookViewModel = bookViewModel,
                userViewModel = userViewModel,
                planViewModel = planViewModel,
                auditLogViewModel = auditLogViewModel,
                localSettingViewModel = localSettingViewModel,
                objectName = "",
                objectId = ""
            )
        }
        composable(
            Routes.ADMIN_DASHBOARD_OBJECTS,
            arguments = listOf(
                navArgument("objectName") { type = NavType.StringType }
            )
        ) {
            val objectName = it.arguments?.getString("objectName") ?: ""
            AdminDashBoardScreen(
                navController = navController,
                bookViewModel = bookViewModel,
                userViewModel = userViewModel,
                planViewModel = planViewModel,
                auditLogViewModel = auditLogViewModel,
                localSettingViewModel = localSettingViewModel,
                objectName = objectName,
                objectId = ""
            )
        }
        composable(
            Routes.ADMIN_DASHBOARD_OBJECT,
            arguments = listOf(
                navArgument("objectName") { type = NavType.StringType },
                navArgument("objectId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
                val objectName = backStackEntry.arguments?.getString("objectName") ?: ""
                val objectId = backStackEntry.arguments?.getString("objectId") ?: ""
                AdminDashBoardScreen(
                    navController = navController,
                    bookViewModel = bookViewModel,
                    userViewModel = userViewModel,
                    planViewModel = planViewModel,
                    auditLogViewModel = auditLogViewModel,
                    localSettingViewModel = localSettingViewModel,
                    objectName = objectName,
                    objectId = objectId
                )
        }

        composable(
            Routes.BOOK,
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId")
            BookScreen(
                navController = navController,
                bookId = bookId ?: "",
                bookViewModel = bookViewModel,
                localSettingViewModel = localSettingViewModel
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
        composable(Routes.LISTS) {
           ListsScreen(
               navController = navController
           )
        }
        composable(Routes.LOGIN) {
            LoginScreen(
                navController = navController,
                authViewModel = authViewModel,
                userViewModel = userViewModel,
                localSettingViewModel = localSettingViewModel
            )
        }
        composable(Routes.LOGOUT) {
            localSettingViewModel.clearUserSettings()
            Toast.makeText(LocalContext.current, "Successfully logged out.", Toast.LENGTH_LONG).show()
            navController.navigate(Routes.WELCOME)
        }
        composable(Routes.MAIN) {
            MainScreen(
                navController = navController,
                bookViewModel = bookViewModel,
                userViewModel = userViewModel,
                recentBookViewModel = recentBookViewModel,
                localSettingViewModel = localSettingViewModel,
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
            ProfileScreen(
                navController = navController,
                userViewModel = userViewModel,
                localSettingViewModel = localSettingViewModel
            )
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
                localSettingViewModel = localSettingViewModel
            )
        }
        composable(Routes.WELCOME) {
            WelcomeScreen(
                navController = navController,
                bookViewModel = bookViewModel,
                planViewModel = planViewModel,
                bookCollectionViewModel = bookCollectionViewModel,
                localSettingViewModel = localSettingViewModel
            )
        }
        composable(Routes.SIGNUP){
            RegisterScreen(genderViewModel,authViewModel,navController)
        }

    }
}
