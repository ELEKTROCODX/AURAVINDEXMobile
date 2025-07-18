package com.elektrocodx.auravindex.navigation

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.elektrocodx.auravindex.utils.functions.rememberActivePlanViewModel
import com.elektrocodx.auravindex.utils.functions.rememberAuditLogViewModel
import com.elektrocodx.auravindex.utils.functions.rememberAuthViewModel
import com.elektrocodx.auravindex.utils.functions.rememberBookCollectionViewModel
import com.elektrocodx.auravindex.utils.functions.rememberBookViewModel
import com.elektrocodx.auravindex.utils.functions.rememberGenderViewModel
import com.elektrocodx.auravindex.utils.functions.rememberLoanStatusViewModel
import com.elektrocodx.auravindex.utils.functions.rememberLoanViewModel
import com.elektrocodx.auravindex.utils.functions.rememberLocalSettingViewModel
import com.elektrocodx.auravindex.utils.functions.rememberNotificationViewModel
import com.elektrocodx.auravindex.utils.functions.rememberPlanViewModel
import com.elektrocodx.auravindex.utils.functions.rememberRecentBookViewModel
import com.elektrocodx.auravindex.utils.functions.rememberUserViewModel
import com.elektrocodx.auravindex.utils.objects.AuthPrefsHelper
import com.elektrocodx.auravindex.view.AdminDashboardScreen
import com.elektrocodx.auravindex.view.BookScreen
import com.elektrocodx.auravindex.view.BooksCollectionScreen
import com.elektrocodx.auravindex.view.ListsScreen
import com.elektrocodx.auravindex.view.LoansScreen
import com.elektrocodx.auravindex.view.LoginScreen
import com.elektrocodx.auravindex.view.MainScreen
import com.elektrocodx.auravindex.view.MyListScreen
import com.elektrocodx.auravindex.view.NotificationsScreen
import com.elektrocodx.auravindex.view.PlanScreen
import com.elektrocodx.auravindex.view.ProfileScreen
import com.elektrocodx.auravindex.view.RegisterScreen
import com.elektrocodx.auravindex.view.SearchResultsScreen
import com.elektrocodx.auravindex.view.SearchScreen
import com.elektrocodx.auravindex.view.SettingsScreen
import com.elektrocodx.auravindex.view.WelcomeScreen
import com.elektrocodx.auravindex.viewmodels.ActivePlanViewModel
import com.elektrocodx.auravindex.viewmodels.AuditLogViewModel
import com.elektrocodx.auravindex.viewmodels.AuthViewModel
import com.elektrocodx.auravindex.viewmodels.BookCollectionViewModel
import com.elektrocodx.auravindex.viewmodels.BookListViewModel
import com.elektrocodx.auravindex.viewmodels.BookViewModel
import com.elektrocodx.auravindex.viewmodels.GenderViewModel
import com.elektrocodx.auravindex.viewmodels.LoanStatusViewModel
import com.elektrocodx.auravindex.viewmodels.LoanViewModel
import com.elektrocodx.auravindex.viewmodels.LocalSettingViewModel
import com.elektrocodx.auravindex.viewmodels.NotificationViewModel
import com.elektrocodx.auravindex.viewmodels.PlanViewModel
import com.elektrocodx.auravindex.viewmodels.RecentBookViewModel
import com.elektrocodx.auravindex.viewmodels.UserViewModel

