package com.elektro24team.auravindex.ui.components

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
import com.elektro24team.auravindex.model.User
import com.elektro24team.auravindex.utils.functions.TableCell
import com.elektro24team.auravindex.utils.functions.TableHeaderCell


@Composable
fun AdminUserTable(
    navController: NavController,
    users: List<User>
) {
    var rowsPerPage by remember { mutableStateOf(9) }
    var currentPage by remember { mutableStateOf(0) }
    var totalPages = (users.size + rowsPerPage - 1) / rowsPerPage
    val currentPageUsers = users.drop(currentPage * rowsPerPage).take(rowsPerPage)
    Text(
        text = "Users",
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
                    TableHeaderCell("Name", 120.dp)
                    TableHeaderCell("Email", 180.dp)
                    TableHeaderCell("Role", 120.dp)
                }
                Divider()
                currentPageUsers.forEach { user ->
                    Row(
                        modifier = Modifier
                            .clickable { navController.navigate("admin_dashboard/user/${user._id}") }
                            .padding(vertical = 6.dp)
                    ) {
                        TableCell("${user.name} ${user.last_name}", 120.dp)
                        TableCell(user.email, 180.dp)
                        TableCell(user.role?.name ?: "Unknown", 120.dp)
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
