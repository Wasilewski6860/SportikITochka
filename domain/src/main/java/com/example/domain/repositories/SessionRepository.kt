package com.example.domain.repositories

import com.example.domain.models.UserData
import com.example.domain.models.UserSession
import com.example.domain.models.UserType


interface SessionRepository {
    fun saveSession(authSession: UserSession?)
    fun getSession(): UserSession?

    fun getUserRole(): UserType?

    fun saveUserData(userData: UserData?)
    fun getUserData(): UserData?
}