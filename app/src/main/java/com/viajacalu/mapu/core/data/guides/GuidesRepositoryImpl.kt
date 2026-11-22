package com.viajacalu.mapu.core.data.guides

import com.viajacalu.mapu.core.model.Guide
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class GuidesRepositoryImpl @Inject constructor(
    private val guidesDataSource: GuidesDataSource
): GuidesRepository {
    override suspend fun getGuides(): Result<List<Guide>> =
        try {
            Result.success(guidesDataSource.getGuides())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }

}