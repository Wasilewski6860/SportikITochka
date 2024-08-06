package com.example.domain.use_cases.admin_action

import com.example.domain.models.AdminAction

class BlockUserUseCase(private val adminActionRepository: com.example.domain.repositories.AdminActionRepository) {

    suspend fun execute(userId: String) = adminActionRepository.adminAction(
        userId,
        AdminAction.BLOCK.action
    )
}