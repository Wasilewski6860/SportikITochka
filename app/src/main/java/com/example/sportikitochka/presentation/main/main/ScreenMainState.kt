package com.example.sportikitochka.presentation.main.main

sealed class ScreenMainState {
    object Loading: ScreenMainState()
    object ProfileLoaded: ScreenMainState()
    object ProfileLoadingError: ScreenMainState()
    object ActivitiesLoadedRemote: ScreenMainState()
    object ActivitiesLoadedLocal: ScreenMainState()
    object ErrorActivities: ScreenMainState()
    object Empty: ScreenMainState()
}