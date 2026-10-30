package com.viajacalu.mapu.core.auth.data

import com.viajacalu.mapu.core.auth.domain.AuthRepository
import com.viajacalu.mapu.core.model.User
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource
) : AuthRepository {

    override fun isLoggedIn(): Boolean = authDataSource.getCurrentUser() != null

    override suspend fun signInWithGoogle(idToken: String): Result<User> =
        try {
            Result.success(authDataSource.signInWithGoogle(idToken))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }

    override fun signOut() {
        authDataSource.signOut()
    }
}
