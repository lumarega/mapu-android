package com.viajacalu.mapu.feature.auth.presentation

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Devices
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
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        // Min height lets the spacer push the sign in area to the bottom, while still scrolling on small screens
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .heightIn(min = maxHeight)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            LoginIllustration(modifier = Modifier.fillMaxWidth())

            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(start = 4.dp, top = 32.dp)
                    .semantics { heading() }
            )

            Text(
                text = stringResource(R.string.login_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 4.dp, top = 8.dp)
            )

            Spacer(
                modifier = Modifier
                    .heightIn(min = 32.dp)
                    .weight(1f)
            )

            SignInArea(
                uiState = uiState,
                onSignInClick = onSignInClick,
                onUpdatePlayServicesClick = onUpdatePlayServicesClick,
                onErrorDismissed = onErrorDismissed
            )
        }
    }
}

@Composable
private fun SignInArea(
    uiState: LoginUiState,
    onSignInClick: () -> Unit,
    onUpdatePlayServicesClick: () -> Unit,
    onErrorDismissed: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when (uiState) {
            // Keeps showing progress on success until navigation to Guides happens
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

                // Message sits above the button so it is read before the retry action
                LoginError.NoGoogleAccount -> {
                    ErrorMessage(R.string.login_error_no_google_account)
                    SignInButton(onClick = onSignInClick)
                }

                LoginError.SignInFailed -> {
                    ErrorMessage(R.string.login_error_sign_in_failed)
                    SignInButton(onClick = onSignInClick)
                }
            }
        }
    }
}

@Composable
private fun SignInButton(onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_google_logo),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = stringResource(R.string.login_sign_in_with_google),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun SigningInIndicator() {
    val signingInDescription = stringResource(R.string.login_signing_in)
    // Same height as the button so the layout doesn't jump while signing in
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(32.dp)
                .semantics {
                    contentDescription = signingInDescription
                    liveRegion = LiveRegionMode.Polite
                }
        )
    }
}

@Composable
private fun ErrorMessage(messageRes: Int) {
    Surface(
        color = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .semantics { liveRegion = LiveRegionMode.Polite }
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_info),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = stringResource(messageRes),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
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

@Preview(device = Devices.PIXEL_7)
@Composable
fun LoginScreenIdlePreview() {
    LoginScreenPreview(LoginUiState.Idle)
}

@Preview(device = Devices.PIXEL_7, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoginScreenIdleDarkPreview() {
    LoginScreenPreview(LoginUiState.Idle)
}

@Preview(device = Devices.PIXEL_7)
@Composable
fun LoginScreenLoadingPreview() {
    LoginScreenPreview(LoginUiState.Loading)
}

@Preview(device = Devices.PIXEL_7)
@Composable
fun LoginScreenSignInFailedPreview() {
    LoginScreenPreview(LoginUiState.Error(LoginError.SignInFailed))
}

@Preview(device = Devices.PIXEL_7, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoginScreenNoGoogleAccountDarkPreview() {
    LoginScreenPreview(LoginUiState.Error(LoginError.NoGoogleAccount))
}

// Small screen and large text make the content taller than the screen, so it should scroll (use Interactive Mode)
@Preview(device = "spec:width=360dp,height=640dp", fontScale = 1.5f)
@Composable
fun LoginScreenSmallScreenPreview() {
    LoginScreenPreview(LoginUiState.Error(LoginError.SignInFailed))
}

@Preview(device = Devices.PIXEL_7)
@Composable
fun LoginScreenPlayServicesUpdatePreview() {
    LoginScreenPreview(LoginUiState.Error(LoginError.PlayServicesUpdateRequired))
}

@Preview(device = Devices.PIXEL_7)
@Composable
fun LoginScreenPlayServicesUnavailablePreview() {
    LoginScreenPreview(LoginUiState.Error(LoginError.PlayServicesUnavailable))
}

@Composable
private fun LoginScreenPreview(uiState: LoginUiState) {
    MapuTheme {
        // Stands in for the app Scaffold, which paints the background at runtime
        Surface(color = MaterialTheme.colorScheme.background) {
            LoginScreen(
                uiState = uiState,
                onSignInClick = {},
                onUpdatePlayServicesClick = {},
                onErrorDismissed = {}
            )
        }
    }
}
