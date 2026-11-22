package com.viajacalu.mapu.core.data.guides

import com.viajacalu.mapu.core.model.Guide
import kotlinx.serialization.Serializable

@Serializable
internal data class GuideDto(
    val id: String,
    val name: String,
    val country: String,
    val region: String,
    val tagline: String,
    val myMapsId: String,
    val isFeatured: Boolean = false
)

internal fun GuideDto.toGuide() = Guide(
    id = id,
    name = name,
    country = country,
    region = region,
    tagline = tagline,
    myMapsId = myMapsId,
    isFeatured = isFeatured
)