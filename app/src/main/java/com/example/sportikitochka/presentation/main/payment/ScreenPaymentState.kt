package com.example.sportikitochka.presentation.main.payment

sealed class ScreenPaymentState {
    object Loading: ScreenPaymentState()
    object CardsLoaded: ScreenPaymentState()
    object CardsLoadingError: ScreenPaymentState()
    object CardOperationError: ScreenPaymentState()
    object CardOperationSuccess: ScreenPaymentState()
    object BuyingSuccess: ScreenPaymentState()
    object BuyingError: ScreenPaymentState()
}