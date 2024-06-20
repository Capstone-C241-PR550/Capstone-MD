package com.bangkit.financeguard.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.financeguard.data.Repository
import com.bangkit.financeguard.data.ResultState
import com.bangkit.financeguard.data.pref.UserModel
import com.bangkit.financeguard.data.remote.response.LoginResponse
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.firstOrNull

class LoginViewModel(private val repository: Repository) : ViewModel() {
    private val _isButtonEnabled = MutableLiveData<Boolean>()
    val isButtonEnabled: LiveData<Boolean>
        get() = _isButtonEnabled

    init {
        _isButtonEnabled.value = false
    }

    fun checkText(username: String, password: String) {
        val isButtonEnabled = username.isNotEmpty() && password.length >= 8
        _isButtonEnabled.value = isButtonEnabled
    }

    private val _loginResult = MutableLiveData<ResultState<LoginResponse>>()
    val loginResult: LiveData<ResultState<LoginResponse>> = _loginResult

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = ResultState.Loading
            when (val result = repository.login(username, password)) {
                is ResultState.Success -> {
                    val userModel = UserModel(
                        accessToken = result.data.token ?: "",
                        isLogin = true
                    )
                    repository.saveSession(userModel) {
                        _loginResult.value = ResultState.Success(result.data)
                    }
                }
                is ResultState.Error -> _loginResult.value = result
                else -> {}
            }
        }
    }
}
