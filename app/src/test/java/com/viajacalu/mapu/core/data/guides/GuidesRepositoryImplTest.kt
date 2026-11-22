package com.viajacalu.mapu.core.data.guides

import com.viajacalu.mapu.core.model.Guide
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test


class GuidesRepositoryImplTest {

    private val londonGuide = guide(id = "london123")
    private val lisbonGuide = guide(id = "lisbon123")

    private val guides = listOf(londonGuide, lisbonGuide)
    private val dataSource = FakeGuidesDataSource(guides = guides)
    private val repository = GuidesRepositoryImpl(guidesDataSource = dataSource)

    @Test
    fun `getGuides returns guides from the data source`() = runTest{

        val result = repository.getGuides()
        assertEquals(Result.success(listOf(londonGuide, lisbonGuide)), result)
    }

    @Test
    fun `getGuides returns a failure when there's an error`() = runTest {
        val error = Exception("Error reading file")
        dataSource.error = error

        val result = repository.getGuides()
        assertTrue(result.isFailure)
        assertEquals(error, result.exceptionOrNull())
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