package com.example.domain.use_cases.admin_action

import com.example.domain.repositories.AdminActionRepository


class GrantPremiumUseCase(private val adminActionRepository: AdminActionRepository) {

    suspend fun execute(userId: String) = adminActionRepository.grantPremium(
        userId
    )
}