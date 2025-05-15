package com.gousto.kmm.presentation.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gousto.kmm.domain.GetCurrentUserProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DashboardScreenViewModel(
    private val getCurrentUserProfileUseCase: GetCurrentUserProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow("Usuario") // por defecto
    val uiState: StateFlow<String> = _uiState

    init {
        fetchUserName()
    }

    private fun fetchUserName() {

        viewModelScope.launch {
            try {
                _uiState.value = getCurrentUserProfileUseCase.invoke()?.name ?: "Usuario"
            } catch (e: Exception) {
                _uiState.value = "Usuario + $e.message"
            }
        }
    }
}