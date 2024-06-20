package com.bangkit.financeguard.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.financeguard.data.Repository
import com.bangkit.financeguard.data.ResultState
import com.bangkit.financeguard.data.pref.UserModel
import com.bangkit.financeguard.data.remote.response.ShowSpesificRecordResponse
import com.bangkit.financeguard.data.remote.response.SpesificRecordItem
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {
    private val _currentRecord = MutableLiveData<SpesificRecordItem?>()
    val currentRecord: LiveData<SpesificRecordItem?> get() = _currentRecord

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun fetchAndSaveSpecificRecord() {
        viewModelScope.launch {
            when (val result = repository.fetchAndSaveSpecificRecord()) {
                is ResultState.Success -> {
                    _currentRecord.value = result.data?.data?.firstOrNull()
                }
                is ResultState.Error -> {
                    // Handle error
                }
                ResultState.Loading -> {
                    // Handle loading state if necessary
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}
