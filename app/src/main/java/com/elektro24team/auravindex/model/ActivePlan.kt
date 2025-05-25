package com.elektro24team.auravindex.model

data class ActivePlan(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val updatedAt: String,
    val user: User,
    val plan: Plan,
    val planStatus: PlanStatus,
    val endingDate: String?,
    val finishedDate: String?
)