package com.viajacalu.mapu.feature.auth.data

import com.viajacalu.mapu.feature.auth.domain.AuthUser

interface AuthDataSource {
    fun getCurrentUser(): AuthUser?

    suspend fun signInWithGoogle(idToken: String): AuthUser

    fun signOut()
}
