package com.gousto.kmm.presentation.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gousto.kmm.presentation.screen.dashboard.events.DashboardUiEvent
import com.gousto.kmm.presentation.screen.dashboard.state.DashboardUiState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class DashboardScreenViewModel(
    private val dashboardScreenDecorator: DashboardScreenDecorator
) : ViewModel() {

//    Usa UiState para cosas persistentes (loading, datos, errores que bloquean funcionalidad)
//    Usa UiEvent para cosas efímeras (mensajes, navegación, errores tipo Toast/Snackbar)
    private val _event = MutableSharedFlow<DashboardUiEvent>()
    val event = _event.asSharedFlow()

    private val _uiState = MutableStateFlow(DashboardUiState(isLoading = true))
    val uiState: StateFlow<DashboardUiState> = _uiState


    init {
        fetchUserName()
    }

    private fun fetchUserName() {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, error ->
                viewModelScope.launch {
                    handleError(error)
                }
            }
        ) {
            _uiState.value = dashboardScreenDecorator.getDashboardUiState()
        }
    }

    private suspend fun handleError(error: Throwable) {
        _event.emit(DashboardUiEvent.ShowError(error.message ?: "Ha ocurrido un error inesperado"))
    }

}