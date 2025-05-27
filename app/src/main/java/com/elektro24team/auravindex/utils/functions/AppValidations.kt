package com.elektro24team.auravindex.utils.functions

import android.util.Patterns
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.elektro24team.auravindex.navigation.Routes
import com.elektro24team.auravindex.utils.enums.AppAction


@Composable
fun mustBeLoggedInToast(appAction: AppAction, navController: NavController?, route: String?) {
    Toast.makeText(LocalContext.current, "You must be logged in to ${appAction.appActionTitle}.", Toast.LENGTH_LONG).show()
    navController?.navigate(route?: Routes.MAIN)

}

fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}