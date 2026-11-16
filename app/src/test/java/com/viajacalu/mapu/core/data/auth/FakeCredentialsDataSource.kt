package com.viajacalu.mapu.core.data.auth

class FakeCredentialsDataSource(
    var clearError: Exception? = null
) : CredentialsDataSource {

    var credentialsCleared = false
        private set

    override suspend fun clearCredentials() {
        clearError?.let { throw it }
        credentialsCleared = true
    }
}
