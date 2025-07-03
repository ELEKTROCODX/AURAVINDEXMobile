package com.elektrocodx.auravindex.ui.components.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.NotInterested
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.elektrocodx.auravindex.utils.enums.SettingKey
import com.elektrocodx.auravindex.utils.functions.formatUtcToLocalWithDate
import com.elektrocodx.auravindex.utils.functions.formatUtcToLocalWithHourAndSeconds
import com.elektrocodx.auravindex.viewmodels.LoanViewModel
import com.elektrocodx.auravindex.viewmodels.LocalSettingViewModel
import com.elektrocodx.auravindex.viewmodels.NotificationViewModel


@Composable
fun AdminNotificationCard(
    navController: NavController,
    notificationViewModel: NotificationViewModel,
    localSettingViewModel: LocalSettingViewModel,
    notificationId: String
) {
    val notification = notificationViewModel.notification.collectAsState()
    val localSettings = localSettingViewModel.settings.collectAsState()
    androidx.compose.material3.MaterialTheme.colorScheme
    LaunchedEffect(Unit) {
        notificationViewModel.loadNotificationById(
            localSettings.value.getOrDefault(SettingKey.TOKEN.keySetting, ""),
            notificationId
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
            text = "Notification Card",
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
                text = "Notification Details",
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
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Title: ",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF572365)
                    ),
                )
                Text(
                    text = notification.value?.title.toString(),
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
                    text = buildAnnotatedString {
                        append("Message: ")
                        withStyle(SpanStyle(fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight.Normal)) {
                            append(notification.value?.message ?: "Not available")
                        }
                    },
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF572365)
                    ),
                    modifier = Modifier.padding(bottom = 12.dp)
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
                    text = "Notification type: ",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF572365)
                    ),
                )
                Text(
                    text = notification.value?.notification_type.toString(),
                    style = TextStyle(fontSize = 16.sp, color = Color.Black)
                )
            }
            Divider(color = Color.LightGray, thickness = 1.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { navController.navigate("admin_dashboard/user/${notification.value?.receiver?._id}") },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Receiver: ",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF572365)
                    ),
                )
                Text(
                    text = "${notification.value?.receiver?.name} ${notification.value?.receiver?.last_name}",
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
                    text = "Sent at: ",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF572365)
                    ),
                )
                Text(
                    text = formatUtcToLocalWithHourAndSeconds(notification.value?.createdAt),
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
                    text = "Has the user read it yet?",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF572365)
                    ),
                )
                if (notification.value?.is_read == true) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Read",
                        tint = Color(0xFF9C27B0),
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.NotInterested,
                        contentDescription = "Not read",
                        tint = Color(0xFF9C27B0),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}