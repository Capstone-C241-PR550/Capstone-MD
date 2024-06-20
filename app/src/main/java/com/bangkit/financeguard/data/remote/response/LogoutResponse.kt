package com.bangkit.financeguard.data.remote.response

import com.google.gson.annotations.SerializedName

data class LogoutResponse(

    @field:SerializedName("status")
    val status: Boolean,

    @field:SerializedName("message")
    val message: String
)