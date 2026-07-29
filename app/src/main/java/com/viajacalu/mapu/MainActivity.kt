package com.viajacalu.mapu

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.viajacalu.mapu.launch.LaunchNavigation
import com.viajacalu.mapu.launch.LaunchUiEvent
import com.viajacalu.mapu.launch.LaunchViewModel
import com.viajacalu.mapu.ui.theme.MapuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val context = LocalContext.current
            val viewModel = LaunchViewModel()

            LaunchedEffect(Unit) {
                viewModel.navigation.collect { destination ->
                    when (destination) {
                        LaunchNavigation.Login -> {
                            Toast.makeText(
                                context,
                                "Login",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        LaunchNavigation.Home -> {
                            Toast.makeText(
                                context,
                                "Home",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            MapuTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MapuApp(Modifier.padding(innerPadding), viewModel::onUiEvent)
                }
            }
        }
    }
}

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
