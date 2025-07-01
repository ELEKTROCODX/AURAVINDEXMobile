package com.elektrocodx.auravindex.utils.functions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elektrocodx.auravindex.data.local.AuraVindexDatabase
import com.elektrocodx.auravindex.data.repository.AuditLogRepository
import com.elektrocodx.auravindex.data.repository.BookCollectionRepository
import com.elektrocodx.auravindex.data.repository.BookRepository
import com.elektrocodx.auravindex.data.repository.GenderRepository
import com.elektrocodx.auravindex.data.repository.LocalSettingRepository
import com.elektrocodx.auravindex.data.repository.PlanRepository
import com.elektrocodx.auravindex.data.repository.UserRepository
import com.elektrocodx.auravindex.viewmodels.ActivePlanViewModel
import com.elektrocodx.auravindex.viewmodels.AuditLogViewModel
import com.elektrocodx.auravindex.viewmodels.AuthViewModel
import com.elektrocodx.auravindex.viewmodels.BookCollectionViewModel
import com.elektrocodx.auravindex.viewmodels.BookViewModel
import com.elektrocodx.auravindex.viewmodels.GenderViewModel
import com.elektrocodx.auravindex.viewmodels.LoanStatusViewModel
import com.elektrocodx.auravindex.viewmodels.LoanViewModel
import com.elektrocodx.auravindex.viewmodels.LocalSettingViewModel
import com.elektrocodx.auravindex.viewmodels.NotificationViewModel
import com.elektrocodx.auravindex.viewmodels.PlanStatusViewModel
import com.elektrocodx.auravindex.viewmodels.PlanViewModel
import com.elektrocodx.auravindex.viewmodels.RecentBookViewModel
import com.elektrocodx.auravindex.viewmodels.UserViewModel
import com.elektrocodx.auravindex.viewmodels.factories.AuditLogViewModelFactory
import com.elektrocodx.auravindex.viewmodels.factories.BookCollectionViewModelFactory
import com.elektrocodx.auravindex.viewmodels.factories.BookViewModelFactory
import com.elektrocodx.auravindex.viewmodels.factories.GenderViewModelFactory
import com.elektrocodx.auravindex.viewmodels.factories.LocalSettingViewModelFactory
import com.elektrocodx.auravindex.viewmodels.factories.PlanViewModelFactory
import com.elektrocodx.auravindex.viewmodels.factories.UserViewModelFactory

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

@Composable
fun rememberActivePlanViewModel(): ActivePlanViewModel {
    return viewModel()
}

@Composable
fun rememberLoanViewModel(): LoanViewModel {
    return viewModel()
}

@Composable
fun rememberLoanStatusViewModel(): LoanStatusViewModel {
    return viewModel()
}

@Composable
fun rememberPlanStatusViewModel(): PlanStatusViewModel {
    return viewModel()
}

@Composable
fun rememberNotificationViewModel(): NotificationViewModel {
    return viewModel ()
}

@Composable
fun rememberGenderViewModel(): GenderViewModel{
    val repository = remember { GenderRepository() }
    val factory = remember { GenderViewModelFactory(repository) }
    val owner = LocalViewModelStoreOwner.current!!
    return  ViewModelProvider(owner, factory)[GenderViewModel::class.java]
}