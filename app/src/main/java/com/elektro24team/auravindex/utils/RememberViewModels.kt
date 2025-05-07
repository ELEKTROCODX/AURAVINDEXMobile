package com.elektro24team.auravindex.utils

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elektro24team.auravindex.data.local.AuraVindexDatabase
import com.elektro24team.auravindex.data.repository.BookCollectionRepository
import com.elektro24team.auravindex.data.repository.LocalSettingRepository
import com.elektro24team.auravindex.data.repository.PlanRepository
import com.elektro24team.auravindex.viewmodels.BookCollectionViewModel
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel
import com.elektro24team.auravindex.viewmodels.PlanViewModel
import com.elektro24team.auravindex.viewmodels.factories.BookCollectionViewModelFactory
import com.elektro24team.auravindex.viewmodels.factories.LocalSettingViewModelFactory
import com.elektro24team.auravindex.viewmodels.factories.PlanViewModelFactory

@Composable
fun rememberPlanViewModel(): PlanViewModel {
    val context = LocalContext.current
    val db = remember { AuraVindexDatabase.getInstance(context) }
    val repository = remember { PlanRepository(db.planDao()) }
    val factory = remember { PlanViewModelFactory(repository) }
    return viewModel(factory = factory)
}

@Composable
fun rememberBookCollectionViewModel(): BookCollectionViewModel {
    val context = LocalContext.current
    val db = remember { AuraVindexDatabase.getInstance(context) }
    val repository = remember { BookCollectionRepository(db.bookCollectionDao()) }
    val factory = remember { BookCollectionViewModelFactory(repository) }
    return viewModel(factory = factory)
}

@Composable
fun rememberLocalSettingViewModel(): LocalSettingViewModel {
    val context = LocalContext.current
    val db = remember { AuraVindexDatabase.getInstance(context) }
    val repository = remember { LocalSettingRepository(db.localSettingDao()) }
    val factory = remember { LocalSettingViewModelFactory(repository) }
    return viewModel(factory = factory)
}