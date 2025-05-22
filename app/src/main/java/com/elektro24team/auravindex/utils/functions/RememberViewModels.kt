package com.elektro24team.auravindex.utils.functions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elektro24team.auravindex.data.local.AuraVindexDatabase
import com.elektro24team.auravindex.data.repository.AuditLogRepository
import com.elektro24team.auravindex.data.repository.BookCollectionRepository
import com.elektro24team.auravindex.data.repository.BookRepository
import com.elektro24team.auravindex.data.repository.LocalSettingRepository
import com.elektro24team.auravindex.data.repository.PlanRepository
import com.elektro24team.auravindex.data.repository.UserRepository
import com.elektro24team.auravindex.viewmodels.AuditLogViewModel
import com.elektro24team.auravindex.viewmodels.AuthViewModel
import com.elektro24team.auravindex.viewmodels.BookCollectionViewModel
import com.elektro24team.auravindex.viewmodels.BookViewModel
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel
import com.elektro24team.auravindex.viewmodels.PlanViewModel
import com.elektro24team.auravindex.viewmodels.RecentBookViewModel
import com.elektro24team.auravindex.viewmodels.UserViewModel
import com.elektro24team.auravindex.viewmodels.factories.AuditLogViewModelFactory
import com.elektro24team.auravindex.viewmodels.factories.BookCollectionViewModelFactory
import com.elektro24team.auravindex.viewmodels.factories.BookViewModelFactory
import com.elektro24team.auravindex.viewmodels.factories.LocalSettingViewModelFactory
import com.elektro24team.auravindex.viewmodels.factories.PlanViewModelFactory
import com.elektro24team.auravindex.viewmodels.factories.UserViewModelFactory

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
fun rememberBookViewModel(): BookViewModel {
    val context = LocalContext.current
    val db = remember { AuraVindexDatabase.getInstance(context) }
    val repository = remember { BookRepository(db.bookDao()) }
    val factory = remember { BookViewModelFactory(repository) }
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


@Composable
fun rememberUserViewModel(): UserViewModel {
    val context = LocalContext.current
    val db = remember { AuraVindexDatabase.getInstance(context) }
    val repository = remember { UserRepository(db.userDao(), db.genderDao(), db.roleDao()) }
    val factory = remember { UserViewModelFactory(repository) }
    return viewModel(factory = factory)

}

@Composable
fun rememberAuditLogViewModel(): AuditLogViewModel {
    val context = LocalContext.current
    val db = remember { AuraVindexDatabase.getInstance(context) }
    val repository = remember { AuditLogRepository(db.auditLogDao()) }
    val factory = remember { AuditLogViewModelFactory(repository) }
    return viewModel(factory = factory)
}

@Composable
fun rememberAuthViewModel(): AuthViewModel {
    return viewModel()

}

@Composable
fun rememberRecentBookViewModel(): RecentBookViewModel {
    return viewModel()
}