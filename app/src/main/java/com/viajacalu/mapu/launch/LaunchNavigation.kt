package com.viajacalu.mapu.launch

sealed interface LaunchNavigation {
    data object Home : LaunchNavigation
    data object Login : LaunchNavigation
}