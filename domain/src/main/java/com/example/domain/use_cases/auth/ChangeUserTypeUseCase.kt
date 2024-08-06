package com.example.domain.use_cases.auth

import com.example.domain.models.UserType
import com.example.domain.repositories.AuthRepository
import com.example.domain.repositories.SessionRepository

class ChangeUserTypeUseCase(private val authRepository: AuthRepository, private  val sessionRepository: SessionRepository) {

    fun execute(userType: UserType)  {
        var session = sessionRepository.getSession()
        session?.role = userType.toString()
        session?.let { authRepository.saveSession(it) }
    }
}