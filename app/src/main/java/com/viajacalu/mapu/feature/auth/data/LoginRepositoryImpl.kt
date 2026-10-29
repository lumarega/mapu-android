package com.viajacalu.mapu.feature.auth.data

import com.viajacalu.mapu.feature.auth.domain.AuthUser
import com.viajacalu.mapu.feature.auth.domain.LoginRepository
import kotlin.coroutines.cancellation.CancellationException

class LoginRepositoryImpl(
    private val authDataSource: AuthDataSource
) : LoginRepository {

    override fun isLoggedIn(): Boolean = authDataSource.getCurrentUser() != null

    override suspend fun signInWithGoogle(idToken: String): Result<AuthUser> =
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
