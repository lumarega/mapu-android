package com.viajacalu.mapu.feature.guides

import com.viajacalu.mapu.core.data.guides.FakeGuidesRepository
import com.viajacalu.mapu.core.model.Guide
import com.viajacalu.mapu.core.testing.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GuidesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val guides = listOf(guide("london"), guide("edinburgh"))
    private val repository = FakeGuidesRepository(guides = guides)

    @Test
    fun `starts in Loading`() {
        val viewModel = GuidesViewModel(repository)

        assertEquals(GuidesUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `shows the guides once loaded`() = runTest {
        val viewModel = GuidesViewModel(repository)
        advanceUntilIdle()

        assertEquals(GuidesUiState.Success(guides), viewModel.uiState.value)
    }

    @Test
    fun `shows Error when the guides can't be loaded`() = runTest {
        repository.error = Exception("Broken file")
        val viewModel = GuidesViewModel(repository)
        advanceUntilIdle()

        assertEquals(GuidesUiState.Error, viewModel.uiState.value)
    }

    @Test
    fun `trying again after an error shows the guides`() = runTest {
        repository.error = Exception("Broken file")
        val viewModel = GuidesViewModel(repository)
        advanceUntilIdle()

        repository.error = null
        viewModel.loadGuides()
        advanceUntilIdle()

        assertEquals(GuidesUiState.Success(guides), viewModel.uiState.value)
    }

    private fun guide(id: String) = Guide(
        id = id,
        name = id,
        country = "England",
        region = "Europe",
        tagline = "Tagline",
        myMapsId = "map-$id",
        isFeatured = false
    )
}