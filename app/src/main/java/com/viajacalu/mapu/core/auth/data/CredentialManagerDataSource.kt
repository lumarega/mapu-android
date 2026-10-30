package com.viajacalu.mapu.core.auth.data

import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import javax.inject.Inject

// CredentialManager is Android's API for the accounts saved on the device. When signing in, it gives
// us the Google ID token for Firebase (requested by the Login screen, as it needs an Activity to show
// the account picker) and remembers which account was used with Mapu, so returning users are signed
// in automatically.
class CredentialManagerDataSource @Inject constructor(
    private val credentialManager: CredentialManager
) : CredentialsDataSource {

    // Makes the device forget the account used with Mapu, so the next sign in shows the account picker.
    // It doesn't sign the user out of Google on the device or remove Mapu's access to the account.
    override suspend fun clearCredentials() {
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
    }
}
