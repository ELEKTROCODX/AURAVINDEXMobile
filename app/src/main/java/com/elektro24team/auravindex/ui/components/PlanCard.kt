package com.elektro24team.auravindex.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elektro24team.auravindex.model.Plan
import com.elektro24team.auravindex.ui.theme.PurpleC

@Composable
fun PlanCard(plan: Plan) {
    val colors = MaterialTheme.colorScheme

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
                    text = plan.name,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PurpleC
                )
            }

            HorizontalDivider(thickness = 1.dp, color = colors.outlineVariant)

            // Beneficios
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Suscription: $${plan.fixed_price}",
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    color = colors.primary
                )

                Text(
                    text = "Monthly: $${plan.monthly_price}",
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
                            append("${plan.max_simultaneous_loans} simultaneous loans")
                        }
                    },
                    fontSize = 14.sp,
                    color = colors.onSurface
                )

                Text(
                    text = buildAnnotatedString {
                        append("Return books in up to ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = colors.primary)) {
                            append("${plan.max_return_days} days")
                        }
                    },
                    fontSize = 14.sp,
                    color = colors.onSurface
                )

                Text(
                    text = buildAnnotatedString {
                        append("Renovate up to ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = colors.primary)) {
                            append("${plan.max_renovations_per_loan} times")
                        }
                        append(" per loan")
                    },
                    fontSize = 14.sp,
                    color = colors.onSurface
                )
            }

            // Botón
            Button(
                onClick = { /* TODO: Acción de suscripción */ },
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