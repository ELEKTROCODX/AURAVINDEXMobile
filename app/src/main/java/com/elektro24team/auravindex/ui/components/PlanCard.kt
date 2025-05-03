package com.elektro24team.auravindex.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import com.elektro24team.auravindex.model.Plan

@Composable
fun PlanCard(plan: Plan) {
    val colors = MaterialTheme.colorScheme
    Card (
        modifier = Modifier
            .fillMaxWidth()
           /* .border(1.dp, Color.Black, RoundedCornerShape(8.dp))*/
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.onBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = plan.name,
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                color = colors.onPrimary
            )
            Text(
                text = "$${plan.fixed_price} once and $${plan.monthly_price} monthly",
                style = TextStyle(fontStyle = FontStyle.Italic),
                color = colors.onPrimary
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
        ) {
            Text(
                text = "Benefits:",
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                color = colors.onPrimary
            )
            Text(
                text = buildAnnotatedString {
                    append("You can have up to ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = colors.secondary)) {
                        append("${plan.max_simultaneous_loans} loans")
                    }

                    append(" at the same time")
                },
                color = colors.onPrimary
            )
            Text(
                text = buildAnnotatedString {
                    append("You can return the book within ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = colors.secondary)) {
                        append("${plan.max_return_days} days")
                    }
                },
                color = colors.onPrimary
            )
            Text(
                text = buildAnnotatedString {
                    append("You can renovate up to ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = colors.secondary)) {
                        append("${plan.max_renovations_per_loan} times")
                    }
                    append(" per loan")
                },
                color = colors.onPrimary
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { /* Handle button click */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "SUBSCRIBE",
                    color = colors.onPrimary
                )
            }
        }
    }
}