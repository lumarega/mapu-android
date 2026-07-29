package com.viajacalu.mapu.launch

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LaunchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when LoggedIn event is received emits Home navigation event`() = runTest {
        val viewModel = LaunchViewModel()

        val navigation = async {
            viewModel.navigation.first()
        }

        viewModel.onUiEvent(LaunchUiEvent.LoggedIn)

        advanceUntilIdle()

        assertEquals(
            LaunchNavigation.Home,
            navigation.await()
        )
    }

    @Test
    fun `when LoggedOut event is received emits Login navigation event`() = runTest {
        val viewModel = LaunchViewModel()

        val navigation = async {
            viewModel.navigation.first()
        }

        viewModel.onUiEvent(LaunchUiEvent.LoggedOut)

        advanceUntilIdle()

        assertEquals(
            LaunchNavigation.Login,
            navigation.await()
        )
    }

    @Test
    fun `when onNavigationEvent is called emits navigation event`() = runTest {
        val viewModel = LaunchViewModel()

        val navigation = async {
            viewModel.navigation.first()
        }

        viewModel.onNavigationEvent(LaunchNavigation.Home)

        advanceUntilIdle()

        assertEquals(
            LaunchNavigation.Home,
            navigation.await()
        )
    }
}