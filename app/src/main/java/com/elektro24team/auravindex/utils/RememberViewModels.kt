package com.elektro24team.auravindex.utils

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elektro24team.auravindex.data.local.AuraVindexDatabase
import com.elektro24team.auravindex.data.repository.BookCollectionRepository
import com.elektro24team.auravindex.data.repository.PlanRepository
import com.elektro24team.auravindex.viewmodels.BookCollectionViewModel
import com.elektro24team.auravindex.viewmodels.PlanViewModel
import com.elektro24team.auravindex.viewmodels.factories.BookCollectionViewModelFactory
import com.elektro24team.auravindex.viewmodels.factories.PlanViewModelFactory

@Composable
fun rememberPlanViewModel(): PlanViewModel {
    val context = LocalContext.current
    val db = remember { AuraVindexDatabase.getInstance(context) }
    val repository = remember {
       try {
           PlanRepository(db.planDao())
       } catch(e: Exception) {
           Log.e("PlanRepository", "Error creating plan repository", e)
       }
    }
    val factory = remember {
        try {
            PlanViewModelFactory(repository as PlanRepository)
        } catch(e: Exception) {
            Log.e("PlanViewModelFactory", "Error creating plan view model factory", e)
        }
    }
    return viewModel(factory = factory as ViewModelProvider.Factory?)
}

@Composable
fun rememberBookCollectionViewModel(): BookCollectionViewModel {
    val context = LocalContext.current
    val db = remember { AuraVindexDatabase.getInstance(context) }
    val repository = remember {
        try {
            BookCollectionRepository(db.bookCollectionDao())
        } catch(e: Exception) {
            Log.e("BookCollectionRepository", "Error creating book controller repository", e)
        }
    }
    val factory = remember {
        try {
            BookCollectionViewModelFactory(repository as BookCollectionRepository)
        } catch(e: Exception) {
            Log.e("BookCollectionViewModelFactory", "Error creating book collection view model factory", e)
        }
    }
    return viewModel(factory = factory as ViewModelProvider.Factory?)
}