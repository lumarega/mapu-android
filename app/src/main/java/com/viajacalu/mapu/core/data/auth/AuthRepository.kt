package com.viajacalu.mapu.core.data.auth

import com.viajacalu.mapu.core.model.User

interface AuthRepository {
    fun isLoggedIn(): Boolean

    fun getCurrentUser(): User?

    suspend fun signInWithGoogle(idToken: String): Result<User>

    suspend fun signOut()
}
