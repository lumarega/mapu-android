package com.viajacalu.mapu.core.data.guides

import com.viajacalu.mapu.core.model.Guide
import javax.inject.Inject

class GuidesRepositoryImpl @Inject constructor(
    private val guidesDataSource: GuidesDataSource
): GuidesRepository {

    override suspend fun getGuides(): List<Guide> = guidesDataSource.getGuides()
}