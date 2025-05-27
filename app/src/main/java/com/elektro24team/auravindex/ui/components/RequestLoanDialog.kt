package com.elektro24team.auravindex.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.room.parser.Section.Text
import com.elektro24team.auravindex.model.Book
import com.elektro24team.auravindex.model.BookStatus
import com.elektro24team.auravindex.model.api.LoanRequest
import com.elektro24team.auravindex.model.LoanStatus
import com.elektro24team.auravindex.model.Plan
import com.elektro24team.auravindex.model.User
import com.elektro24team.auravindex.viewmodels.LoanViewModel
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RequestLoanDialog(
    showRequestLoanDialog: MutableState<Boolean>,
    loanViewModel: LoanViewModel,
    loanStatus: LoanStatus,
    token: String,
    book: Book,
    plan: Plan,
    userId: String
) {
    AlertDialog(
        onDismissRequest = {
            showRequestLoanDialog.value = false
        },
        title = { Text(text = "Loan book") },
        text = {
            Column {
                Text(
                    "Please confirm the information below to request the loan." +
                            "\n1. You are requesting the book ${book.title} from ${book.authors.joinToString { "${it.name} ${it.last_name}" }}." +
                            "\n2. Your book will be reserved by an employee and you have 24 hours to get to the library and request the book." +
                            "\n3. You must return the book within ${plan.max_return_days} days." +
                            "\n4. You can renew the book up to ${plan.max_renewals_per_loan} times." +
                            "\n5. Additional charges will be done if the book is damaged, lost or not returned."
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                showRequestLoanDialog.value = false
                val returnDate = LocalDate.now().plusDays(plan.max_return_days.toLong())
                val formattedDate = returnDate.atStartOfDay().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME)
                loanViewModel.createLoan(
                    token = token,
                    loan = LoanRequest(
                        book = book._id,
                        user = userId,
                        loan_status = loanStatus.loanStatus,
                        return_date = formattedDate
                    )
                )
            }) {
                Text("Request loan")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                showRequestLoanDialog.value = false
            }) {
                Text("Cancel")
            }
        }
    )
}