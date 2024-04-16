package com.example.sportikitochka.domain.use_cases.auth

import com.example.sportikitochka.data.models.response.auth.LoginResponse
import com.example.sportikitochka.data.models.response.auth.UserType
import com.example.sportikitochka.domain.repositories.AuthRepository
import com.example.sportikitochka.domain.repositories.SessionRepository

class ChangeUserTypeUseCase(private val authRepository: AuthRepository, private  val sessionRepository: SessionRepository) {

    fun execute(userType: UserType)  {
        var session = sessionRepository.getSession()
        session?.role = userType.toString()
        session?.let { authRepository.saveSession(it) }
    }
}