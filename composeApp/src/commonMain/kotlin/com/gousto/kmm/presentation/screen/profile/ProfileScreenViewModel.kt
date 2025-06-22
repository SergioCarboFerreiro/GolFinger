package com.gousto.kmm.presentation.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gousto.kmm.domain.GetCurrentUserProfileUseCase
import com.gousto.kmm.domain.SignOutUseCase
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
    private val profileScreenDecorator: ProfileScreenDecorator,
    private val signOutUseCase: SignOutUseCase
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
            //todo manejar el estado nullable en vez de lanzar una exception
            _uiState.value = profileScreenDecorator.getProfileUiState() ?: throw Exception("Error al cargar el perfil")
        }
    }

    fun signOut() {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, error ->
                viewModelScope.launch { handleError(error) }
            }
        ) {
            signOutUseCase()
            _event.emit(ProfileScreenUiEvent.NavigateToLoginScreen)
        }
    }

    private suspend fun handleError(error: Throwable) {
        _event.emit(ProfileScreenUiEvent.ShowError(error.message ?: "Error al cargar el perfil"))
        _uiState.update { it.copy(isLoading = false) }
    }
}