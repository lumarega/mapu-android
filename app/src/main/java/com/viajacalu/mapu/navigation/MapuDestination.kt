package com.viajacalu.mapu.navigation

import kotlinx.serialization.Serializable

sealed interface MapuDestination {
    @Serializable
    data object Login : MapuDestination

    @Serializable
    data object Home : MapuDestination
}
