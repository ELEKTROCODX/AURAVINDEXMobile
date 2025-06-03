package com.elektro24team.auravindex.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.Text
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.elektro24team.auravindex.model.Notification
import com.elektro24team.auravindex.ui.theme.PurpleC
import com.elektro24team.auravindex.utils.enums.SettingKey
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveTokenExpiration
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveInsufficientPermissions
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveError
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveSuccess
import com.elektro24team.auravindex.utils.functions.formatUtcToLocalWithHour
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel
import com.elektro24team.auravindex.viewmodels.NotificationViewModel

@Composable
fun NotificationCard(notification: Notification, navController: NavController,  notificationViewModel: NotificationViewModel, localSettingViewModel: LocalSettingViewModel) {
    val localSettings by localSettingViewModel.settings.collectAsState()
    ObserveTokenExpiration(
        viewModel = notificationViewModel,
        navController = navController,
        localSettingViewModel = localSettingViewModel
    )
    ObserveInsufficientPermissions(
        viewModel = notificationViewModel,
        navController = navController
    )
    ObserveError(notificationViewModel)
    ObserveSuccess(notificationViewModel)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(10.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = notification.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = notification.notification_type,
                    modifier = Modifier.padding(end = 8.dp),
                    style = TextStyle(fontStyle = FontStyle.Italic)
                )
            }
            Text(
                text = buildAnnotatedString {
                    append("${notification.message} - ")
                    withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                        append(formatUtcToLocalWithHour(notification.createdAt))
                    }
                }
            )
            if(!notification.is_read){
                Button(
                    onClick = {
                        notificationViewModel.markNotificationAsRead(
                            token = localSettings[SettingKey.TOKEN.keySetting].toString(),
                            notificationId = notification._id
                        )
                    },
                    modifier = Modifier.height(40.dp).padding(8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = PurpleC),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.RemoveRedEye,
                        contentDescription = "Mark as read",
                        tint = Color.White,
                        modifier = Modifier.padding(end = 8.dp).size(12.dp)
                    )
                    Text(
                        text = "Mark as read",
                        color = Color.White,
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    )
                }
            }
        }
    }
}