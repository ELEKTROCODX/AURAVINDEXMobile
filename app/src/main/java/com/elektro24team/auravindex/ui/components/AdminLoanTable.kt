package com.elektro24team.auravindex.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elektro24team.auravindex.model.Loan
import com.elektro24team.auravindex.utils.functions.TableCell
import com.elektro24team.auravindex.utils.functions.TableHeaderCell
import com.elektro24team.auravindex.utils.functions.formatUtcToLocalWithHourAndSeconds


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AdminLoanTable(
    navController: NavController,
    loans: List<Loan>
) {
    var rowsPerPage by remember { mutableStateOf(8) }
    var currentPage by remember { mutableStateOf(0) }
    val totalPages = (loans.size + rowsPerPage - 1) / rowsPerPage
    val currentPageLoans = loans.drop(currentPage * rowsPerPage).take(rowsPerPage)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        Text(
            text = "Loans",
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .background(androidx.compose.material3.MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                ) {
                    TableHeaderCell("User", 180.dp)
                    TableHeaderCell("Book", 180.dp)
                    TableHeaderCell("Status", 140.dp)
                    TableHeaderCell("Date", 160.dp)
                }

                Divider()

                currentPageLoans.forEachIndexed { index, loan ->
                    val backgroundColor = if (index % 2 == 0)
                        androidx.compose.material3.MaterialTheme.colorScheme.surface
                    else
                        androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant

                    Row(
                        modifier = Modifier
                            .background(backgroundColor)
                            .clickable {
                                navController.navigate("book/${loan.book._id}")
                            }
                            .padding(vertical = 8.dp)
                    ) {
                        TableCell(loan.user?.email ?: "Unknown", 180.dp)
                        TableCell(loan.book.title, 180.dp)
                        TableCell(loan.loan_status.loan_status, 140.dp)
                        TableCell(formatUtcToLocalWithHourAndSeconds(loan.createdAt), 160.dp)
                    }

                    Divider(thickness = 0.5.dp)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { if (currentPage > 0) currentPage-- },
                enabled = currentPage > 0
            ) {
                Text("Prev")
            }

            Text(
                text = "Page ${currentPage + 1} of $totalPages",
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Button(
                onClick = { if (currentPage < totalPages - 1) currentPage++ },
                enabled = currentPage < totalPages - 1
            ) {
                Text("Next")
            }
        }
    }
}