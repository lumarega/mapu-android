package com.viajacalu.mapu.feature.login

sealed interface LoginUiState {
    data object Idle : LoginUiState
    data object Loading : LoginUiState
    data object LoginSuccess : LoginUiState
    data class Error(val error: LoginError) : LoginUiState
}

enum class LoginError {
    NoGoogleAccount,
    NoInternet,
    PlayServicesUpdateRequired,
    PlayServicesUnavailable,
    SignInFailed
}
