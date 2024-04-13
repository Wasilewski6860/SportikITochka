package com.example.sportikitochka.domain.repositories

import com.example.sportikitochka.data.models.response.auth.LoginResponse
import com.example.sportikitochka.data.models.response.auth.UserType
import com.example.sportikitochka.data.models.response.user_data.UserDataResponse

interface SessionRepository {
    fun saveSession(authSession: LoginResponse?)
    fun getSession(): LoginResponse?

    fun getUserRole(): UserType?

    fun saveUserData(userDataResponse: UserDataResponse?)
    fun getUserData(): UserDataResponse?
}