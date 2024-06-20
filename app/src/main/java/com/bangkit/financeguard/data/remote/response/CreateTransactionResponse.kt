package com.bangkit.financeguard.data.remote.response

data class CreateTransactionResponse(
    val error: Boolean,
    val message: String,
    val data: TransactionData
)

data class TransactionData(
    val id: Int,
    val title: String,
    val amount: Double,
    val date: String
)
