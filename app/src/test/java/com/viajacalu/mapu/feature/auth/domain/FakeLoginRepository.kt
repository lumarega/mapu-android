package com.viajacalu.mapu.feature.auth.domain

class FakeLoginRepository(
    var loggedIn: Boolean = false,
    var signInError: Exception? = null
) : LoginRepository {

    var lastIdToken: String? = null
        private set

    override fun isLoggedIn(): Boolean = loggedIn

    override suspend fun signInWithGoogle(idToken: String): Result<AuthUser> {
        lastIdToken = idToken
        signInError?.let { return Result.failure(it) }

        loggedIn = true
        return Result.success(
            AuthUser(
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
