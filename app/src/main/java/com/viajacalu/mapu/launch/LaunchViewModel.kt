package com.viajacalu.mapu.launch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class LaunchViewModel : ViewModel() {
    private val _navigation = MutableSharedFlow<LaunchNavigation>()
    val navigation = _navigation.asSharedFlow()

    fun onUiEvent(uiEvent: LaunchUiEvent){
        when(uiEvent){
            LaunchUiEvent.LoggedIn -> onNavigationEvent(LaunchNavigation.Home)
            LaunchUiEvent.LoggedOut -> onNavigationEvent(LaunchNavigation.Login)
        }
    }

    fun onNavigationEvent(navigationEvent: LaunchNavigation){
       viewModelScope.launch {
           _navigation.emit(navigationEvent)
       }
    }
}
