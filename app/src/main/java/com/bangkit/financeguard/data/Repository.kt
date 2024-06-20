package com.bangkit.financeguard.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.bangkit.financeguard.data.pref.UserModel
import com.bangkit.financeguard.data.pref.UserPreference
import com.bangkit.financeguard.data.remote.response.*
import com.bangkit.financeguard.data.remote.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.File
import java.net.SocketTimeoutException

class Repository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    // Fungsi untuk menyimpan sesi pengguna
    suspend fun saveSession(user: UserModel, onComplete: () -> Unit) {
        userPreference.saveSession(user)
        onComplete.invoke()
    }

    // Fungsi untuk mendapatkan sesi pengguna
    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    // Fungsi untuk registrasi pengguna
    suspend fun register(
        name: String,
        email: String,
        password: String
    ): ResultState<RegisterResponse> {
        return try {
            val response = apiService.register(name, email, password)
            if (response.status == "success") {
                ResultState.Success(response)
            } else {
                Log.e("Repository", "Registration failed: ${response.message}")
                ResultState.Error(response.message ?: "Registration failed")
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            Log.e("Repository", "HttpException: ${e.message}")
            ResultState.Error(errorResponse.message ?: "Registration failed")
        } catch (e: SocketTimeoutException) {
            Log.e("Repository", "SocketTimeoutException: ${e.message}")
            ResultState.Error("Timeout: Server tidak merespons dalam waktu yang ditentukan.")
        } catch (e: Exception) {
            Log.e("Repository", "Exception: ${e.message}")
            ResultState.Error("Registration failed: ${e.message}")
        }
    }

    // Fungsi untuk login pengguna
    suspend fun login(email: String, password: String): ResultState<LoginResponse> {
        return try {
            val response: Response<LoginResponse> = apiService.login(email, password)
            if (response.isSuccessful) {
                response.body()?.let {
                    ResultState.Success(it)
                } ?: ResultState.Error("Login failed: No response body")
            } else {
                ResultState.Error("Login failed: ${response.code()}")
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            ResultState.Error(errorResponse.message ?: "Login failed http")
        } catch (e: SocketTimeoutException) {
            ResultState.Error("Timeout: Server tidak merespons dalam waktu yang ditentukan.")
        } catch (e: Exception) {
            ResultState.Error("Login failed exc")
        }
    }

    // Fungsi untuk logout pengguna
    suspend fun logout() {
        userPreference.logout()
    }

    // Fungsi untuk memperbarui profil pengguna
    fun updateProfile(
        name: String?, imageFile: File?
    ): LiveData<ResultState<ProfileUpdateResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val user = userPreference.getSession().first()
            val authorization = "Bearer ${user.accessToken}"

            val namePart = name?.toRequestBody("text/plain".toMediaType())
            val photoPart = imageFile?.let {
                val requestImageFile = it.asRequestBody("image/jpeg".toMediaType())
                MultipartBody.Part.createFormData("photo", it.name, requestImageFile)
            }

            val response = apiService.updateProfile(authorization, namePart, photoPart)
            if (!response.error) {
                userPreference.saveSession(
                    UserModel(
                        accessToken = user.accessToken,
                        isLogin = true
                    )
                )
                emit(ResultState.Success(response))
            } else {
                emit(ResultState.Error(response.message))
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "An error occurred"))
        }
    }

    // Fungsi untuk membuat transaksi
    fun createTransaction(
        date: String,
        amount: String,
        description: String
    ): LiveData<ResultState<TransactionResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val user = userPreference.getSession().first()
            val authorization = "Bearer ${user.accessToken}"
            val response = apiService.createTransaction(authorization, date, amount, description)
            if (!response.error) {
                emit(ResultState.Success(response))
            } else {
                emit(ResultState.Error(response.message))
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Error creating transaction"))
        }
    }

    // Fungsi untuk menyimpan transaksi pendapatan
    fun saveIncomeTransaction(date: String, amount: String, description: String): LiveData<ResultState<TransactionResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val user = userPreference.getSession().first()
            val authorization = "Bearer ${user.accessToken}"
            val response = apiService.createTransaction(authorization, date, amount, description)
            if (!response.error) {
                emit(ResultState.Success(response))
            } else {
                emit(ResultState.Error(response.message))
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Error saving income transaction"))
        }
    }

    // Fungsi untuk menyimpan transaksi pengeluaran
    fun saveExpenseTransaction(date: String, amount: String, description: String): LiveData<ResultState<TransactionResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val user = userPreference.getSession().first()
            val authorization = "Bearer ${user.accessToken}"
            val response = apiService.createTransaction(authorization, date, amount, description)
            if (!response.error) {
                emit(ResultState.Success(response))
            } else {
                emit(ResultState.Error(response.message))
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Error saving expense transaction"))
        }
    }

    // Fungsi untuk menghapus transaksi
    suspend fun deleteTransaction(id: Int): ResultState<DeleteTransactionResponse> {
        return try {
            val user = userPreference.getSession().first()
            val authorization = "Bearer ${user.accessToken}"
            val response = apiService.deleteTransaction(authorization, id)
            if (!response.error) {
                ResultState.Success(response)
            } else {
                ResultState.Error(response.message)
            }
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Error deleting transaction")
        }
    }

    suspend fun fetchAndSaveSpecificRecord(): ResultState<ShowSpesificRecordResponse> {
        return try {
            val user = userPreference.getSession().first()
            val authorization = "Bearer ${user.accessToken}"
            val response = apiService.fetchSpecificRecord(authorization)
            if (response.code == 200) {
                val record = response.data?.firstOrNull()
                if (record != null) {
                    val updatedUser = user.copy(
                        penghasilanBulanan = record.penghasilanBulanan?.toInt() ?: 0,
                        pengeluaranBulanan = record.pengeluaranBulanan?.toInt() ?: 0,
                        tabunganBulanan = record.tabunganBulanan?.toInt() ?: 0,
                        label = record.label ?: ""
                    )
                    userPreference.saveSession(updatedUser)
                    Log.d("Repository", "Data updated: $updatedUser")
                    ResultState.Success(response)
                } else {
                    ResultState.Error("No records found")
                }
            } else {
                ResultState.Error(response.status ?: "Unknown error")
            }
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "An error occurred")
        }
    }


    suspend fun storeRecord(penghasilan: String, pengeluaran: String, tabungan: String): ResultState<StoreRecordResponse> {
        return try {
            val user = userPreference.getSession().first()
            val authorization = "Bearer ${user.accessToken}"
            val response = apiService.storeRecord(authorization, penghasilan, pengeluaran, tabungan)
            if (response.code == 200) {
                ResultState.Success(response)
            } else {
                ResultState.Error(response.status ?: "Unknown error")
            }
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun updateRecord(id: Int, penghasilan: String, pengeluaran: String, tabungan: String): UpdateRecordResponse {
        val user = userPreference.getSession().first()
        val authorization = "Bearer ${user.accessToken}"
        return apiService.updateRecord(authorization, id, penghasilan, pengeluaran, tabungan)
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(userPreference: UserPreference, apiService: ApiService): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(userPreference, apiService)
            }.also { instance = it }
    }
}
