package com.viajacalu.mapu.feature.auth.presentation

sealed interface LoginUiState {
    data object Idle : LoginUiState
    data object Loading : LoginUiState
    data object LoginSuccess : LoginUiState
    data class Error(val error: LoginError) : LoginUiState
}

enum class LoginError {
    NoGoogleAccount,
    PlayServicesUpdateRequired,
    PlayServicesUnavailable,
    SignInFailed
}
