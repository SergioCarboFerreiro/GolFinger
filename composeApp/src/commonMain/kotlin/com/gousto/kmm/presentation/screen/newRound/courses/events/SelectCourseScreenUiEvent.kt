package com.gousto.kmm.presentation.screen.newRound.courses.events


sealed class SelectCourseScreenUiEvent {
    data class ShowError(val message: String) : SelectCourseScreenUiEvent()
}