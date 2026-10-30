package com.viajacalu.mapu.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viajacalu.mapu.core.auth.domain.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun onSignInStarted() {
        _uiState.value = LoginUiState.Loading
    }

    fun onGoogleSignInResult(result: GoogleSignInResult) {
        when (result) {
            is GoogleSignInResult.Success -> signInWithGoogle(result.idToken)
            GoogleSignInResult.Cancelled -> _uiState.value = LoginUiState.Idle
            GoogleSignInResult.NoGoogleAccount -> showError(LoginError.NoGoogleAccount)
            GoogleSignInResult.PlayServicesUpdateRequired -> showError(LoginError.PlayServicesUpdateRequired)
            GoogleSignInResult.PlayServicesUnavailable -> showError(LoginError.PlayServicesUnavailable)
            is GoogleSignInResult.Failure -> showError(LoginError.SignInFailed)
        }
    }

    fun onErrorDismissed() {
        _uiState.value = LoginUiState.Idle
    }

    private fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            authRepository.signInWithGoogle(idToken)
                .onSuccess { _uiState.value = LoginUiState.LoginSuccess }
                .onFailure { showError(LoginError.SignInFailed) }
        }
    }

    private fun showError(error: LoginError) {
        _uiState.value = LoginUiState.Error(error)
    }
}
