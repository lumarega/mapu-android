package com.viajacalu.mapu

import com.viajacalu.mapu.core.testing.FakeAuthRepository
import com.viajacalu.mapu.core.testing.MainDispatcherRule
import com.viajacalu.mapu.navigation.MapuDestination
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
    fun `initial state is Loading`() {
        val viewModel = MainActivityViewModel(FakeAuthRepository())

        assertEquals(MainUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `when user is logged in start destination is Guides`() = runTest {
        val viewModel = MainActivityViewModel(FakeAuthRepository(loggedIn = true))

        advanceUntilIdle()

        assertEquals(
            MainUiState.Ready(MapuDestination.Guides),
            viewModel.uiState.value
        )
    }

    @Test
    fun `when user is logged out start destination is Login`() = runTest {
        val viewModel = MainActivityViewModel(FakeAuthRepository(loggedIn = false))

        advanceUntilIdle()

        assertEquals(
            MainUiState.Ready(MapuDestination.Login),
            viewModel.uiState.value
        )
    }
}
