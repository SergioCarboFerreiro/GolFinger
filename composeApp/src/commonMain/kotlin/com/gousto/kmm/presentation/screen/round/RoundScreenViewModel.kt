package com.gousto.kmm.presentation.screen.round

import androidx.lifecycle.ViewModel
import com.gousto.kmm.presentation.screen.round.state.RoundScreenUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RoundScreenViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow(RoundScreenUiState())
    val uiState: StateFlow<RoundScreenUiState> = _uiState

    init {

    }
}