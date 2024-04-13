package com.example.sportikitochka.presentation.main.tracking

sealed interface ScreenTrackingState {
    object Loading: ScreenTrackingState
    object Content: ScreenTrackingState
    class Error(val message: String? = null): ScreenTrackingState
    object Empty: ScreenTrackingState
    object NoAccess : ScreenTrackingState
    object Success : ScreenTrackingState
}