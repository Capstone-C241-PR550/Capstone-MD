package com.bangkit.financeguard.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = user.accessToken
            preferences[IS_LOGIN_KEY] = user.isLogin
            preferences[PENGHASILAN_BULANAN_KEY] = user.penghasilanBulanan
            preferences[PENGELUARAN_BULANAN_KEY] = user.pengeluaranBulanan
            preferences[TABUNGAN_BULANAN_KEY] = user.tabunganBulanan
            preferences[LABEL_KEY] = user.label
        }
    }

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[TOKEN_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false,
                preferences[PENGHASILAN_BULANAN_KEY] ?: 0,
                preferences[PENGELUARAN_BULANAN_KEY] ?: 0,
                preferences[TABUNGAN_BULANAN_KEY] ?: 0,
                preferences[LABEL_KEY] ?: ""
            )
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")
        private val PENGHASILAN_BULANAN_KEY = intPreferencesKey("penghasilanBulanan")
        private val PENGELUARAN_BULANAN_KEY = intPreferencesKey("pengeluaranBulanan")
        private val TABUNGAN_BULANAN_KEY = intPreferencesKey("tabunganBulanan")
        private val LABEL_KEY = stringPreferencesKey("label")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
