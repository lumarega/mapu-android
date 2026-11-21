package com.viajacalu.mapu.core.model

data class Guide(
    val id: String,
    val name: String,
    val country: String,
    val region: String,
    val tagline: String,
    val myMapsId: String,
    val isFeatured: Boolean
)