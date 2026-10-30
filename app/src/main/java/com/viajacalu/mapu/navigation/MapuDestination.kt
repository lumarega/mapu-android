package com.viajacalu.mapu.navigation

import kotlinx.serialization.Serializable

sealed interface MapuDestination {
    @Serializable
    data object Login : MapuDestination

    @Serializable
    data object Guides : MapuDestination

    @Serializable
    data object Profile : MapuDestination
}
