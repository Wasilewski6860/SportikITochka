package com.example.sportikitochka.domain.repositories

import com.example.sportikitochka.data.models.response.auth.LoginResponse

interface SessionRepository {
    fun saveSession(authSession: LoginResponse?)
    fun getSession(): LoginResponse?
}