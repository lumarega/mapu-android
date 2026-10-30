package com.viajacalu.mapu

import androidx.activity.ComponentActivity
import dagger.hilt.android.AndroidEntryPoint

// Empty activity for UI tests of screens that get their ViewModels from Hilt.
// It lives in the debug build only, because tests run against the debug build.
@AndroidEntryPoint
class HiltTestActivity : ComponentActivity()
