package com.elektro24team.auravindex.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.elektro24team.auravindex.model.ActivePlan
import com.elektro24team.auravindex.utils.functions.TableCell
import com.elektro24team.auravindex.utils.functions.TableHeaderCell
import com.elektro24team.auravindex.utils.functions.formatUtcToLocalWithHourAndSeconds

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AdminActivePlanTable(
    navController: NavController,
    activePlans: List<ActivePlan>
) {
    var rowsPerPage by remember { mutableStateOf(9) }
    var currentPage by remember { mutableStateOf(0) }
    var totalPages = (activePlans.size + rowsPerPage - 1) / rowsPerPage
    val currentPageActivePlans = activePlans.drop(currentPage * rowsPerPage).take(rowsPerPage)
    Text(
        text = "API Audit logs",
        style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(start = 4.dp, top = 16.dp, end = 0.dp, bottom = 16.dp)
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
        ) {
            Column {
                Row(modifier = Modifier.padding(vertical = 8.dp)) {
                    TableHeaderCell("User", 180.dp)
                    TableHeaderCell("Plan", 180.dp)
                    TableHeaderCell("Status", 180.dp)
                    TableHeaderCell("Ending date", 180.dp)
                }
                Divider()
                currentPageActivePlans.forEach { activePlan ->
                    Row(
                        modifier = Modifier
                            .padding(vertical = 6.dp)
                    ) {
                        TableCell(activePlan.user?.email ?: "Unknown", 180.dp)
                        TableCell(activePlan.plan?.name ?: "Unknown", 180.dp)
                        TableCell(activePlan.plan_status?.plan_status ?: "Unknown", 180.dp)
                        TableCell(formatUtcToLocalWithHourAndSeconds(activePlan.ending_date), 180.dp)
                    }
                    Divider()
                }
            }
        }
        // Pagination controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp).horizontalScroll(
                    rememberScrollState()
                ),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { if (currentPage > 0) currentPage-- },
                enabled = currentPage > 0
            ) {
                Text("<")
            }
            Text(
                text = "${currentPage + 1} of $totalPages",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterVertically)
            )
            Button(
                onClick = { if (currentPage < totalPages - 1) currentPage++ },
                enabled = currentPage < totalPages - 1
            ) {
                Text(">")
            }
        }

    }
}
