package com.example.sportikitochka.domain.repositories

import com.example.sportikitochka.data.models.response.auth.LoginResponse
import com.example.sportikitochka.data.models.response.auth.UserType

interface SessionRepository {
    fun saveSession(authSession: LoginResponse?)
    fun getSession(): LoginResponse?

    fun getUserRole(): UserType?
}