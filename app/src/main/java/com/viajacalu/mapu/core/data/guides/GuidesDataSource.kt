package com.viajacalu.mapu.core.data.guides

import com.viajacalu.mapu.core.model.Guide

interface GuidesDataSource {
    suspend fun getGuides(): List<Guide>
}