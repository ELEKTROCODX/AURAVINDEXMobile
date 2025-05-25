package com.elektro24team.auravindex.model

data class LoanRequest(
    val user: User,
    val book: Book,
    val loan_status: LoanStatus,
    val return_date: String,

    )