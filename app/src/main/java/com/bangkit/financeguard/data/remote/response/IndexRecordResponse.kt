package com.bangkit.financeguard.data.remote.response

import com.google.gson.annotations.SerializedName

data class IndexRecordResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: List<IndexRecordItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class IndexRecordItem(

	@field:SerializedName("pengeluaran_bulanan")
	val pengeluaranBulanan: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("tabungan_bulanan")
	val tabunganBulanan: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("penghasilan_bulanan")
	val penghasilanBulanan: Int? = null,

	@field:SerializedName("label")
	val label: String? = null
)
