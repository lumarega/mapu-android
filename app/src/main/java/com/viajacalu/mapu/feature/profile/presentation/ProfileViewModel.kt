package com.viajacalu.mapu.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viajacalu.mapu.core.auth.domain.AuthRepository
import com.viajacalu.mapu.core.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(authRepository.getCurrentUser().toUiState())
    val uiState = _uiState.asStateFlow()

    fun onSignOutClick() {
        viewModelScope.launch {
            authRepository.signOut()
            _uiState.value = ProfileUiState.SignedOut
        }
    }

    private fun User?.toUiState(): ProfileUiState =
        if (this == null) {
            ProfileUiState.SignedOut
        } else {
            ProfileUiState.SignedIn(
                name = name,
                email = email,
                photoUrl = photoUrl,
                initials = initialsOf(name, email)
            )
        }

    // "Luana Marega" -> "LM", "Luana" -> "L", and the email's first letter when there is no name
    private fun initialsOf(name: String?, email: String?): String {
        val words = name.orEmpty().split(" ").filter { it.isNotBlank() }
        val initials = when {
            words.size > 1 -> "${words.first().first()}${words.last().first()}"
            words.size == 1 -> words.first().take(1)
            else -> email.orEmpty().take(1)
        }
        return initials.uppercase()
    }
}
