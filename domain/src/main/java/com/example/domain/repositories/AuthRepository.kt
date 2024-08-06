package com.example.domain.repositories

import com.example.domain.coroutines.Response
import com.example.domain.models.UserSession
import java.io.File

interface AuthRepository {

    suspend fun login( email: String, password: String) : Response<UserSession>
    suspend fun validateEmail(email: String) : Response<Unit>
    suspend fun register(
        email: String,
        name: String,
        weight: Int,
        image: File,
        phone: String,
        birthday: String,
        password: String
    ) : Response<Unit>
    suspend fun register(
        email: String,
        name: String,
        image: File,
        phone: String,
        birthday: String,
        password: String
    ) : Response<Unit>
    fun saveSession(loginResponse: UserSession): Boolean

}