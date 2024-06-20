package com.bangkit.financeguard.view.updatetransaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.bangkit.financeguard.data.Repository
import com.bangkit.financeguard.data.ResultState
import com.bangkit.financeguard.data.remote.response.UpdateRecordResponse

class UpdateTransactionViewModel(private val repository: Repository) : ViewModel() {
    fun updateRecord(
        id: Int,
        penghasilan: String,
        pengeluaran: String,
        tabungan: String
    ): LiveData<ResultState<UpdateRecordResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val response = repository.updateRecord(id, penghasilan, pengeluaran, tabungan)
            emit(ResultState.Success(response))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Error updating record"))
        }
    }
}
