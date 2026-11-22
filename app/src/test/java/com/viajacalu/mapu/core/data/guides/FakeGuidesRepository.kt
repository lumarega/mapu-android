package com.viajacalu.mapu.core.data.guides

import com.viajacalu.mapu.core.model.Guide

class FakeGuidesRepository(
var guides: List<Guide> = emptyList(),
var error: Exception? = null
): GuidesRepository {

    override suspend fun getGuides(): Result<List<Guide>> {
        return error?.let { Result.failure(it) } ?: Result.success(guides)
    }
}