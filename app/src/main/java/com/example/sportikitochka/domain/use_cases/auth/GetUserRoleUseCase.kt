package com.example.sportikitochka.domain.use_cases.auth

import com.example.sportikitochka.domain.repositories.SessionRepository

class GetUserRoleUseCase(private val sessionRepository: SessionRepository) {

    fun execute() = sessionRepository.getUserRole()
}