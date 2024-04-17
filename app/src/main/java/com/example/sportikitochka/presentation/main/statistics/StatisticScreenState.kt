package com.example.sportikitochka.presentation.main.statistics

sealed class StatisticScreenState {
    object Loading: StatisticScreenState()
    object Success: StatisticScreenState()
    object Error: StatisticScreenState()
}