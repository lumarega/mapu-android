package com.viajacalu.mapu

sealed interface SessionState {
    data object Checking : SessionState
    data object SignedIn : SessionState
    data object SignedOut : SessionState
}
