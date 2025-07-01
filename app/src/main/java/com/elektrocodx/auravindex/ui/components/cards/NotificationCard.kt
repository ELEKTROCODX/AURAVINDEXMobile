package com.elektrocodx.auravindex.ui.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elektrocodx.auravindex.model.Notification
import com.elektrocodx.auravindex.ui.theme.OrangeC
import com.elektrocodx.auravindex.ui.theme.PurpleC
import com.elektrocodx.auravindex.utils.enums.SettingKey
import com.elektrocodx.auravindex.utils.functions.APIerrorHandlers.ObserveError
import com.elektrocodx.auravindex.utils.functions.APIerrorHandlers.ObserveInsufficientPermissions
import com.elektrocodx.auravindex.utils.functions.APIerrorHandlers.ObserveSuccess
import com.elektrocodx.auravindex.utils.functions.APIerrorHandlers.ObserveTokenExpiration
import com.elektrocodx.auravindex.utils.functions.formatUtcToLocalWithHour
import com.elektrocodx.auravindex.viewmodels.LocalSettingViewModel
import com.elektrocodx.auravindex.viewmodels.NotificationViewModel

@Composable
fun NotificationCard(notification: Notification, navController: NavController, notificationViewModel: NotificationViewModel, localSettingViewModel: LocalSettingViewModel) {
    val localSettings by localSettingViewModel.settings.collectAsState()
    val cardBackgroundColor = if (notification.is_read) {
        MaterialTheme.colorScheme.surfaceContainerLow
    } else {
        MaterialTheme.colorScheme.surface
    }

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
            .padding(horizontal = 5.dp, vertical = 0.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(15.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = notification.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = OrangeC,
                    modifier = Modifier.weight(2f)
                )
                Text(
                    text = notification.notification_type,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelSmall.copy(fontStyle = FontStyle.Italic, textAlign = TextAlign.End),
                    color = PurpleC
                )
            }
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = buildAnnotatedString {
                    append(notification.message)
                    withStyle(SpanStyle(fontStyle = FontStyle.Italic, color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                        append(" - ${formatUtcToLocalWithHour(notification.createdAt)}")
                    }
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            if(!notification.is_read){
                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        notificationViewModel.markNotificationAsRead(
                            token = localSettings[SettingKey.TOKEN.keySetting].toString(),
                            notificationId = notification._id
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = MaterialTheme.shapes.small,
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.RemoveRedEye,
                        contentDescription = "Mark as read",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(
                        text = "Mark as read",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}