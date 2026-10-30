package com.viajacalu.mapu.core.auth.data

interface CredentialsDataSource {
    suspend fun clearCredentials()
}
