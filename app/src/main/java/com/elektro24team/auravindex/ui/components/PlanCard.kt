package com.elektro24team.auravindex.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.elektro24team.auravindex.model.Plan
import com.elektro24team.auravindex.ui.theme.PurpleC
import com.elektro24team.auravindex.utils.enums.AppAction
import com.elektro24team.auravindex.utils.enums.SettingKey
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveError
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveInsufficentPermissions
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveSuccess
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveTokenExpiration
import com.elektro24team.auravindex.utils.functions.isLoggedIn
import com.elektro24team.auravindex.utils.functions.mustBeLoggedInToast
import com.elektro24team.auravindex.viewmodels.ActivePlanViewModel
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel

@Composable
fun PlanCard(
    plan: Plan?,
    navController: NavController,
    localSettingViewModel: LocalSettingViewModel,
    activePlanViewModel: ActivePlanViewModel
) {
    val colors = MaterialTheme.colorScheme
    val context = LocalContext.current
    val localSettings = localSettingViewModel.settings.collectAsState()
    val activePlan = activePlanViewModel.activePlan.observeAsState()
    LaunchedEffect(activePlan.value) {
        if(!activePlan.value?.plan?._id.isNullOrEmpty()) {
            localSettingViewModel.saveSetting(
                SettingKey.ACTIVE_PLAN.keySetting,
                activePlan.value?.plan?._id.toString()
            )
            localSettingViewModel.saveSetting(
                SettingKey.ACTIVE_PLAN_ID.keySetting,
                activePlan.value?._id.toString()
            )
        }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Título del plan
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = plan?.name.toString(),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PurpleC
                )
            }

            HorizontalDivider(thickness = 1.dp, color = colors.outlineVariant)

            // Beneficios
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Inscription fee: $${plan?.fixed_price}",
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    color = colors.primary
                )

                Text(
                    text = "Monthly price: $${plan?.monthly_price}",
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    color = colors.primary
                )

                Text(
                    text = "Benefits",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.onSurface
                )

                Text(
                    text = buildAnnotatedString {
                        append("Up to ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = colors.primary)) {
                            append("${plan?.max_simultaneous_loans} simultaneous loans")
                        }
                    },
                    fontSize = 14.sp,
                    color = colors.onSurface
                )

                Text(
                    text = buildAnnotatedString {
                        append("Return books in up to ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = colors.primary)) {
                            append("${plan?.max_return_days} days")
                        }
                    },
                    fontSize = 14.sp,
                    color = colors.onSurface
                )

                Text(
                    text = buildAnnotatedString {
                        append("Renovate up to ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = colors.primary)) {
                            append("${plan?.max_renovations_per_loan} times")
                        }
                        append(" per loan")
                    },
                    fontSize = 14.sp,
                    color = colors.onSurface
                )
            }

            // Botón
            if(localSettings.value.getOrDefault(SettingKey.ACTIVE_PLAN.keySetting, "").toString() == plan?._id) {
                /* Todo: add cancel subscription button */
                Text(
                    text = "CURRENT SUBSCRIPTION.",
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    color = colors.primary
                )
            } else {
                Button(
                    onClick = {
                        if(isLoggedIn(localSettings.value)) {
                            activePlanViewModel.createActivePlan(
                                token = localSettings.value.getOrDefault(SettingKey.TOKEN.keySetting, ""),
                                userId = localSettings.value.getOrDefault(SettingKey.ID.keySetting, ""),
                                planId = plan?._id.toString(),
                            )
                        } else {
                            mustBeLoggedInToast(context, AppAction.SUBSCRIBE_TO_PLAN, navController)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "SUBSCRIBE",
                        style = MaterialTheme.typography.labelLarge,
                        color = colors.onPrimary
                    )
                }
            }

        }
    }
}
