package com.viajacalu.mapu.feature.auth.presentation

sealed interface GoogleSignInResult {
    data class Success(val idToken: String) : GoogleSignInResult
    data object Cancelled : GoogleSignInResult
    data object NoGoogleAccount : GoogleSignInResult
    data object PlayServicesUpdateRequired : GoogleSignInResult
    data object PlayServicesUnavailable : GoogleSignInResult
    data class Failure(val error: Exception) : GoogleSignInResult
}
