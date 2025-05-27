package com.elektro24team.auravindex.utils.functions

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.elektro24team.auravindex.navigation.Routes
import com.elektro24team.auravindex.utils.enums.AppAction

fun mustBeLoggedInToast(context: Context, appAction: AppAction, navController: NavController?, route: String? = "") {
    Toast.makeText(context, "You must be logged in to ${appAction.appActionTitle}.", Toast.LENGTH_SHORT).show()
    if(!route.isNullOrEmpty()) {
        navController?.navigate(route)
    }
}
fun mustBeSubscribedToast(context: Context, appAction: AppAction, navController: NavController?, route: String? = "") {
    Toast.makeText(context, "You must be subscribed to a plan to ${appAction.appActionTitle}.", Toast.LENGTH_SHORT).show()
    if(!route.isNullOrEmpty()) {
        navController?.navigate(route)
    }

}