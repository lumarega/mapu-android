package com.viajacalu.mapu.feature.guides

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viajacalu.mapu.core.data.guides.GuidesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuidesViewModel @Inject constructor(
    val repository: GuidesRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<GuidesUiState>(GuidesUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadGuides()
    }

    fun loadGuides(){
        _uiState.value = GuidesUiState.Loading
        viewModelScope.launch {
            _uiState.value = repository.getGuides().fold(
                onSuccess = { guides -> GuidesUiState.Success(guides) },
                onFailure = { GuidesUiState.Error }
            )
        }
    }

}