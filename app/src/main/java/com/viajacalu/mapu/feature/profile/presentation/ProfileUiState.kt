package com.viajacalu.mapu.feature.profile.presentation

sealed interface ProfileUiState {
    data class SignedIn(
        val name: String?,
        val email: String?,
        val photoUrl: String?,
        val initials: String
    ) : ProfileUiState

    data object SignedOut : ProfileUiState
}
