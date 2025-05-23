package com.elektro24team.auravindex.mapper

import com.elektro24team.auravindex.model.Plan
import com.elektro24team.auravindex.model.local.PlanEntity

fun Plan.toEntity(): PlanEntity {
    return PlanEntity(
        __v = this.__v,
        _id = this._id,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        name = this.name,
        fixed_price = this.fixed_price,
        monthly_price = this.monthly_price,
        max_renovations_per_loan = this.max_renovations_per_loan,
        max_return_days = this.max_return_days,
        max_simultaneous_loans = this.max_simultaneous_loans
    )
}

fun PlanEntity.toDomain(): Plan {
    return Plan(
        __v = this.__v,
        _id = this._id,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        name = this.name,
        fixed_price = this.fixed_price,
        monthly_price = this.monthly_price,
        max_renovations_per_loan = this.max_renovations_per_loan,
        max_return_days = this.max_return_days,
        max_simultaneous_loans = this.max_simultaneous_loans
    )
}