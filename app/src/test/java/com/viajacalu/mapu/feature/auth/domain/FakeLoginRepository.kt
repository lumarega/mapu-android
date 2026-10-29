package com.viajacalu.mapu.feature.auth.domain

class FakeLoginRepository(
    var loggedIn: Boolean = false
) : LoginRepository {

    override fun isLoggedIn(): Boolean = loggedIn

    override suspend fun signInWithGoogle(idToken: String): Result<AuthUser> {
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
