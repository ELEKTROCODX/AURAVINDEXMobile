package com.elektro24team.auravindex.model.api

import com.elektro24team.auravindex.model.LoanStatus

data class LoanRequest(
    val user: String,
    val book: String,
    val loan_status: LoanStatus,
    val return_date: String,

    )