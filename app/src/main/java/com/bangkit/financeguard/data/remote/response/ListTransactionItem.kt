package com.bangkit.financeguard.data.remote.response

data class ListTransactionItem(
    val id: Int,
    val title: String,
    val amount: Double,
    val date: String
)
