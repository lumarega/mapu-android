package com.viajacalu.mapu.feature.launch.presentation

sealed interface LaunchUiEvent {
    data object LoggedIn : LaunchUiEvent
    data object LoggedOut : LaunchUiEvent
}