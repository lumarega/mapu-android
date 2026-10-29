package com.viajacalu.mapu.feature.launch.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.viajacalu.mapu.core.designsystem.theme.MapuTheme

@Composable
fun MapuApp(modifier: Modifier = Modifier, onUiEvent: (LaunchUiEvent) -> Unit){
    Column(modifier = modifier.padding(24.dp)) {
        NavigationButtons(onButtonToggle = onUiEvent)
    }
}


@Composable
fun NavigationButtons(onButtonToggle: (LaunchUiEvent) -> Unit) {
    Column(modifier = Modifier.padding(top = 20.dp)) {
        Text(
            text = "Where do you want to navigate to?",
        )

        Row(modifier = Modifier.padding(top = 8.dp)) {
            Button(onClick = { onButtonToggle(LaunchUiEvent.LoggedIn)} ){
                Text( text = "Home")
            }

            Button(modifier = Modifier.padding(start = 8.dp), onClick = { onButtonToggle(LaunchUiEvent.LoggedOut)} ){
                Text( text = "Login")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MapuAppPreview() {
    MapuTheme {
        MapuApp(onUiEvent = {})
    }
}

@Preview(showBackground = true)
@Composable
fun NavigationButtonsPreview() {
    MapuTheme {
        NavigationButtons {}
    }
}
