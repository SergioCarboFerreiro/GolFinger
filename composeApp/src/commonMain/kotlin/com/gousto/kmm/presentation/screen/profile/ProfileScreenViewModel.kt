package com.gousto.kmm.presentation.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gousto.kmm.domain.GetCurrentUserProfileUseCase
import com.gousto.kmm.presentation.screen.profile.events.ProfileScreenUiEvent
import com.gousto.kmm.presentation.screen.profile.uiState.ProfileUiState
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileScreenViewModel(
    private val getCurrentUserProfileUseCase: GetCurrentUserProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    private val _event = MutableSharedFlow<ProfileScreenUiEvent>()
    val event = _event.asSharedFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, error ->
                viewModelScope.launch { handleError(error) }
            }
        ) {
            val profile = getCurrentUserProfileUseCase()
            _uiState.value = ProfileUiState(
                name = profile?.name ?: "Desconocido",
                email = Firebase.auth.currentUser?.email ?: "",
                handicap = profile?.handicap ?: "N/A",
                isLoading = false
            )
        }
    }

    private suspend fun handleError(error: Throwable) {
        _event.emit(ProfileScreenUiEvent.ShowError(error.message ?: "Error al cargar el perfil"))
        _uiState.update { it.copy(isLoading = false) }
    }
}