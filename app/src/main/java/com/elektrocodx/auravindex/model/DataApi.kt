package com.elektrocodx.auravindex.model

//Is the JSON data from the API, separated in data and pagination
data class ApiResponse<T>(
    val data: T,
    val pagination: Pagination
)

data class Pagination(
    val page: Int,
    val total: Int
)

