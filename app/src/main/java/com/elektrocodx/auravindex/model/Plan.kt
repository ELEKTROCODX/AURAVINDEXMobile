package com.elektrocodx.auravindex.model

data class Plan(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val fixed_price: String,
    val max_renewals_per_loan: Int,
    val max_return_days: Int,
    val max_simultaneous_loans: Int,
    val monthly_price: String,
    val name: String,
    val updatedAt: String
)