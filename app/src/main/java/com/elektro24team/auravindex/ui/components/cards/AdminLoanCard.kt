package com.elektro24team.auravindex.ui.components.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.elektro24team.auravindex.utils.enums.SettingKey
import com.elektro24team.auravindex.utils.functions.formatUtcToLocalWithDate
import com.elektro24team.auravindex.utils.functions.formatUtcToLocalWithHourAndSeconds
import com.elektro24team.auravindex.viewmodels.LoanViewModel
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel

@Composable
fun AdminLoanCard(
    navController: NavController,
    loanViewModel: LoanViewModel,
    localSettingViewModel: LocalSettingViewModel,
    loanId: String
) {
    val loan = loanViewModel.loan.collectAsState()
    val localSettings = localSettingViewModel.settings.collectAsState()
    androidx.compose.material3.MaterialTheme.colorScheme
    LaunchedEffect(Unit) {
        loanViewModel.loadLoanById(
            localSettings.value.getOrDefault(SettingKey.TOKEN.keySetting, ""),
            loanId
        )
    }
    Card(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = loan.value?.book?.title ?: "Name",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                color = androidx.compose.material3.MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(top = 16.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Loan Details",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF572365)
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Divider(color = Color.LightGray, thickness = 1.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { navController.navigate("book/${loan.value?.book?._id}")},
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Book: ",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF572365)
                    ),
                )
                Text(
                    text = "${loan.value?.book?.title} (${loan.value?.book?.authors?.joinToString { "${it.name} ${it.last_name}" }})",
                    style = TextStyle(fontSize = 16.sp, color = Color.Black)
                )
            }
            Divider(color = Color.LightGray, thickness = 1.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { navController.navigate("admin_dashboard/user/${loan.value?.user?._id}")},
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "User: ",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF572365)
                    ),
                )
                Text(
                    text = "${loan.value?.user?.name} ${loan.value?.user?.last_name} (${loan.value?.user?.email})",
                    style = TextStyle(fontSize = 16.sp, color = Color.Black)
                )
            }
            Divider(color = Color.LightGray, thickness = 1.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Status: ",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF572365)
                    ),
                )
                Text(
                    text = "${loan.value?.loan_status?.loan_status}",
                    style = TextStyle(fontSize = 16.sp, color = Color.Black)
                )
            }
            Divider(color = Color.LightGray, thickness = 1.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Renewals: ",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF572365)
                    ),
                )
                Text(
                    text = "${loan.value?.renewals}",
                    style = TextStyle(fontSize = 16.sp, color = Color.Black)
                )
            }
            Divider(color = Color.LightGray, thickness = 1.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Created at: ",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF572365)
                    ),
                )
                Text(
                    text = formatUtcToLocalWithHourAndSeconds(loan.value?.createdAt),
                    style = TextStyle(fontSize = 16.sp, color = Color.Black)
                )
            }
            Divider(color = Color.LightGray, thickness = 1.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Return date: ",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF572365)
                    ),
                )
                Text(
                    text = formatUtcToLocalWithDate(loan.value?.return_date),
                    style = TextStyle(fontSize = 16.sp, color = Color.Black)
                )
            }
            Divider(color = Color.LightGray, thickness = 1.dp)
            if(loan.value?.returned_date != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Return date: ",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF572365)
                        ),
                    )
                    Text(
                        text = formatUtcToLocalWithDate(loan.value?.returned_date),
                        style = TextStyle(fontSize = 16.sp, color = Color.Black)
                    )
                }
                Divider(color = Color.LightGray, thickness = 1.dp)
            }
        }
    }
}