package com.viajacalu.mapu.core.data.guides

import com.viajacalu.mapu.core.model.Guide

interface GuidesRepository {
    suspend fun getGuides(): Result<List<Guide>>
}