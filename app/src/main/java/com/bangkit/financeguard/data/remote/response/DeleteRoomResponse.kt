package com.bangkit.financeguard.data.remote.response

import com.google.gson.annotations.SerializedName

data class DeleteRoomResponse(

	@field:SerializedName("data")
	val data: Int,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)
