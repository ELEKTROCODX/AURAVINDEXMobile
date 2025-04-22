package com.elektro24team.auravindex.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import com.elektro24team.auravindex.navigation.Routes

fun openLink(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}

fun hamburguerMenuNavigator(
    route: String,
    navController: NavController,
    showTermsDialog: MutableState<Boolean>,
    showPrivacyDialog: MutableState<Boolean>,
    showTeamDialog: MutableState<Boolean>

) {
    when(route) {
        Routes.TERMS -> showTermsDialog.value = true
        Routes.PRIVACY -> showPrivacyDialog.value = true
        Routes.TEAM -> showTeamDialog.value = true
        else -> navController.navigate(route)
    }
}