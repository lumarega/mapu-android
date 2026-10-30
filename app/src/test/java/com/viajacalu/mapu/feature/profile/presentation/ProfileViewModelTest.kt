package com.viajacalu.mapu.feature.profile.presentation

import com.viajacalu.mapu.core.model.User
import com.viajacalu.mapu.core.testing.FakeAuthRepository
import com.viajacalu.mapu.core.testing.MainDispatcherRule
import com.viajacalu.mapu.core.testing.testUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when signed in shows the user details`() {
        val user = testUser.copy(photoUrl = "https://example.com/photo.jpg")
        val viewModel = ProfileViewModel(FakeAuthRepository(signedInUser = user))

        assertEquals(
            ProfileUiState.SignedIn(
                name = "Test User",
                email = "test@example.com",
                photoUrl = "https://example.com/photo.jpg",
                initials = "TU"
            ),
            viewModel.uiState.value
        )
    }

    @Test
    fun `when there is no user state is SignedOut`() {
        val viewModel = ProfileViewModel(FakeAuthRepository(signedInUser = null))

        assertEquals(ProfileUiState.SignedOut, viewModel.uiState.value)
    }

    @Test
    fun `initials use the first and last names`() {
        assertEquals("LM", initialsFor(testUser.copy(name = "Luana Costa Marega")))
    }

    @Test
    fun `initials use the only name when there is one`() {
        assertEquals("L", initialsFor(testUser.copy(name = "luana")))
    }

    @Test
    fun `initials use the email when there is no name`() {
        assertEquals("T", initialsFor(testUser.copy(name = null)))
    }

    @Test
    fun `when signing out the user is signed out and state is SignedOut`() = runTest {
        val repository = FakeAuthRepository(signedInUser = testUser)
        val viewModel = ProfileViewModel(repository)

        viewModel.onSignOutClick()
        advanceUntilIdle()

        assertFalse(repository.isLoggedIn())
        assertEquals(ProfileUiState.SignedOut, viewModel.uiState.value)
    }

    private fun initialsFor(user: User): String {
        val state = ProfileViewModel(FakeAuthRepository(signedInUser = user)).uiState.value
        return (state as ProfileUiState.SignedIn).initials
    }
}
