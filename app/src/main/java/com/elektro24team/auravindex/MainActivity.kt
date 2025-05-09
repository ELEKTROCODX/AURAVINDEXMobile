package com.elektro24team.auravindex

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.elektro24team.auravindex.data.local.AuraVindexDatabase
import com.elektro24team.auravindex.data.repository.PlanRepository
import com.elektro24team.auravindex.navigation.NavGraph
import com.elektro24team.auravindex.ui.theme.AppTheme
import com.elektro24team.auravindex.viewmodels.PlanViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                NavGraph()
            }
        }
    }
}