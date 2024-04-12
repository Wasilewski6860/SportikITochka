package com.example.sportikitochka.presentation.auth.sign_up

sealed interface SignUpScreenState {
    object Loading: SignUpScreenState
    object ValidationSuccess: SignUpScreenState
    object ValidationError : SignUpScreenState
    object AnyError : SignUpScreenState
    object Success : SignUpScreenState
}