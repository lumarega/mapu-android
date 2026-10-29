package com.viajacalu.mapu.feature.launch.presentation

sealed interface LaunchNavigation {
    data object Home : LaunchNavigation
    data object Login : LaunchNavigation
}