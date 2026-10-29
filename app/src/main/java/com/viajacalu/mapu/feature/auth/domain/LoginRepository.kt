package com.viajacalu.mapu.feature.auth.domain

interface LoginRepository {
    fun isLoggedIn(): Boolean

    suspend fun signInWithGoogle(idToken: String): Result<AuthUser>

    fun signOut()
}
