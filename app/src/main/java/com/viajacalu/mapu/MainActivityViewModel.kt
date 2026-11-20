package com.viajacalu.mapu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viajacalu.mapu.core.data.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Checking)
    val sessionState = _sessionState.asStateFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            _sessionState.value = if (authRepository.isLoggedIn()) SessionState.SignedIn else SessionState.SignedOut
        }
    }
}
