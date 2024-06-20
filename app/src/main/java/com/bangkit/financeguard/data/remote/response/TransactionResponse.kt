package com.bangkit.financeguard.data.remote.response

import com.google.gson.annotations.SerializedName

data class TransactionResponse(
    @SerializedName("error")
    val error: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("transaction")
    val transaction: TransactionDetail
)

data class TransactionDetail(
    @SerializedName("id")
    val id: Int,

    @SerializedName("date")
    val date: String,

    @SerializedName("category")
    val category: Int,

    @SerializedName("amount")
    val amount: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("type")
    val type: String
)
