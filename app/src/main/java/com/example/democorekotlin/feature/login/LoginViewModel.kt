package com.example.democorekotlin.feature.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.api.ApiResult
import com.example.democorekotlin.LoginUiState
import com.example.democorekotlin.network.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState

    var phone by mutableStateOf("0865831896")
    var password by mutableStateOf("123456")

    fun onLogin() {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            when (val result = repository.loginApi(phone, password)) {
                is ApiResult.Success -> {
                    _uiState.value = LoginUiState.Success(result.value)
                }
                is ApiResult.Failure -> {
                    _uiState.value = LoginUiState.Error(
                        "${result.message} + ${result.errorCode}" ?: "Có lỗi xảy ra"
                    )
                }
            }
        }
    }
}