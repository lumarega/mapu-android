package com.viajacalu.mapu.core.auth.data

import com.viajacalu.mapu.core.model.User
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthRepositoryImplTest {

    private val loggedInUser = User(
        id = "user-id",
        name = "Test User",
        email = "test@example.com",
        photoUrl = null
    )

    @Test
    fun `when there is a current user isLoggedIn returns true`() {
        val repository = AuthRepositoryImpl(FakeAuthDataSource(signedInUser = loggedInUser))

        assertTrue(repository.isLoggedIn())
    }

    @Test
    fun `when there is no current user isLoggedIn returns false`() {
        val repository = AuthRepositoryImpl(FakeAuthDataSource(signedInUser = null))

        assertFalse(repository.isLoggedIn())
    }

    @Test
    fun `when sign in succeeds returns the signed in user`() = runTest {
        val dataSource = FakeAuthDataSource()
        val repository = AuthRepositoryImpl(dataSource)

        val result = repository.signInWithGoogle("id-token")

        assertEquals(loggedInUser, result.getOrNull())
        assertEquals("id-token", dataSource.lastIdToken)
        assertTrue(repository.isLoggedIn())
    }

    @Test
    fun `when sign in fails returns the error`() = runTest {
        val error = IllegalStateException("Sign in failed")
        val repository = AuthRepositoryImpl(FakeAuthDataSource(signInError = error))

        val result = repository.signInWithGoogle("id-token")

        assertTrue(result.isFailure)
        assertSame(error, result.exceptionOrNull())
        assertFalse(repository.isLoggedIn())
    }

    @Test(expected = CancellationException::class)
    fun `when sign in is cancelled rethrows the cancellation`() = runTest {
        val repository = AuthRepositoryImpl(
            FakeAuthDataSource(signInError = CancellationException("Cancelled"))
        )

        repository.signInWithGoogle("id-token")
    }

    @Test
    fun `when signing out user is no longer logged in`() {
        val repository = AuthRepositoryImpl(FakeAuthDataSource(signedInUser = loggedInUser))

        repository.signOut()

        assertFalse(repository.isLoggedIn())
    }
}
