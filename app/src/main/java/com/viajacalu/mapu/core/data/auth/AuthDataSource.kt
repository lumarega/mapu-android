package com.viajacalu.mapu.core.data.auth

import com.viajacalu.mapu.core.model.User

interface AuthDataSource {
    fun getCurrentUser(): User?

    suspend fun signInWithGoogle(idToken: String): User

    suspend fun signOut()
}
