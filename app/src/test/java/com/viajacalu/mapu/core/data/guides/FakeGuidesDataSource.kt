package com.viajacalu.mapu.core.data.guides

import com.viajacalu.mapu.core.model.Guide

class FakeGuidesDataSource(
    var guides: List<Guide> = emptyList(),
    var error: Exception? = null
): GuidesDataSource {

    override suspend fun getGuides(): List<Guide> {
        error?.let { throw it }
        return guides
    }
}