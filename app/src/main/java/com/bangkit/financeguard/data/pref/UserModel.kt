package com.bangkit.financeguard.data.pref

data class UserModel(
    val accessToken: String,
    val isLogin: Boolean,
    val penghasilanBulanan: Int = 0,
    val pengeluaranBulanan: Int = 0,
    val tabunganBulanan: Int = 0,
    val label: String = ""
)
