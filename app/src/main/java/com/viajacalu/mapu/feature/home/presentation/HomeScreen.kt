package com.viajacalu.mapu.feature.home.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.viajacalu.mapu.R
import com.viajacalu.mapu.core.designsystem.theme.MapuTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.home_title),
        modifier = modifier
            .padding(24.dp)
            .semantics { heading() }
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MapuTheme {
        HomeScreen()
    }
}
