package com.viajacalu.mapu

import com.viajacalu.mapu.core.testing.FakeAuthRepository
import com.viajacalu.mapu.core.testing.MainDispatcherRule
import com.viajacalu.mapu.core.testing.testUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainActivityViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `initial state is Checking`() {
        val viewModel = MainActivityViewModel(FakeAuthRepository())

        assertEquals(SessionState.Checking, viewModel.sessionState.value)
    }

    @Test
    fun `when user is logged in state is SignedIn`() = runTest {
        val viewModel = MainActivityViewModel(FakeAuthRepository(signedInUser = testUser))

        advanceUntilIdle()

        assertEquals(SessionState.SignedIn, viewModel.sessionState.value)
    }

    @Test
    fun `when user is logged out state is SignedOut`() = runTest {
        val viewModel = MainActivityViewModel(FakeAuthRepository(signedInUser = null))

        advanceUntilIdle()

        assertEquals(SessionState.SignedOut, viewModel.sessionState.value)
    }
}
