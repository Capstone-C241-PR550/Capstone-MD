package com.bangkit.financeguard.view.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.bangkit.financeguard.data.Repository
import com.bangkit.financeguard.data.ResultState
import com.bangkit.financeguard.data.remote.response.StoreRecordResponse

class TransactionViewModel(private val repository: Repository) : ViewModel() {
    fun storeRecord(
        penghasilan: String,
        pengeluaran: String,
        tabungan: String
    ): LiveData<ResultState<StoreRecordResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val response = repository.storeRecord(penghasilan, pengeluaran, tabungan)
            emit(response) // Emit the response directly
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Error storing record"))
        }
    }
}
