package com.viajacalu.mapu.core.testing

import com.viajacalu.mapu.core.auth.domain.AuthRepository
import com.viajacalu.mapu.core.model.User

val testUser = User(
    id = "user-id",
    name = "Test User",
    email = "test@example.com",
    photoUrl = null
)

class FakeAuthRepository(
    var signedInUser: User? = null,
    var signInError: Exception? = null
) : AuthRepository {

    var lastIdToken: String? = null
        private set

    override fun isLoggedIn(): Boolean = signedInUser != null

    override fun getCurrentUser(): User? = signedInUser

    override suspend fun signInWithGoogle(idToken: String): Result<User> {
        lastIdToken = idToken
        signInError?.let { return Result.failure(it) }

        signedInUser = testUser
        return Result.success(testUser)
    }

    override suspend fun signOut() {
        signedInUser = null
    }
}
