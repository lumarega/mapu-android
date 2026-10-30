package com.viajacalu.mapu.feature.profile.presentation

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.viajacalu.mapu.BuildConfig
import com.viajacalu.mapu.R
import com.viajacalu.mapu.core.designsystem.theme.Manrope
import com.viajacalu.mapu.core.designsystem.theme.MapuTheme

@Composable
fun ProfileRoute(
    onSignedOut: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        if (uiState == ProfileUiState.SignedOut) onSignedOut()
    }

    ProfileScreen(
        uiState = uiState,
        appVersion = BuildConfig.VERSION_NAME,
        onSignOutClick = viewModel::onSignOutClick,
        modifier = modifier
    )
}

@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    appVersion: String,
    onSignOutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        // Min height lets the spacer push the sign out button to the bottom, while still scrolling on small screens
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .heightIn(min = maxHeight)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.profile_title),
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier
                    .padding(top = 32.dp)
                    .semantics { heading() }
            )

            // Nothing to show once signed out, while navigating back to Login
            if (uiState is ProfileUiState.SignedIn) {
                UserDetails(
                    user = uiState,
                    modifier = Modifier.padding(top = 32.dp)
                )
            }

            Spacer(
                modifier = Modifier
                    .heightIn(min = 32.dp)
                    .weight(1f)
            )

            SignOutButton(
                onClick = onSignOutClick,
                enabled = uiState is ProfileUiState.SignedIn
            )

            Text(
                text = stringResource(R.string.profile_version, stringResource(R.string.app_name), appVersion),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
        }
    }
}

@Composable
private fun UserDetails(
    user: ProfileUiState.SignedIn,
    modifier: Modifier = Modifier
) {
    // Merged so screen readers read the name and email together
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier.semantics(mergeDescendants = true) {}
    ) {
        ProfilePhoto(photoUrl = user.photoUrl, initials = user.initials)

        Column {
            user.name?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleLarge.copy(fontFamily = Manrope, fontWeight = FontWeight.Bold)
                )
            }
            user.email?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ProfilePhoto(photoUrl: String?, initials: String) {
    // Initials show while the photo loads, and stay if there is no photo or it fails to load.
    // Both are decorative because the name is read right after.
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clearAndSetSemantics {}
    ) {
        Text(
            text = initials,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        if (photoUrl != null) {
            AsyncImage(
                model = photoUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun SignOutButton(onClick: () -> Unit, enabled: Boolean) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_sign_out),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = stringResource(R.string.profile_sign_out),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

private val previewUser = ProfileUiState.SignedIn(
    name = "Luana Marega",
    email = "name@gmail.com",
    photoUrl = null,
    initials = "LM"
)

@Preview(device = Devices.PIXEL_7)
@Composable
fun ProfileScreenPreview() {
    ProfileScreenPreview(previewUser)
}

@Preview(device = Devices.PIXEL_7, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProfileScreenDarkPreview() {
    ProfileScreenPreview(previewUser)
}

// Small screen and large text make the content taller than the screen, so it should scroll (use Interactive Mode)
@Preview(device = "spec:width=360dp,height=640dp", fontScale = 1.5f)
@Composable
fun ProfileScreenSmallScreenPreview() {
    ProfileScreenPreview(previewUser)
}

@Composable
private fun ProfileScreenPreview(uiState: ProfileUiState) {
    MapuTheme {
        // Stands in for the app Scaffold, which paints the background at runtime
        Surface(color = MaterialTheme.colorScheme.background) {
            ProfileScreen(
                uiState = uiState,
                appVersion = "1.0",
                onSignOutClick = {}
            )
        }
    }
}
