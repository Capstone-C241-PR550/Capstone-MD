package com.bangkit.financeguard.data.remote.response

import com.google.gson.annotations.SerializedName

data class PagingResponse<T>(
    @SerializedName("data")
    val data: List<T>,

    @SerializedName("page")
    val page: Int,

    @SerializedName("size")
    val size: Int,

    @SerializedName("total")
    val total: Int
)
