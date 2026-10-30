package com.viajacalu.mapu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.viajacalu.mapu.core.designsystem.theme.MapuTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            MapuTheme {
                // Paints the app background while the session is being checked
                Surface(modifier = Modifier.fillMaxSize()) {
                    when (val state = uiState) {
                        MainUiState.Loading -> Unit
                        is MainUiState.Ready -> MapuApp(startDestination = state.startDestination)
                    }
                }
            }
        }
    }
}
