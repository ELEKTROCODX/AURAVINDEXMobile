package com.elektro24team.auravindex.model

data class LoanRequest(
    val user: User,
    val book: Book,
    val loanStatus: LoanStatus,
    val return_date: String,

)