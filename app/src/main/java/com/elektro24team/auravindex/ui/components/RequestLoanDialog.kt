package com.elektro24team.auravindex.ui.components

import android.icu.util.TimeZone
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elektro24team.auravindex.model.Book
import com.elektro24team.auravindex.model.BookStatus
import com.elektro24team.auravindex.model.api.LoanRequest
import com.elektro24team.auravindex.model.LoanStatus
import com.elektro24team.auravindex.model.Plan
import com.elektro24team.auravindex.model.User
import com.elektro24team.auravindex.utils.functions.formatApiDateFormat
import com.elektro24team.auravindex.viewmodels.BookViewModel
import com.elektro24team.auravindex.viewmodels.LoanViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@RequiresPermission(android.Manifest.permission.POST_NOTIFICATIONS)
@Composable
fun RequestLoanDialog(
    showRequestLoanDialog: MutableState<Boolean>,
    loanViewModel: LoanViewModel,
    bookViewModel: BookViewModel,
    loanStatus: LoanStatus,
    token: String,
    book: Book,
    plan: Plan,
    userId: String
) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = {
            showRequestLoanDialog.value = false
        },
        title = {
            Text(
                text = "Book loan request form",
            )
                },
        modifier = Modifier
            .padding(start = 0.dp, top = 48.dp, end = 0.dp, bottom = 48.dp),
        text = {
            Column {
                Text(
                    text = buildAnnotatedString {
                        append(
                            "Please read and confirm the information below to request the loan." +
                                    "\n\n1. You are requesting the book ")
                        withStyle(SpanStyle(color = Color.Black, fontWeight = FontWeight.Normal)) {
                            append(book.title)
                        }
                        append(" from ")
                        withStyle(SpanStyle(color = Color.Black, fontWeight = FontWeight.Normal)) {
                            append(book.authors.joinToString(", ") { it.name + " " + it.last_name })
                        }
                        append(
                            ".\n2. Your book will be reserved by an employee and you have "
                        )
                        withStyle(SpanStyle(color = Color.Black, fontWeight = FontWeight.Normal)) {
                            append("24 hours")
                        }
                        append(
                            " to get to the library and request the book.\n3. You must return the book within "
                        )
                        withStyle(SpanStyle(color = Color.Black, fontWeight = FontWeight.Normal)) {
                            append("${plan.max_return_days} days")
                        }
                        append(
                            ".\n4. You can renew the loan up to "
                        )
                        withStyle(SpanStyle(color = Color.Black, fontWeight = FontWeight.Normal)) {
                            append("${plan.max_renewals_per_loan} times")
                        }
                        append(
                            ".\n5. Additional charges will be done if the book is damaged, lost or not returned."
                        )
                    }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                showRequestLoanDialog.value = false
                val returnDate = LocalDate.now().plusDays(plan.max_return_days.toLong())
                val formatter = DateTimeFormatter.ISO_DATE
                val formattedDate = returnDate.format(formatter)
                val loanRequest = LoanRequest(
                    book = book._id,
                    user = userId,
                    loan_status = loanStatus._id,
                    return_date = formattedDate
                )
                loanViewModel.createLoan(
                    token = token,
                    loan = loanRequest
                )
                bookViewModel.loadBook(book._id)
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