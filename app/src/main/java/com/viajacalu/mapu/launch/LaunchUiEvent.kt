package com.viajacalu.mapu.launch

sealed interface LaunchUiEvent {
    data object LoggedIn : LaunchUiEvent
    data object LoggedOut : LaunchUiEvent
}