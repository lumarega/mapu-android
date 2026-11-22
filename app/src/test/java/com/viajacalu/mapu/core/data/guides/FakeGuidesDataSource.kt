package com.viajacalu.mapu.core.data.guides

import com.viajacalu.mapu.core.model.Guide

class FakeGuidesDataSource(
    val guides: List<Guide> = emptyList()
): GuidesDataSource {

    override suspend fun getGuides(): List<Guide> {
        return guides
    }
}