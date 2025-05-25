package com.elektro24team.auravindex.utils.functions

import java.text.SimpleDateFormat
import java.util.*

fun formatUtcToLocalWithHour(input: String?): String {
    if(!input.isNullOrEmpty()) {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        outputFormat.timeZone = TimeZone.getTimeZone("UTC-6")
        val date = inputFormat.parse(input)
        return outputFormat.format(date!!)
    } else {
        return "Unknown"
    }
}
fun formatUtcToLocalWithDate(input: String?): String {
    if(!input.isNullOrEmpty()) {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        outputFormat.timeZone = TimeZone.getTimeZone("UTC-6")

        val date = inputFormat.parse(input)
        return outputFormat.format(date!!)
    } else {
        return "Unknown"
    }
}