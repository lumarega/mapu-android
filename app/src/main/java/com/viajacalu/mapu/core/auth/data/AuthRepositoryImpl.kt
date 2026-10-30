package com.viajacalu.mapu.core.auth.data

import com.viajacalu.mapu.core.auth.domain.AuthRepository
import com.viajacalu.mapu.core.model.User
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val credentialsDataSource: CredentialsDataSource
) : AuthRepository {

    override fun isLoggedIn(): Boolean = authDataSource.getCurrentUser() != null

    override fun getCurrentUser(): User? = authDataSource.getCurrentUser()

    override suspend fun signInWithGoogle(idToken: String): Result<User> =
        try {
            Result.success(authDataSource.signInWithGoogle(idToken))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun signOut() {
        // Ends the user's Mapu session
        authDataSource.signOut()

        // Without this, the next sign in would pick the same Google account automatically, with no
        // account picker to choose a different one. It only affects the next sign in, so if it fails
        // the user is still signed out.
        try {
            credentialsDataSource.clearCredentials()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            // Ignored on purpose, the user is already signed out of Mapu
        }
    }
}
