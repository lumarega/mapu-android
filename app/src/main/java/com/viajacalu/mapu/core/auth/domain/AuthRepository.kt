package com.viajacalu.mapu.core.auth.domain

import com.viajacalu.mapu.core.model.User

interface AuthRepository {
    fun isLoggedIn(): Boolean

    suspend fun signInWithGoogle(idToken: String): Result<User>

    fun signOut()
}
