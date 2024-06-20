package com.bangkit.financeguard.data.remote.retrofit

import com.bangkit.financeguard.data.remote.response.DeleteTransactionResponse
import com.bangkit.financeguard.data.remote.response.ListTransactionItem
import com.bangkit.financeguard.data.remote.response.LoginResponse
import com.bangkit.financeguard.data.remote.response.LogoutResponse
import com.bangkit.financeguard.data.remote.response.PagingResponse
import com.bangkit.financeguard.data.remote.response.ProfileUpdateResponse
import com.bangkit.financeguard.data.remote.response.RegisterResponse
import com.bangkit.financeguard.data.remote.response.ShowSpesificRecordResponse
import com.bangkit.financeguard.data.remote.response.StoreRecordResponse
import com.bangkit.financeguard.data.remote.response.TransactionResponse
import com.bangkit.financeguard.data.remote.response.UpdateRecordResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("full_name") name: String,
        @Field("username") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("username") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @POST("logout")
    suspend fun logout(
        @Header("Authorization") authorization: String
    ): LogoutResponse

    @Multipart
    @POST("user")
    suspend fun updateProfile(
        @Header("Authorization") authorization: String,
        @Part("name") name: RequestBody?,
        @Part photo: MultipartBody.Part?
    ): ProfileUpdateResponse

    @FormUrlEncoded
    @POST("transaction/create")
    suspend fun createTransaction(
        @Header("Authorization") authorization: String,
        @Field("date") date: String,
        @Field("amount") amount: String,
        @Field("description") description: String
    ): TransactionResponse

    @DELETE("transactions/{id}")
    suspend fun deleteTransaction(
        @Header("Authorization") authHeader: String,
        @Path("id") id: Int
    ): DeleteTransactionResponse

    @GET("transactions")
    suspend fun getTransactions(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): PagingResponse<ListTransactionItem>

    @FormUrlEncoded
    @POST("records")
    suspend fun storeRecord(
        @Header("Authorization") authorization: String,
        @Field("penghasilan_bulanan") penghasilan: String,
        @Field("pengeluaran_bulanan") pengeluaran: String,
        @Field("tabungan_bulanan") tabungan: String
    ): StoreRecordResponse

    @FormUrlEncoded
    @PUT("records/{id}")
    suspend fun updateRecord(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int,
        @Field("penghasilan_bulanan") penghasilan: String,
        @Field("pengeluaran_bulanan") pengeluaran: String,
        @Field("tabungan_bulanan") tabungan: String
    ): UpdateRecordResponse

    @GET("records")
    suspend fun fetchSpecificRecord(
        @Header("Authorization") authorization: String
    ): ShowSpesificRecordResponse

}
