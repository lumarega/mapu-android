package com.viajacalu.mapu.feature.guides

import com.viajacalu.mapu.core.model.Guide

sealed interface GuidesUiState {
    data object Loading: GuidesUiState
    data class Success(val guidesList: List<Guide>): GuidesUiState
    data object Error: GuidesUiState
}