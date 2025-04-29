package com.elektro24team.auravindex.model

//Plan Model
data class Plan(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val fixed_price: FixedPrice,
    val max_renovations_per_loan: Int,
    val max_return_days: Int,
    val max_simultaneous_loans: Int,
    val monthly_price: MonthlyPrice,
    val name: String,
    val updatedAt: String
)