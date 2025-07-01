package com.elektrocodx.auravindex.model.api

data class LoanRequest(
    val user: String,
    val book: String,
    val loan_status: String,
    val return_date: String,

    )