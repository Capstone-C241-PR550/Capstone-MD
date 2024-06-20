package com.bangkit.financeguard.di

import android.content.Context
import com.bangkit.financeguard.data.pref.UserPreference
import com.bangkit.financeguard.data.pref.dataStore
import com.bangkit.financeguard.data.Repository
import com.bangkit.financeguard.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(pref, apiService)
    }
}
