package com.example.domain.use_cases.admin_action

import com.example.domain.models.AdminAction
import com.example.domain.repositories.AdminActionRepository

class RevokePremiumUseCase(private val adminActionRepository: com.example.domain.repositories.AdminActionRepository) {

    suspend fun execute(userId: String) = adminActionRepository.adminAction(
        userId,
        AdminAction.REVOKE_PREMIUM.action
    )
}