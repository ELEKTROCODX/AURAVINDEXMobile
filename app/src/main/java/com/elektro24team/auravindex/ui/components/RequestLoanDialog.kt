package com.elektro24team.auravindex.ui.components

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
import com.elektro24team.auravindex.model.Plan
import com.elektro24team.auravindex.model.User
import com.elektro24team.auravindex.viewmodels.LoanViewModel

@Composable
fun RequestLoanDialog(
    showRequestLoanDialog: MutableState<Boolean>,
    loanViewModel: LoanViewModel,
    book: Book,
    plan: Plan,
    user_id: String
) {
    AlertDialog(
        onDismissRequest = {
            showRequestLoanDialog.value = true
        },
        title = { Text(text = "Loan book") },
        text = {
            Column {
                Text(
                    "Please confirm the information below to request the loan." +
                            "\n1. You are requesting the book ${book.title} from ${book.authors.joinToString { "${it.name} ${it.last_name}" }}." +
                            "\n2. You must return the book within ${plan.max_return_days} days." +
                            "\n3. You can renew "
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                showRequestLoanDialog.value = true
            }) {
                Text("Request loan")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                showRequestLoanDialog.value = true
            }) {
                Text("Cancel")
            }
        }
    )
}