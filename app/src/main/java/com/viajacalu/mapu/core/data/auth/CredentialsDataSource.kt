package com.viajacalu.mapu.core.data.auth

interface CredentialsDataSource {
    suspend fun clearCredentials()
}
