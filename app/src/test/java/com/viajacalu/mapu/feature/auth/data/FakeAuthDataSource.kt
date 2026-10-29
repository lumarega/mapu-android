package com.viajacalu.mapu.feature.auth.data

import com.viajacalu.mapu.feature.auth.domain.AuthUser

class FakeAuthDataSource(
    var signedInUser: AuthUser? = null,
    var signInError: Exception? = null
) : AuthDataSource {

    var lastIdToken: String? = null
        private set

    override fun getCurrentUser(): AuthUser? = signedInUser

    override suspend fun signInWithGoogle(idToken: String): AuthUser {
        lastIdToken = idToken
        signInError?.let { throw it }
        return AuthUser(
            id = "user-id",
            name = "Test User",
            email = "test@example.com",
            photoUrl = null
        ).also { signedInUser = it }
    }

    override fun signOut() {
        signedInUser = null
    }
}
