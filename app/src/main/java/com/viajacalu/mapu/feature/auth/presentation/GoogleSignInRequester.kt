package com.viajacalu.mapu.feature.auth.presentation

import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.net.toUri
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.viajacalu.mapu.R

// Needs an Activity context because Credential Manager shows Google's account picker on top of it
class GoogleSignInRequester(private val context: Context) {

    private val credentialManager = CredentialManager.create(context)
    private val webClientId = context.getString(R.string.default_web_client_id)
    private val googleApiAvailability = GoogleApiAvailability.getInstance()

    suspend fun requestIdToken(): GoogleSignInResult {
        checkPlayServices()?.let { return it }

        // Returning users: only accounts that signed in before, picked automatically when there is one
        val result = request(authorizedAccountsRequest())
        if (result != GoogleSignInResult.NoGoogleAccount) return result

        // First sign in: full account picker, which also lets the user add an account
        return request(signInWithGoogleRequest())
    }

    // Opens the Play Store (or settings, if Play services is disabled) so the user can fix it
    fun updatePlayServices() {
        val resolution = googleApiAvailability
            .getErrorResolutionPendingIntent(context, playServicesStatus(), PLAY_SERVICES_REQUEST_CODE)
        try {
            resolution?.send() ?: openPlayServicesWebPage()
        } catch (e: PendingIntent.CanceledException) {
            // Happens when the Play Store app is missing, e.g. on emulators without Google Play
            Log.w(TAG, "Could not open the Google Play services update", e)
            openPlayServicesWebPage()
        }
    }

    private fun openPlayServicesWebPage() {
        val intent = Intent(Intent.ACTION_VIEW, PLAY_SERVICES_WEB_PAGE.toUri())
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Log.w(TAG, "No app can open the Google Play services page", e)
        }
    }

    // Credential Manager gets Google accounts from Play services, so it needs a recent enough version
    private fun playServicesStatus(): Int =
        googleApiAvailability.isGooglePlayServicesAvailable(context, MIN_PLAY_SERVICES_VERSION)

    private fun checkPlayServices(): GoogleSignInResult? {
        val status = playServicesStatus()
        return when {
            status == ConnectionResult.SUCCESS -> null
            googleApiAvailability.isUserResolvableError(status) -> GoogleSignInResult.PlayServicesUpdateRequired
            else -> GoogleSignInResult.PlayServicesUnavailable
        }
    }

    private suspend fun request(request: GetCredentialRequest): GoogleSignInResult =
        try {
            val credential = credentialManager.getCredential(context, request).credential
            GoogleSignInResult.Success(credential.toIdToken())
        } catch (e: NoCredentialException) {
            GoogleSignInResult.NoGoogleAccount
        } catch (e: GetCredentialCancellationException) {
            GoogleSignInResult.Cancelled
        } catch (e: GetCredentialException) {
            Log.w(TAG, "Google sign in failed", e)
            GoogleSignInResult.Failure(e)
        } catch (e: GoogleIdTokenParsingException) {
            Log.w(TAG, "Google sign in returned an invalid credential", e)
            GoogleSignInResult.Failure(e)
        }

    private fun Credential.toIdToken(): String {
        if (this !is CustomCredential || type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            throw GoogleIdTokenParsingException(IllegalStateException("Unexpected credential type: $type"))
        }
        return GoogleIdTokenCredential.createFrom(data).idToken
    }

    private fun authorizedAccountsRequest() = GetCredentialRequest.Builder()
        .addCredentialOption(
            GetGoogleIdOption.Builder()
                .setServerClientId(webClientId)
                .setFilterByAuthorizedAccounts(true)
                .setAutoSelectEnabled(true)
                .build()
        )
        .build()

    private fun signInWithGoogleRequest() = GetCredentialRequest.Builder()
        .addCredentialOption(GetSignInWithGoogleOption.Builder(webClientId).build())
        .build()

    private companion object {
        const val TAG = "GoogleSignInRequester"

        // Same minimum Credential Manager uses (MIN_GMS_APK_VERSION in credentials-play-services-auth),
        // which is restricted to the library so it can't be referenced directly
        const val MIN_PLAY_SERVICES_VERSION = 230815045

        // Required by the API but unused: the user taps sign in again after updating
        const val PLAY_SERVICES_REQUEST_CODE = 9000

        const val PLAY_SERVICES_WEB_PAGE = "https://play.google.com/store/apps/details?id=com.google.android.gms"
    }
}
