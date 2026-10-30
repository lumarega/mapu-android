package com.viajacalu.mapu.core.testing

import com.viajacalu.mapu.core.auth.domain.AuthRepository
import com.viajacalu.mapu.core.model.User

class FakeAuthRepository(
    var loggedIn: Boolean = false,
    var signInError: Exception? = null
) : AuthRepository {

    var lastIdToken: String? = null
        private set

    override fun isLoggedIn(): Boolean = loggedIn

    override suspend fun signInWithGoogle(idToken: String): Result<User> {
        lastIdToken = idToken
        signInError?.let { return Result.failure(it) }

        loggedIn = true
        return Result.success(
            User(
                id = "user-id",
                name = "Test User",
                email = "test@example.com",
                photoUrl = null
            )
        )
    }

    override fun signOut() {
        loggedIn = false
    }
}
