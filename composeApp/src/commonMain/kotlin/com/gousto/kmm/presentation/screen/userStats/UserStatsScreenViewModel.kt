package com.gousto.kmm.presentation.screen.userStats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gousto.kmm.domain.GetCurrentUserProfileUseCase
import com.gousto.kmm.presentation.screen.userStats.events.UserStatsScreenUiEvent
import com.gousto.kmm.presentation.screen.userStats.state.UserStatsScreenUiState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserStatsScreenViewModel(
    private val userStatsScreenDecorator: UserStatsScreenDecorator,
    private val getCurrentUserProfileUseCase: GetCurrentUserProfileUseCase
) : ViewModel() {

    private val _event = MutableSharedFlow<UserStatsScreenUiEvent>()
    val event = _event.asSharedFlow()

    private val _uiState = MutableStateFlow(UserStatsScreenUiState())
    val uiState: StateFlow<UserStatsScreenUiState> = _uiState


    fun getUserStatsUiState() {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, error ->
                viewModelScope.launch { handleError(error) }
            }) {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val userId =
                getCurrentUserProfileUseCase()?.id ?: throw IllegalStateException("User not found")
            val result = userStatsScreenDecorator.getStatsForUser(userId = userId)
            _uiState.value = result
        }
    }


    private suspend fun handleError(error: Throwable) {
        _event.emit(UserStatsScreenUiEvent.ShowError(error.message ?: "Error al cargar el perfil"))
        _uiState.update { it.copy(isLoading = false) }
    }
}
