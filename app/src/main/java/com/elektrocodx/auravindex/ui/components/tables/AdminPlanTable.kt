package com.elektrocodx.auravindex.ui.components.tables

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elektrocodx.auravindex.model.Plan
import com.elektrocodx.auravindex.utils.functions.TableCell
import com.elektrocodx.auravindex.utils.functions.TableHeaderCell

@Composable
fun AdminPlanTable(
    navController: NavController,
    plans: List<Plan>
) {
    var rowsPerPage by remember { mutableStateOf(9) }
    var currentPage by remember { mutableStateOf(0) }
    val totalPages = (plans.size + rowsPerPage - 1) / rowsPerPage
    val currentPagePlans = plans.drop(currentPage * rowsPerPage).take(rowsPerPage)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Plans",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
            Column {
                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                        .padding(vertical = 8.dp)
                ) {
                    TableHeaderCell("Name", 220.dp)
                    TableHeaderCell("Fixed Price", 120.dp)
                    TableHeaderCell("Monthly Price", 120.dp)
                }

                Divider()

                currentPagePlans.forEachIndexed { index, plan ->
                    val backgroundColor = if (index % 2 == 0)
                        MaterialTheme.colorScheme.surface
                    else
                        MaterialTheme.colorScheme.surfaceVariant

                    Row(
                        modifier = Modifier
                            .background(backgroundColor)
                            .clickable { navController.navigate("admin_dashboard/plan/${plan._id}") }
                            .padding(vertical = 6.dp)
                    ) {
                        TableCell(plan.name, 220.dp)
                        TableCell("$${plan.fixed_price}", 120.dp)
                        TableCell("$${plan.monthly_price}", 120.dp)
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
