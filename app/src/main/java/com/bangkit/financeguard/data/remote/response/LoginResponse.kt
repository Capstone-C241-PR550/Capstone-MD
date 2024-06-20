package com.bangkit.financeguard.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
	@SerializedName("type")
	val type: String? = null,

	@SerializedName("token")
	val token: String? = null
)
