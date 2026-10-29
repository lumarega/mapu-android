package com.viajacalu.mapu

import com.viajacalu.mapu.navigation.MapuDestination

sealed interface MainUiState {
    data object Loading : MainUiState
    data class Ready(val startDestination: MapuDestination) : MainUiState
}
