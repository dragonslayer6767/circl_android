package com.fragne.circl_app.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// import com.fragne.circl_app.core.data.AppStateManager
// import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
// import javax.inject.Inject

/**
 * UI state for login screen
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccessful: Boolean = false
)

/**
 * ViewModel for login screen
 */
// @HiltViewModel
class LoginViewModel(
    // private val appStateManager: AppStateManager
    // Add AuthRepository when implemented
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, errorMessage = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    fun login() {
        val email = _uiState.value.email
        val password = _uiState.value.password

        // Basic validation
        if (email.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please fill in all fields") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                // TODO: Replace with actual API call to Django backend
                // For now, simulate login
                kotlinx.coroutines.delay(1000)

                // Mock successful login
                // appStateManager.setLoggedIn(
                //     isLoggedIn = true,
                //     userId = 1,
                //     username = email.substringBefore("@"),
                //     authToken = "mock_token",
                //     email = email
                // )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoginSuccessful = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Login failed: ${e.message}"
                    )
                }
            }
        }
    }
}

