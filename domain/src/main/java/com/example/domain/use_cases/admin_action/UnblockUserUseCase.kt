package com.example.domain.use_cases.admin_action

import com.example.domain.models.AdminAction
import com.example.domain.repositories.AdminActionRepository

class UnblockUserUseCase(private val adminActionRepository: AdminActionRepository) {

    suspend fun execute(userId: String) = adminActionRepository.adminAction(
        userId,
        AdminAction.UNBLOCK.action
    )
}