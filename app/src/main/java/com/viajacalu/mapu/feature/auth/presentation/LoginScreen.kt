package com.viajacalu.mapu.feature.auth.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.viajacalu.mapu.R
import com.viajacalu.mapu.core.designsystem.theme.MapuTheme
import kotlinx.coroutines.launch

@Composable
fun LoginRoute(
    onSignedIn: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val googleSignInRequester = remember(context) { GoogleSignInRequester(context) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState) {
        if (uiState == LoginUiState.LoginSuccess) onSignedIn()
    }

    LoginScreen(
        uiState = uiState,
        onSignInClick = {
            viewModel.onSignInStarted()
            scope.launch {
                viewModel.onGoogleSignInResult(googleSignInRequester.requestIdToken())
            }
        },
        onUpdatePlayServicesClick = {
            viewModel.onErrorDismissed()
            googleSignInRequester.updatePlayServices()
        },
        onErrorDismissed = viewModel::onErrorDismissed,
        modifier = modifier
    )
}

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onSignInClick: () -> Unit,
    onUpdatePlayServicesClick: () -> Unit,
    onErrorDismissed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.semantics { heading() }
        )

        Text(
            text = stringResource(R.string.login_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )

        when (uiState) {
            // Keeps showing progress on success until navigation to Home happens
            LoginUiState.Loading, LoginUiState.LoginSuccess -> SigningInIndicator()

            LoginUiState.Idle -> SignInButton(onClick = onSignInClick)

            is LoginUiState.Error -> when (uiState.error) {
                // Without Play services Google sign in can never work, so there is nothing to retry
                LoginError.PlayServicesUnavailable -> ErrorMessage(R.string.login_error_play_services_unavailable)

                // The button stays behind the dialog so the user can sign in after updating
                LoginError.PlayServicesUpdateRequired -> {
                    SignInButton(onClick = onSignInClick)
                    UpdatePlayServicesDialog(
                        onUpdateClick = onUpdatePlayServicesClick,
                        onDismiss = onErrorDismissed
                    )
                }

                LoginError.NoGoogleAccount -> {
                    SignInButton(onClick = onSignInClick)
                    ErrorMessage(R.string.login_error_no_google_account)
                }

                LoginError.SignInFailed -> {
                    SignInButton(onClick = onSignInClick)
                    ErrorMessage(R.string.login_error_sign_in_failed)
                }
            }
        }
    }
}

@Composable
private fun SignInButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(top = 32.dp)
    ) {
        Text(text = stringResource(R.string.login_sign_in_with_google))
    }
}

@Composable
private fun SigningInIndicator() {
    val signingInDescription = stringResource(R.string.login_signing_in)
    CircularProgressIndicator(
        modifier = Modifier
            .padding(top = 32.dp)
            .semantics {
                contentDescription = signingInDescription
                liveRegion = LiveRegionMode.Polite
            }
    )
}

@Composable
private fun ErrorMessage(messageRes: Int) {
    Text(
        text = stringResource(messageRes),
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(top = 16.dp)
            .semantics { liveRegion = LiveRegionMode.Polite }
    )
}

@Composable
private fun UpdatePlayServicesDialog(
    onUpdateClick: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.login_play_services_update_title)) },
        text = { Text(text = stringResource(R.string.login_error_play_services_update_required)) },
        confirmButton = {
            TextButton(onClick = onUpdateClick) {
                Text(text = stringResource(R.string.login_play_services_update_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.login_play_services_update_dismiss))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun LoginScreenIdlePreview() {
    LoginScreenPreview(LoginUiState.Idle)
}

@Preview(showBackground = true)
@Composable
fun LoginScreenLoadingPreview() {
    LoginScreenPreview(LoginUiState.Loading)
}

@Preview(showBackground = true)
@Composable
fun LoginScreenSignInFailedPreview() {
    LoginScreenPreview(LoginUiState.Error(LoginError.SignInFailed))
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPlayServicesUpdatePreview() {
    LoginScreenPreview(LoginUiState.Error(LoginError.PlayServicesUpdateRequired))
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPlayServicesUnavailablePreview() {
    LoginScreenPreview(LoginUiState.Error(LoginError.PlayServicesUnavailable))
}

@Composable
private fun LoginScreenPreview(uiState: LoginUiState) {
    MapuTheme {
        LoginScreen(
            uiState = uiState,
            onSignInClick = {},
            onUpdatePlayServicesClick = {},
            onErrorDismissed = {}
        )
    }
}
