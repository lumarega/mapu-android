package com.viajacalu.mapu.feature.auth.domain

data class AuthUser(
    val id: String,
    val name: String?,
    val email: String?,
    val photoUrl: String?
)
