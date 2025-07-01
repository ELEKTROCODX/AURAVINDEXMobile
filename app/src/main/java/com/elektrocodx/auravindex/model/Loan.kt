package com.elektrocodx.auravindex.model

data class Loan(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val updatedAt: String,
    val user: User,
    val book: Book,
    val loan_status: LoanStatus,
    val return_date: String,
    val returned_date: String?,
    val renewals: String

)