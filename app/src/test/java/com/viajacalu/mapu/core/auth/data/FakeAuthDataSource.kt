package com.viajacalu.mapu.core.auth.data

import com.viajacalu.mapu.core.model.User

class FakeAuthDataSource(
    var signedInUser: User? = null,
    var signInError: Exception? = null
) : AuthDataSource {

    var lastIdToken: String? = null
        private set

    override fun getCurrentUser(): User? = signedInUser

    override suspend fun signInWithGoogle(idToken: String): User {
        lastIdToken = idToken
        signInError?.let { throw it }
        return User(
            id = "user-id",
            name = "Test User",
            email = "test@example.com",
            photoUrl = null
        ).also { signedInUser = it }
    }

    override suspend fun signOut() {
        signedInUser = null
    }
}
