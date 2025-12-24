package com.fragne.circl_app.ui.navigation

import androidx.lifecycle.ViewModel
// import com.fragne.circl_app.core.data.AppStateManager
// import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
// import javax.inject.Inject

/**
 * ViewModel for root navigation
 * Observes authentication state to determine app entry point
 */
// @HiltViewModel
class RootNavigationViewModel(
    // private val appStateManager: AppStateManager
) : ViewModel() {

    // Temporarily return false until we implement proper auth
    val isLoggedIn: Flow<Boolean> = flowOf(false)
}

