package com.example.sportikitochka.presentation.auth.sign_in

sealed interface SignInScreenState {
    object Loading: SignInScreenState
    object UserBlockedError: SignInScreenState
    object UserNotFoundError : SignInScreenState
    object IncorrectPasswordError : SignInScreenState
    object AnyError : SignInScreenState
    object Success : SignInScreenState
}