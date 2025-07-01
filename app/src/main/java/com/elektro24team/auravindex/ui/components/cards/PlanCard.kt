package com.elektro24team.auravindex.ui.components.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.elektro24team.auravindex.R
import com.elektro24team.auravindex.model.Plan
import com.elektro24team.auravindex.ui.theme.BlackC
import com.elektro24team.auravindex.ui.theme.OrangeC
import com.elektro24team.auravindex.ui.theme.WhiteC
import com.elektro24team.auravindex.utils.enums.AppAction
import com.elektro24team.auravindex.utils.enums.SettingKey
import com.elektro24team.auravindex.utils.functions.formatUtcToLocalWithDate
import com.elektro24team.auravindex.utils.functions.isLoggedIn
import com.elektro24team.auravindex.utils.functions.mustBeLoggedInToast
import com.elektro24team.auravindex.viewmodels.ActivePlanViewModel
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel
import com.elektro24team.auravindex.viewmodels.NotificationViewModel

@Composable
fun PlanCard(
    plan: Plan?,
    navController: NavController,
    localSettingViewModel: LocalSettingViewModel,
    activePlanViewModel: ActivePlanViewModel,
    notificationViewModel: NotificationViewModel
) {
    MaterialTheme.colorScheme
    val context = LocalContext.current
    val localSettings = localSettingViewModel.settings.collectAsState()
    val activePlan = activePlanViewModel.activePlan.observeAsState()

    LaunchedEffect(activePlan.value) {
        if (!activePlan.value?.plan?._id.isNullOrEmpty()) {
            localSettingViewModel.saveSetting(SettingKey.ACTIVE_PLAN.keySetting, activePlan.value?.plan?._id.toString())
            localSettingViewModel.saveSetting(SettingKey.ACTIVE_PLAN_ID.keySetting, activePlan.value?._id.toString())
            localSettingViewModel.saveSetting(SettingKey.ACTIVE_PLAN_ENDING_DATE.keySetting, activePlan.value?.ending_date.toString())
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(550.dp)
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(10.dp),
    ) {
        Box(
            modifier = Modifier
                .heightIn(min = 150.dp)
                .clip(RoundedCornerShape(24.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg),
                contentDescription = "Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = plan?.name ?: "Plan",
                    fontSize = 50.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )


                Text(
                    text = "$${plan?.monthly_price ?: "--"} / month",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Divider(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(0.6f),
                    color = Color.White.copy(alpha = 0.5f)
                )

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row {
                        Icon(Icons.Filled.CollectionsBookmark, contentDescription = null, tint = Color.White)
                        Text(" Up to ${plan?.max_simultaneous_loans} simultaneous loans", color = Color.White, fontSize = 20.sp)
                    }
                    Row {
                        Icon(Icons.Filled.LockClock, contentDescription = null, tint = Color.White)
                        Text(" Return in ${plan?.max_return_days} days", color = Color.White, fontSize = 20.sp)
                    }
                    Row {
                        Icon(Icons.Filled.ConfirmationNumber, contentDescription = null, tint = Color.White)
                        Text(" ${plan?.max_renewals_per_loan} renewals per loan", color = Color.White, fontSize = 20.sp)
                    }
                }


                Spacer(modifier = Modifier.height(12.dp))

                if (activePlan.value != null && activePlan.value?.plan?._id == plan?._id) {
                    Text(
                        text = "ACTIVE PLAN (until ${formatUtcToLocalWithDate(activePlan.value?.ending_date.toString())})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color=WhiteC
                    )
                    Spacer(modifier = Modifier)
                    Row {
                        Button(
                            onClick = {
                                if (isLoggedIn(localSettings.value)) {
                                    activePlanViewModel.renewActivePlan(
                                        localSettings.value.getOrDefault(SettingKey.TOKEN.keySetting, ""),
                                        activePlan.value!!,
                                        notificationViewModel
                                    )
                                } else {
                                    mustBeLoggedInToast(context, AppAction.RENEW_ACTIVE_PLAN, navController)
                                }
                            },
                            modifier = Modifier
                                .width(110.dp)
                                .height(60.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = OrangeC,
                                contentColor = WhiteC
                            ),
                            elevation = ButtonDefaults.elevation(defaultElevation = 8.dp)
                        ) {
                            Text(
                                text = "RENEW",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = WhiteC
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = {
                                if (isLoggedIn(localSettings.value)) {
                                    activePlanViewModel.cancelActivePlan(
                                        localSettings.value.getOrDefault(SettingKey.TOKEN.keySetting, ""),
                                        activePlan.value!!,
                                        notificationViewModel
                                    )
                                    activePlanViewModel.clearViewModelData()
                                    localSettingViewModel.clearUserActivePlanSettings()
                                } else {
                                    mustBeLoggedInToast(context, AppAction.CANCEL_ACTIVE_PLAN, navController)
                                }
                            },
                            modifier = Modifier
                                .width(110.dp)
                                .height(60.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = WhiteC,
                                contentColor = BlackC
                            ),
                            elevation = ButtonDefaults.elevation(defaultElevation = 8.dp)
                        ) {
                            Text(
                                text = "CANCEL",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = BlackC
                            )
                        }

                    }
                } else {
                    Button(
                        onClick = {
                            if (isLoggedIn(localSettings.value)) {
                                activePlanViewModel.createActivePlan(
                                    token = localSettings.value.getOrDefault(SettingKey.TOKEN.keySetting, ""),
                                    userId = localSettings.value.getOrDefault(SettingKey.ID.keySetting, ""),
                                    plan = plan!!,
                                    notificationViewModel
                                )
                            } else {
                                mustBeLoggedInToast(context, AppAction.SUBSCRIBE_TO_PLAN, navController)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = OrangeC,
                            contentColor = WhiteC
                        ),
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 8.dp
                        )
                    ) {
                        Text(
                            text = "SUBSCRIBE",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = WhiteC
                        )
                    }
                }
            }
        }
    }
}
