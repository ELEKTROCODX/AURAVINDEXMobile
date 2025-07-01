package com.elektrocodx.auravindex.model

data class ActivePlan(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val updatedAt: String,
    val user: User,
    val plan: Plan,
    val plan_status: PlanStatus?,
    val ending_date: String?,
    val finished_date: String?
)