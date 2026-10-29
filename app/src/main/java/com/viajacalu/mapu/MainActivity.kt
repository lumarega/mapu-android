package com.viajacalu.mapu

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.viajacalu.mapu.core.designsystem.theme.MapuTheme
import com.viajacalu.mapu.feature.launch.presentation.LaunchNavigation
import com.viajacalu.mapu.feature.launch.presentation.LaunchViewModel
import com.viajacalu.mapu.feature.launch.presentation.MapuApp

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
