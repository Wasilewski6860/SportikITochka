package com.example.sportikitochka.presentation.sign_up

sealed interface SignUpScreenState {
    object Loading: SignUpScreenState
    object ValidationSuccess: SignUpScreenState
    object ValidationError : SignUpScreenState
    object AnyError : SignUpScreenState
    object Success : SignUpScreenState
}