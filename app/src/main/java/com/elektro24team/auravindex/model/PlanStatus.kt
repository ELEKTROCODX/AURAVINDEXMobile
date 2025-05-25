package com.elektro24team.auravindex.model

data class PlanStatus(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val updatedAt: String,
    val loanStatus: PlanStatus

)