object Routes {
    const val ADMIN_DASHBOARD = "admin_dashboard"
    const val ADMIN_DASHBOARD_OBJECTS = "admin_dashboard/{objectName}"
    const val ADMIN_DASHBOARD_OBJECT = "admin_dashboard/{objectName}/{objectId}"
    const val BOOK = "book/{bookId}"
    const val COLLECTION_BOOKS = "collection_books/{collectionName}/{collectionId}"
    const val LISTS = "lists"
    const val LOANS = "loans"
    const val LOGIN = "login"
    const val SIGNUP ="signup"
    const val LOGOUT = "logout"
    const val MAIN = "main"
    const val MY_LIST = "myList/{listId}"
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
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@RequiresPermission(android.Manifest.permission.POST_NOTIFICATIONS)
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
    val activePlanViewModel: ActivePlanViewModel = rememberActivePlanViewModel()
    val loanViewModel: LoanViewModel = rememberLoanViewModel()
    val recentBookViewModel: RecentBookViewModel = rememberRecentBookViewModel()
    val loanStatusViewModel: LoanStatusViewModel = rememberLoanStatusViewModel()
    val notificationViewModel: NotificationViewModel = rememberNotificationViewModel()
    val genderViewModel : GenderViewModel = rememberGenderViewModel()
    val localSettings by localSettingViewModel.settings.collectAsState()
    val bookListViewModel: BookListViewModel = viewModel()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.ADMIN_DASHBOARD) {
            AdminDashboardScreen(
                navController = navController,
                bookViewModel = bookViewModel,
                userViewModel = userViewModel,
                loanViewModel = loanViewModel,
                planViewModel = planViewModel,
                activePlanViewModel = activePlanViewModel,
                auditLogViewModel = auditLogViewModel,
                notificationViewModel = notificationViewModel,
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
            AdminDashboardScreen(
                navController = navController,
                bookViewModel = bookViewModel,
                userViewModel = userViewModel,
                loanViewModel = loanViewModel,
                planViewModel = planViewModel,
                activePlanViewModel = activePlanViewModel,
                auditLogViewModel = auditLogViewModel,
                notificationViewModel = notificationViewModel,
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
                AdminDashboardScreen(
                    navController = navController,
                    bookViewModel = bookViewModel,
                    userViewModel = userViewModel,
                    loanViewModel = loanViewModel,
                    planViewModel = planViewModel,
                    activePlanViewModel = activePlanViewModel,
                    auditLogViewModel = auditLogViewModel,
                    notificationViewModel = notificationViewModel,
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
                activePlanViewModel = activePlanViewModel,
                loanViewModel = loanViewModel,
                loanStatusViewModel = loanStatusViewModel,
                localSettingViewModel = localSettingViewModel,
                userViewModel = userViewModel,
                notificationViewModel = notificationViewModel,
                bookListViewModel = bookListViewModel
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
               navController = navController,
               userViewModel = userViewModel,
               notificationViewModel = notificationViewModel,
               localSettingViewModel = localSettingViewModel,
               bookListViewModel = bookListViewModel
           )
        }
        composable(Routes.LOANS) {
            LoansScreen(
                navController = navController,
                loanViewModel = loanViewModel,
                notificationViewModel = notificationViewModel,
                userViewModel = userViewModel,
                localSettingViewModel = localSettingViewModel
            )
        }
        composable(Routes.LOGIN) {
            LoginScreen(
                navController = navController,
                authViewModel = authViewModel,
                userViewModel = userViewModel,
                activePlanViewModel = activePlanViewModel,
                localSettingViewModel = localSettingViewModel
            )
        }
        composable(Routes.LOGOUT) {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                localSettingViewModel.clearUserSettings()
                activePlanViewModel.clearViewModelData()
                auditLogViewModel.clearViewModelData()
                bookViewModel.clearViewModelData()
                bookCollectionViewModel.clearViewModelData()
                loanViewModel.clearViewModelData()
                planViewModel.clearViewModelData()
                recentBookViewModel.clearViewModelData()
                userViewModel.clearViewModelData()
                authViewModel.clearViewModelData()
                AuthPrefsHelper.clearAuthToken(context)
                AuthPrefsHelper.clearFcmToken(context)
                AuthPrefsHelper.clearPermissionRequested(context)
                AuthPrefsHelper.clearUserId(context)
                Toast.makeText(context, "Successfully logged out.", Toast.LENGTH_SHORT).show()
                navController.navigate(Routes.WELCOME) {
                    popUpTo(Routes.WELCOME) {
                        inclusive = true
                    }
                }
            }
        }
        composable(Routes.MAIN){
            MainScreen(
                navController = navController,
                bookViewModel = bookViewModel,
                userViewModel = userViewModel,
                notificationViewModel = notificationViewModel,
                recentBookViewModel = recentBookViewModel,
                localSettingViewModel = localSettingViewModel,
            )
        }
        composable(
            Routes.MY_LIST,
            arguments = listOf(
                navArgument("listId") { type = NavType.StringType })
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getString("listId") ?: ""
            MyListScreen(
                navController = navController,
                userViewModel = userViewModel,
                localSettingViewModel = localSettingViewModel,
                bookListViewModel = bookListViewModel,
                notificationViewModel = notificationViewModel,
                listId = listId
            )
        }
        composable(Routes.NOTIFICATIONS) {
            NotificationsScreen(
                navController = navController,
                localSettingViewModel = localSettingViewModel,
                userViewModel = userViewModel,
                notificationViewModel = notificationViewModel
            )
        }
        composable(Routes.PLANS) {
            PlanScreen(
                navController = navController,
                planViewModel = planViewModel,
                activePlanViewModel = activePlanViewModel,
                localSettingViewModel = localSettingViewModel,
                userViewModel = userViewModel,
                notificationViewModel = notificationViewModel
            )
        }
        composable(Routes.PROFILE) {
            ProfileScreen(
                navController = navController,
                userViewModel = userViewModel,
                localSettingViewModel = localSettingViewModel,
                notificationViewModel = notificationViewModel
            )
        }
        composable(Routes.SEARCH) {
            SearchScreen(
                navController = navController,
                bookViewModel = bookViewModel,
                bookCollectionViewModel = bookCollectionViewModel,
                userViewModel = userViewModel,
                notificationViewModel = notificationViewModel,
                localSettingViewModel = localSettingViewModel
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
                localSettingViewModel = localSettingViewModel,
                userViewModel = userViewModel,
                notificationViewModel = notificationViewModel
            )
        }
        composable(Routes.WELCOME) {
            WelcomeScreen(
                navController = navController,
                bookViewModel = bookViewModel,
                planViewModel = planViewModel,
                bookCollectionViewModel = bookCollectionViewModel,
                activePlanViewModel = activePlanViewModel,
                userViewModel = userViewModel,
                localSettingViewModel = localSettingViewModel
            )
        }
        composable(Routes.SIGNUP){
            RegisterScreen(genderViewModel,authViewModel,navController)
        }
    }
}
