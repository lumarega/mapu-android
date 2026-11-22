package com.viajacalu.mapu.core.data.guides

import android.content.Context
import com.viajacalu.mapu.core.data.di.IoDispatcher
import com.viajacalu.mapu.core.model.Guide
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class LocalGuidesDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : GuidesDataSource {

    // Skips fields the app doesn't know yet, so adding one to the JSON doesn't crash older versions
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getGuides(): List<Guide> = withContext(ioDispatcher) {
        val text = context.assets.open(GUIDES_FILE).bufferedReader().use { it.readText() }
        json.decodeFromString<List<GuideDto>>(text).map { it.toGuide() }
    }

    private companion object {
        const val GUIDES_FILE = "guides.json"
    }
}