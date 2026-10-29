package com.viajacalu.mapu.feature.auth.data

import com.viajacalu.mapu.feature.auth.domain.AuthUser
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginRepositoryImplTest {

    private val loggedInUser = AuthUser(
        id = "user-id",
        name = "Test User",
        email = "test@example.com",
        photoUrl = null
    )

    @Test
    fun `when there is a current user isLoggedIn returns true`() {
        val repository = LoginRepositoryImpl(FakeAuthDataSource(signedInUser = loggedInUser))

        assertTrue(repository.isLoggedIn())
    }

    @Test
    fun `when there is no current user isLoggedIn returns false`() {
        val repository = LoginRepositoryImpl(FakeAuthDataSource(signedInUser = null))

        assertFalse(repository.isLoggedIn())
    }

    @Test
    fun `when sign in succeeds returns the signed in user`() = runTest {
        val dataSource = FakeAuthDataSource()
        val repository = LoginRepositoryImpl(dataSource)

        val result = repository.signInWithGoogle("id-token")

        assertEquals(loggedInUser, result.getOrNull())
        assertEquals("id-token", dataSource.lastIdToken)
        assertTrue(repository.isLoggedIn())
    }

    @Test
    fun `when sign in fails returns the error`() = runTest {
        val error = IllegalStateException("Sign in failed")
        val repository = LoginRepositoryImpl(FakeAuthDataSource(signInError = error))

        val result = repository.signInWithGoogle("id-token")

        assertTrue(result.isFailure)
        assertSame(error, result.exceptionOrNull())
        assertFalse(repository.isLoggedIn())
    }

    @Test(expected = CancellationException::class)
    fun `when sign in is cancelled rethrows the cancellation`() = runTest {
        val repository = LoginRepositoryImpl(
            FakeAuthDataSource(signInError = CancellationException("Cancelled"))
        )

        repository.signInWithGoogle("id-token")
    }

    @Test
    fun `when signing out user is no longer logged in`() {
        val repository = LoginRepositoryImpl(FakeAuthDataSource(signedInUser = loggedInUser))

        repository.signOut()

        assertFalse(repository.isLoggedIn())
    }
}
