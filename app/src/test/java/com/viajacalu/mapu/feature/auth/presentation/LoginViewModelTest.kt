package com.viajacalu.mapu.feature.auth.presentation

import com.viajacalu.mapu.core.testing.FakeAuthRepository
import com.viajacalu.mapu.core.testing.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeAuthRepository()

    @Test
    fun `initial state is Idle`() {
        val viewModel = LoginViewModel(repository)

        assertEquals(LoginUiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun `when sign in starts state is Loading`() {
        val viewModel = LoginViewModel(repository)

        viewModel.onSignInStarted()

        assertEquals(LoginUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `when sign in starts after an error the error is replaced by Loading`() {
        val viewModel = LoginViewModel(repository)
        viewModel.onGoogleSignInResult(GoogleSignInResult.NoGoogleAccount)

        viewModel.onSignInStarted()

        assertEquals(LoginUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `when Google returns a token and Firebase sign in succeeds state is LoginSuccess`() = runTest {
        val viewModel = LoginViewModel(repository)
        viewModel.onSignInStarted()

        viewModel.onGoogleSignInResult(GoogleSignInResult.Success("id-token"))
        advanceUntilIdle()

        assertEquals(LoginUiState.LoginSuccess, viewModel.uiState.value)
        assertEquals("id-token", repository.lastIdToken)
    }

    @Test
    fun `when Firebase sign in fails shows sign in failed error`() = runTest {
        repository.signInError = IllegalStateException("Firebase error")
        val viewModel = LoginViewModel(repository)
        viewModel.onSignInStarted()

        viewModel.onGoogleSignInResult(GoogleSignInResult.Success("id-token"))
        advanceUntilIdle()

        assertEquals(LoginUiState.Error(LoginError.SignInFailed), viewModel.uiState.value)
    }

    @Test
    fun `when user cancels Google sign in goes back to Idle`() {
        val viewModel = LoginViewModel(repository)
        viewModel.onSignInStarted()

        viewModel.onGoogleSignInResult(GoogleSignInResult.Cancelled)

        assertEquals(LoginUiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun `when there is no Google account shows no account error`() {
        val viewModel = LoginViewModel(repository)
        viewModel.onSignInStarted()

        viewModel.onGoogleSignInResult(GoogleSignInResult.NoGoogleAccount)

        assertEquals(LoginUiState.Error(LoginError.NoGoogleAccount), viewModel.uiState.value)
    }

    @Test
    fun `when Google sign in fails shows sign in failed error`() {
        val viewModel = LoginViewModel(repository)
        viewModel.onSignInStarted()

        viewModel.onGoogleSignInResult(GoogleSignInResult.Failure(IllegalStateException("Google error")))

        assertEquals(LoginUiState.Error(LoginError.SignInFailed), viewModel.uiState.value)
    }

    @Test
    fun `when Play services needs an update shows update required error`() {
        val viewModel = LoginViewModel(repository)
        viewModel.onSignInStarted()

        viewModel.onGoogleSignInResult(GoogleSignInResult.PlayServicesUpdateRequired)

        assertEquals(LoginUiState.Error(LoginError.PlayServicesUpdateRequired), viewModel.uiState.value)
    }

    @Test
    fun `when Play services is not available shows unsupported device error`() {
        val viewModel = LoginViewModel(repository)
        viewModel.onSignInStarted()

        viewModel.onGoogleSignInResult(GoogleSignInResult.PlayServicesUnavailable)

        assertEquals(LoginUiState.Error(LoginError.PlayServicesUnavailable), viewModel.uiState.value)
    }

    @Test
    fun `when an error is dismissed goes back to Idle`() {
        val viewModel = LoginViewModel(repository)
        viewModel.onGoogleSignInResult(GoogleSignInResult.PlayServicesUpdateRequired)

        viewModel.onErrorDismissed()

        assertEquals(LoginUiState.Idle, viewModel.uiState.value)
    }
}
