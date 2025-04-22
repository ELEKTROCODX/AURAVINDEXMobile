package com.elektro24team.auravindex.model

data class Plan(
    val id: String,
    val name: String,
    val fixedPrice: Float,
    val monthlyPrice: Float,
    val maxSimultaneousLoans: Int,
    val maxReturnDays: Int,
    val maxRenovationsPerLoan: Int
)