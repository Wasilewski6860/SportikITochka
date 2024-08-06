package com.example.domain.use_cases.auth

import com.example.domain.repositories.SessionRepository

class GetUserRoleUseCase(private val sessionRepository: SessionRepository) {

    fun execute() = sessionRepository.getUserRole()
}