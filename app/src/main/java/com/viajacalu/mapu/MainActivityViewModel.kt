package com.viajacalu.mapu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viajacalu.mapu.navigation.MapuDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// isLoggedIn is a mock until the login repository exists
class MainActivityViewModel(
    private val isLoggedIn: Boolean = false
) : ViewModel() {
    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            val startDestination = if (isLoggedIn) MapuDestination.Home else MapuDestination.Login
            _uiState.value = MainUiState.Ready(startDestination)
        }
    }
}
